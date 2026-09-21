/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.domain.rbac2.entity.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@NoArgsConstructor
@Getter
@Setter
public class RouteMeta {
    /**
     * 页面标题
     */
    private String title;
    /**
     * 菜单图标
     */
    private String icon;
    /**
     * 是否禁用页面缓存
     */
    private boolean noCache;
    /**
     * 外部链接地址
     */
    private String link;

    public RouteMeta(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }

    public RouteMeta(String title, String icon, String link) {
        this.title = title;
        this.icon = icon;
        this.link = link;
    }

    public RouteMeta(String title, String icon, boolean noCache, String link) {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        this.link = link;
    }
}
