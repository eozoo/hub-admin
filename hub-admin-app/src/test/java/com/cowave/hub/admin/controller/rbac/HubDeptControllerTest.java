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
import com.cowave.hub.admin.domain.rbac.entity.pto.DeptInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.DeptListPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.DeptPostPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.DeptUserPto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.cowave.hub.admin.domain.rbac.enums.YesNo.NO;
import static com.cowave.hub.admin.domain.rbac.enums.YesNo.YES;

/**
 * @author shanhuiming
 */
public class HubDeptControllerTest extends SpringTest {

    /**
     * 登录 -> 新增部门 -> 列表验证 -> 详情 -> 修改 -> 详情验证修改
     *     -> 导出 -> 组织架构图 -> 岗位树 -> 用户树 -> 删除 -> 列表验证删除 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/dept
     * get /api/v1/dept
     * get /api/v1/dept/{deptId}
     * patch /api/v1/dept
     * get /api/v1/dept/{deptId}
     * post /api/v1/dept/export
     * get /api/v1/dept/diagram
     * get /api/v1/dept/diagram/post
     * get /api/v1/dept/diagram/user
     * delete /api/v1/dept/{deptIds}
     * get /api/v1/dept
     * delete /api/v1/auth/logout
     */
    @Test
    public void deptCrud() throws Exception {
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
        // 新增部门（挂在 deptId=4 下）
        String deptName = "test-dept-crud";
        body = """
                {
                    "deptName": "%s",
                    "parentIds": [4]
                }
                """.formatted(deptName);
        mockPost("/api/v1/dept", body, accessToken);
        // 列表，验证新增
        mvcResult = mockGet("/api/v1/dept?deptName=" + deptName + "&page=1&pageSize=100", accessToken);
        List<DeptListPto> deptList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, deptList.size());
        DeptListPto dept = deptList.get(0);
        Integer deptId = dept.getDeptId();
        Assertions.assertEquals(deptName, dept.getDeptName());
        // 详情
        mvcResult = mockGet("/api/v1/dept/" + deptId, accessToken);
        DeptInfoPto info = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(deptId, info.getDeptId());
        Assertions.assertEquals(deptName, info.getDeptName());
        // 修改部门
        String updatedName = "test-dept-crud-updated";
        body = """
                {
                    "deptId": %d,
                    "deptName": "%s",
                    "parentIds": [4]
                }
                """.formatted(deptId, updatedName);
        mockPatch("/api/v1/dept", body, accessToken);
        // 详情，验证修改
        mvcResult = mockGet("/api/v1/dept/" + deptId, accessToken);
        info = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(updatedName, info.getDeptName());
        // 导出部门
        mockExport("/api/v1/dept/export", null, "target/dept.xlsx", accessToken);
        // 组织架构图
        mockGet("/api/v1/dept/diagram?deptId=" + deptId, accessToken);
        // 部门岗位树
        mockGet("/api/v1/dept/diagram/post", accessToken);
        // 部门用户树
        mockGet("/api/v1/dept/diagram/user", accessToken);
        // 删除部门
        mockDelete("/api/v1/dept/" + deptId, accessToken);
        // 列表，验证删除
        mvcResult = mockGet("/api/v1/dept?deptName=" + updatedName + "&page=1&pageSize=100", accessToken);
        deptList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(0, deptList.size());
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }

    /**
     * 登录 -> 新增部门 -> 查未设置岗位 -> 添加岗位 -> 查已设置岗位验证
     *     -> 查未加入成员 -> 添加成员 -> 查已加入成员验证(枚举映射) -> 部门流程候选人 -> 部门名称查询
     *     -> 移除成员 -> 移除岗位 -> 删除部门 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/dept
     * get /api/v1/dept/posts/unConfigured
     * post /api/v1/dept/posts
     * get /api/v1/dept/posts/configured
     * get /api/v1/dept/members/unJoined
     * post /api/v1/dept/members
     * get /api/v1/dept/members/joined
     * get /api/v1/dept/candidates/{deptCode}
     * get /api/v1/dept/name/{userIds}
     * delete /api/v1/dept/members/{deptId}/{userIds}
     * delete /api/v1/dept/posts/{deptId}/{postIds}
     * delete /api/v1/dept/{deptIds}
     * delete /api/v1/auth/logout
     */
    @Test
    public void deptPostsAndMembers() throws Exception {
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
        // 新增部门
        String deptName = "test-dept-post";
        body = """
                {
                    "deptName": "%s",
                    "parentIds": [4]
                }
                """.formatted(deptName);
        mockPost("/api/v1/dept", body, accessToken);
        // 列表获取deptId
        mvcResult = mockGet("/api/v1/dept?deptName=" + deptName + "&page=1&pageSize=100", accessToken);
        List<DeptListPto> deptList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, deptList.size());
        Integer deptId = deptList.get(0).getDeptId();
        // 查未设置岗位
        mvcResult = mockGet("/api/v1/dept/posts/unConfigured?deptId=" + deptId + "&page=1&pageSize=100", accessToken);
        List<DeptPostPto> unConfiguredPosts = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertTrue(unConfiguredPosts.size() >= 2, "至少应有2个未设置岗位");
        // 添加岗位（取前2个未设置岗位）
        Integer postId1 = unConfiguredPosts.get(0).getPostId();
        Integer postId2 = unConfiguredPosts.get(1).getPostId();
        body = """
                [
                    {"deptId": %d, "postId": %d, "isDefault": 1},
                    {"deptId": %d, "postId": %d, "isDefault": 0}
                ]
                """.formatted(deptId, postId1, deptId, postId2);
        mockPost("/api/v1/dept/posts", body, accessToken);
        // 查已设置岗位，验证数量
        mvcResult = mockGet("/api/v1/dept/posts/configured?deptId=" + deptId + "&page=1&pageSize=100", accessToken);
        List<DeptPostPto> configuredPosts = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(2, configuredPosts.size(), "添加2个岗位后已设置岗位应为2");
        // 查未加入成员
        mvcResult = mockGet("/api/v1/dept/members/unJoined?deptId=" + deptId + "&page=1&pageSize=100", accessToken);
        List<DeptUserPto> unJoinedMembers = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertTrue(unJoinedMembers.size() >= 2, "至少应有2个未加入成员");
        // 添加成员（取前2个未加入成员，postId 用已设置的岗位）
        Integer userId1 = unJoinedMembers.get(0).getUserId();
        Integer userId2 = unJoinedMembers.get(1).getUserId();
        body = """
                [
                    {"userId": %d, "deptId": %d, "postId": %d, "isDefault": 1, "isLeader": 1},
                    {"userId": %d, "deptId": %d, "postId": %d, "isDefault": 0, "isLeader": 0}
                ]
                """.formatted(userId1, deptId, postId1, userId2, deptId, postId2);
        mockPost("/api/v1/dept/members", body, accessToken);
        // 查已加入成员，验证数量
        mvcResult = mockGet("/api/v1/dept/members/joined?deptId=" + deptId + "&page=1&pageSize=100", accessToken);
        List<DeptUserPto> joinedMembers = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(2, joinedMembers.size(), "添加2个成员后已加入成员应为2");
        // 验证枚举映射正确：参数→DB→响应
        Assertions.assertEquals(YES, joinedMembers.get(0).getIsDefault(), "第一个成员isDefault应为YES(1)");
        Assertions.assertEquals(YES, joinedMembers.get(0).getIsLeader(), "第一个成员isLeader应为YES(1)");
        Assertions.assertEquals(NO, joinedMembers.get(1).getIsDefault(), "第二个成员isDefault应为NO(0)");
        Assertions.assertEquals(NO, joinedMembers.get(1).getIsLeader(), "第二个成员isLeader应为NO(0)");
        // 部门流程候选人
        mockGet("/api/v1/dept/candidates/FD", accessToken);
        // 部门名称查询
        mockGet("/api/v1/dept/name/" + userId1 + "," + userId2, accessToken);
        // 移除成员
        mockDelete("/api/v1/dept/members/" + deptId + "/" + userId1 + "," + userId2, accessToken);
        // 移除岗位
        mockDelete("/api/v1/dept/posts/" + deptId + "/" + postId1 + "," + postId2, accessToken);
        // 查已设置岗位，验证清空
        mvcResult = mockGet("/api/v1/dept/posts/configured?deptId=" + deptId + "&page=1&pageSize=100", accessToken);
        configuredPosts = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(0, configuredPosts.size(), "移除后已设置岗位应为0");
        // 删除部门（清理）
        mockDelete("/api/v1/dept/" + deptId, accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
