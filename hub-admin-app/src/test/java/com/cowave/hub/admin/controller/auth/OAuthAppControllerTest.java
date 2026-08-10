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
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.*;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

/**
 * @author shanhuiming
 */
public class OAuthAppControllerTest extends SpringTest {

    /**
     * 登录 -> 新增授权应用 -> 获取列表 -> 获取选项 -> 获取人员授权应用 -> 给角色授权 -> 获取角色授权应用 -> 获取应用菜单列表
     *     -> OAuth2授权码登录: 获取授权码 -> 应用回调 -> code换token -> 刷新令牌
     *     -> 撤销应用令牌 -> 刷新令牌失败 -> 删除授权应用 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/oauth/app
     * get /api/v1/oauth/app
     * get /api/v1/oauth/app/options
     * get /api/v1/oauth/app/card
     * post /api/v1/oauth/app/role
     * get /api/v1/oauth/app/role/{roleId}
     * get /api/v1/oauth/app/menu/{appId}
     * post /api/v1/oauth/client/authorize/code
     * get /api/v1/oauth/client/redirect/{code}
     * post /api/v1/oauth/client/authorize/token
     * get /api/v1/oauth/client/authorize/refresh
     * delete /api/v1/oauth/client/token
     * get /api/v1/oauth/client/authorize/refresh (fail)
     * delete /api/v1/oauth/app/{id}
     * delete /api/v1/auth/logout
     */
    @Test
    public void oauthApp() throws Exception {
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
        // 新增授权应用
        body = """
                {
                    "clientName": "测试OAuth应用",
                    "cardName": "测试卡片",
                    "cardIcon": "test-icon",
                    "grantType": ["authorization_code", "refresh_token"],
                    "authScope": ["read", "write"],
                    "redirectUrl": "http://localhost:8080/callback"
                }
                """;
        mvcResult = mockPost("/api/v1/oauth/app", body, accessToken);
        String clientId = readString(mvcResult, "/data/clientId");
        String clientSecret = readString(mvcResult, "/data/clientSecret");
        Integer appId = Integer.parseInt(readString(mvcResult, "/data/id"));
        Assertions.assertNotNull(clientId);
        Assertions.assertNotNull(clientSecret);
        Assertions.assertNotNull(appId);
        // 获取授权应用列表
        mockGet("/api/v1/oauth/app?clientName=测试", accessToken);
        // 获取授权应用选项
        mockGet("/api/v1/oauth/app/options", accessToken);
        // 获取人员授权应用
        mockGet("/api/v1/oauth/app/card", accessToken);
        // 给普通角色授权应用
        body = String.format("""
                {
                    "roleId": 3,
                    "appIdList": [%d]
                }
                """, appId);
        mockPost("/api/v1/oauth/app/role", body, accessToken);
        // 获取普通角色授权应用
        mvcResult = mockGet("/api/v1/oauth/app/role/3", accessToken);
        List<Integer> roleAppIds = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertTrue(roleAppIds.contains(appId));
        // 获取授权应用的菜单列表
        mockGet("/api/v1/oauth/app/menu/" + appId, accessToken);

        /* **************************************** */
        // ① 获取授权码（需要用户登录态）
        String redirectUri = "http://localhost:8080/callback";
        body = String.format("""
                {
                    "client_id": "%s",
                    "redirect_uri": "%s",
                    "response_type": "code"
                }
                """, clientId, redirectUri);
        mvcResult = mockPost("/api/v1/oauth/client/authorize/code", body, accessToken);
        String code = readString(mvcResult, "/data/code");
        Assertions.assertNotNull(code);
        // ② 应用回调（302跳转回客户端应用，携带code和state）
        MvcResult redirectResult = mockGet("/api/v1/oauth/client/redirect/" + code, null, 302);
        String location = redirectResult.getResponse().getHeader("Location");
        Assertions.assertNotNull(location);
        Assertions.assertTrue(location.startsWith(redirectUri));
        Assertions.assertTrue(location.contains("code=" + code));
        // ③ code换token
        body = String.format("""
                {
                    "client_id": "%s",
                    "client_secret": "%s",
                    "redirect_uri": "%s",
                    "code": "%s"
                }
                """, clientId, clientSecret, redirectUri, code);
        mvcResult = mockPost("/api/v1/oauth/client/authorize/token", body);
        String oauthAccessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        String refreshToken = readString(mvcResult, "/data/refreshToken");
        Assertions.assertNotNull(oauthAccessToken);
        Assertions.assertNotNull(refreshToken);
        // ④ 刷新令牌
        mvcResult = mockGet("/api/v1/oauth/client/authorize/refresh?refreshToken=" + refreshToken);
        refreshToken = readString(mvcResult, "/data/refreshToken");

        /* **************************************** */
        // ⑤ 撤销应用令牌
        String authType = readString(mvcResult, "/data/authType");
        String username = readString(mvcResult, "/data/username");
        String oauthId = readString(mvcResult, "/data/oauthId");
        mockDelete("/api/v1/oauth/client/token?type=" + authType + "&account=" + username + "&id=" + oauthId, accessToken);
        // ⑥ 刷新令牌401
        mockGet("/api/v1/oauth/client/authorize/refresh?refreshToken=" + refreshToken, null, 401);
        // ⑦ 删除授权应用
        mockDelete("/api/v1/oauth/app/" + appId, accessToken);
        // ⑧ 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
