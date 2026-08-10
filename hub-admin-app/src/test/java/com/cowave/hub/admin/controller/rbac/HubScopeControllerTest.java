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
package com.cowave.hub.admin.controller.rbac;

import com.cowave.hub.admin.SpringTest;
import com.cowave.hub.admin.domain.rbac.entity.HubScope;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

/**
 * @author shanhuiming
 */
public class HubScopeControllerTest extends SpringTest {

    /**
     * 登录 -> 新增数据权限 -> 列表验证 -> 详情 -> 修改名称 -> 详情验证 -> 修改状态 -> 修改权限内容
     *     -> 详情验证内容 -> 删除 -> 列表验证删除 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/scope
     * get /api/v1/scope
     * get /api/v1/scope/{scopeId}
     * patch /api/v1/scope
     * get /api/v1/scope/{scopeId}
     * patch /api/v1/scope/status
     * patch /api/v1/scope/content
     * get /api/v1/scope/{scopeId}
     * delete /api/v1/scope/{scopeIds}
     * get /api/v1/scope
     * delete /api/v1/auth/logout
     */
    @Test
    public void scopeCrud() throws Exception {
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
        // 新增数据权限
        String scopeModule = "test_scope_crud";
        String scopeName = "test-scope-crud";
        body = """
                {
                    "scopeModule": "%s",
                    "scopeName": "%s",
                    "scopeStatus": 1,
                    "remark": "单元测试数据权限"
                }
                """.formatted(scopeModule, scopeName);
        mockPost("/api/v1/scope", body, accessToken);
        // 列表，验证新增
        mvcResult = mockGet("/api/v1/scope?scopeModule=" + scopeModule + "&page=1&pageSize=100", accessToken);
        List<HubScope> scopeList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, scopeList.size());
        HubScope scope = scopeList.get(0);
        Integer scopeId = scope.getScopeId();
        Assertions.assertEquals(scopeModule, scope.getScopeModule());
        Assertions.assertEquals(scopeName, scope.getScopeName());
        Assertions.assertEquals(1, scope.getScopeStatus().intValue());
        // 详情
        mvcResult = mockGet("/api/v1/scope/" + scopeId, accessToken);
        HubScope info = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(scopeId, info.getScopeId());
        Assertions.assertEquals(scopeModule, info.getScopeModule());
        Assertions.assertEquals(scopeName, info.getScopeName());
        // 修改名称
        String updatedName = "test-scope-crud-updated";
        body = """
                {
                    "scopeId": %d,
                    "scopeName": "%s"
                }
                """.formatted(scopeId, updatedName);
        mockPatch("/api/v1/scope", body, accessToken);
        // 详情，验证名称修改
        mvcResult = mockGet("/api/v1/scope/" + scopeId, accessToken);
        info = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(updatedName, info.getScopeName());
        // 修改状态（停用）
        body = """
                {
                    "scopeId": %d,
                    "scopeStatus": 0
                }
                """.formatted(scopeId);
        mockPatch("/api/v1/scope/status", body, accessToken);
        // 修改权限内容
        body = """
                {
                    "scopeId": %d,
                    "scopeContent": {
                        "deptScope": "ALL",
                        "userScope": "SELF"
                    }
                }
                """.formatted(scopeId);
        mockPatch("/api/v1/scope/content", body, accessToken);
        // 详情，验证内容修改
        mvcResult = mockGet("/api/v1/scope/" + scopeId, accessToken);
        info = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertNotNull(info.getScopeContent());
        Assertions.assertEquals("ALL", info.getScopeContent().get("deptScope"));
        Assertions.assertEquals("SELF", info.getScopeContent().get("userScope"));
        // 删除数据权限
        mockDelete("/api/v1/scope/" + scopeId, accessToken);
        // 列表，验证删除
        mvcResult = mockGet("/api/v1/scope?scopeModule=" + scopeModule + "&page=1&pageSize=100", accessToken);
        scopeList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(0, scopeList.size());
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
