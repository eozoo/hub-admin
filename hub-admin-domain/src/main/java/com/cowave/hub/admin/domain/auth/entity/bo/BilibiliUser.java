package com.cowave.hub.admin.domain.auth.entity.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class BilibiliUser {

    /**
     * 应用内稳定用户标识
     */
    private String openid;

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 用户头像
     */
    @JsonProperty("face")
    private String avatarUrl;
}
