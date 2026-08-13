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
import com.cowave.hub.admin.domain.rbac.entity.SysPost;
import com.cowave.hub.admin.domain.rbac.entity.pto.PostInfoPto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
public class HubPostControllerTest extends SpringTest {

    /**
     * 登录 -> 新增岗位 -> 列表验证 -> 详情 -> 修改 -> 详情验证 -> 导出 -> 删除 -> 列表验证 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/post
     * get /api/v1/post
     * get /api/v1/post/{postId}
     * patch /api/v1/post
     * get /api/v1/post/{postId}
     * post /api/v1/post/export
     * delete /api/v1/post/{postIds}
     * get /api/v1/post
     * delete /api/v1/auth/logout
     */
    @Test
    public void postCrud() throws Exception {
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
        // 新增岗位
        String postName = "test-post-crud";
        body = """
                {
                    "postName": "%s",
                    "postStatus": 1
                }
                """.formatted(postName);
        mockPost("/api/v1/post", body, accessToken);
        // 列表，验证新增
        mvcResult = mockGet("/api/v1/post?postName=" + postName + "&page=1&pageSize=100", accessToken);
        List<SysPost> postList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, postList.size());
        SysPost post = postList.get(0);
        Integer postId = post.getPostId();
        Assertions.assertEquals(postName, post.getPostName());
        // 详情
        mvcResult = mockGet("/api/v1/post/" + postId, accessToken);
        PostInfoPto detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(postId, detail.getPostId());
        Assertions.assertEquals(postName, detail.getPostName());
        // 修改岗位
        String updatedName = "test-post-crud-updated";
        body = """
                {
                    "postId": %d,
                    "postName": "%s",
                    "postStatus": 0
                }
                """.formatted(postId, updatedName);
        mockPatch("/api/v1/post", body, accessToken);
        // 详情，验证修改
        mvcResult = mockGet("/api/v1/post/" + postId, accessToken);
        detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(updatedName, detail.getPostName());
        Assertions.assertEquals(0, detail.getPostStatus().getVal());
        // 导出岗位
        mockExport("/api/v1/post/export", "postName=" + updatedName, "target/post.xlsx", accessToken);
        // 删除岗位
        mockDelete("/api/v1/post/" + postId, accessToken);
        // 列表，验证删除
        mvcResult = mockGet("/api/v1/post?postName=" + updatedName + "&page=1&pageSize=100", accessToken);
        postList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(0, postList.size());
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }

    /**
     * 登录 -> 列表 -> 详情 -> 组织架构 -> 候选人 -> 名称查询 -> 部门岗位名称查询 -> 退出登录
     * post /api/v1/auth/public/logon
     * get /api/v1/post
     * get /api/v1/post/{postId}
     * get /api/v1/post/diagram
     * get /api/v1/post/candidates/{postCode}
     * get /api/v1/post/name/{postId}
     * get /api/v1/post/dept/name/{deptPosts}
     * delete /api/v1/auth/logout
     */
    @Test
    public void postQuery() throws Exception {
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
        // 岗位列表
        mvcResult = mockGet("/api/v1/post?page=1&pageSize=100", accessToken);
        List<SysPost> postList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(postList.isEmpty(), "岗位列表至少应有1条");
        // 岗位详情（postId=1 总经理）
        mvcResult = mockGet("/api/v1/post/1", accessToken);
        PostInfoPto detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertNotNull(detail);
        Assertions.assertEquals("GM", detail.getPostCode());
        // 岗位组织架构
        mvcResult = mockGet("/api/v1/post/diagram", accessToken);
        Map<String, Object> diagram = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertNotNull(diagram);
        // 岗位流程候选人（postCode=GM）
        mockGet("/api/v1/post/candidates/GM", accessToken);
        // 岗位名称查询（postId=1）
        mvcResult = mockGet("/api/v1/post/name/1", accessToken);
        String postName = readString(mvcResult, "/data");
        Assertions.assertNotNull(postName, "岗位名称不应为空");
        // 部门岗位名称查询（deptId=1, postId=1,2）
        mockGet("/api/v1/post/dept/name/1-1,1-2", accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
