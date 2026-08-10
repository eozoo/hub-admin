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
import com.cowave.hub.admin.domain.auth.entity.HubLdap;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.*;
import org.springframework.test.web.servlet.MvcResult;

/**
 * @author shanhuiming
 */
public class LdapControllerTest extends SpringTest {

    /**
     * 登录 -> 获取配置 -> 修改测试 -> 提交修改 -> 退出登录
     * post /api/v1/auth/public/logon
     * get /api/v1/ldap
     * post /api/v1/ldap/valid
     * patch /api/v1/ldap
     * get /api/v1/ldap
     * delete /api/v1/auth/logout
     */
    @Test
    public void info() throws Exception {
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
        // 获取配置
        mvcResult = mockGet("/api/v1/ldap", accessToken);
        HubLdap hubLdap = readData(mvcResult, "/data", new TypeReference<>(){});
        // 测试配置
        mockPost("/api/v1/ldap/valid", writeString(hubLdap), accessToken);
        // 修改配置
        hubLdap.setRoleCode("sysAdmin");
        mockPatch("/api/v1/ldap", writeString(hubLdap), accessToken);
        // 获取验证
        mvcResult = mockGet("/api/v1/ldap", accessToken);
        String roleCode = readString(mvcResult, "/data/roleCode");
        Assertions.assertEquals("sysAdmin", roleCode);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
