/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.infra.rbac2.dao;

import com.cowave.hub.admin.domain.rbac2.entity.SysMenu;
import com.cowave.hub.admin.domain.rbac2.entity.pto.ApiPermitMenu;
import com.cowave.hub.admin.domain.rbac2.repository.SysMenuRepository;
import com.cowave.hub.admin.infra.rbac2.mapper.SysMenu2Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class SysMenu2Dao implements SysMenuRepository {

    private final SysMenu2Mapper menuMapper;

    @Override
    public List<SysMenu> queryMenusByAdmin(Integer tenantId) {
        return menuMapper.selectMenusByAdmin(tenantId);
    }

    @Override
    public List<SysMenu> queryMenusByUser(Integer tenantId, Integer userId) {
        return menuMapper.selectMenusByUser(tenantId, userId);
    }

    @Override
    public List<ApiPermitMenu> queryApiPermits(Integer tenantId, Integer userId, boolean admin) {
        List<ApiPermitMenu> menus = admin
                ? menuMapper.selectApiPermitsByAdmin(tenantId)
                : menuMapper.selectApiPermitsByUser(tenantId, userId);
        for (ApiPermitMenu menu : menus) {
            menu.setScopes(admin
                    ? menuMapper.selectApiScopes(tenantId, menu.getMenuId())
                    : menuMapper.selectApiScopesByUser(tenantId, userId, menu.getMenuId()));
            if (!admin && !menu.getScopes().isEmpty()) {
                menu.setScopeId(menu.getScopes().get(0).getScopeId());
            }
        }
        return menus;
    }
}
