package com.cowave.hub.admin.domain.auth.entity.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class MfaChallenge {

    /**
     * 本地账号
     */
    private String userAccount;

    /**
     * 外部身份id
     */
    private Long identityId;
}
