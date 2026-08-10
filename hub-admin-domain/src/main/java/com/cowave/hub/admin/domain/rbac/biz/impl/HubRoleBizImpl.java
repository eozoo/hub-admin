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
package com.cowave.hub.admin.domain.rbac.biz.impl;

import com.cowave.hub.admin.domain.rbac.biz.HubRoleBiz;
import com.cowave.hub.admin.domain.rbac.entity.HubRole;
import com.cowave.hub.admin.domain.rbac.entity.HubRoleMenu;
import com.cowave.hub.admin.domain.rbac.entity.command.RoleMenuUpdate;
import com.cowave.hub.admin.domain.rbac.entity.command.RoleUserUpdate;
import com.cowave.hub.admin.domain.rbac.repository.HubRoleRepository;
import com.cowave.zoo.framework.access.operation.OperationContext;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.tools.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.zoo.http.client.constants.HttpCode.NOT_FOUND;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubRoleBizImpl implements HubRoleBiz {

    private final HubRoleRepository roleRepository;

    @Override
    public void createRole(String tenantId, HubRole hubRole) {
        long codeCount = roleRepository.countRoleCode(tenantId, hubRole.getRoleCode(), null);
        HttpAsserts.isTrue(codeCount == 0,
                BAD_REQUEST, "{admin.role.code.conflict}", hubRole.getRoleCode());
        roleRepository.save(hubRole);
    }

    @Override
    public void deleteRoles(String tenantId, List<Integer> roleIds) {
        List<HubRole> list = roleRepository.queryListByIds(tenantId, roleIds);
        OperationContext.prepareContent(list);

        List<Integer> deleteList = Collections.copyToList(list, HubRole::getRoleId);
        if (!deleteList.isEmpty()) {
            roleRepository.removeByIds(deleteList);
            roleRepository.deleteRoleMenusByRoleIds(deleteList);
            roleRepository.deleteUserRolesByRoleIds(deleteList);
        }
    }

    @Override
    public void editRole(String tenantId, HubRole hubRole) {
        HttpAsserts.notNull(hubRole.getRoleId(), BAD_REQUEST, "{admin.role.id.null}");

        long codeCount = roleRepository.countRoleCode(tenantId, hubRole.getRoleCode(), hubRole.getRoleId());
        HttpAsserts.isTrue(codeCount == 0, BAD_REQUEST, "{admin.role.code.conflict}", hubRole.getRoleCode());

        HubRole preRole = roleRepository.queryById(tenantId, hubRole.getRoleId());
        HttpAsserts.notNull(preRole, NOT_FOUND, "{admin.role.not.exist}", hubRole.getRoleId());
        OperationContext.prepareContent(preRole);

        roleRepository.updateRole(hubRole);
    }

    @Override
    public void updateMenus(String tenantId, RoleMenuUpdate roleUpdate) {
        HubRole preRole = roleRepository.queryById(tenantId, roleUpdate.getRoleId());
        HttpAsserts.notNull(preRole, NOT_FOUND, "{admin.role.not.exist}", roleUpdate.getRoleId());

        roleRepository.deleteRoleMenusByRoleId(roleUpdate.getRoleId());
        roleRepository.saveRoleMenuBatch(Collections.copyToList(roleUpdate.getMenuScopes(),
                ms -> new HubRoleMenu(roleUpdate.getRoleId(), ms.getMenuId(), ms.getScopeId())));
    }

    @Override
    public void grantUser(String tenantId, RoleUserUpdate roleUpdate) {
        roleRepository.addRoleUser(tenantId, roleUpdate);
    }

    @Override
    public void cancelUser(String tenantId, RoleUserUpdate roleUpdate) {
        HubRole preRole = roleRepository.queryById(tenantId, roleUpdate.getRoleId());
        HttpAsserts.notNull(preRole, NOT_FOUND, "{admin.role.not.exist}", roleUpdate.getRoleId());
        roleRepository.deleteRoleUsers(roleUpdate.getRoleId(), roleUpdate.getUserIds());
    }
}
