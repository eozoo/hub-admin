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
import com.cowave.hub.admin.domain.rbac.entity.HubMenu;
import com.cowave.hub.admin.domain.rbac.entity.pto.MenuTreePto;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.domain.rbac.repository.HubMenuRepository;
import com.cowave.hub.admin.infra.rbac.mapper.HubMenuMapper;
import com.cowave.hub.admin.infra.rbac.mapper.HubRoleMenuMapper;
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
public class HubMenuDao extends ServiceImpl<HubMenuMapper, HubMenu> implements HubMenuRepository {

    private final HubRoleMenuMapper roleMenuMapper;

    @Override
    public List<HubMenu> queryMenusByAdmin(String tenantId) {
        return lambdaQuery()
                .eq(HubMenu::getIsVisible, 1)
                .eq(HubMenu::getMenuStatus, 1)
                .in(HubMenu::getTenantId, List.of("#", tenantId))
                .in(HubMenu::getMenuType, List.of("C", "M"))
                .orderByAsc(HubMenu::getParentId, HubMenu::getMenuOrder)
                .list();
    }

    @Override
    public List<HubMenu> queryMenusInPublic(String tenantId) {
        return lambdaQuery()
                .eq(HubMenu::getIsVisible, 1)
                .eq(HubMenu::getMenuStatus, 1)
                .eq(HubMenu::getIsProtected, 0)
                .in(HubMenu::getTenantId, List.of("#", tenantId))
                .in(HubMenu::getMenuType, List.of("C", "M"))
                .orderByAsc(HubMenu::getParentId, HubMenu::getMenuOrder)
                .list();
    }

    @Override
    public List<HubMenu> queryMenusByRoles(String tenantId, List<String> roleList) {
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
    public List<HubMenu> queryList(String menuName, EnableStatus menuStatus) {
        return lambdaQuery()
                .eq(menuStatus != null, HubMenu::getMenuStatus, menuStatus)
                .like(StringUtils.isNotBlank(menuName), HubMenu::getMenuName, menuName)
                .orderByAsc(HubMenu::getParentId, HubMenu::getMenuOrder)
                .list();
    }

    @Override
    public HubMenu queryById(Integer menuId) {
        return getById(menuId);
    }

    @Override
    public List<String> queryPermitsByIds(List<Integer> menuIds) {
        List<HubMenu> menuList = lambdaQuery()
                .in(HubMenu::getMenuId, menuIds)
                .select(HubMenu::getMenuPermit)
                .list();
        return Collections.filterCopyToList(menuList, HubMenu::getMenuPermit, Objects::nonNull);
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
    public void updateMenu(HubMenu hubMenu) {
        lambdaUpdate().eq(HubMenu::getMenuId, hubMenu.getMenuId())
                .set(HubMenu::getTenantId, hubMenu.getTenantId())
                .set(HubMenu::getParentId, hubMenu.getParentId())
                .set(HubMenu::getMenuName, hubMenu.getMenuName())
                .set(HubMenu::getMenuOrder, hubMenu.getMenuOrder())
                .set(HubMenu::getMenuPermit, hubMenu.getMenuPermit())
                .set(HubMenu::getMenuPath, hubMenu.getMenuPath())
                .set(HubMenu::getMenuParam, hubMenu.getMenuParam())
                .set(HubMenu::getMenuType, hubMenu.getMenuType())
                .set(HubMenu::getMenuIcon, hubMenu.getMenuIcon())
                .set(HubMenu::getMenuStatus, hubMenu.getMenuStatus())
                .set(HubMenu::getComponent, hubMenu.getComponent())
                .set(HubMenu::getIsFrame, hubMenu.getIsFrame())
                .set(HubMenu::getIsCache, hubMenu.getIsCache())
                .set(HubMenu::getIsVisible, hubMenu.getIsVisible())
                .set(HubMenu::getIsProtected, hubMenu.getIsProtected())
                .set(HubMenu::getRemark, hubMenu.getRemark())
                .set(HubMenu::getUpdateBy, Access.userCode())
                .set(HubMenu::getUpdateTime, new Date())
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
