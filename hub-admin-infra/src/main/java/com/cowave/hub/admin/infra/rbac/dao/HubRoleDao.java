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

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.rbac.entity.*;
import com.cowave.hub.admin.domain.rbac.entity.command.RoleUserUpdate;
import com.cowave.hub.admin.domain.rbac.entity.pto.RoleInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.RoleUserPto;
import com.cowave.hub.admin.domain.rbac.entity.query.RoleQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.RoleUserQuery;
import com.cowave.hub.admin.domain.rbac.repository.HubRoleRepository;
import com.cowave.hub.admin.infra.rbac.mapper.HubRoleMapper;
import com.cowave.hub.admin.infra.rbac.mapper.HubRoleMenuMapper;
import com.cowave.hub.admin.infra.rbac.mapper.HubUserRoleMapper;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.tools.Collections;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class HubRoleDao extends ServiceImpl<HubRoleMapper, HubRole> implements HubRoleRepository {
    private final HubRoleMenuMapper roleMenuMapper;
    private final HubUserRoleMapper userRoleMapper;

    @Override
    public Page<HubRole> queryPage(String tenantId, RoleQuery query) {
        return lambdaQuery()
                .in(HubRole::getTenantId, List.of("#", tenantId))
                .eq(StringUtils.isNotBlank(query.getRoleCode()), HubRole::getRoleCode, query.getRoleCode())
                .eq(StringUtils.isNotBlank(query.getRoleName()), HubRole::getRoleName, query.getRoleName())
                .page(Access.page());
    }

    @Override
    public RoleInfoPto queryInfo(String tenantId, Integer roleId) {
        return baseMapper.info(tenantId, roleId);
    }

    @Override
    public HubRole queryById(String tenantId, Integer roleId) {
        return lambdaQuery()
                .eq(HubRole::getTenantId, tenantId)
                .eq(HubRole::getRoleId, roleId)
                .one();
    }

    @Override
    public HubRole queryByCode(String tenantId, String roleCode) {
        return lambdaQuery()
                .in(HubRole::getTenantId, List.of("#", tenantId))
                .eq(HubRole::getRoleCode, roleCode)
                .one();
    }

    @Override
    public List<HubRole> queryListByIds(String tenantId, List<Integer> roleIds) {
        return lambdaQuery()
                .in(HubRole::getTenantId, List.of("#", tenantId))
                .in(HubRole::getRoleId, roleIds)
                .list();
    }

    @Override
    public List<String> queryNameByIds(String tenantId, List<Integer> roleIds) {
        if (roleIds.isEmpty()) {
            return List.of();
        }
        List<HubRole> list = lambdaQuery()
                .eq(HubRole::getTenantId, tenantId)
                .in(HubRole::getRoleId, roleIds)
                .select(HubRole::getRoleName)
                .list();
        return Collections.copyToList(list, HubRole::getRoleName);
    }

    @Override
    public long countRoleCode(String tenantId, String roleCode, Integer roleId) {
        return lambdaQuery()
                .in(HubRole::getTenantId, List.of("#", tenantId))
                .eq(HubRole::getRoleCode, roleCode)
                .ne(roleId != null, HubRole::getRoleId, roleId)
                .count();
    }

    @Override
    public Page<RoleUserPto> queryAuthedUser(String tenantId, RoleUserQuery query) {
        return userRoleMapper.getAuthedUser(tenantId, query, Access.page());
    }

    @Override
    public Page<RoleUserPto> queryUnAuthedUser(String tenantId, RoleUserQuery query) {
        return userRoleMapper.getUnAuthedUser(tenantId, query, Access.page());
    }

    @Override
    public void updateRole(HubRole hubRole) {
        lambdaUpdate()
                .eq(HubRole::getRoleId, hubRole.getRoleId())
                .set(HubRole::getUpdateBy, Access.userCode())
                .set(HubRole::getUpdateTime, new Date())
                .set(HubRole::getRoleName, hubRole.getRoleName())
                .set(HubRole::getRoleCode, hubRole.getRoleCode())
                .set(HubRole::getRoleType, hubRole.getRoleType())
                .set(HubRole::getRemark, hubRole.getRemark())
                .update();
    }

    @Override
    public void deleteRoleMenusByRoleId(Integer roleId) {
        roleMenuMapper.delete(new LambdaUpdateWrapper<HubRoleMenu>()
                .eq(HubRoleMenu::getRoleId, roleId));
    }

    @Override
    public void deleteRoleMenusByRoleIds(List<Integer> roleIds) {
        roleMenuMapper.delete(new LambdaUpdateWrapper<HubRoleMenu>()
                .in(HubRoleMenu::getRoleId, roleIds));
    }

    @Override
    public void saveRoleMenuBatch(List<HubRoleMenu> list) {
        for (HubRoleMenu menu : list) {
            roleMenuMapper.insert(menu);
        }
    }

    @Override
    public void addRoleUser(String tenantId, RoleUserUpdate roleUpdate) {
        userRoleMapper.addRoleUser(tenantId, roleUpdate);
    }

    @Override
    public void deleteRoleUsers(Integer roleId, List<Integer> userIds) {
        userRoleMapper.delete(new LambdaUpdateWrapper<HubUserRole>()
                .eq(HubUserRole::getRoleId, roleId)
                .in(HubUserRole::getUserId, userIds));
    }

    @Override
    public void deleteUserRolesByRoleIds(List<Integer> roleIds) {
        userRoleMapper.delete(new LambdaUpdateWrapper<HubUserRole>()
                .in(HubUserRole::getRoleId, roleIds));
    }

}
