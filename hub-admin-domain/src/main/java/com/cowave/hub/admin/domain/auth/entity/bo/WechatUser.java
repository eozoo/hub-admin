package com.cowave.hub.admin.domain.auth.entity.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class WechatUser {

    /**
     * 微信用户标识
     */
    private String openid;

    /**
     * 统一用户标识
     */
    private String unionid;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    @JsonProperty("headimgurl")
    private String avatarUrl;
}
