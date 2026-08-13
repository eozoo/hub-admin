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
import com.cowave.hub.admin.domain.rbac.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac.entity.pto.TenantManagerPto;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

/**
 * @author shanhuiming
 */
public class HubTenantControllerTest extends SpringTest {

    /**
     * 登录 -> 新增租户 -> 列表验证 -> 详情 -> 修改 -> 详情验证 -> 修改状态 -> 列表验证状态 -> 租户选项 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/tenant
     * get /api/v1/tenant
     * get /api/v1/tenant/{tenantId}
     * patch /api/v1/tenant
     * get /api/v1/tenant/{tenantId}
     * patch /api/v1/tenant/status
     * get /api/v1/tenant
     * get /api/v1/tenant/options
     * delete /api/v1/auth/logout
     */
    @Test
    public void tenantCrud() throws Exception {
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
        // 新增租户
        String tenantId = "test-crud";
        String tenantName = "test-tenant-crud";
        body = """
                {
                    "tenantId": "%s",
                    "tenantName": "%s",
                    "title": "%s",
                    "tenantUser": "zhangsan",
                    "tenantPhone": "13800138000",
                    "tenantEmail": "test@cowave.com",
                    "tenantAddr": "测试地址",
                    "remark": "单元测试租户"
                }
                """.formatted(tenantId, tenantName, tenantName);
        mockPost("/api/v1/tenant", body, accessToken);
        // 列表，验证新增
        mvcResult = mockGet("/api/v1/tenant?tenantId=" + tenantId + "&page=1&pageSize=100", accessToken);
        List<SysTenant> tenantList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, tenantList.size());
        SysTenant tenant = tenantList.get(0);
        Assertions.assertEquals(tenantId, tenant.getTenantId());
        Assertions.assertEquals(tenantName, tenant.getTenantName());
        Assertions.assertEquals(EnableStatus.ENABLE, tenant.getStatus());
        // 详情
        mvcResult = mockGet("/api/v1/tenant/" + tenantId, accessToken);
        SysTenant info = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(tenantId, info.getTenantId());
        Assertions.assertEquals(tenantName, info.getTenantName());
        Assertions.assertEquals("zhangsan", info.getTenantUser());
        Assertions.assertEquals("test@cowave.com", info.getTenantEmail());
        // 修改租户
        String updatedName = "test-tenant-crud-updated";
        body = """
                {
                    "tenantId": "%s",
                    "tenantName": "%s",
                    "title": "%s",
                    "tenantUser": "lisi",
                    "tenantPhone": "13900139000",
                    "tenantEmail": "updated@cowave.com",
                    "tenantAddr": "更新后地址",
                    "remark": "已更新"
                }
                """.formatted(tenantId, updatedName, updatedName);
        mockPatch("/api/v1/tenant", body, accessToken);
        // 详情，验证修改
        mvcResult = mockGet("/api/v1/tenant/" + tenantId, accessToken);
        info = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(updatedName, info.getTenantName());
        Assertions.assertEquals("lisi", info.getTenantUser());
        Assertions.assertEquals("updated@cowave.com", info.getTenantEmail());
        // 修改状态（停用）
        body = """
                {
                    "tenantId": "%s",
                    "tenantName": "%s",
                    "status": 0
                }
                """.formatted(tenantId, updatedName);
        mockPatch("/api/v1/tenant/status", body, accessToken);
        // 列表，验证状态变更
        mvcResult = mockGet("/api/v1/tenant?tenantId=" + tenantId + "&page=1&pageSize=100", accessToken);
        tenantList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, tenantList.size());
        Assertions.assertEquals(EnableStatus.DISABLE, tenantList.get(0).getStatus());
        // 租户选项
        mockGet("/api/v1/tenant/options", accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }

    /**
     * 登录 -> 新增租户 -> 新增管理员 -> 管理员列表验证 -> 移除管理员 -> 管理员列表验证空 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/tenant
     * post /api/v1/tenant/manager
     * get /api/v1/tenant/manager/{tenantId}
     * patch /api/v1/tenant/manager/remove
     * get /api/v1/tenant/manager/{tenantId}
     * delete /api/v1/auth/logout
     */
    @Test
    public void tenantManager() throws Exception {
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
        // 新增租户
        String tenantId = "test-mgr";
        String tenantName = "test-tenant-mgr";
        body = """
                {
                    "tenantId": "%s",
                    "tenantName": "%s",
                    "title": "%s"
                }
                """.formatted(tenantId, tenantName, tenantName);
        mockPost("/api/v1/tenant", body, accessToken);
        // 新增管理员
        String userName = "test-tenant-admin";
        String userAccount = "test_tenant_admin";
        body = """
                {
                    "tenantId": "%s",
                    "userName": "%s",
                    "userAccount": "%s",
                    "userPasswd": "123456"
                }
                """.formatted(tenantId, userName, userAccount);
        mockPost("/api/v1/tenant/manager", body, accessToken);
        // 管理员列表，验证新增
        mvcResult = mockGet("/api/v1/tenant/manager/" + tenantId + "?page=1&pageSize=100", accessToken);
        List<TenantManagerPto> managerList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, managerList.size());
        TenantManagerPto manager = managerList.get(0);
        Integer userId = manager.getUserId();
        Assertions.assertEquals(tenantId, manager.getTenantId());
        Assertions.assertEquals(userName, manager.getUserName());
        Assertions.assertEquals(userAccount, manager.getUserAccount());
        // 移除管理员
        body = """
                {
                    "tenantId": "%s",
                    "userIds": [%d]
                }
                """.formatted(tenantId, userId);
        mockPatch("/api/v1/tenant/manager/remove", body, accessToken);
        // 管理员列表，验证移除
        mvcResult = mockGet("/api/v1/tenant/manager/" + tenantId + "?page=1&pageSize=100", accessToken);
        managerList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(0, managerList.size());
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
