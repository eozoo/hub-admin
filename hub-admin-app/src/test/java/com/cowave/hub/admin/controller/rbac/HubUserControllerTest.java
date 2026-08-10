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
import com.cowave.hub.admin.domain.rbac.entity.pto.UserInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserListPto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

/**
 * @author shanhuiming
 */
public class HubUserControllerTest extends SpringTest {

    /**
     * 登录 -> 新增用户 -> 列表验证 -> 详情 -> 修改 -> 详情验证 -> 修改状态 -> 导出 -> 删除 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/user
     * get /api/v1/user
     * get /api/v1/user/{userId}
     * patch /api/v1/user
     * get /api/v1/user/{userId}
     * patch /api/v1/user/status
     * post /api/v1/user/export
     * delete /api/v1/user/{userIds}
     * delete /api/v1/auth/logout
     */
    @Test
    public void userCrud() throws Exception {
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
        // 新增用户
        String userName = "test-user-crud";
        body = """
                {
                    "userAccount": "test_user_crud",
                    "userName": "%s",
                    "userPasswd": "123456"
                }
                """.formatted(userName);
        mockPost("/api/v1/user", body, accessToken);
        // 列表，验证新增
        mvcResult = mockGet("/api/v1/user?userName=" + userName + "&page=1&pageSize=100", accessToken);
        List<UserListPto> userList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, userList.size());
        UserListPto user = userList.get(0);
        Integer userId = user.getUserId();
        Assertions.assertEquals(userName, user.getUserName());
        // 详情
        mvcResult = mockGet("/api/v1/user/" + userId, accessToken);
        UserInfoPto detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(userId, detail.getUserId());
        Assertions.assertEquals(userName, detail.getUserName());
        // 修改用户
        String updatedName = "test-user-crud-updated";
        body = """
                {
                    "userId": %d,
                    "userAccount": "test_user_crud",
                    "userName": "%s",
                    "userPasswd": "654321"
                }
                """.formatted(userId, updatedName);
        mockPatch("/api/v1/user", body, accessToken);
        // 详情，验证修改
        mvcResult = mockGet("/api/v1/user/" + userId, accessToken);
        detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(updatedName, detail.getUserName());
        // 修改状态（停用）
        body = """
                {
                    "userId": %d,
                    "userName": "%s",
                    "userStatus": 0
                }
                """.formatted(userId, updatedName);
        mockPatch("/api/v1/user/status", body, accessToken);
        // 导出用户
        mockExport("/api/v1/user/export", "userName=" + updatedName, "target/user.xlsx", accessToken);
        // 删除用户
        mockDelete("/api/v1/user/" + userId, accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }

    /**
     * 登录 -> 新增用户 -> 修改角色 -> 修改密码 -> 组织架构 -> 候选人 -> 名称查询 -> 导出模板 -> 成员选项 -> 导入用户 -> 删除 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/user
     * patch /api/v1/user/roles
     * patch /api/v1/user/passwd
     * get /api/v1/user/diagram
     * get /api/v1/user/candidates
     * get /api/v1/user/name/{userIds}
     * post /api/v1/user/export/template
     * delete /api/v1/user/{userIds}
     * delete /api/v1/auth/logout
     */
    @Test
    public void userQuery() throws Exception {
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
        // 新增用户
        String userName = "test-user-query";
        body = """
                {
                    "userAccount": "test_user_query",
                    "userName": "%s",
                    "userPasswd": "123456"
                }
                """.formatted(userName);
        mockPost("/api/v1/user", body, accessToken);
        // 列表获取userId
        mvcResult = mockGet("/api/v1/user?userName=" + userName + "&page=1&pageSize=100", accessToken);
        List<UserListPto> userList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, userList.size());
        Integer userId = userList.get(0).getUserId();
        // 修改角色
        body = """
                {
                    "userId": %d,
                    "userName": "%s",
                    "roleIds": [2]
                }
                """.formatted(userId, userName);
        mockPatch("/api/v1/user/roles", body, accessToken);
        // 修改密码
        body = """
                {
                    "userId": %d,
                    "userName": "%s",
                    "userPasswd": "newpass123"
                }
                """.formatted(userId, userName);
        mockPatch("/api/v1/user/passwd", body, accessToken);
        // 用户组织架构
        mockGet("/api/v1/user/diagram", accessToken);
        // 用户流程候选人
        mockGet("/api/v1/user/candidates?userId=" + userId, accessToken);
        // 用户名称查询
        mockGet("/api/v1/user/name/1," + userId, accessToken);
        // 导出模板
        mockExport("/api/v1/user/export/template", null, "target/user-template.xlsx", accessToken);
        // 用户成员选项
        body = """
                {
                    "userName": "%s"
                }
                """.formatted(userName);
        mockPost("/api/v1/user/options", body, accessToken);
        // 导入用户
        mockImport("/api/v1/user/import?updateSupport=true", null, "source/user-import.xlsx", accessToken);
        // 删除用户
        mockDelete("/api/v1/user/" + userId, accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
