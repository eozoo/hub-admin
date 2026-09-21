/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.domain.auth.entity.vo;

import com.cowave.hub.admin.domain.rbac2.entity.vo.Route;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class AuthVo {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 当前租户id
     */
    private Integer tenantId;

    /**
     * 当前租户编码
     */
    private String tenantCode;

    /**
     * 当前租户名称
     */
    private String tenantTitle;

    /**
     * 是否需要修改密码
     */
    private Integer needChangePasswd;

    /**
     * 当前角色编码
     */
    private List<String> roles = new ArrayList<>();

    /**
     * 当前权限编码
     */
    private List<String> permissions = new ArrayList<>();

    /**
     * 当前动态菜单
     */
    private List<Route> menus = new ArrayList<>();
}
