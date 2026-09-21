package com.cowave.hub.admin.domain.auth.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.cowave.zoo.tools.EnumVal;
import com.cowave.zoo.http.client.asserts.HttpException;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;

/**
 * @author shanhuiming
 */
@Getter
@RequiredArgsConstructor
public enum ProviderCode implements EnumVal<String> {

    /**
     * LDAP认证方式
     */
    LDAP("ldap"),

    /**
     * GitLab认证提供方
     */
    GITLAB("gitlab"),

    /**
     * GitHub认证提供方
     */
    GITHUB("github"),

    /**
     * 微信认证提供方
     */
    WECHAT("wechat"),

    /**
     * QQ认证提供方
     */
    QQ("qq"),

    /**
     * 哔哩哔哩认证提供方
     */
    BILIBILI("bilibili"),

    /**
     * 控维认证提供方
     */
    COWAVE("cowave"),

    /**
     * 邮箱入口
     */
    EMAIL("email");

    @EnumValue
    @JsonValue
    private final String val;

    public static ProviderCode of(String code) {
        for (ProviderCode providerCode : values()) {
            if (providerCode.getVal().equals(code)) {
                return providerCode;
            }
        }
        throw new HttpException(BAD_REQUEST, "{admin.auth.provider.unsupported}");
    }
}
