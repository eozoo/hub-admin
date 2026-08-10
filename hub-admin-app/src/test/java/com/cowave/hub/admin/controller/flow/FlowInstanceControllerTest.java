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
import com.cowave.hub.admin.domain.flow.entity.FlowInstance;
import com.cowave.hub.admin.domain.flow.entity.FlowTask;
import com.cowave.hub.admin.domain.flow.entity.FlowVariable;
import com.fasterxml.jackson.core.type.TypeReference;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
public class FlowInstanceControllerTest extends SpringTest {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    /**
     * 登录 -> designer创建通用流程并部署 -> 启动实例1 -> 执行实例 -> 实例列表 -> 流程进度 -> 流程记录
     *     -> 流程变量 -> 修改变量 -> 删除变量 -> 挂起 -> 唤醒 -> 删除实例1 -> 启动实例2 -> 跳转流程 -> 清理 -> 退出登录
     * post /api/v1/auth/public/logon
     * get /api/v1/flow/designer/create/{flowKey}
     * post /api/v1/flow/instance/executions
     * post /api/v1/flow/instance/list
     * get /api/v1/flow/instance/progress/{id}
     * post /api/v1/flow/instance/history
     * post /api/v1/flow/instance/variables
     * post /api/v1/flow/instance/variables/edit
     * post /api/v1/flow/instance/variables/delete
     * get /api/v1/flow/instance/suspend/{id}
     * get /api/v1/flow/instance/activate/{id}
     * get /api/v1/flow/instance/delete/{ids}
     * get /api/v1/flow/instance/jump/{taskId}/{tid}
     * delete /api/v1/auth/logout
     */
    @Test
    public void instanceManage() throws Exception {
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
        // designer创建流程，并部署
        mvcResult = mockGet("/api/v1/flow/designer/create/instance-test");
        String modelId = readString(mvcResult, "/data");
        // 启动实例1（带流程变量）
        Map<String, Object> variables = new HashMap<>();
        variables.put("testKey", "testValue");
        ProcessInstance process1 = runtimeService.startProcessInstanceByKey("instance-test", variables);
        String processId1 = process1.getProcessInstanceId();
        // 执行实例
        mvcResult = mockPost("/api/v1/flow/instance/executions?name=流程创建测试", "", accessToken);
        List<FlowInstance> execList = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertFalse(execList.isEmpty());
        // 实例列表
        mvcResult = mockPost("/api/v1/flow/instance/list", "{}", accessToken);
        List<FlowInstance> instanceList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(instanceList.isEmpty());
        // 流程进度
        mvcResult = mockGet("/api/v1/flow/instance/progress/" + processId1, accessToken);
        String progressCode = readString(mvcResult, "/code");
        Assertions.assertEquals("200", progressCode);
        // 流程记录
        body = """
                {"processInstanceId": "%s"}
                """.formatted(processId1);
        mvcResult = mockPost("/api/v1/flow/instance/history", body, accessToken);
        List<FlowTask> historyList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(historyList.isEmpty());
        // 流程变量
        mvcResult = mockPost("/api/v1/flow/instance/variables", body, accessToken);
        List<FlowVariable> varList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(varList.isEmpty());
        FlowVariable var = varList.stream().filter(v -> "testKey".equals(v.getVariableName())).findFirst().orElse(null);
        Assertions.assertNotNull(var);
        Assertions.assertEquals("testValue", var.getValue());
        // 修改流程变量
        body = """
                {"executionId": "%s", "variableName": "testKey", "value": "newValue"}
                """.formatted(processId1);
        mockPost("/api/v1/flow/instance/variables/edit", body, accessToken);
        // 验证变量已修改
        body = """
                {"processInstanceId": "%s"}
                """.formatted(processId1);
        mvcResult = mockPost("/api/v1/flow/instance/variables", body, accessToken);
        varList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        var = varList.stream().filter(v -> "testKey".equals(v.getVariableName())).findFirst().orElse(null);
        Assertions.assertNotNull(var);
        Assertions.assertEquals("newValue", var.getValue());
        // 删除流程变量
        body = """
                {"executionId": "%s", "variableName": "testKey"}
                """.formatted(processId1);
        mockPost("/api/v1/flow/instance/variables/delete", body, accessToken);
        // 验证变量已删除
        body = """
                {"processInstanceId": "%s"}
                """.formatted(processId1);
        mvcResult = mockPost("/api/v1/flow/instance/variables", body, accessToken);
        varList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        var = varList.stream().filter(v -> "testKey".equals(v.getVariableName())).findFirst().orElse(null);
        Assertions.assertNull(var);
        // 挂起流程
        mockGet("/api/v1/flow/instance/suspend/" + processId1, accessToken);
        // 唤醒流程
        mockGet("/api/v1/flow/instance/activate/" + processId1, accessToken);
        // 删除实例1
        mockGet("/api/v1/flow/instance/delete/" + processId1, accessToken);
        // 启动实例2（用于跳转测试，跳转会结束流程）
        ProcessInstance process2 = runtimeService.startProcessInstanceByKey("instance-test");
        String processId2 = process2.getProcessInstanceId();
        // 跳转流程：从当前活跃任务跳到end节点
        Task task = taskService.createTaskQuery().processInstanceId(processId2).active().singleResult();
        mockGet("/api/v1/flow/instance/jump/" + task.getId() + "/end?comment=测试跳转", accessToken);
        // 跳转后流程已结束，清理历史
        historyService.deleteHistoricProcessInstance(processId2);
        // 清理模型
        mockGet("/api/v1/flow/model/delete/" + modelId, accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
