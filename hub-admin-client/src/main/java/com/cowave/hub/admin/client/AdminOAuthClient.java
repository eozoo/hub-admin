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
import com.cowave.zoo.http.client.invoke.codec.decoder.ResponseDecoder;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.hub.admin.client.request.OAuth2TokenRequest;
import com.cowave.hub.admin.client.dto.OAuthAppCardDto;
import com.cowave.hub.admin.client.dto.OAuthEntryDto;
import com.cowave.hub.admin.client.dto.MemberProfileDto;
import com.cowave.hub.admin.client.dto.UserProfileDto;

import java.util.List;

import static com.cowave.zoo.http.client.constants.HttpHeader.Authorization;

/**
 * @author shanhuiming
 */
@HttpClient(url = "${spring.access.oauth.oauthTokenUri}", decoder = ResponseDecoder.class)
public interface AdminOAuthClient {

    /**
     * 授权服务列表
     */
    @HttpLine("GET /admin/api/v1/home/oauth/list?tenantId={tenantId}")
    List<OAuthEntryDto> getOauthList(@HttpParam("tenantId") String tenantId);

    /**
     * 系统授权令牌
     */
    @HttpLine("POST /admin/api/v1/home/app/authorize/token")
    AccessUserDetails getAuthorizeToken(OAuth2TokenRequest request);

    /**
     * gitlab授权令牌
     */
    @HttpLine("GET /admin/api/v1/home/oauth/gitlab?tenantId={tenantId}&code={code}")
    AccessUserDetails gitlabAuthorizeToken(@HttpParam("tenantId") String tenantId, @HttpParam("code") String code);

    /**
     * 刷新授权令牌
     */
    @HttpLine("GET /admin/api/v1/home/app/authorize/refresh?refreshToken={refreshToken}")
    AccessUserDetails refreshAuthorizeToken(@HttpParam("refreshToken") String refreshToken);

    /**
     * 系统用户profile
     */
    @HttpHeaders({Authorization + ": {accessToken}"})
    @HttpLine("GET /admin/api/v1/profile")
    UserProfileDto getUserProfile(@HttpParam("accessToken") String accessToken);

    /**
     * 三方用户profile
     */
    @HttpHeaders({Authorization + ": {accessToken}"})
    @HttpLine("GET /admin/api/v1/home/oauth/member/profile")
    MemberProfileDto getMemberProfile(@HttpParam("accessToken") String accessToken);

    /**
     * 应用导航列表（匿名）
     */
    @HttpLine("GET /admin/api/v1/home/app/nav?tenantId={tenantId}")
    List<OAuthAppCardDto> getAppNav(@HttpParam("tenantId") String tenantId);

    /**
     * 应用导航列表（登录）
     */
    @HttpHeaders({Authorization + ": {accessToken}"})
    @HttpLine("GET /admin/api/v1/home/app/nav/authorized")
    List<OAuthAppCardDto> getAuthorizedAppNav(@HttpParam("accessToken") String accessToken);
}
