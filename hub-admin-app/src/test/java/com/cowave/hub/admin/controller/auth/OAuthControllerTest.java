///*
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0.txt
// *
// * Unless required by applicable law or agreed to in writing,
// * software distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and limitations under the License.
// */
//package com.cowave.hub.admin.controller.auth;
//
//import com.cowave.hub.admin.SpringTest;
//import com.cowave.hub.admin.domain.auth2.entity.SysAuthProvider;
//import com.cowave.hub.admin.domain.auth2.entity.command.GitlabConfigUpdate;
//import com.fasterxml.jackson.core.type.TypeReference;
//import org.junit.jupiter.api.*;
//import org.springframework.test.web.servlet.MvcResult;
//
///**
// * @author shanhuiming
// */
//public class OAuthControllerTest extends SpringTest {
//
//    /**
//     * 登录 -> 获取配置 -> 修改 -> 确认修改 -> 退出登录
//     * post /api/v1/auth/public/logon
//     * get /api/v1/oauth/config/{serverType}
//     * patch /api/v1/oauth/config
//     * get /api/v1/oauth/config/{serverType}
//     * delete /api/v1/auth/logout
//     */
//    @Test
//    public void info() throws Exception {
//        // 登录
//        String body = """
//                {
//                "tenantId" : "cowave",
//                "userAccount" : "liubei",
//                "passWord" : "12345678"
//                }
//                """;
//        MvcResult mvcResult = mockPost("/api/v1/auth/public/logon", body);
//        String accessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
//        // 获取授权服务配置
//        mvcResult = mockGet("/api/v1/oauth/config/gitlab", accessToken);
//        SysAuthProvider provider = readData(mvcResult, "/data", new TypeReference<>(){});
//        // 修改配置
//        GitlabConfigUpdate command = new GitlabConfigUpdate();
//        command.setClientId(provider.getClientId());
//        command.setClientSecret(provider.getClientSecret());
//        command.setAuthUrl(provider.getAuthUrl());
//        command.setRedirectUrl(provider.getRedirectUrl());
//        command.setAuthScope(provider.getAuthScope());
//        command.setStatus(provider.getStatus());
//        mockPatch("/api/v1/oauth/config", writeString(command), accessToken);
//        // 获取验证
//        mvcResult = mockGet("/api/v1/oauth/config/gitlab", accessToken);
//        String clientId = readString(mvcResult, "/data/clientId");
//        Assertions.assertEquals(provider.getClientId(), clientId);
//        // 退出登录
//        mockDelete("/api/v1/auth/logout", accessToken);
//    }
//}
