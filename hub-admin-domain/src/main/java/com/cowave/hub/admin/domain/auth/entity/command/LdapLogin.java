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
public class LdapLogin {

    /**
     * LDAP 账号
     */
    @NotBlank(message = "{admin.user.account.null}")
    private String userAccount;

    /**
     * LDAP 密码
     */
    @Sensitive
    @NotBlank(message = "{admin.user.passwd.null}")
    private String passWord;
}
