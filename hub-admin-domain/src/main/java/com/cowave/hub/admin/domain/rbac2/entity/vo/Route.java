/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.domain.rbac2.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shanhuiming
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Route {
    /**
     * 路由名称
     */
    private String name;
    /**
     * 路由路径
     */
    private String path;
    /**
     * 是否隐藏菜单
     */
    private boolean hidden;
    /**
     * 重定向地址
     */
    private String redirect;
    /**
     * 前端组件路径
     */
    private String component;
    /**
     * 路由查询参数
     */
    private String query;
    /**
     * 是否始终显示父菜单
     */
    private Boolean alwaysShow;
    /**
     * 路由展示元信息
     */
    private RouteMeta meta;
    /**
     * 子路由
     */
    private List<Route> children;
}
