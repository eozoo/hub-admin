package com.cowave.hub.admin.domain.auth.entity.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class QqUser {

    /**
     * QQ用户标识
     */
    private String openid;

    /**
     * 返回码，0表示成功
     */
    private Integer ret;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * QQ大头像
     */
    @JsonProperty("figureurl_qq_2")
    private String avatarUrl;

    /**
     * QQ小头像
     */
    @JsonProperty("figureurl_qq_1")
    private String smallAvatarUrl;
}
