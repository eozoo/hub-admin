/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.domain.rbac2.entity.pto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class TenantAccessPto {

    /**
     * 当前租户 ID
     */
    private Integer tenantId;

    /**
     * 当前租户编码
     */
    private String tenantCode;

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
     * 主部门 ID
     */
    private Integer deptId;

    /**
     * 主部门编码
     */
    private String deptCode;

    /**
     * 主部门名称
     */
    private String deptName;
}
