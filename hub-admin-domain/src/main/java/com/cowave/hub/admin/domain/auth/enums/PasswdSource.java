package com.cowave.hub.admin.domain.auth.enums;

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
public enum PasswdSource implements EnumVal<String> {

    /**
     * 首次创建
     */
    INITIAL("initial"),

    /**
     * 用户修改
     */
    SELF("self"),

    /**
     * 管理员重置
     */
    ADMIN("admin"),

    /**
     * 密码找回
     */
    RECOVERY("recovery");

    @EnumValue
    @JsonValue
    private final String val;
}
