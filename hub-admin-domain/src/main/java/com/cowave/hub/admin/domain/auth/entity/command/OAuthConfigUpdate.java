package com.cowave.hub.admin.domain.auth.entity.command;

import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * OAuth提供方配置修改
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class OAuthConfigUpdate {

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 授权服务地址
     */
    private String authUrl;

    /**
     * 回调地址
     */
    private String redirectUrl;

    /**
     * 授权范围
     */
    private String authScope;

    /**
     * 状态，0关闭、1开启
     */
    @NotNull(message = "{admin.auth.oauth.status.null}")
    private EnableStatus status;
}
