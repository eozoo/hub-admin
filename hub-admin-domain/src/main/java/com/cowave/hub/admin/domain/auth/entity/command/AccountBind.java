package com.cowave.hub.admin.domain.auth.entity.command;

import com.cowave.zoo.framework.access.annotation.Sensitive;
import com.cowave.hub.admin.domain.auth.enums.ProviderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class AccountBind {

    /**
     * 绑定凭证
     */
    @Sensitive
    @NotBlank(message = "{admin.auth.bind.expired}")
    private String bindToken;

    /**
     * 绑定类型：ldap 或 oauth
     */
    @NotNull(message = "{admin.auth.bind.expired}")
    private ProviderType bindType;

    /**
     * 用户账号
     */
    @NotBlank(message = "{admin.user.account.null}")
    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9_]{2,19}$", message = "{admin.auth.bind.account.invalid}")
    private String userAccount;

    /**
     * 用户名称
     */
    @NotBlank(message = "{admin.user.name.null}")
    @Size(max = 64, message = "{admin.auth.bind.name.invalid}")
    private String userName;

    /**
     * 用户密码
     */
    @Sensitive
    @NotBlank(message = "{admin.user.passwd.null}")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,72}$", message = "{admin.auth.bind.passwd.invalid}")
    private String passWord;

}
