package com.cowave.hub.admin.domain.auth.entity.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class UserRegister {

    /**
     * 邮箱验证码
     */
    @NotBlank(message = "{admin.captcha.failed}")
    private String captcha;

    /**
     * 用户邮箱
     */
    @NotBlank(message = "{admin.user.email.null}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{admin.user.email.invalid}")
    private String userEmail;

    /**
     * 用户账号
     */
    @NotBlank(message = "{admin.user.account.null}")
    private String userAccount;

    /**
     * 用户名称
     */
    @NotBlank(message = "{admin.user.name.null}")
    private String userName;
}
