package com.cowave.hub.admin.domain.auth.entity.query;

import lombok.Getter;
import lombok.Setter;

/**
 * OAuth提供方身份查询
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class OAuthUserQuery {

    /**
     * OAuth提供方账号
     */
    private String userAccount;
}
