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
public enum ProviderType implements EnumVal<String> {

    /**
     * LDAP身份
     */
    LDAP("ldap"),

    /**
     * OAuth2身份
     */
    OAUTH("oauth");

    @EnumValue
    @JsonValue
    private final String val;
}
