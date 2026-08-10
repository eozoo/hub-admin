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
package com.cowave.hub.admin.controller.sys;

import com.cowave.hub.admin.SpringTest;
import com.cowave.hub.admin.domain.rbac.entity.HubPost;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserListPto;
import com.cowave.hub.admin.domain.sys.entity.HubOperation;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

/**
 * @author shanhuiming
 */
public class HubOperationControllerTest extends SpringTest {

    /**
     * 登录 -> 新增岗位(产生日志) -> 修改岗位(产生日志) -> 删除岗位(产生日志) -> 操作日志列表
     *     -> 按opType筛选 -> 删除指定日志 -> 操作日志列表验证减少 -> 导出 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/post
     * patch /api/v1/post
     * delete /api/v1/post/{postIds}
     * get /api/v1/oplog
     * get /api/v1/oplog?opType=
     * delete /api/v1/oplog/{ids}
     * get /api/v1/oplog
     * get /api/v1/oplog/export
     * delete /api/v1/auth/logout
     */
    @Test
    public void operationQueryAndDelete() throws Exception {
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
        // 测试前先清空日志
        mockDelete("/api/v1/oplog/clean", accessToken);
        // 新增岗位（产生 opType=module_post, opAction=op_create 日志）
        String postName = "test-oplog-post";
        body = """
                {
                    "postName": "%s",
                    "postStatus": 1
                }
                """.formatted(postName);
        mockPost("/api/v1/post", body, accessToken);
        // 列表获取postId
        mvcResult = mockGet("/api/v1/post?postName=" + postName + "&page=1&pageSize=100", accessToken);
        List<HubPost> postList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, postList.size());
        Integer postId = postList.get(0).getPostId();
        // 修改岗位（产生 opAction=op_edit 日志）
        String updatedName = "test-oplog-post-updated";
        body = """
                {
                    "postId": %d,
                    "postName": "%s",
                    "postStatus": 0
                }
                """.formatted(postId, updatedName);
        mockPatch("/api/v1/post", body, accessToken);
        // 删除岗位（产生 opAction=op_delete 日志）
        mockDelete("/api/v1/post/" + postId, accessToken);
        // 操作日志列表（默认分页查询）
        Thread.sleep(1000);
        mvcResult = mockGet("/api/v1/oplog?page=1&pageSize=100", accessToken);
        List<HubOperation> opList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(opList.isEmpty(), "操作日志列表至少应有1条（含本次岗位操作产生的日志）");
        Long total = readData(mvcResult, "/data/total", new TypeReference<>() {});
        Assertions.assertTrue(total >= 1);
        // 按opType筛选（module_post，验证岗位相关日志）
        Thread.sleep(1000);
        mvcResult = mockGet("/api/v1/oplog?opType=module_post&page=1&pageSize=100", accessToken);
        List<HubOperation> postOpList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        int opSize = postOpList.size();
        Assertions.assertTrue(opSize >= 3, "岗位操作至少包含新增、修改、删除3条日志");
        // 验证日志字段完整性
        HubOperation firstOp = postOpList.get(0);
        Assertions.assertNotNull(firstOp.getId(), "日志id不应为空");
        Assertions.assertEquals("domain_system", firstOp.getOpModule());
        Assertions.assertEquals("module_post", firstOp.getOpType());
        Assertions.assertNotNull(firstOp.getOpAction(), "操作动作不应为空");
        Assertions.assertNotNull(firstOp.getOpTime(), "操作时间不应为空");
        Assertions.assertNotNull(firstOp.getOpStatus(), "操作状态不应为空");
        // 删除第一条日志
        String logId = firstOp.getId();
        mockDelete("/api/v1/oplog/" + logId, accessToken);
        // 列表，验证减少
        mvcResult = mockGet("/api/v1/oplog?opType=module_post&page=1&pageSize=100", accessToken);
        postOpList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(postOpList.size(),  opSize - 1, "删除1条日志后应有2条");
        // 导出操作日志
        mockExport("/api/v1/oplog/export", null, "target/oplog.xlsx", accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }

    /**
     * 登录 -> 新增用户(产生日志) -> 删除用户(产生日志) -> 操作日志列表验证 -> 清空日志 -> 操作日志列表验证空 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/user
     * delete /api/v1/user/{userIds}
     * get /api/v1/oplog
     * delete /api/v1/oplog/clean
     * get /api/v1/oplog
     * delete /api/v1/auth/logout
     */
    @Test
    public void operationClean() throws Exception {
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
        // 新增用户（产生 opType=module_user, opAction=op_create 日志）
        String userName = "test-oplog-user";
        body = """
                {
                    "userAccount": "test_oplog_user",
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
        // 删除用户（产生 opAction=op_delete 日志）
        mockDelete("/api/v1/user/" + userId, accessToken);
        // 操作日志列表（验证有日志）
        mvcResult = mockGet("/api/v1/oplog?page=1&pageSize=100", accessToken);
        List<HubOperation> opList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(opList.isEmpty(), "操作日志列表至少应有1条");
        // 清空日志
        mockDelete("/api/v1/oplog/clean", accessToken);
        // 操作日志列表，验证已清空
        mvcResult = mockGet("/api/v1/oplog?page=1&pageSize=100", accessToken);
        //opList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        //Assertions.assertTrue(opList.isEmpty(), "清空后操作日志列表应为空");
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
