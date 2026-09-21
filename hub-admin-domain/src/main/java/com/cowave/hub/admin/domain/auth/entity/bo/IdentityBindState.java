package com.cowave.hub.admin.domain.auth.entity.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * 账号绑定授权上下文
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class IdentityBindState {

    /**
     * 发起绑定的用户ID
     */
    private Integer userId;

    /**
     * 发起绑定的提供方ID
     */
    private Integer providerId;
}
