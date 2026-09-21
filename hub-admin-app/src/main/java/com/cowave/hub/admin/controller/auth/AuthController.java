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
import com.cowave.hub.admin.service.auth.AuthService;
import com.cowave.hub.admin.service.auth.support.CaptchaService;
import com.cowave.hub.admin.service.auth.LdapService;
import com.cowave.hub.admin.domain.auth.enums.ProviderType;
import com.cowave.zoo.http.client.asserts.HttpException;
import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import com.cowave.hub.admin.service.auth.OAuthService;
import com.cowave.hub.admin.domain.auth.entity.command.LdapLogin;
import com.cowave.hub.admin.domain.auth.entity.command.AccountBind;
import com.cowave.hub.admin.domain.auth.entity.command.MfaLogin;
import com.cowave.hub.admin.domain.auth.entity.vo.AuthVo;
import com.cowave.hub.admin.domain.auth.entity.vo.LoginVo;
import com.cowave.hub.admin.domain.auth.entity.command.RefreshTokenCommand;
import com.cowave.hub.admin.domain.auth.entity.vo.CaptchaVo;
import com.cowave.hub.admin.domain.auth.entity.command.UserLogin;
import com.cowave.hub.admin.domain.auth.entity.command.UserRegister;
import com.cowave.hub.admin.domain.auth.entity.query.OnlineQuery;
import com.cowave.hub.admin.domain.auth.entity.vo.OnlineVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import java.io.IOException;

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
    public Response<CaptchaVo> captcha() throws IOException {
        return Response.success(captchaService.captcha());
    }

    /**
     * 邮箱验证码
     */
    @AnonymousGetMapping("/public/captcha/email")
    public Response<Void> captchaEmail(@Email(message = "{admin.user.email.invalid}") @RequestParam("email") String email) {
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
    @AnonymousPostMapping("/public/login")
    public Response<LoginVo> login(@Validated @RequestBody UserLogin userLogin) {
        captchaService.validCaptcha(userLogin.getCaptchaId(), userLogin.getCaptcha());
        return Response.success(authService.login(userLogin.getUserAccount(), userLogin.getPassWord()));
    }

    /**
     * MFA认证
     */
    @AnonymousPostMapping("/public/mfa")
    public Response<LoginVo> mfa(@Validated @RequestBody MfaLogin mfaLogin) {
        return Response.success(authService.mfa(mfaLogin.getMfaToken(), mfaLogin.getMfaCode()));
    }

    /**
     * Ldap认证
     */
    @AnonymousPostMapping("/public/ldap")
    public Response<LoginVo> ldap(@Validated @RequestBody LdapLogin login) {
        return Response.success(ldapService.authenticate(login.getUserAccount(), login.getPassWord()));
    }

    /**
     * 认证账号绑定
     */
    @AnonymousPostMapping("/public/bind")
    public Response<LoginVo> bind(@Validated @RequestBody AccountBind bind) {
        if (bind.getBindType() == ProviderType.LDAP) {
            return Response.success(ldapService.bindAccount(bind));
        }
        if (bind.getBindType() == ProviderType.OAUTH) {
            return Response.success(oauthService.bindAccount(bind));
        }
        throw new HttpException(BAD_REQUEST, "{admin.auth.bind.expired}");
    }

    /**
     * Gitlab回调认证
     */
    @AnonymousGetMapping("/public/gitlab")
    public Response<LoginVo> gitlabCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        return Response.success(oauthService.gitlabCallback(code, state));
    }

    /**
     * GitHub回调认证
     */
    @AnonymousGetMapping("/public/github")
    public Response<LoginVo> githubCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        return Response.success(oauthService.githubCallback(code, state));
    }

    /**
     * 微信回调认证
     */
    @AnonymousGetMapping("/public/wechat")
    public Response<LoginVo> wechatCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        return Response.success(oauthService.wechatCallback(code, state));
    }

    /**
     * QQ回调认证
     */
    @AnonymousGetMapping("/public/qq")
    public Response<LoginVo> qqCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        return Response.success(oauthService.qqCallback(code, state));
    }

    /**
     * Bilibili回调认证
     */
    @AnonymousGetMapping("/public/bilibili")
    public Response<LoginVo> bilibiliCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        return Response.success(oauthService.bilibiliCallback(code, state));
    }

    /**
     * 令牌刷新
     */
    @AnonymousPostMapping("/public/refresh")
    public Response<LoginVo> refresh(@Validated @RequestBody RefreshTokenCommand command) throws Exception {
        return Response.success(authService.refresh(command.getRefreshToken()));
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
     * 在线用户
     */
    @PreAuthorize("@permits.hasPermit('monitor:online:query')")
    @PostMapping("/online")
    public Response<Response.Page<OnlineVo>> onlineList(@RequestBody OnlineQuery query) {
        return Response.success(authService.onlineList(query));
    }

    /**
     * 撤销Access令牌
     */
    @PreAuthorize("@permits.hasPermit('monitor:online:force')")
    @DeleteMapping("/access")
    public Response<Void> revokeAccess(@RequestParam("id") String id, @RequestParam("account") String account,
                                       @RequestParam("sessionId") String sessionId) {
        authService.revokeAccess(account, sessionId, id);
        return Response.success();
    }

    /**
     * 撤销Refresh令牌
     */
    @PreAuthorize("@permits.hasPermit('monitor:online:force')")
    @DeleteMapping("/refresh")
    public Response<Void> revokeRefresh(
            @RequestParam("account") String account, @RequestParam("sessionId") String sessionId) {
        authService.revokeRefresh(account, sessionId);
        return Response.success();
    }
}
