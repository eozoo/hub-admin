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
public enum TenantType implements EnumVal<String> {

    /**
     * 系统租户
     */
    SYSTEM("system"),

    /**
     * 普通租户
     */
    NORMAL("normal"),

    /**
     * 试用租户
     */
    TRIAL("trial"),

    /**
     * 公共访客租户
     */
    PUBLIC("public");

    @EnumValue
    @JsonValue
    private final String val;
}
