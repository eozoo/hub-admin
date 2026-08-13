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
import com.cowave.hub.admin.domain.rbac.repository.SysRoleRepository;
import com.cowave.hub.admin.infra.rbac.mapper.SysRoleMapper;
import com.cowave.hub.admin.infra.rbac.mapper.SysRoleMenuMapper;
import com.cowave.hub.admin.infra.rbac.mapper.SysUserRoleMapper;
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
public class SysRoleDao extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleRepository {
    private final SysRoleMenuMapper roleMenuMapper;
    private final SysUserRoleMapper userRoleMapper;

    @Override
    public Page<SysRole> queryPage(String tenantId, RoleQuery query) {
        return lambdaQuery()
                .in(SysRole::getTenantId, List.of("#", tenantId))
                .eq(StringUtils.isNotBlank(query.getRoleCode()), SysRole::getRoleCode, query.getRoleCode())
                .eq(StringUtils.isNotBlank(query.getRoleName()), SysRole::getRoleName, query.getRoleName())
                .page(Access.page());
    }

    @Override
    public RoleInfoPto queryInfo(String tenantId, Integer roleId) {
        return baseMapper.info(tenantId, roleId);
    }

    @Override
    public SysRole queryById(String tenantId, Integer roleId) {
        return lambdaQuery()
                .eq(SysRole::getTenantId, tenantId)
                .eq(SysRole::getRoleId, roleId)
                .one();
    }

    @Override
    public SysRole queryByCode(String tenantId, String roleCode) {
        return lambdaQuery()
                .in(SysRole::getTenantId, List.of("#", tenantId))
                .eq(SysRole::getRoleCode, roleCode)
                .one();
    }

    @Override
    public List<SysRole> queryListByIds(String tenantId, List<Integer> roleIds) {
        return lambdaQuery()
                .in(SysRole::getTenantId, List.of("#", tenantId))
                .in(SysRole::getRoleId, roleIds)
                .list();
    }

    @Override
    public List<String> queryNameByIds(String tenantId, List<Integer> roleIds) {
        if (roleIds.isEmpty()) {
            return List.of();
        }
        List<SysRole> list = lambdaQuery()
                .eq(SysRole::getTenantId, tenantId)
                .in(SysRole::getRoleId, roleIds)
                .select(SysRole::getRoleName)
                .list();
        return Collections.copyToList(list, SysRole::getRoleName);
    }

    @Override
    public long countRoleCode(String tenantId, String roleCode, Integer roleId) {
        return lambdaQuery()
                .in(SysRole::getTenantId, List.of("#", tenantId))
                .eq(SysRole::getRoleCode, roleCode)
                .ne(roleId != null, SysRole::getRoleId, roleId)
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
    public void updateRole(SysRole sysRole) {
        lambdaUpdate()
                .eq(SysRole::getRoleId, sysRole.getRoleId())
                .set(SysRole::getUpdateBy, Access.userCode())
                .set(SysRole::getUpdateTime, new Date())
                .set(SysRole::getRoleName, sysRole.getRoleName())
                .set(SysRole::getRoleCode, sysRole.getRoleCode())
                .set(SysRole::getRoleType, sysRole.getRoleType())
                .set(SysRole::getRemark, sysRole.getRemark())
                .update();
    }

    @Override
    public void deleteRoleMenusByRoleId(Integer roleId) {
        roleMenuMapper.delete(new LambdaUpdateWrapper<SysRoleMenu>()
                .eq(SysRoleMenu::getRoleId, roleId));
    }

    @Override
    public void deleteRoleMenusByRoleIds(List<Integer> roleIds) {
        roleMenuMapper.delete(new LambdaUpdateWrapper<SysRoleMenu>()
                .in(SysRoleMenu::getRoleId, roleIds));
    }

    @Override
    public void saveRoleMenuBatch(List<SysRoleMenu> list) {
        for (SysRoleMenu menu : list) {
            roleMenuMapper.insert(menu);
        }
    }

    @Override
    public void addRoleUser(String tenantId, RoleUserUpdate roleUpdate) {
        userRoleMapper.addRoleUser(tenantId, roleUpdate);
    }

    @Override
    public void deleteRoleUsers(Integer roleId, List<Integer> userIds) {
        userRoleMapper.delete(new LambdaUpdateWrapper<SysUserRole>()
                .eq(SysUserRole::getRoleId, roleId)
                .in(SysUserRole::getUserId, userIds));
    }

    @Override
    public void deleteUserRolesByRoleIds(List<Integer> roleIds) {
        userRoleMapper.delete(new LambdaUpdateWrapper<SysUserRole>()
                .in(SysUserRole::getRoleId, roleIds));
    }

}
