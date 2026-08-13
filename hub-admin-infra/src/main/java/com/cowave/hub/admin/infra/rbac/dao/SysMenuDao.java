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
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.rbac.entity.SysMenu;
import com.cowave.hub.admin.domain.rbac.entity.pto.MenuTreePto;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.domain.rbac.repository.SysMenuRepository;
import com.cowave.hub.admin.infra.rbac.mapper.SysMenuMapper;
import com.cowave.hub.admin.infra.rbac.mapper.SysRoleMenuMapper;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.tools.Collections;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.cowave.hub.admin.domain.rbac.entity.pto.DiagramNode.DIAGRAM_CONFIG;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class SysMenuDao extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuRepository {

    private final SysRoleMenuMapper roleMenuMapper;

    @Override
    public List<SysMenu> queryMenusByAdmin(String tenantId) {
        return lambdaQuery()
                .eq(SysMenu::getIsVisible, 1)
                .eq(SysMenu::getMenuStatus, 1)
                .in(SysMenu::getTenantId, List.of("#", tenantId))
                .in(SysMenu::getMenuType, List.of("C", "M"))
                .orderByAsc(SysMenu::getParentId, SysMenu::getMenuOrder)
                .list();
    }

    @Override
    public List<SysMenu> queryMenusInPublic(String tenantId) {
        return lambdaQuery()
                .eq(SysMenu::getIsVisible, 1)
                .eq(SysMenu::getMenuStatus, 1)
                .eq(SysMenu::getIsProtected, 0)
                .in(SysMenu::getTenantId, List.of("#", tenantId))
                .in(SysMenu::getMenuType, List.of("C", "M"))
                .orderByAsc(SysMenu::getParentId, SysMenu::getMenuOrder)
                .list();
    }

    @Override
    public List<SysMenu> queryMenusByRoles(String tenantId, List<String> roleList) {
        return baseMapper.listMenusByRoles(tenantId, roleList);
    }

    @Override
    public List<Tree<Integer>> queryMenuTree(String tenantId) {
        List<MenuTreePto> list = baseMapper.listTree(tenantId);
        return TreeUtil.build(list, 0, DIAGRAM_CONFIG, (menu, node) -> {
            node.setId(menu.getMenuId());
            node.setParentId(menu.getParentId());
            node.setName(menu.getMenuName());
            node.setWeight(menu.getMenuOrder());
            node.put("order", menu.getMenuOrder());
            node.put("menuType", menu.getMenuType());
            node.put("protected", menu.getIsProtected());
            node.put("scopes", menu.getScopes());
        });
    }

    @Override
    public List<SysMenu> queryList(String menuName, EnableStatus menuStatus) {
        return lambdaQuery()
                .eq(menuStatus != null, SysMenu::getMenuStatus, menuStatus)
                .like(StringUtils.isNotBlank(menuName), SysMenu::getMenuName, menuName)
                .orderByAsc(SysMenu::getParentId, SysMenu::getMenuOrder)
                .list();
    }

    @Override
    public SysMenu queryById(Integer menuId) {
        return getById(menuId);
    }

    @Override
    public List<String> queryPermitsByIds(List<Integer> menuIds) {
        List<SysMenu> menuList = lambdaQuery()
                .in(SysMenu::getMenuId, menuIds)
                .select(SysMenu::getMenuPermit)
                .list();
        return Collections.filterCopyToList(menuList, SysMenu::getMenuPermit, Objects::nonNull);
    }

    @Override
    public List<MenuTreePto> queryApiPermitsByAdmin(String tenantId) {
        return baseMapper.listApiPermitsByAdmin(tenantId);
    }

    @Override
    public List<MenuTreePto> queryApiPermitsByRoles(String tenantId, List<String> roleList) {
        return baseMapper.listApiPermitsByRoles(tenantId, roleList);
    }

    @Override
    public void updateMenu(SysMenu sysMenu) {
        lambdaUpdate().eq(SysMenu::getMenuId, sysMenu.getMenuId())
                .set(SysMenu::getTenantId, sysMenu.getTenantId())
                .set(SysMenu::getParentId, sysMenu.getParentId())
                .set(SysMenu::getMenuName, sysMenu.getMenuName())
                .set(SysMenu::getMenuOrder, sysMenu.getMenuOrder())
                .set(SysMenu::getMenuPermit, sysMenu.getMenuPermit())
                .set(SysMenu::getMenuPath, sysMenu.getMenuPath())
                .set(SysMenu::getMenuParam, sysMenu.getMenuParam())
                .set(SysMenu::getMenuType, sysMenu.getMenuType())
                .set(SysMenu::getMenuIcon, sysMenu.getMenuIcon())
                .set(SysMenu::getMenuStatus, sysMenu.getMenuStatus())
                .set(SysMenu::getComponent, sysMenu.getComponent())
                .set(SysMenu::getIsFrame, sysMenu.getIsFrame())
                .set(SysMenu::getIsCache, sysMenu.getIsCache())
                .set(SysMenu::getIsVisible, sysMenu.getIsVisible())
                .set(SysMenu::getIsProtected, sysMenu.getIsProtected())
                .set(SysMenu::getRemark, sysMenu.getRemark())
                .set(SysMenu::getUpdateBy, Access.userCode())
                .set(SysMenu::getUpdateTime, new Date())
                .update();
    }

    @Override
    public void loopDeleteMenuRoles(Integer menuId) {
        roleMenuMapper.loopDeleteMenuRoles(menuId);
    }

    @Override
    public void loopDeleteMenus(Integer menuId) {
        baseMapper.loopDeleteMenus(menuId);
    }
}
