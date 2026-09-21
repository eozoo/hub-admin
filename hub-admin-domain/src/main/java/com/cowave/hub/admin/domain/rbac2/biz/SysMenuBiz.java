/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.domain.rbac2.biz;

import com.cowave.hub.admin.domain.rbac2.entity.vo.Route;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysMenuBiz {

    /**
     * 构建用户动态路由
     */
    List<Route> buildRoutes(Integer tenantId, Integer userId, boolean admin);
}
