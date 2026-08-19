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
package com.cowave.hub.admin.controller.auth;

import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.hub.admin.domain.auth.entity.SysOAuth;
import com.cowave.hub.admin.domain.auth.entity.SysOAuthUser;
import com.cowave.hub.admin.domain.auth.entity.query.OAuthUserQuery;
import com.cowave.hub.admin.service.auth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * OAuth授权
 * @order 11
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth")
public class OAuthController {

    private final OAuthService oauthService;

    /**
     * 获取授权服务配置
     *
     * @param serverType 服务类型
     */
    @PreAuthorize("@permits.hasPermit('oauth:gitlab:query')")
    @GetMapping("/config/{serverType}")
    public Response<SysOAuth> getOauth(@PathVariable String serverType) {
        return Response.success(oauthService.getOauth(Access.tenantId(), serverType));
    }

    /**
     * 修改授权服务配置
     */
    @PreAuthorize("@permits.hasPermit('oauth:gitlab:edit')")
    @PatchMapping("/config")
    public Response<Void> editOauth(@RequestBody SysOAuth oauth) {
        oauthService.editOauth(Access.tenantId(), oauth);
        return Response.success();
    }

    /**
     * 用户列表
     */
    @PreAuthorize("@permits.hasPermit('oauth:gitlab:user:query')")
    @GetMapping("/user")
    public Response<Response.Page<SysOAuthUser>> listUser(OAuthUserQuery query) {
        return Response.page(oauthService.listUser(Access.tenantId(), query));
    }
}
