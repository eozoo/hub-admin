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
package com.cowave.hub.admin.client;

import com.cowave.zoo.http.client.response.HttpResponse;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.AccessProperties;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.hub.admin.client.request.OAuth2TokenRequest;
import com.cowave.hub.admin.client.dto.OAuthAppCardDto;
import com.cowave.hub.admin.client.dto.OAuthEntryDto;
import com.cowave.hub.admin.client.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class AdminOAuthService {

    private final AccessProperties accessProperties;

    private final AdminOAuthClient adminOAuthClient;

    /**
     * 获取授权令牌
     */
    public AccessUserDetails getAuthorizeToken(String code) {
        return getAuthorizeToken(code, null);
    }

    /**
     * 获取授权令牌（支持 PKCE code_verifier）
     */
    public AccessUserDetails getAuthorizeToken(String code, String codeVerifier) {
        OAuth2TokenRequest tokenRequest = new OAuth2TokenRequest();
        tokenRequest.setCode(code);
        tokenRequest.setClientId(accessProperties.oauthAppId());
        tokenRequest.setClientSecret(accessProperties.oauthAppSecret());
        tokenRequest.setRedirectUri(accessProperties.oauthAppRedirectUri());
        tokenRequest.setCodeVerifier(codeVerifier);
        Response<AccessUserDetails> response =
                adminOAuthClient.getAuthorizeToken(accessProperties.oauthTokenUri(), tokenRequest);
        return response.getData();
    }

    /**
     * 刷新授权令牌
     */
    public AccessUserDetails refreshAuthorizeToken(String refreshToken) {
        Response<AccessUserDetails> response =
                adminOAuthClient.refreshAuthorizeToken(accessProperties.oauthTokenUri(), refreshToken);
        return response.getData();
    }

    /**
     * 获取应用卡片
     */
    public HttpResponse<Response<List<OAuthAppCardDto>>> getAuthorizedApps(String accessToken){
        return adminOAuthClient.getOauthAppCards(accessProperties.oauthTokenUri(), accessToken);
    }

    /**
     * 获取用户信息
     */
    public UserProfileDto getUserProfile(String accessToken) {
        Response<UserProfileDto> response =
                adminOAuthClient.getUserProfile(accessProperties.oauthTokenUri(), accessToken);
        return response.getData();
    }

    /**
     * 三方授权方式列表
     */
    public List<OAuthEntryDto> getMemberOauthList(String tenantId) {
        Response<List<OAuthEntryDto>> response =
                adminOAuthClient.getMemberOauthApps(accessProperties.oauthTokenUri(), tenantId);
        return response.getData();
    }

    /**
     * Gitlab回调认证
     */
    public AccessUserDetails getMemberGitlabToken(String tenantId, String code) {
        Response<AccessUserDetails> response =
                adminOAuthClient.getMemberGitlabToken(accessProperties.oauthTokenUri(), tenantId, code);
        return response.getData();
    }
}
