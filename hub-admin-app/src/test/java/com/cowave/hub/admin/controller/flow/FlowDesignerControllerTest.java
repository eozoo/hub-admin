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
import com.fasterxml.jackson.core.type.TypeReference;
import org.flowable.validation.ValidationError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

/**
 * @author shanhuiming
 */
public class FlowDesignerControllerTest extends SpringTest {

    /**
     * 创建流程 -> 保存 -> 校验 -> 获取模型信息
     * get /api/v1/flow/designer/create/{flowKey}
     * post /api/v1/flow/designer/save/{modelId}
     * post /api/v1/flow/designer/validate
     * get /api/v1/flow/designer/info/{modelId}
     */
    @Test
    public void designerAnonymousApis() throws Exception {
        // 创建流程
        MvcResult mvcResult = mockGet("/api/v1/flow/designer/create/designer-test-flow");
        String code = readString(mvcResult, "/code");
        Assertions.assertEquals("200", code);
        String modelId = readString(mvcResult, "/data");
        // 保存模型（更新名称）
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/flow/designer/save/" + modelId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("json_xml", "{\"properties\":{\"process_id\":\"designer-test-flow\"},\"stencilset\":{\"namespace\":\"http://b3mn.org/stencilset/bpmn2.0#\"}}")
                        .param("name", "更新后的名称")
                        .param("key", "designer-test-flow")
                        .param("description", "更新描述"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // 校验BPMN
        String body = """
                {
                    "properties": {"process_id": "designer-test-flow"},
                    "childShapes": []
                }
                """;
        mvcResult = mockPost("/api/v1/flow/designer/validate", body);
        List<ValidationError> errors = readData(mvcResult, "", new TypeReference<>() {});
        Assertions.assertNotNull(errors);
        // 获取模型信息（验证保存生效）
        mvcResult = mockGet("/api/v1/flow/designer/info/" + modelId);
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());
        // 登录（用于删除模型）
        body = """
                {
                "tenantId" : "cowave",
                "userAccount" : "liubei",
                "passWord" : "12345678"
                }
                """;
        mvcResult = mockPost("/api/v1/auth/public/logon", body);
        String accessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        // 清理：删除模型
        mockGet("/api/v1/flow/model/delete/" + modelId, accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
