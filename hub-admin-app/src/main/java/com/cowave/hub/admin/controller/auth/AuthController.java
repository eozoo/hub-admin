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
import com.cowave.hub.admin.service.auth.AuthService;
import com.cowave.hub.admin.service.auth.support.CaptchaService;
import com.cowave.hub.admin.service.auth.LdapService;
import com.cowave.hub.admin.service.auth.OAuthService;
import com.cowave.hub.admin.domain.auth.entity.command.LdapLogin;
import com.cowave.hub.admin.domain.auth.entity.command.MfaLogin;
import com.cowave.hub.admin.domain.auth.entity.vo.AuthVo;
import com.cowave.hub.admin.domain.auth.entity.vo.CaptchaVo;
import com.cowave.hub.admin.domain.auth.entity.command.UserLogin;
import com.cowave.hub.admin.domain.auth.entity.command.UserRegister;
import com.cowave.hub.admin.domain.auth.entity.vo.OnlineVo;
import com.cowave.hub.admin.domain.rbac.entity.vo.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * 鉴权
 * @order 9
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final CaptchaService captchaService;
    private final AuthService authService;
    private final LdapService ldapService;
    private final OAuthService oauthService;

    /**
     * 验证码
     */
    @AnonymousGetMapping("/public/captcha")
    public Response<CaptchaVo> captcha(@NotNull(message = "{admin.tenant.id.null}") String tenantId) throws IOException {
        return Response.success(captchaService.captcha(tenantId));
    }

    /**
     * 邮箱验证码
     */
    @AnonymousGetMapping("/public/captcha/email")
    public Response<Void> captchaEmail(@NotNull(message = "{admin.user.email.null}") String email) {
        captchaService.captchaEmail(email);
        return Response.success();
    }

    /**
     * 注册
     */
    @AnonymousPostMapping("/public/register")
    public Response<String> register(@Validated @RequestBody UserRegister userRegister) {
        captchaService.validEmail(userRegister.getUserEmail(), userRegister.getCaptcha());
        return Response.success(authService.register(userRegister));
    }

    /**
     * 登录
     */
    @AnonymousPostMapping("/public/logon")
    public Response<AccessUserDetails> logon(@Validated @RequestBody UserLogin userLogin) {
        return Response.success(authService.login(userLogin.getTenantId(), userLogin.getUserAccount(), userLogin.getPassWord()));
    }

    /**
     * 登录（验证码）
     */
    @AnonymousPostMapping("/public/login")
    public Response<AccessUserDetails> login(@Validated @RequestBody UserLogin userLogin) {
        captchaService.validCaptcha(userLogin.getTenantId(), userLogin.getCaptchaId(), userLogin.getCaptcha());
        return Response.success(authService.login(userLogin.getTenantId(), userLogin.getUserAccount(), userLogin.getPassWord()));
    }

    /**
     * MFA认证
     */
    @AnonymousPostMapping("/public/mfa")
    public Response<AccessUserDetails> mfa(@Validated @RequestBody MfaLogin mfaLogin) {
        return Response.success(authService.mfa(mfaLogin.getMfaToken(), mfaLogin.getMfaCode()));
    }

    /**
     * Ldap认证
     */
    @AnonymousPostMapping("/public/ldap")
    public Response<AccessUserDetails> ldap(@Validated @RequestBody LdapLogin login) {
        return Response.success(ldapService.authenticate(login.getTenantId(), login.getUserAccount(), login.getPassWord()));
    }

    /**
     * Gitlab回调认证
     */
    @AnonymousGetMapping("/public/gitlab")
    public Response<AccessUserDetails> gitlabCallback(
            @RequestParam("tenantId") String tenantId, @RequestParam("code") String code) {
        return Response.success(oauthService.gitlabCallback(tenantId, code));
    }

    /**
     * 令牌刷新
     */
    @AnonymousGetMapping("/public/refresh")
    public Response<AccessUserDetails> refresh(
            @NotNull(message = "{admin.refreshToken.null}") String refreshToken) throws Exception {
        return Response.success(authService.refresh(refreshToken));
    }

    /**
     * 退出
     */
    @DeleteMapping("/logout")
    public Response<Void> logout() throws IOException {
        authService.logout();
        return Response.success();
    }

    /**
     * 登录信息
     */
    @GetMapping("/info")
    public Response<AuthVo> getAuth() throws Exception {
        return Response.success(authService.getAuth());
    }

    /**
     * 菜单权限
     */
    @GetMapping("/menus")
    public Response<List<Route>> routes() {
        return Response.success(authService.menus());
    }

    /**
     * 在线用户
     */
    @PostMapping("/online")
    public Response<Response.Page<OnlineVo>> onlineList() {
        return Response.page(authService.onlineList());
    }

    /**
     * 撤销Access令牌
     */
    @PreAuthorize("@permits.hasPermit('monitor:online:force')")
    @DeleteMapping("/access")
    public Response<Void> revokeAccess(String type, String account, String id) {
        authService.revokeAccess(Access.tenantId(), type, account, id);
        return Response.success();
    }

    /**
     * 撤销Refresh令牌
     */
    @PreAuthorize("@permits.hasPermit('monitor:online:force')")
    @DeleteMapping("/refresh")
    public Response<Void> revokeRefresh(String type, String account) {
        authService.revokeRefresh(Access.tenantId(), type, account);
        return Response.success();
    }
}
