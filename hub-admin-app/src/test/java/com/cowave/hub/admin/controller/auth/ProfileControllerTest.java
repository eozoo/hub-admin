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
import com.cowave.hub.admin.domain.auth.entity.vo.TokenVo;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

/**
 * @author shanhuiming
 */
public class ProfileControllerTest extends SpringTest {

    /**
     * 登录 -> 修改profile -> 修改密码 -> 重新登录 -> 获取Profile -> 恢复密码
     * post /api/v1/auth/public/logon
     * patch /api/v1/profile
     * patch /api/v1/profile/passwd
     * post /api/v1/auth/public/logon
     * get /api/v1/profile
     * patch /api/v1/profile/passwd
     */
    @Test
    public void profile() throws Exception {
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
        // 修改profile
        body = """
                {
                "userName" : "刘备",
                "userSex" : 2,
                "userPhone" : "18013840395",
                "userEmail" : "shanhm1991@163.com"
                }
                """;
        mockPatch("/api/v1/profile", body, accessToken);
        // 修改密码
        body = """
                {
                "oldPasswd" : "12345678",
                "newPasswd" : "123456"
                }
                """;
        mockPatch("/api/v1/profile/passwd", body, accessToken, 401);
        // 重新登录
        body = """
                {
                "tenantId" : "cowave",
                "userAccount" : "liubei",
                "passWord" : "123456"
                }
                """;
        mvcResult = mockPost("/api/v1/auth/public/logon", body);
        accessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        // 获取Profile
        mvcResult = mockGet("/api/v1/profile", accessToken);
        String userPhone = readString(mvcResult, "/data/userPhone");
        String userEmail = readString(mvcResult, "/data/userEmail");
        Assertions.assertEquals("18013840395", userPhone);
        Assertions.assertEquals("shanhm1991@163.com", userEmail);
        // 恢复密码（不能影响其它厕所）
        body = """
                {
                "oldPasswd" : "123456",
                "newPasswd" : "12345678"
                }
                """;
        mockPatch("/api/v1/profile/passwd", body, accessToken, 401);
    }

    /**
     * 登录 -> 获取用户权限集合 -> 创建ApiToken -> 获取ApiToken -> 验证 -> 删除ApiToken -> 验证 -> 退出登录
     * post /api/v1/auth/public/logon
     * get /api/v1/profile/api/permits
     * post /api/v1/profile/api/token
     * get /api/v1/profile/api/token
     * get /api/v1/user
     * delete /api/v1/profile/api/token/{tokenId}
     * get /api/v1/user
     * delete /api/v1/auth/logout
     */
    @Test
    public void apiToken() throws Exception {
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
        // 当前用户的权限树
        mockGet("/api/v1/profile/api/permits", accessToken);
        // 创建Api Token
        body = """
                {
                "tokenName" : "Test-ApiToken",
                "menuScopes" : [
                  {"permit":"sys:user:query", "scopeId":null}
                ]
                }
                """;
        mvcResult = mockPost("/api/v1/profile/api/token", body, accessToken);
        String apiToken = readString(mvcResult, "/data");
        // 获取Api Token列表
        mvcResult = mockGet("/api/v1/profile/api/token", accessToken);
        List<TokenVo> list = readData(mvcResult, "/data", new TypeReference<>() {});
        TokenVo tokenVo = list.get(0);
        Integer tokenId = tokenVo.getTokenId();
        // 验证一下Api Token
        mockGet("/api/v1/user", apiToken);
        // 删除Api Token
        mockDelete("/api/v1/profile/api/token/" + tokenId, accessToken);
        // 再次访问应该401
        mockGet("/api/v1/user", apiToken, 401);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
