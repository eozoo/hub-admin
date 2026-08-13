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

import com.cowave.hub.admin.SpringTest;
import com.cowave.hub.admin.domain.auth.entity.SysLdapUser;
import com.cowave.hub.admin.domain.auth.entity.SysOAuthUser;
import com.cowave.hub.admin.service.auth.support.MfaAuthVerifier;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.*;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_CAPTCHA;

/**
 * @author shanhuiming
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest extends SpringTest {

    /**
     * 注册 -> 登录 -> 访问 -> 刷新令牌 -> 退出
     * get /api/v1/auth/public/captcha/email
     * post /api/v1/auth/public/register
     * get /api/v1/auth/public/captcha
     * post /api/v1/auth/public/login
     * get /api/v1/auth/info
     * get /api/v1/auth/menus
     * get /api/v1/auth/public/refresh
     * delete /api/v1/auth/logout
     */
    @Order(1)
    @Test
    public void login() throws Exception {
        // 邮箱验证码
        mockGet("/api/v1/auth/public/captcha/email?email=shanhm1991@163.com");
        String emailCode = redisHelper.keys("hub-admin:auth:captcha:*").stream().findFirst().map(
                k -> k.replace("hub-admin:auth:captcha:", "")).orElse(null);
        // 注册用户
        String body = String.format("""
                {
                            "tenantId" : "cowave",
                            "userName" : "shanhm",
                            "userAccount" : "shanhm",
                            "userEmail" : "shanhm1991@163.com",
                            "captcha" : "%s"
                        }

                """, emailCode);
        MvcResult mvcResult = mockPost("/api/v1/auth/public/register", body);
        String passWord = readString(mvcResult, "/data");
        // 获取验证码
        mvcResult = mockGet("/api/v1/auth/public/captcha?tenantId=cowave");
        String captchaId = readString(mvcResult, "/data/uuid");
        String captcha = redisHelper.getValue(AUTH_CAPTCHA.formatted(captchaId));
        // 登录
        body = String.format("""
                {
                    "tenantId" : "cowave",
                    "userAccount" : "shanhm",
                    "passWord" : "%s",
                    "captchaId" : "%s",
                    "captcha" : "%s"
                }
                """, passWord, captchaId, captcha);
        mvcResult = mockPost("/api/v1/auth/public/login", body);
        String accessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        String refreshToken = readString(mvcResult, "/data/refreshToken");
        // 获取登录信息
        mockGet("/api/v1/auth/info", accessToken);
        // 获取权限菜单
        mockGet("/api/v1/auth/menus", accessToken);
        // 令牌刷新
        mvcResult = mockGet("/api/v1/auth/public/refresh?refreshToken=" + refreshToken);
        accessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }

    /**
     * 登录 -> 绑定MFA -> 重新登录 -> 二次认证 -> 访问 -> 解绑MFA -> 撤销Access -> 访问401 -> 令牌刷新 -> 访问200 -> 撤销refresh -> 访问401
     * post /api/v1/auth/public/logon
     * get /api/v1/profile/mfa
     * patch /api/v1/profile/mfa/enable
     * delete /api/v1/auth/logout
     * post /api/v1/auth/public/logon
     * post /api/v1/auth/public/mfa
     * post /api/v1/auth/online
     * patch /api/v1/profile/mfa/disable
     * delete /api/v1/auth/access
     * post /api/v1/auth/online
     * get /api/v1/auth/public/refresh
     * post /api/v1/auth/online
     * delete /api/v1/auth/refresh
     * post /api/v1/auth/online
     */
    @Order(2)
    @Test
    public void mfa() throws Exception {
        // 登录
        String body = """
                {
                "tenantId" : "cowave",
                "userAccount" : "liubei",
                "passWord" : "12345678"
                }
                """;
        MvcResult mvcResult = mockPost("/api/v1/auth/public/logon", body);
        String accessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        // 创建MFA密钥
        mvcResult = mockGet("/api/v1/profile/mfa", accessToken);
        String mfaKey = readString(mvcResult, "/data/mfaKey");
        String mfaCode = MfaAuthVerifier.generateCode(mfaKey);
        // MFA绑定
        body = String.format("""
                {
                    "mfaKey" : "%s",
                    "mfaCode" : "%s"
                }
                """, mfaKey, mfaCode);
        mockPatch("/api/v1/profile/mfa/enable", body, accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);

        /* ******************重新登录********************** */
        body = """
                {
                "tenantId" : "cowave",
                "userAccount" : "liubei",
                "passWord" : "12345678"
                }
                """;
        mvcResult = mockPost("/api/v1/auth/public/logon", body);
        String mfaToken = readString(mvcResult, "/data/accessToken");
        // MFA二次认证（mfaCode就直接用上面的）
        body = String.format("""
                {
                    "mfaToken" : "%s",
                    "mfaCode" : "%s"
                }
                """, mfaToken, mfaCode);
        mvcResult = mockPost("/api/v1/auth/public/mfa", body);
        accessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        String refreshToken = readString(mvcResult, "/data/refreshToken");
        // 获取在线用户
        mockPost("/api/v1/auth/online", "{}", accessToken);
        // MFA取消绑定（不能影响其它厕所）
        body = String.format("""
                {
                    "mfaKey" : "%s",
                    "mfaCode" : "%s"
                }
                """, mfaKey, mfaCode);
        mockPatch("/api/v1/profile/mfa/disable", body, accessToken);
        // 撤销Access令牌
        String accessType = readString(mvcResult, "/data/authType");
        String accessId = readString(mvcResult, "/data/accessId");
        String userAccount = readString(mvcResult, "/data/username");
        mockDelete("/api/v1/auth/access?type="
                + accessType + "&account=" + userAccount + "&id=" + accessId, accessToken);
        // 访问 401
        mockPost("/api/v1/auth/online", "{}", accessToken, 401);
        // 令牌刷新
        mvcResult = mockGet("/api/v1/auth/public/refresh?refreshToken=" + refreshToken);
        accessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        // 访问 200
        mockPost("/api/v1/auth/online", "{}", accessToken);
        // 撤销refresh
        mockDelete("/api/v1/auth/refresh?type=" + accessType + "&account=" + userAccount, accessToken);
        // 访问 401
        mockPost("/api/v1/auth/online", "{}", accessToken, 401);
    }

    /**
     * Gitlab回调登录 -> 访问 -> 令牌刷新 -> 退出
     * get /api/v1/auth/public/gitlab
     * get /api/v1/oauth/user
     * get /api/v1/auth/public/refresh
     * delete /api/v1/auth/logout
     */
    @Order(3)
    @Test
    public void gitlab() throws Exception {
        // Gitlab回调登录
        MvcResult mvcResult = mockGet("/api/v1/auth/public/gitlab?tenantId=cowave&code=test-auth-code");
        String accessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        String refreshToken = readString(mvcResult, "/data/refreshToken");
        // 获取gitlab用户列表
        mvcResult = mockGet("/api/v1/oauth/user?serverType=gitlab", accessToken);
        List<SysOAuthUser> userList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, userList.size());
        SysOAuthUser oAuthUser = userList.get(0);
        Assertions.assertEquals("gitlabtest", oAuthUser.getUserAccount());
        // 令牌刷新
        mvcResult = mockGet("/api/v1/auth/public/refresh?refreshToken=" + refreshToken);
        accessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }

    /**
     * LDAP登录 -> 访问 -> 令牌刷新 -> 退出
     * post /api/v1/auth/public/ldap
     * get /api/v1/ldap/user
     * get /api/v1/auth/public/refresh
     * delete /api/v1/auth/logout
     */
    @Order(4)
    @Test
    public void ldap() throws Exception {
        // LDAP登录
        String body = """
                {
                    "tenantId" : "cowave",
                    "userAccount" : "ldaptest",
                    "passWord" : "12345678"
                }
                """;
        MvcResult mvcResult = mockPost("/api/v1/auth/public/ldap", body);
        String accessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        String refreshToken = readString(mvcResult, "/data/refreshToken");
        // 获取ldap用户列表
        mvcResult = mockGet("/api/v1/ldap/user", accessToken);
        List<SysLdapUser> userList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, userList.size());
        SysLdapUser ldapUser = userList.get(0);
        Assertions.assertEquals("ldaptest", ldapUser.getUserAccount());
        // 令牌刷新
        mvcResult = mockGet("/api/v1/auth/public/refresh?refreshToken=" + refreshToken);
        accessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
