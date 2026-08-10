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
package com.cowave.hub.admin.service.rbac.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rbac.biz.HubRoleBiz;
import com.cowave.hub.admin.domain.rbac.entity.HubRole;
import com.cowave.hub.admin.domain.rbac.entity.command.RoleMenuUpdate;
import com.cowave.hub.admin.domain.rbac.entity.command.RoleUserUpdate;
import com.cowave.hub.admin.domain.rbac.entity.pto.RoleInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.RoleUserPto;
import com.cowave.hub.admin.domain.rbac.entity.query.RoleQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.RoleUserQuery;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubRoleRepositoryFacade;
import com.cowave.hub.admin.service.rbac.HubRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
public class HubRoleServiceImpl implements HubRoleService {

    private final HubRoleBiz roleBiz;

    private final HubRoleRepositoryFacade roleRepositoryFacade;

    @Override
    public Page<HubRole> list(String tenantId, RoleQuery query) {
        return roleRepositoryFacade.queryPage(tenantId, query);
    }

    @Override
    public RoleInfoPto info(String tenantId, Integer roleId) {
        return roleRepositoryFacade.queryInfo(tenantId, roleId);
    }

    @Override
    public void add(String tenantId, HubRole hubRole) {
        roleBiz.createRole(tenantId, hubRole);
    }

    @Override
    public void delete(String tenantId, List<Integer> roleIds) {
        roleBiz.deleteRoles(tenantId, roleIds);
    }

    @Override
    public void edit(String tenantId, HubRole hubRole) {
        roleBiz.editRole(tenantId, hubRole);
    }

    @Override
    public void updateMenus(String tenantId, RoleMenuUpdate roleUpdate) {
        roleBiz.updateMenus(tenantId, roleUpdate);
    }

    @Override
    public void grantUser(String tenantId, RoleUserUpdate roleUpdate) {
        roleBiz.grantUser(tenantId, roleUpdate);
    }

    @Override
    public void cancelUser(String tenantId, RoleUserUpdate roleUpdate) {
        roleBiz.cancelUser(tenantId, roleUpdate);
    }

    @Override
    public Page<RoleUserPto> queryAuthedUser(String tenantId, RoleUserQuery query) {
        return roleRepositoryFacade.queryAuthedUser(tenantId, query);
    }

    @Override
    public Page<RoleUserPto> queryUnAuthedUser(String tenantId, RoleUserQuery query) {
        return roleRepositoryFacade.queryUnAuthedUser(tenantId, query);
    }

    @Override
    public List<String> queryNames(String tenantId, List<Integer> roleIds) {
        return roleRepositoryFacade.queryNameByIds(tenantId, roleIds);
    }
}
