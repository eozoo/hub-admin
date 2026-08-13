/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra.rbac.dao;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.auth.entity.pto.UserProfile;
import com.cowave.hub.admin.domain.auth.entity.command.ProfileUpdate;
import com.cowave.hub.admin.domain.rbac.entity.*;
import com.cowave.hub.admin.domain.rbac.entity.command.TenantManagerRemove;
import com.cowave.hub.admin.domain.rbac.entity.command.UserCreate;
import com.cowave.hub.admin.domain.rbac.entity.pto.TenantManagerPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserListPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserNamePto;
import com.cowave.hub.admin.domain.rbac.entity.query.UserExportQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.UserMemberQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.UserQuery;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserDiagramPto;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.domain.rbac.enums.UserType;
import com.cowave.hub.admin.domain.rbac.repository.SysUserRepository;
import com.cowave.hub.admin.infra.rbac.mapper.*;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.tools.Collections;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.cowave.hub.admin.domain.AdminRedisKeys.USER_DIAGRAM;
import static com.cowave.hub.admin.domain.rbac.entity.pto.DiagramNode.DIAGRAM_CONFIG;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class SysUserDao extends ServiceImpl<SysUserMapper, SysUser> implements SysUserRepository {
    private final SysUserRoleMapper userRoleMapper;
    private final SysUserDeptMapper userDeptMapper;
    private final SysUserDiagramMapper userDiagramMapper;
    private final SysTenantMapper tenantMapper;

    @Override
    public List<UserListPto> queryList(String tenantId, UserQuery query) {
        return baseMapper.list(tenantId, query);
    }

    @Override
    public int countList(String tenantId, UserQuery query) {
        return baseMapper.count(tenantId, query);
    }

    @Override
    public List<UserListPto> queryListOfDept(String tenantId, UserQuery query) {
        return baseMapper.listOfDept(tenantId, query);
    }

    @Override
    public int countListOfDept(String tenantId, UserQuery query) {
        return baseMapper.countOfDept(tenantId, query);
    }

    @Override
    public UserInfoPto queryInfo(String tenantId, Integer userId) {
        return baseMapper.getById(tenantId, userId);
    }

    @Override
    public List<SysUser> queryListForExport(String tenantId, UserExportQuery query) {
        return lambdaQuery()
                .eq(SysUser::getTenantId, tenantId)
                .eq(StringUtils.isNotBlank(query.getUserRank()), SysUser::getUserRank, query.getUserRank())
                .like(StringUtils.isNotBlank(query.getUserName()), SysUser::getUserName, query.getUserName())
                .like(StringUtils.isNotBlank(query.getUserPhone()), SysUser::getUserPhone, query.getUserPhone())
                .list();
    }

    @Cacheable(value = USER_DIAGRAM, key = "#tenantId")
    @Override
    public Tree<Integer> queryDiagram(String tenantId) {
        List<UserDiagramPto> list = userDiagramMapper.listDiagramNodes(tenantId);
        SysTenant sysTenant = tenantMapper.selectById(tenantId);
        list.add(UserDiagramPto.newRootNode(sysTenant.getTenantName()));
        return TreeUtil.build(list, -1, DIAGRAM_CONFIG, (u, node) -> {
            node.setId(u.getId());
            node.setParentId(u.getPid());
            node.setName(u.getLabel());
            node.put("rank", u.getUserRank());
        }).get(0);
    }

    @Override
    public List<Integer> queryChildUserIds(Integer userId) {
        return userDiagramMapper.childIds(userId);
    }

    @Override
    public List<UserNamePto> queryCandidates(String tenantId, Integer userId) {
        return baseMapper.getUserCandidates(tenantId, userId);
    }

    @Override
    public String queryNameById(Integer userId) {
        return lambdaQuery()
                .eq(SysUser::getUserId, userId)
                .select(SysUser::getUserName)
                .oneOpt().map(SysUser::getUserName).orElse(null);
    }

    @Override
    public String queryNameByCode(String userCode) {
        return lambdaQuery()
                .eq(SysUser::getUserCode, userCode)
                .select(SysUser::getUserName)
                .oneOpt().map(SysUser::getUserName).orElse(null);
    }

    @Override
    public List<String> queryNamesById(String tenantId, List<Integer> userIds) {
        if (userIds.isEmpty()) {
            return List.of();
        }
        List<SysUser> list = lambdaQuery()
                .eq(SysUser::getTenantId, tenantId)
                .in(SysUser::getUserId, userIds)
                .select(SysUser::getUserName)
                .list();
        return Collections.copyToList(list, SysUser::getUserName);
    }

    @Override
    public Map<String, String> queryCodeNameMap(Collection<String> userCodes) {
        if (userCodes.isEmpty()) {
            return new HashMap<>();
        }
        List<SysUser> list = lambdaQuery()
                .in(SysUser::getUserCode, userCodes)
                .select(SysUser::getUserCode, SysUser::getUserName)
                .list();
        return Collections.copyToMap(list, SysUser::getUserCode, SysUser::getUserName);
    }

    @Override
    public Page<SysUser> queryUserOptions(String tenantId, UserMemberQuery query) {
        return lambdaQuery().eq(SysUser::getTenantId, tenantId)
                .like(StringUtils.isNotBlank(query.getUserName()), SysUser::getUserName, query.getUserName())
                .notIn(CollectionUtils.isNotEmpty(query.getUserCodes()), SysUser::getUserCode, query.getUserCodes())
                .page(Access.page());
    }

    @Override
    public SysUser queryByAccount(String tenantId, UserType userType, String userAccount) {
        return lambdaQuery()
                .eq(SysUser::getTenantId, tenantId)
                .eq(SysUser::getUserType, userType)
                .eq(SysUser::getUserAccount, userAccount)
                .one();
    }

    @Override
    public SysUser queryByCode(String userCode) {
        return lambdaQuery().eq(SysUser::getUserCode, userCode).one();
    }

    @Override
    public long countByAccount(String tenantId, UserType userType, String userAccount, Integer userId) {
        return lambdaQuery()
                .eq(SysUser::getTenantId, tenantId)
                .eq(SysUser::getUserType, userType)
                .eq(SysUser::getUserAccount, userAccount)
                .ne(userId != null, SysUser::getUserId, userId)
                .count();
    }

    @Override
    public List<Integer> queryUserRoleIdsByUserId(Integer userId) {
        return userRoleMapper.selectList(new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId))
                .stream().map(SysUserRole::getRoleId).toList();
    }

    @Override
    public Page<TenantManagerPto> queryTenantManager(String tenantId) {
        return baseMapper.listTenantManager(tenantId, Access.page());
    }

    @Override
    public void removeTenantManager(TenantManagerRemove managerRemove) {
        baseMapper.removeTenantManager(managerRemove);
    }

    @Override
    public void updateUser(UserCreate user) {
        lambdaUpdate().eq(SysUser::getUserId, user.getUserId())
                .set(SysUser::getUpdateBy, Access.userCode())
                .set(SysUser::getUpdateTime, new Date())
                .set(SysUser::getUserName, user.getUserName())
                .set(SysUser::getUserAccount, user.getUserAccount())
                .set(SysUser::getUserSex, user.getUserSex())
                .set(SysUser::getUserPhone, user.getUserPhone())
                .set(SysUser::getUserEmail, user.getUserEmail())
                .set(SysUser::getUserRank, user.getUserRank())
                .set(SysUser::getRemark, user.getRemark())
                .update();
    }

    @Override
    public void updateStatusById(Integer userId, EnableStatus status) {
        lambdaUpdate().eq(SysUser::getUserId, userId)
                .set(SysUser::getUpdateBy, Access.userCode())
                .set(SysUser::getUpdateTime, new Date())
                .set(SysUser::getUserStatus, status)
                .update();
    }

    @Override
    public void updatePasswdById(Integer userId, String passwd) {
        lambdaUpdate().eq(SysUser::getUserId, userId)
                .set(SysUser::getUpdateBy, Access.userCode())
                .set(SysUser::getUpdateTime, new Date())
                .set(SysUser::getUserPasswd, passwd)
                .update();
    }

    @Override
    public void removeUserRolesByUserId(Integer userId) {
        userRoleMapper.delete(new LambdaUpdateWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId));
    }

    @Override
    public void removeUserDeptsByUserId(Integer userId) {
        userDeptMapper.delete(new LambdaUpdateWrapper<SysUserDept>()
                .eq(SysUserDept::getUserId, userId));
    }

    @Override
    public void removeUserDiagramParentsByUserId(Integer userId) {
        userDiagramMapper.delete(new LambdaUpdateWrapper<SysUserDiagram>()
                .eq(SysUserDiagram::getUserId, userId));
    }

    @Override
    public void removeUserDiagramChildrenByUserId(Integer userId) {
        userDiagramMapper.delete(new LambdaUpdateWrapper<SysUserDiagram>()
                .eq(SysUserDiagram::getParentId, userId));
    }

    @Override
    public void saveUserRolesBatch(List<SysUserRole> list) {
        for (SysUserRole ur : list) {
            userRoleMapper.insert(ur);
        }
    }

    @Override
    public void saveUserDeptsBatch(List<SysUserDept> list) {
        for (SysUserDept ud : list) {
            userDeptMapper.insert(ud);
        }
    }

    @Override
    public void saveUserDiagramBatch(List<SysUserDiagram> list) {
        for (SysUserDiagram ud : list) {
            userDiagramMapper.insert(ud);
        }
    }

    @Override
    public void batchInsert(List<SysUser> list, boolean overwrite) {
        baseMapper.batchInsert(list, overwrite);
    }

    @Override
    public UserProfile queryUserProfile(Integer userId) {
        return baseMapper.getUserProfile(userId);
    }

    @Override
    public void setMfa(Integer userId, String mfaKey) {
        lambdaUpdate().eq(SysUser::getUserId, userId).set(SysUser::getMfa, mfaKey).update();
    }

    @Override
    public void deleteMfa(Integer userId) {
        lambdaUpdate().eq(SysUser::getUserId, userId).set(SysUser::getMfa, null).update();
    }

    @Override
    public void updateProfileById(Integer userId, ProfileUpdate profile) {
        lambdaUpdate().eq(SysUser::getUserId, userId)
                .set(SysUser::getUpdateBy, Access.userCode())
                .set(SysUser::getUpdateTime, new Date())
                .set(SysUser::getUserSex, profile.getUserSex())
                .set(SysUser::getUserName, profile.getUserName())
                .set(SysUser::getUserEmail, profile.getUserEmail())
                .set(SysUser::getUserPhone, profile.getUserPhone())
                .update();
    }

    @Override
    public void updateLdapByCode(SysUser sysUser) {
        lambdaUpdate()
                .eq(SysUser::getUserCode, sysUser.getUserCode())
                .set(SysUser::getUserName, sysUser.getUserName())
                .set(SysUser::getUserPhone, sysUser.getUserPhone())
                .set(SysUser::getUserEmail, sysUser.getUserEmail())
                .update();
    }

    @Override
    public void updateGitlabByCode(SysUser sysUser) {
        lambdaUpdate()
                .eq(SysUser::getUserCode, sysUser.getUserCode())
                .set(SysUser::getUserName, sysUser.getUserName())
                .set(SysUser::getUserEmail, sysUser.getUserEmail())
                .update();
    }
}
