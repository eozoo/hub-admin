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
import com.cowave.hub.admin.domain.rbac.repository.HubUserRepository;
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
public class HubUserDao extends ServiceImpl<HubUserMapper, HubUser> implements HubUserRepository {
    private final HubUserRoleMapper userRoleMapper;
    private final HubUserDeptMapper userDeptMapper;
    private final HubUserDiagramMapper userDiagramMapper;
    private final HubTenantMapper tenantMapper;

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
    public List<HubUser> queryListForExport(String tenantId, UserExportQuery query) {
        return lambdaQuery()
                .eq(HubUser::getTenantId, tenantId)
                .eq(StringUtils.isNotBlank(query.getUserRank()), HubUser::getUserRank, query.getUserRank())
                .like(StringUtils.isNotBlank(query.getUserName()), HubUser::getUserName, query.getUserName())
                .like(StringUtils.isNotBlank(query.getUserPhone()), HubUser::getUserPhone, query.getUserPhone())
                .list();
    }

    @Cacheable(value = USER_DIAGRAM, key = "#tenantId")
    @Override
    public Tree<Integer> queryDiagram(String tenantId) {
        List<UserDiagramPto> list = userDiagramMapper.listDiagramNodes(tenantId);
        HubTenant hubTenant = tenantMapper.selectById(tenantId);
        list.add(UserDiagramPto.newRootNode(hubTenant.getTenantName()));
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
                .eq(HubUser::getUserId, userId)
                .select(HubUser::getUserName)
                .oneOpt().map(HubUser::getUserName).orElse(null);
    }

    @Override
    public String queryNameByCode(String userCode) {
        return lambdaQuery()
                .eq(HubUser::getUserCode, userCode)
                .select(HubUser::getUserName)
                .oneOpt().map(HubUser::getUserName).orElse(null);
    }

    @Override
    public List<String> queryNamesById(String tenantId, List<Integer> userIds) {
        if (userIds.isEmpty()) {
            return List.of();
        }
        List<HubUser> list = lambdaQuery()
                .eq(HubUser::getTenantId, tenantId)
                .in(HubUser::getUserId, userIds)
                .select(HubUser::getUserName)
                .list();
        return Collections.copyToList(list, HubUser::getUserName);
    }

    @Override
    public Map<String, String> queryCodeNameMap(Collection<String> userCodes) {
        if (userCodes.isEmpty()) {
            return new HashMap<>();
        }
        List<HubUser> list = lambdaQuery()
                .in(HubUser::getUserCode, userCodes)
                .select(HubUser::getUserCode, HubUser::getUserName)
                .list();
        return Collections.copyToMap(list, HubUser::getUserCode, HubUser::getUserName);
    }

    @Override
    public Page<HubUser> queryUserOptions(String tenantId, UserMemberQuery query) {
        return lambdaQuery().eq(HubUser::getTenantId, tenantId)
                .like(StringUtils.isNotBlank(query.getUserName()), HubUser::getUserName, query.getUserName())
                .notIn(CollectionUtils.isNotEmpty(query.getUserCodes()), HubUser::getUserCode, query.getUserCodes())
                .page(Access.page());
    }

    @Override
    public HubUser queryByAccount(String tenantId, UserType userType, String userAccount) {
        return lambdaQuery()
                .eq(HubUser::getTenantId, tenantId)
                .eq(HubUser::getUserType, userType)
                .eq(HubUser::getUserAccount, userAccount)
                .one();
    }

    @Override
    public HubUser queryByCode(String userCode) {
        return lambdaQuery().eq(HubUser::getUserCode, userCode).one();
    }

    @Override
    public long countByAccount(String tenantId, UserType userType, String userAccount, Integer userId) {
        return lambdaQuery()
                .eq(HubUser::getTenantId, tenantId)
                .eq(HubUser::getUserType, userType)
                .eq(HubUser::getUserAccount, userAccount)
                .ne(userId != null, HubUser::getUserId, userId)
                .count();
    }

    @Override
    public List<Integer> queryUserRoleIdsByUserId(Integer userId) {
        return userRoleMapper.selectList(new LambdaQueryWrapper<HubUserRole>()
                        .eq(HubUserRole::getUserId, userId))
                .stream().map(HubUserRole::getRoleId).toList();
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
        lambdaUpdate().eq(HubUser::getUserId, user.getUserId())
                .set(HubUser::getUpdateBy, Access.userCode())
                .set(HubUser::getUpdateTime, new Date())
                .set(HubUser::getUserName, user.getUserName())
                .set(HubUser::getUserAccount, user.getUserAccount())
                .set(HubUser::getUserSex, user.getUserSex())
                .set(HubUser::getUserPhone, user.getUserPhone())
                .set(HubUser::getUserEmail, user.getUserEmail())
                .set(HubUser::getUserRank, user.getUserRank())
                .set(HubUser::getRemark, user.getRemark())
                .update();
    }

    @Override
    public void updateStatusById(Integer userId, EnableStatus status) {
        lambdaUpdate().eq(HubUser::getUserId, userId)
                .set(HubUser::getUpdateBy, Access.userCode())
                .set(HubUser::getUpdateTime, new Date())
                .set(HubUser::getUserStatus, status)
                .update();
    }

    @Override
    public void updatePasswdById(Integer userId, String passwd) {
        lambdaUpdate().eq(HubUser::getUserId, userId)
                .set(HubUser::getUpdateBy, Access.userCode())
                .set(HubUser::getUpdateTime, new Date())
                .set(HubUser::getUserPasswd, passwd)
                .update();
    }

    @Override
    public void removeUserRolesByUserId(Integer userId) {
        userRoleMapper.delete(new LambdaUpdateWrapper<HubUserRole>()
                .eq(HubUserRole::getUserId, userId));
    }

    @Override
    public void removeUserDeptsByUserId(Integer userId) {
        userDeptMapper.delete(new LambdaUpdateWrapper<HubUserDept>()
                .eq(HubUserDept::getUserId, userId));
    }

    @Override
    public void removeUserDiagramParentsByUserId(Integer userId) {
        userDiagramMapper.delete(new LambdaUpdateWrapper<HubUserDiagram>()
                .eq(HubUserDiagram::getUserId, userId));
    }

    @Override
    public void removeUserDiagramChildrenByUserId(Integer userId) {
        userDiagramMapper.delete(new LambdaUpdateWrapper<HubUserDiagram>()
                .eq(HubUserDiagram::getParentId, userId));
    }

    @Override
    public void saveUserRolesBatch(List<HubUserRole> list) {
        for (HubUserRole ur : list) {
            userRoleMapper.insert(ur);
        }
    }

    @Override
    public void saveUserDeptsBatch(List<HubUserDept> list) {
        for (HubUserDept ud : list) {
            userDeptMapper.insert(ud);
        }
    }

    @Override
    public void saveUserDiagramBatch(List<HubUserDiagram> list) {
        for (HubUserDiagram ud : list) {
            userDiagramMapper.insert(ud);
        }
    }

    @Override
    public void batchInsert(List<HubUser> list, boolean overwrite) {
        baseMapper.batchInsert(list, overwrite);
    }

    @Override
    public UserProfile queryUserProfile(Integer userId) {
        return baseMapper.getUserProfile(userId);
    }

    @Override
    public void setMfa(Integer userId, String mfaKey) {
        lambdaUpdate().eq(HubUser::getUserId, userId).set(HubUser::getMfa, mfaKey).update();
    }

    @Override
    public void deleteMfa(Integer userId) {
        lambdaUpdate().eq(HubUser::getUserId, userId).set(HubUser::getMfa, null).update();
    }

    @Override
    public void updateProfileById(Integer userId, ProfileUpdate profile) {
        lambdaUpdate().eq(HubUser::getUserId, userId)
                .set(HubUser::getUpdateBy, Access.userCode())
                .set(HubUser::getUpdateTime, new Date())
                .set(HubUser::getUserSex, profile.getUserSex())
                .set(HubUser::getUserName, profile.getUserName())
                .set(HubUser::getUserEmail, profile.getUserEmail())
                .set(HubUser::getUserPhone, profile.getUserPhone())
                .update();
    }

    @Override
    public void updateLdapByCode(HubUser hubUser) {
        lambdaUpdate()
                .eq(HubUser::getUserCode, hubUser.getUserCode())
                .set(HubUser::getUserName, hubUser.getUserName())
                .set(HubUser::getUserPhone, hubUser.getUserPhone())
                .set(HubUser::getUserEmail, hubUser.getUserEmail())
                .update();
    }

    @Override
    public void updateGitlabByCode(HubUser hubUser) {
        lambdaUpdate()
                .eq(HubUser::getUserCode, hubUser.getUserCode())
                .set(HubUser::getUserName, hubUser.getUserName())
                .set(HubUser::getUserEmail, hubUser.getUserEmail())
                .update();
    }
}
