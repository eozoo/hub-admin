/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.domain.rbac2.entity;

import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 租户用户关系
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysTenantUser {

    /**
     * 租户 ID
     */
    private Integer tenantId;

    /**
     * 全局用户 ID
     */
    private Integer userId;

    /**
     * 租户成员类型
     */
    private String userType;

    /**
     * 租户内用户编码
     */
    private String userCode;

    /**
     * 租户内展示名称
     */
    private String displayName;

    /**
     * 用户职级
     */
    private String userRank;

    /**
     * 成员启停状态
     */
    private EnableStatus status;

    /**
     * 是否默认租户
     */
    private Integer isDefault;

    /**
     * 加入租户时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date joinTime;

    /**
     * 离开租户时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date leaveTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人账号
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人账号
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
