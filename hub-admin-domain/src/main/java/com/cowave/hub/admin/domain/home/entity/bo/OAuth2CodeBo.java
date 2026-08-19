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
package com.cowave.hub.admin.domain.home.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * @author shanhuiming
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OAuth2CodeBo {

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 令牌类型
     */
    private String authType;

    /**
     * 应用ID（授权码绑定，换token时校验一致性）
     */
    private String clientId;

    /**
     * 应用状态
     */
    private String state;

    /**
     * 回调地址
     */
    private String redirectUri;

    /**
     * PKCE code_challenge（授权请求时由客户端传入，换 token 时用于验证 code_verifier）
     */
    private String codeChallenge;

    /**
     * PKCE code_challenge_method（S256 / plain）
     */
    private String codeChallengeMethod;
}
