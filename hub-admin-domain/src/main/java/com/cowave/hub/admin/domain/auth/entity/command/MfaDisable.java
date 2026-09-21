package com.cowave.hub.admin.domain.auth.entity.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class MfaDisable {

    /**
     * MFA口令
     */
    @NotBlank(message = "{admin.mfa.code.null}")
    private String mfaCode;
}
