package com.cowave.hub.admin.domain.auth.entity.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class GithubToken {

    /**
     * 令牌类型
     */
    @JsonProperty("token_type")
    private String tokenType;

    /**
     * GitHub Access Token
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 授权范围
     */
    private String scope;
}
