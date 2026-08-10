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
package com.cowave.hub.admin.domain.auth.entity.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class OAuth2TokenReq {

    /**
     * 应用id
     */
    @NotBlank(message = "client_id is required")
    @JsonProperty("client_id")
    private String clientId;

    /**
     * 应用密钥（如果密钥无法保证安全需要额外添加PKCE机制）
     */
    @NotBlank(message = "client_secret is required")
    @JsonProperty("client_secret")
    private String clientSecret;

    /**
     * 回调地址，需要一致（防止重定向攻击）
     */
    @NotBlank(message = "redirect_uri is required")
    @JsonProperty("redirect_uri")
    private String redirectUri;

    /**
     * 授权码
     */
    @NotBlank(message = "code is required")
    private String code;

    /**
     * PKCE
     */
    @JsonProperty("code_verifier")
    private String codeVerifier;

    /**
     * 授权范围
     */
    private String scope;
}
