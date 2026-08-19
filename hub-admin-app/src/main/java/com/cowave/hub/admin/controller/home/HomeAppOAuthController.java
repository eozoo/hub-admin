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
package com.cowave.hub.admin.controller.home;

import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.annotation.AnonymousGetMapping;
import com.cowave.zoo.framework.access.annotation.AnonymousPostMapping;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.hub.admin.domain.home.entity.command.OAuth2CodeReq;
import com.cowave.hub.admin.domain.home.entity.command.OAuth2TokenReq;
import com.cowave.hub.admin.domain.home.entity.vo.OAuth2CodeVo;
import com.cowave.hub.admin.service.home.HomeAppOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Home门户 OAuth应用授权
 * @order 23
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/home/app/authorize")
public class HomeAppOAuthController {

    private final HomeAppOAuthService homeAppOauthService;

    /**
     * 应用获取授权码
     */
    @PostMapping("/code")
    public Response<OAuth2CodeVo> getClientCode(@Validated @RequestBody OAuth2CodeReq codeReq){
        return Response.success(homeAppOauthService.getClientCode(codeReq));
    }

    /**
     * 应用回调（用户确认）
     */
    @AnonymousGetMapping("/redirect/{code}")
    public void clientRedirect(@PathVariable String code, HttpServletResponse response) throws IOException {
        homeAppOauthService.clientRedirect(code, response);
    }

    /**
     * 应用获取令牌
     */
    @AnonymousPostMapping("/token")
    public Response<AccessUserDetails> getClientToken(@Validated @RequestBody OAuth2TokenReq tokenReq){
        return Response.success(homeAppOauthService.getClientToken(tokenReq));
    }

    /**
     * 应用刷新令牌
     */
    @AnonymousGetMapping("/refresh")
    public Response<AccessUserDetails> refreshClientToken(
            @NotNull(message = "{admin.refreshToken.null}") String refreshToken) {
        return Response.success(homeAppOauthService.refreshClientToken(refreshToken));
    }

    /**
     * 撤销应用令牌
     */
    @PreAuthorize("@permits.hasPermit('monitor:online:force')")
    @DeleteMapping("/token")
    public Response<Void> revokeClientToken(String type, String account, String id) {
        homeAppOauthService.revokeClientToken(Access.tenantId(), type, account, id);
        return Response.success();
    }
}
