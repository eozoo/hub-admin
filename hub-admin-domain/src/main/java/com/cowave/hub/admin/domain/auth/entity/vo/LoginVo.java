/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.domain.auth.entity.vo;

import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.access.annotation.Sensitive;
import com.cowave.hub.admin.domain.auth.enums.ProviderType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class LoginVo {

    /**
     * Access Token或MFA临时令牌
     */
    @Sensitive
    private String accessToken;

    /**
     * Refresh Token，MFA第一阶段为空
     */
    @Sensitive
    private String refreshToken;

    /**
     * 是否需要MFA二次认证
     */
    private boolean mfaRequired;

    /**
     * 是否需要绑定本地账号
     */
    private boolean bindRequired;

    /**
     * 绑定凭证
     */
    @Sensitive
    private String bindToken;

    /**
     * 绑定类型
     */
    private ProviderType bindType;

    /**
     * 绑定账号
     */
    private String bindAccount;

    /**
     * 绑定名称
     */
    private String bindName;

    public static LoginVo from(AccessUserDetails userDetails) {
        LoginVo loginVo = new LoginVo();
        loginVo.setAccessToken(userDetails.getAccessToken());
        loginVo.setRefreshToken(userDetails.getRefreshToken());
        loginVo.setMfaRequired(userDetails.isMfaRequired());
        return loginVo;
    }
}
