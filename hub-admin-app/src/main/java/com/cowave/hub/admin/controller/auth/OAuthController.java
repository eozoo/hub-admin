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

import com.cowave.zoo.framework.access.annotation.AnonymousGetMapping;
import com.cowave.zoo.framework.access.annotation.AnonymousPostMapping;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.hub.admin.domain.auth.entity.HubOAuth;
import com.cowave.hub.admin.domain.auth.entity.HubOAuthUser;
import com.cowave.hub.admin.domain.auth.entity.command.OAuth2CodeReq;
import com.cowave.hub.admin.domain.auth.entity.command.OAuth2TokenReq;
import com.cowave.hub.admin.domain.auth.entity.query.OAuthUserQuery;
import com.cowave.hub.admin.domain.auth.entity.vo.OAuth2CodeVo;
import com.cowave.hub.admin.service.auth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

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
    public Response<HubOAuth> getOauth(@PathVariable String serverType) {
        return Response.success(oauthService.getOauth(Access.tenantId(), serverType));
    }

    /**
     * 修改授权服务配置
     */
    @PreAuthorize("@permits.hasPermit('oauth:gitlab:edit')")
    @PatchMapping("/config")
    public Response<Void> editOauth(@RequestBody HubOAuth oauth) {
        oauthService.editOauth(Access.tenantId(), oauth);
        return Response.success();
    }

    /**
     * 用户列表
     */
    @PreAuthorize("@permits.hasPermit('oauth:gitlab:user:query')")
    @GetMapping("/user")
    public Response<Response.Page<HubOAuthUser>> listUser(OAuthUserQuery query) {
        return Response.page(oauthService.listUser(Access.tenantId(), query));
    }

    /**
     * 应用获取授权码
     */
	@PostMapping("/client/authorize/code")
	public Response<OAuth2CodeVo> getClientCode(@Validated @RequestBody OAuth2CodeReq codeCreate){
		return Response.success(oauthService.getClientCode(codeCreate));
	}

    /**
     * 应用回调
     */
	@AnonymousGetMapping("/client/redirect/{code}")
	public void clientRedirect(@PathVariable String code, HttpServletResponse response) throws IOException {
        oauthService.clientRedirect(code, response);
	}

    /**
     * 应用获取令牌
     */
    @AnonymousPostMapping("/client/authorize/token")
	public Response<AccessUserDetails> getClientToken(@Validated @RequestBody OAuth2TokenReq tokenCreate){
		return Response.success(oauthService.getClientToken(tokenCreate));
	}

    /**
     * 应用刷新令牌
     */
    @AnonymousGetMapping("/client/authorize/refresh")
    public Response<AccessUserDetails> refreshClientToken(
            @NotNull(message = "{admin.refreshToken.null}") String refreshToken) {
        return Response.success(oauthService.refreshClientToken(refreshToken));
    }

    /**
     * 撤销应用令牌
     */
    @PreAuthorize("@permits.hasPermit('monitor:online:force')")
    @DeleteMapping("/client/token")
    public Response<Void> revokeClientToken(String type, String account, String id) {
        oauthService.revokeClientToken(Access.tenantId(), type, account, id);
        return Response.success();
    }
}
