package com.cowave.hub.admin.domain.auth.entity.command;

import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class IdentityStatusUpdate {

    /**
     * 身份启用状态
     */
    @NotNull(message = "{admin.auth.oauth.status.null}")
    private EnableStatus authStatus;
}
