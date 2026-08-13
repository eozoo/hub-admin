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
import com.cowave.hub.admin.domain.rbac.entity.SysRole;
import com.cowave.hub.admin.domain.rbac.entity.pto.RoleInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.RoleUserPto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

/**
 * @author shanhuiming
 */
public class HubRoleControllerTest extends SpringTest {

    /**
     * 登录 -> 新增角色 -> 列表验证 -> 详情 -> 修改 -> 详情验证 -> 修改菜单 -> 导出 -> 删除 -> 列表验证 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/role
     * get /api/v1/role
     * get /api/v1/role/{roleId}
     * patch /api/v1/role
     * get /api/v1/role/{roleId}
     * patch /api/v1/role/menus
     * post /api/v1/role/export
     * delete /api/v1/role/{roleIds}
     * get /api/v1/role
     * delete /api/v1/auth/logout
     */
    @Test
    public void roleCrud() throws Exception {
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
        // 新增角色
        String roleName = "test-role-crud";
        body = """
                {
                    "roleCode": "test_role_crud",
                    "roleName": "%s"
                }
                """.formatted(roleName);
        mockPost("/api/v1/role", body, accessToken);
        // 列表，验证新增
        mvcResult = mockGet("/api/v1/role?roleName=" + roleName + "&page=1&pageSize=100", accessToken);
        List<SysRole> roleList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, roleList.size());
        SysRole role = roleList.get(0);
        Integer roleId = role.getRoleId();
        Assertions.assertEquals(roleName, role.getRoleName());
        Assertions.assertEquals("test_role_crud", role.getRoleCode());
        // 详情
        mvcResult = mockGet("/api/v1/role/" + roleId, accessToken);
        RoleInfoPto detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(roleId, detail.getRoleId());
        Assertions.assertEquals(roleName, detail.getRoleName());
        // 修改角色
        String updatedName = "test-role-crud-updated";
        body = """
                {
                    "roleId": %d,
                    "roleCode": "test_role_crud_updated",
                    "roleName": "%s"
                }
                """.formatted(roleId, updatedName);
        mockPatch("/api/v1/role", body, accessToken);
        // 详情，验证修改
        mvcResult = mockGet("/api/v1/role/" + roleId, accessToken);
        detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(updatedName, detail.getRoleName());
        Assertions.assertEquals("test_role_crud_updated", detail.getRoleCode());
        // 修改角色菜单
        body = """
                {
                    "roleId": %d,
                    "menuIds": [1, 5]
                }
                """.formatted(roleId);
        mockPatch("/api/v1/role/menus", body, accessToken);
        // 导出角色
        mockExport("/api/v1/role/export", "roleName=" + updatedName, "target/role.xlsx", accessToken);
        // 删除角色
        mockDelete("/api/v1/role/" + roleId, accessToken);
        // 列表，验证删除
        mvcResult = mockGet("/api/v1/role?roleName=" + updatedName + "&page=1&pageSize=100", accessToken);
        roleList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(0, roleList.size());
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }

    /**
     * 登录 -> 新增角色 -> 未授权用户列表 -> 授权用户 -> 已授权用户列表验证 -> 取消授权
     *     -> 角色名称查询 -> 删除 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/role
     * get /api/v1/role/users/unAuthed
     * post /api/v1/role/user/grant
     * get /api/v1/role/users/authed
     * post /api/v1/role/user/cancel
     * get /api/v1/role/name/{roleIds}
     * delete /api/v1/role/{roleIds}
     * delete /api/v1/auth/logout
     */
    @Test
    public void roleUsers() throws Exception {
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
        // 新增角色
        String roleName = "test-role-user";
        body = """
                {
                    "roleCode": "test_role_user",
                    "roleName": "%s"
                }
                """.formatted(roleName);
        mockPost("/api/v1/role", body, accessToken);
        // 列表获取roleId
        mvcResult = mockGet("/api/v1/role?roleName=" + roleName + "&page=1&pageSize=100", accessToken);
        List<SysRole> roleList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, roleList.size());
        Integer roleId = roleList.get(0).getRoleId();
        // 未授权用户列表
        mvcResult = mockGet("/api/v1/role/users/unAuthed?roleId=" + roleId + "&page=1&pageSize=100", accessToken);
        List<RoleUserPto> unAuthedUsers = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(unAuthedUsers.isEmpty(), "至少应有1个未授权用户");
        // 授权用户（取前2个未授权用户）
        Integer userId1 = unAuthedUsers.get(0).getUserId();
        Integer userId2 = unAuthedUsers.get(1).getUserId();
        body = """
                {
                    "roleId": %d,
                    "userIds": [%d, %d]
                }
                """.formatted(roleId, userId1, userId2);
        mockPost("/api/v1/role/user/grant", body, accessToken);
        // 已授权用户列表，验证新增
        mvcResult = mockGet("/api/v1/role/users/authed?roleId=" + roleId + "&page=1&pageSize=100", accessToken);
        List<RoleUserPto> authedUsers = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(2, authedUsers.size(), "授权2个用户后已授权应为2");
        // 取消授权
        body = """
                {
                    "roleId": %d,
                    "userIds": [%d, %d]
                }
                """.formatted(roleId, userId1, userId2);
        mockPost("/api/v1/role/user/cancel", body, accessToken);
        // 角色名称查询
        mockGet("/api/v1/role/name/" + roleId, accessToken);
        // 删除角色
        mockDelete("/api/v1/role/" + roleId, accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
