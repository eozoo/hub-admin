package com.cowave.hub.admin.domain.auth.entity.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class GithubUser {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户账号
     */
    private String login;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 用户头像
     */
    @JsonProperty("avatar_url")
    private String avatarUrl;

    /**
     * 用户邮箱
     */
    private String email;
}
