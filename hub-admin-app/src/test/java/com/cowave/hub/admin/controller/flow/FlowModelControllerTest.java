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
package com.cowave.hub.admin.controller.flow;

import com.cowave.hub.admin.SpringTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

/**
 * @author shanhuiming
 */
public class FlowModelControllerTest extends SpringTest {

    /**
     * 登录 -> 新增模型 -> 列表验证 -> 删除 -> 转为模型 -> 导出 -> 发布 -> 列表验证(新版本) -> 删除 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/flow/model/add
     * post /api/v1/flow/model/list
     * get /api/v1/flow/model/delete/{modelIds}
     * get /api/v1/flow/deploy/translate/{id}
     * get /api/v1/flow/model/deploy/{modelId}
     * delete /api/v1/auth/logout
     */
    @Test
    public void modelCrud() throws Exception {
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
        // 新增模型
        String modelKey = "test-model-key";
        String modelName = "test-model-name";
        body = """
                {
                    "key": "%s",
                    "name": "%s",
                    "category": "test",
                    "description": "单元测试模型"
                }
                """.formatted(modelKey, modelName);
        mockPost("/api/v1/flow/model/add", body, accessToken);
        // 列表，验证新增
        body = """
                {"key": "%s"}
                """.formatted(modelKey);
        mvcResult = mockPost("/api/v1/flow/model/list", body, accessToken);
        Assertions.assertEquals("1", readString(mvcResult, "/data/total"));
        Assertions.assertEquals(modelKey, readString(mvcResult, "/data/list/0/key"));
        Assertions.assertEquals(modelName, readString(mvcResult, "/data/list/0/name"));
        Assertions.assertEquals("1", readString(mvcResult, "/data/list/0/version"));
        String emptyModelId = readString(mvcResult, "/data/list/0/id");
        // 删除模型
        mockGet("/api/v1/flow/model/delete/" + emptyModelId, accessToken);
        // 列表，验证删除
        body = """
                {"key": "%s"}
                """.formatted(modelKey);
        mvcResult = mockPost("/api/v1/flow/model/list", body, accessToken);
        Assertions.assertEquals("0", readString(mvcResult, "/data/total"));
        // 将预置的leave流程转为模型（含完整BPMNDI，可发布）
        body = """
                {"key": "leave", "latest": true}
                """;
        mvcResult = mockPost("/api/v1/flow/deploy/list", body, accessToken);
        String leaveProcessId = readString(mvcResult, "/data/list/0/id");
        mockGet("/api/v1/flow/deploy/translate/" + leaveProcessId, accessToken);
        // 列表，验证转换后的模型
        body = """
                {"key": "leave"}
                """;
        mvcResult = mockPost("/api/v1/flow/model/list", body, accessToken);
        Assertions.assertEquals("1", readString(mvcResult, "/data/list/0/version"));
        String modelId = readString(mvcResult, "/data/list/0/id");
        // 导出BPMN XML
        mvcResult = mockGet("/api/v1/flow/model/export/" + modelId, accessToken);
        String exportXml = mvcResult.getResponse().getContentAsString();
        Assertions.assertFalse(exportXml.isEmpty());
        Assertions.assertTrue(exportXml.contains("leave"));
        // 发布模型（version会+1）
        mockGet("/api/v1/flow/model/deploy/" + modelId, accessToken);
        // 列表，验证发布后版本更新
        body = """
                {"key": "leave"}
                """;
        mvcResult = mockPost("/api/v1/flow/model/list", body, accessToken);
        Assertions.assertEquals("2", readString(mvcResult, "/data/list/0/version"));
        // 删除模型
        mockGet("/api/v1/flow/model/delete/" + modelId, accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
