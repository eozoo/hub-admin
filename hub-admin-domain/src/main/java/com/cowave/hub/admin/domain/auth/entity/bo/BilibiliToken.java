package com.cowave.hub.admin.domain.auth.entity.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class BilibiliToken {

    /**
     * 访问令牌
     */
    @JsonProperty("access_token")
    private String accessToken;
}
