/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.domain.home.entity.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class OAuth2CodeReq {

    /**
     * 应用状态（避免CSRF攻击，用户已登录授权服务的场景中，攻击者诱使用户再次获取授权码）
     */
    private String state;

    /**
     * PKCE（无法保证应用密钥安全的场景中，攻击者劫持授权码code，结合应用密钥向授权服务兑换令牌）
     */
    @JsonProperty("code_challenge")
    private String codeChallenge;

    /**
     * PKCE
     */
    @JsonProperty("code_challenge_method")
    private String codeChallengeMethod;

    /**
     * 应用id
     */
    @NotBlank(message = "client_id is required")
    @JsonProperty("client_id")
    private String clientId;

    /**
     * 回调地址
     */
    @NotBlank(message = "redirect_uri is required")
    @JsonProperty("redirect_uri")
    private String redirectUri;

    /**
     * 响应类型
     */
    @NotBlank(message = "response_type is required")
    @JsonProperty("response_type")
    private String responseType;
}
