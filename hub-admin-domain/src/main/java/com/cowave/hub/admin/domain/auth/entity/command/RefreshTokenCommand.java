package com.cowave.hub.admin.domain.auth.entity.command;

import com.cowave.zoo.framework.access.annotation.Sensitive;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class RefreshTokenCommand {

    /**
     * 刷新令牌
     */
    @NotBlank(message = "{admin.refreshToken.null}")
    @Sensitive
    private String refreshToken;
}
