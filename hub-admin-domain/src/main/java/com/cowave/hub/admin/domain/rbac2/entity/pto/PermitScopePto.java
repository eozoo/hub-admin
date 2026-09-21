/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.domain.rbac2.entity.pto;

import lombok.Getter;
import lombok.Setter;

/**
 * 操作权限及数据范围
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class PermitScopePto {

    /**
     * 操作权限编码
     */
    private String permit;

    /**
     * 数据范围 ID
     */
    private Integer scopeId;
}
