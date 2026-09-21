/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.domain.rbac2.biz.impl;

import com.cowave.hub.admin.domain.rbac2.biz.SysMenuBiz;
import com.cowave.hub.admin.domain.rbac2.entity.SysMenu;
import com.cowave.hub.admin.domain.rbac2.entity.vo.Route;
import com.cowave.hub.admin.domain.rbac2.entity.vo.RouteMeta;
import com.cowave.hub.admin.domain.rbac2.repository.facade.SysMenuRepositoryFacade;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class SysMenuBiz2Impl implements SysMenuBiz {
    private final SysMenuRepositoryFacade menuRepositoryFacade;

    @Override
    public List<Route> buildRoutes(Integer tenantId, Integer userId, boolean admin) {
        List<SysMenu> menus = admin
                ? menuRepositoryFacade.queryMenusByAdmin(tenantId)
                : menuRepositoryFacade.queryMenusByUser(tenantId, userId);
        if (menus.isEmpty()) {
            return List.of();
        }

        List<SysMenu> roots = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (Objects.equals(menu.getParentId(), 0)) {
                fillChildren(menus, menu);
                roots.add(menu);
            }
        }
        return buildRoutes(roots);
    }

    private void fillChildren(List<SysMenu> menus, SysMenu parent) {
        List<SysMenu> children = menus.stream()
                .filter(menu -> parent.getMenuId().equals(menu.getParentId()))
                .toList();
        parent.setChildren(children);
        children.forEach(child -> fillChildren(menus, child));
    }

    private List<Route> buildRoutes(List<SysMenu> menus) {
        List<Route> routes = new LinkedList<>();
        for (SysMenu menu : menus) {
            Route route = new Route();
            route.setHidden("L".equals(menu.getMenuType()));
            route.setName(menu.routeName());
            route.setPath(menu.routePath());
            route.setComponent(menu.routeComponent());
            route.setQuery(menu.getMenuParam());
            route.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(),
                    false, menu.getMenuPath()));

            List<SysMenu> children = menu.getChildren();
            if (!children.isEmpty() && "M".equals(menu.getMenuType())) {
                route.setAlwaysShow(true);
                route.setRedirect("noRedirect");
                route.setChildren(buildRoutes(children));
            } else if (menu.ifMenuFrame()) {
                route.setMeta(null);
                Route child = new Route();
                child.setPath(menu.getMenuPath());
                child.setComponent(menu.getComponent());
                child.setName(StringUtils.capitalize(menu.getMenuPath()));
                child.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(),
                        false, menu.getMenuPath()));
                child.setQuery(menu.getMenuParam());
                route.setChildren(List.of(child));
            } else if (Objects.equals(menu.getParentId(), 0) && menu.ifInnerLink()) {
                route.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon()));
                route.setPath("/");
                Route child = new Route();
                String path = StringUtils.removeStart(menu.getMenuPath(), "http://");
                path = StringUtils.removeStart(path, "https://");
                child.setPath(path);
                child.setComponent("InnerLink");
                child.setName(StringUtils.capitalize(path));
                child.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), menu.getMenuPath()));
                route.setChildren(List.of(child));
            }
            routes.add(route);
        }
        return routes;
    }
}
