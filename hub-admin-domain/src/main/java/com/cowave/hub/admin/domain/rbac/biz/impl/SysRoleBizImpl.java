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

import com.cowave.hub.admin.domain.rbac.biz.SysRoleBiz;
import com.cowave.hub.admin.domain.rbac.entity.SysRole;
import com.cowave.hub.admin.domain.rbac.entity.SysRoleMenu;
import com.cowave.hub.admin.domain.rbac.entity.command.RoleMenuUpdate;
import com.cowave.hub.admin.domain.rbac.entity.command.RoleUserUpdate;
import com.cowave.hub.admin.domain.rbac.repository.SysRoleRepository;
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
public class SysRoleBizImpl implements SysRoleBiz {

    private final SysRoleRepository roleRepository;

    @Override
    public void createRole(String tenantId, SysRole sysRole) {
        long codeCount = roleRepository.countRoleCode(tenantId, sysRole.getRoleCode(), null);
        HttpAsserts.isTrue(codeCount == 0,
                BAD_REQUEST, "{admin.role.code.conflict}", sysRole.getRoleCode());
        roleRepository.save(sysRole);
    }

    @Override
    public void deleteRoles(String tenantId, List<Integer> roleIds) {
        List<SysRole> list = roleRepository.queryListByIds(tenantId, roleIds);
        OperationContext.prepareContent(list);

        List<Integer> deleteList = Collections.copyToList(list, SysRole::getRoleId);
        if (!deleteList.isEmpty()) {
            roleRepository.removeByIds(deleteList);
            roleRepository.deleteRoleMenusByRoleIds(deleteList);
            roleRepository.deleteUserRolesByRoleIds(deleteList);
        }
    }

    @Override
    public void editRole(String tenantId, SysRole sysRole) {
        HttpAsserts.notNull(sysRole.getRoleId(), BAD_REQUEST, "{admin.role.id.null}");

        long codeCount = roleRepository.countRoleCode(tenantId, sysRole.getRoleCode(), sysRole.getRoleId());
        HttpAsserts.isTrue(codeCount == 0, BAD_REQUEST, "{admin.role.code.conflict}", sysRole.getRoleCode());

        SysRole preRole = roleRepository.queryById(tenantId, sysRole.getRoleId());
        HttpAsserts.notNull(preRole, NOT_FOUND, "{admin.role.not.exist}", sysRole.getRoleId());
        OperationContext.prepareContent(preRole);

        roleRepository.updateRole(sysRole);
    }

    @Override
    public void updateMenus(String tenantId, RoleMenuUpdate roleUpdate) {
        SysRole preRole = roleRepository.queryById(tenantId, roleUpdate.getRoleId());
        HttpAsserts.notNull(preRole, NOT_FOUND, "{admin.role.not.exist}", roleUpdate.getRoleId());

        roleRepository.deleteRoleMenusByRoleId(roleUpdate.getRoleId());
        roleRepository.saveRoleMenuBatch(Collections.copyToList(roleUpdate.getMenuScopes(),
                ms -> new SysRoleMenu(roleUpdate.getRoleId(), ms.getMenuId(), ms.getScopeId())));
    }

    @Override
    public void grantUser(String tenantId, RoleUserUpdate roleUpdate) {
        roleRepository.addRoleUser(tenantId, roleUpdate);
    }

    @Override
    public void cancelUser(String tenantId, RoleUserUpdate roleUpdate) {
        SysRole preRole = roleRepository.queryById(tenantId, roleUpdate.getRoleId());
        HttpAsserts.notNull(preRole, NOT_FOUND, "{admin.role.not.exist}", roleUpdate.getRoleId());
        roleRepository.deleteRoleUsers(roleUpdate.getRoleId(), roleUpdate.getUserIds());
    }
}
