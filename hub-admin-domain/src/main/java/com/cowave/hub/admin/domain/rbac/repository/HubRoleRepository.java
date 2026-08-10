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
package com.cowave.hub.admin.domain.rbac.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cowave.hub.admin.domain.rbac.entity.HubRole;
import com.cowave.hub.admin.domain.rbac.entity.HubRoleMenu;
import com.cowave.hub.admin.domain.rbac.entity.command.RoleUserUpdate;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubRoleRepositoryFacade;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubRoleRepository extends HubRoleRepositoryFacade, IService<HubRole> {

    /**
     * 更新角色
     */
    void updateRole(HubRole hubRole);

    /**
     * 删除角色菜单（按roleId）
     */
    void deleteRoleMenusByRoleId(Integer roleId);

    /**
     * 批量删除角色菜单（按roleId）
     */
    void deleteRoleMenusByRoleIds(List<Integer> roleIds);

    /**
     * 批量保存角色菜单
     */
    void saveRoleMenuBatch(List<HubRoleMenu> list);

    /**
     * 添加角色用户（XML，含租户校验）
     */
    void addRoleUser(String tenantId, RoleUserUpdate roleUpdate);

    /**
     * 删除角色用户
     */
    void deleteRoleUsers(Integer roleId, List<Integer> userIds);

    /**
     * 批量删除角色用户（按roleId）
     */
    void deleteUserRolesByRoleIds(List<Integer> roleIds);
}
