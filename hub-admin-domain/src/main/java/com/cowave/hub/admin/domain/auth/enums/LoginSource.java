package com.cowave.hub.admin.domain.auth.enums;

import com.cowave.zoo.tools.EnumVal;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author shanhuiming
 */
@Getter
@RequiredArgsConstructor
public enum LoginSource implements EnumVal<String> {

    /**
     * 账号密码
     */
    PASSWORD("password"),

    /**
     * LDAP
     */
    LDAP("ldap"),

    /**
     * GitLab
     */
    GITLAB("gitlab"),

    /**
     * GitHub
     */
    GITHUB("github"),

    /**
     * 微信
     */
    WECHAT("wechat"),

    /**
     * QQ
     */
    QQ("qq"),

    /**
     * 哔哩哔哩
     */
    BILIBILI("bilibili");

    @JsonValue
    private final String val;

    public static LoginSource of(String value) {
        for (LoginSource source : values()) {
            if (source.getVal().equals(value)) {
                return source;
            }
        }
        return null;
    }
}
