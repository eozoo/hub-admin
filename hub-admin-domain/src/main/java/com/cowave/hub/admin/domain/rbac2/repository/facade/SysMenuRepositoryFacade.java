/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.domain.rbac2.repository.facade;

import com.cowave.hub.admin.domain.rbac2.entity.SysMenu;
import com.cowave.hub.admin.domain.rbac2.entity.pto.ApiPermitMenu;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysMenuRepositoryFacade {

    /**
     * 查询管理员菜单
     */
    List<SysMenu> queryMenusByAdmin(Integer tenantId);

    /**
     * 查询用户授权菜单
     */
    List<SysMenu> queryMenusByUser(Integer tenantId, Integer userId);

    /**
     * 查询当前用户可授权给 API 令牌的菜单及权限
     */
    List<ApiPermitMenu> queryApiPermits(Integer tenantId, Integer userId, boolean admin);
}
