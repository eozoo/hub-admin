package com.cowave.hub.admin.domain.rbac2.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.cowave.zoo.tools.EnumVal;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author shanhuiming
 */
@Getter
@RequiredArgsConstructor
public enum RoleGrant implements EnumVal<String> {

    /**
     * 直接授权
     */
    DIRECT("direct");

    @EnumValue
    @JsonValue
    private final String val;
}
