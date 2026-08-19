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

import com.cowave.zoo.http.client.annotation.*;
import com.cowave.zoo.http.client.response.HttpResponse;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.hub.admin.client.request.OAuth2TokenRequest;
import com.cowave.hub.admin.client.dto.OAuthAppCardDto;
import com.cowave.hub.admin.client.dto.OAuthEntryDto;
import com.cowave.hub.admin.client.dto.UserProfileDto;

import java.util.List;

import static com.cowave.zoo.http.client.constants.HttpHeader.Authorization;

/**
 * @author shanhuiming
 */
@HttpClient
public interface AdminOAuthClient {

    /**
     * 获取授权令牌
     */
    @HttpLine("POST /admin/api/v1/oauth/client/authorize/token")
    Response<AccessUserDetails> getAuthorizeToken(@HttpHost String httpUrl, OAuth2TokenRequest request);

    /**
     * 刷新授权令牌
     */
    @HttpLine("GET /admin/api/v1/oauth/client/authorize/refresh?refreshToken={refreshToken}")
    Response<AccessUserDetails> refreshAuthorizeToken(@HttpHost String httpUrl, @HttpParam("refreshToken") String refreshToken);

    /**
     * 获取用户信息
     */
    @HttpHeaders({Authorization + ": {accessToken}"})
    @HttpLine("GET /admin/api/v1/profile")
    Response<UserProfileDto> getUserProfile(@HttpHost String httpUrl, @HttpParam("accessToken") String accessToken);

    /**
     * 获取应用卡片
     */
    @HttpHeaders({Authorization + ": {accessToken}"})
    @HttpLine("GET /admin/api/v1/oauth/app/card")
    HttpResponse<Response<List<OAuthAppCardDto>>> getOauthAppCards(@HttpHost String httpUrl, @HttpParam("accessToken") String accessToken);

    /**
     * 三方授权方式列表
     */
    @HttpLine("GET /admin/api/v1/member/oauth/list?tenantId={tenantId}")
    Response<List<OAuthEntryDto>> getMemberOauthApps(@HttpHost String httpUrl, @HttpParam("tenantId") String tenantId);

    /**
     * Gitlab回调认证
     */
    @HttpLine("GET /admin/api/v1/member/oauth/gitlab?tenantId={tenantId}&code={code}")
    Response<AccessUserDetails> getMemberGitlabToken(@HttpHost String httpUrl, @HttpParam("tenantId") String tenantId, @HttpParam("code") String code);

}
