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
import com.cowave.hub.admin.domain.flow.entity.FlowTask;
import com.fasterxml.jackson.core.type.TypeReference;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.engine.TaskService;
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
public class FlowTaskControllerTest extends SpringTest {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    /**
     * 登录 -> 启动请假实例 -> 全部待办 -> 我的待办 -> 任务表单 -> 改签 -> 催办 -> 任务办理 -> 办理过程 -> 我办理的 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/flow/task/list/all
     * post /api/v1/flow/task/list
     * post /api/v1/flow/task/form/{taskId}
     * get /api/v1/flow/task/assignee/{taskId}/{userId}
     * get /api/v1/flow/task/press/{taskId}
     * post /api/v1/flow/task/complete
     * get /api/v1/flow/task/records/{taskId}
     * post /api/v1/flow/task/list/history
     * get /api/v1/flow/instance/delete/{ids}
     * delete /api/v1/auth/logout
     */
    @Test
    public void taskManage() throws Exception {
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
        // 启动预置leave请假流程实例（审批人均为liubei）
        Map<String, Object> variables = new HashMap<>();
        variables.put("applyUser", "cowave-sys-liubei");
        variables.put("deptApprover", "cowave-sys-liubei");
        variables.put("hrApprover", "cowave-sys-liubei");
        ProcessInstance process = runtimeService.startProcessInstanceByKey("leave", variables);
        String processId = process.getProcessInstanceId();
        // 全部待办
        mvcResult = mockPost("/api/v1/flow/task/list/all", "{}", accessToken);
        List<FlowTask> allTasks = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(allTasks.isEmpty());
        // 我的待办（请假申请任务）
        mvcResult = mockPost("/api/v1/flow/task/list", body, accessToken);
        List<FlowTask> myTasks = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(myTasks.isEmpty());
        String taskId = myTasks.get(0).getTaskId();
        // 任务表单
        mvcResult = mockPost("/api/v1/flow/task/form/" + taskId, "", accessToken);
        Assertions.assertEquals("200", readString(mvcResult, "/code"));
        // 改签
        mockGet("/api/v1/flow/task/assignee/" + taskId + "/cowave-sys-liubei", accessToken);
        // 催办
        mockGet("/api/v1/flow/task/press/" + taskId, accessToken);
        // 任务办理（提交请假申请，流程进到部门审批）
        body = """
                {
                    "taskId": "%s",
                    "comment": "提交请假申请"
                }
                """.formatted(taskId);
        mockPost("/api/v1/flow/task/complete", body, accessToken);
        // 办理过程
        mvcResult = mockGet("/api/v1/flow/task/records/" + taskId, accessToken);
        List<FlowTask> records = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertFalse(records.isEmpty());
        // 我办理的
        mvcResult = mockPost("/api/v1/flow/task/list/history", "{}", accessToken);
        List<FlowTask> historyTasks = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(historyTasks.isEmpty());
        // 清理：完成后续任务让流程自然结束，清历史
        Task deptTask = taskService.createTaskQuery().processInstanceId(processId).active().singleResult();
        if (deptTask != null) {
            Map<String, Object> deptVars = new HashMap<>();
            deptVars.put("deptAapproveResult", "true");
            taskService.complete(deptTask.getId(), deptVars);
            Task hrTask = taskService.createTaskQuery().processInstanceId(processId).active().singleResult();
            if (hrTask != null) {
                Map<String, Object> hrVars = new HashMap<>();
                hrVars.put("hrApproveResult", "true");
                taskService.complete(hrTask.getId(), hrVars);
            }
        }
        historyService.deleteHistoricProcessInstance(processId);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
