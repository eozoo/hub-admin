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

import com.cowave.hub.admin.domain.auth.enums.ProviderCode;
import com.cowave.hub.admin.domain.auth.entity.command.IdentityStatusUpdate;

import com.cowave.zoo.http.client.response.Response;
import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.command.OAuthConfigUpdate;
import com.cowave.hub.admin.domain.auth.entity.query.OAuthUserQuery;
import com.cowave.hub.admin.domain.auth.entity.vo.OAuthUserVo;
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
     * @param providerCode 提供方编码
     */
    @PreAuthorize("@permits.hasPermit('oauth:provider:query')")
    @GetMapping("/config/{providerCode}")
    public Response<SysAuthProvider> getOauth(@PathVariable("providerCode") String providerCode) {
        return Response.success(oauthService.getOauth(ProviderCode.of(providerCode)));
    }

    /**
     * 修改授权服务配置
     */
    @PreAuthorize("@permits.hasPermit('oauth:provider:edit')")
    @PatchMapping("/config/{providerCode}")
    public Response<Void> editOauth(@PathVariable("providerCode") String providerCode,
                                    @Validated @RequestBody OAuthConfigUpdate oauthConfig) {
        oauthService.editOauth(ProviderCode.of(providerCode), oauthConfig);
        return Response.success();
    }

    /**
     * 用户列表
     */
    @PreAuthorize("@permits.hasPermit('oauth:provider:user:query')")
    @GetMapping("/user/{providerCode}")
    public Response<Response.Page<OAuthUserVo>> listUser(
            @PathVariable("providerCode") String providerCode, OAuthUserQuery userQuery) {
        return Response.page(oauthService.listUser(ProviderCode.of(providerCode), userQuery));
    }

    /**
     * 修改OAuth身份状态
     */
    @PreAuthorize("@permits.hasPermit('oauth:provider:user:edit')")
    @PatchMapping("/user/{providerCode}/{identityId}/status")
    public Response<Void> updateIdentityStatus(@PathVariable("providerCode") String providerCode,
                                               @PathVariable("identityId") Long identityId,
                                               @Validated @RequestBody IdentityStatusUpdate command) {
        oauthService.updateIdentityStatus(ProviderCode.of(providerCode), identityId, command.getAuthStatus());
        return Response.success();
    }

    /**
     * 删除OAuth身份绑定
     */
    @PreAuthorize("@permits.hasPermit('oauth:provider:user:delete')")
    @DeleteMapping("/user/{providerCode}/{identityId}")
    public Response<Void> deleteIdentity(@PathVariable("providerCode") String providerCode,
                                         @PathVariable("identityId") Long identityId) {
        oauthService.deleteIdentity(ProviderCode.of(providerCode), identityId);
        return Response.success();
    }
}
