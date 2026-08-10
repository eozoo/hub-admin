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
package com.cowave.hub.admin.domain.rbac.biz;

import com.cowave.hub.admin.domain.rbac.entity.HubRole;
import com.cowave.hub.admin.domain.rbac.entity.HubRoleMenu;
import com.cowave.hub.admin.domain.rbac.entity.HubUserRole;
import com.cowave.hub.admin.domain.rbac.entity.command.RoleMenuUpdate;
import com.cowave.hub.admin.domain.rbac.entity.command.RoleUserUpdate;

import java.util.List;

/**
 * HubRole聚合根Command操作
 *
 * @see HubRole
 * @see HubRoleMenu
 * @see HubUserRole
 *
 * @author shanhuiming
 */
public interface HubRoleBiz {

    /**
     * 新增角色
     */
    void createRole(String tenantId, HubRole hubRole);

    /**
     * 删除角色
     */
    void deleteRoles(String tenantId, List<Integer> roleIds);

    /**
     * 修改角色
     */
    void editRole(String tenantId, HubRole hubRole);

    /**
     * 更新角色菜单权限
     */
    void updateMenus(String tenantId, RoleMenuUpdate roleUpdate);

    /**
     * 授权用户
     */
    void grantUser(String tenantId, RoleUserUpdate roleUpdate);

    /**
     * 取消授权
     */
    void cancelUser(String tenantId, RoleUserUpdate roleUpdate);
}
