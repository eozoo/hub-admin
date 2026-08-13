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

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import com.cowave.hub.admin.domain.rbac.biz.SysMenuBiz;
import com.cowave.hub.admin.domain.rbac.entity.SysMenu;
import com.cowave.hub.admin.domain.rbac.entity.pto.MenuTreePto;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysMenuRepositoryFacade;
import com.cowave.hub.admin.service.rbac.SysMenuService;
import com.cowave.zoo.framework.access.Access;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.cowave.hub.admin.domain.rbac.entity.pto.DiagramNode.DIAGRAM_CONFIG;

/**
 * 菜单
 *
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuBiz menuBiz;

    private final SysMenuRepositoryFacade menuRepositoryFacade;

    @Override
    public List<SysMenu> queryMenusByAdmin(String tenantId) {
        return menuRepositoryFacade.queryMenusByAdmin(tenantId);
    }

    @Override
    public List<SysMenu> queryMenusInPublic(String tenantId) {
        return menuRepositoryFacade.queryMenusInPublic(tenantId);
    }

    @Override
    public List<SysMenu> queryMenusByRoles(String tenantId, List<String> roleList) {
        return menuRepositoryFacade.queryMenusByRoles(tenantId, roleList);
    }

    @Override
    public List<Tree<Integer>> queryTree(String tenantId) {
        return menuRepositoryFacade.queryMenuTree(tenantId);
    }

    @Override
    public List<SysMenu> list(String menuName, EnableStatus menuStatus) {
        return menuRepositoryFacade.queryList(menuName, menuStatus);
    }

    @Override
    public List<Tree<Integer>> queryApiPermitsByUser(String tenantId) {
        List<MenuTreePto> list = new ArrayList<>();
        if (Access.isAdminUser()) {
            list = menuRepositoryFacade.queryApiPermitsByAdmin(tenantId);
        } else {
            List<String> roleList = Access.userRoles();
            if (!roleList.isEmpty()) {
                list = menuRepositoryFacade.queryApiPermitsByRoles(Access.tenantId(), roleList);
            }
        }
        return TreeUtil.build(list, 0, DIAGRAM_CONFIG, (menu, node) -> {
            node.setId(menu.getMenuId());
            node.setParentId(menu.getParentId());
            node.setName(menu.getMenuName());
            node.put("menuType", menu.getMenuType());
            node.put("scopeId", menu.getScopeId());
            node.put("scopes", menu.getScopes());
        });
    }

    @Override
    public SysMenu info(Integer menuId) {
        return menuRepositoryFacade.queryById(menuId);
    }

    @Override
    public void add(SysMenu sysMenu) {
        menuBiz.createMenu(sysMenu);
    }

    @Override
    public void delete(Integer menuId) {
        menuBiz.deleteMenu(menuId);
    }

    @Override
    public void edit(SysMenu sysMenu) {
        menuBiz.editMenu(sysMenu);
    }
}
