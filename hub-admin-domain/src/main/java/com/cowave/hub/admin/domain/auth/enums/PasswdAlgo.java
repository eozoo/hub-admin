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
public enum PasswdAlgo implements EnumVal<String> {

    /**
     * Bcrypt 哈希
     */
    BCRYPT("bcrypt"),

    /**
     * Argon2 哈希
     */
    ARGON2("argon2");

    @EnumValue
    @JsonValue
    private final String val;
}
