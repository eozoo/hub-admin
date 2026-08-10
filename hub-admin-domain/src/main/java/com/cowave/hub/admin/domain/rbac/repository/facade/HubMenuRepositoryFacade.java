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
package com.cowave.hub.admin.domain.rbac.repository.facade;

import cn.hutool.core.lang.tree.Tree;
import com.cowave.hub.admin.domain.rbac.entity.HubMenu;
import com.cowave.hub.admin.domain.rbac.entity.HubRoleMenu;
import com.cowave.hub.admin.domain.rbac.entity.pto.MenuTreePto;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;

import java.util.List;

/**
 * HubMenu聚合根Query操作
 *
 * @see HubMenu
 * @see HubRoleMenu
 *
 * @author shanhuiming
 */
public interface HubMenuRepositoryFacade {

    /**
     * 菜单权限（管理员）
     */
    List<HubMenu> queryMenusByAdmin(String tenantId);

    /**
     * 菜单权限（公共）
     */
    List<HubMenu> queryMenusInPublic(String tenantId);

    /**
     * 菜单权限（指定角色）
     */
    List<HubMenu> queryMenusByRoles(String tenantId, List<String> roleList);

    /**
     * 菜单树（含数据权限scope）
     */
    List<Tree<Integer>> queryMenuTree(String tenantId);

    /**
     * 列表
     */
    List<HubMenu> queryList(String menuName, EnableStatus menuStatus);

    /**
     * 详情
     */
    HubMenu queryById(Integer menuId);

    /**
     * 获取菜单权限符
     */
    List<String> queryPermitsByIds(List<Integer> menuIds);

    /**
     * API令牌权限树数据（管理员）
     */
    List<MenuTreePto> queryApiPermitsByAdmin(String tenantId);

    /**
     * API令牌权限树数据（指定角色）
     */
    List<MenuTreePto> queryApiPermitsByRoles(String tenantId, List<String> roleList);
}
