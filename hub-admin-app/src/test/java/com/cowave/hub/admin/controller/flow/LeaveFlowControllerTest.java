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
import com.cowave.hub.admin.domain.flow.entity.pto.LeaveInfoPto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

/**
 * @author shanhuiming
 */
public class LeaveFlowControllerTest extends SpringTest {

    /**
     * 申请人登录 -> 新增请假1 -> 列表 -> 我的请假 -> 详情 -> 修改 -> 撤销 -> 删除请假1 ->
     * 新增请假2 -> 部门经理登录 -> 部门审批通过 -> 人事登录 -> 人事审批通过 -> 流程结束 -> 删除请假2 -> 列表验证全删 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/flow/leave/add
     * post /api/v1/flow/leave/list
     * post /api/v1/flow/leave/list/my
     * get /api/v1/flow/leave/info/{id}
     * post /api/v1/flow/leave/edit
     * get /api/v1/flow/leave/revocate/{id}
     * get /api/v1/flow/leave/delete/{ids}
     * post /api/v1/flow/task/list
     * post /api/v1/flow/task/complete
     * delete /api/v1/auth/logout
     */
    @Test
    public void leaveCrud() throws Exception {
        // 申请人登录（liubei）
        String body = """
                {
                "tenantId" : "cowave",
                "userAccount" : "liubei",
                "passWord" : "12345678"
                }
                """;
        MvcResult mvcResult = mockPost("/api/v1/auth/public/logon", body);
        String liubeiToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        // 新增请假1（add自动完成leaveApply，流程进到部门审批）
        body = """
                {
                    "leaveType": 1,
                    "reason": "单元测试请假-撤销",
                    "beginTime": "2026-08-08 09:00:00",
                    "endTime": "2026-08-08 18:00:00",
                    "deptApprover": "cowave-sys-liubei"
                }
                """;
        mockPost("/api/v1/flow/leave/add", body, liubeiToken);
        // 列表，验证新增
        mvcResult = mockPost("/api/v1/flow/leave/list", "{}", liubeiToken);
        List<LeaveInfoPto> leaveList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, leaveList.size());
        LeaveInfoPto leave = leaveList.get(0);
        String leaveId1 = leave.getId();
        Assertions.assertEquals("单元测试请假-撤销", leave.getReason());
        // 我的请假
        mvcResult = mockPost("/api/v1/flow/leave/list/my", "{}", liubeiToken);
        List<LeaveInfoPto> myLeaves = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(myLeaves.isEmpty());
        // 详情
        mvcResult = mockGet("/api/v1/flow/leave/info/" + leaveId1, liubeiToken);
        LeaveInfoPto info = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(leaveId1, info.getId());
        Assertions.assertEquals("单元测试请假-撤销", info.getReason());
        // 修改
        body = """
                {
                    "id": "%s",
                    "reason": "单元测试请假-已修改"
                }
                """.formatted(leaveId1);
        mockPost("/api/v1/flow/leave/edit", body, liubeiToken);
        // 详情，验证修改
        mvcResult = mockGet("/api/v1/flow/leave/info/" + leaveId1, liubeiToken);
        info = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals("单元测试请假-已修改", info.getReason());
        // 撤销（流程在部门审批节点，撤销跳转到leaveRevocatedEnd结束）
        mockGet("/api/v1/flow/leave/revocate/" + leaveId1, liubeiToken);
        // 删除请假1
        mockGet("/api/v1/flow/leave/delete/" + leaveId1, liubeiToken);
        // 新增请假2（走完整流程）
        body = """
                {
                    "leaveType": 1,
                    "reason": "单元测试请假-走完整流程",
                    "beginTime": "2026-08-09 09:00:00",
                    "endTime": "2026-08-09 18:00:00",
                    "deptApprover": "cowave-sys-liubei"
                }
                """;
        mockPost("/api/v1/flow/leave/add", body, liubeiToken);
        // 列表，查到请假2
        mvcResult = mockPost("/api/v1/flow/leave/list", "{}", liubeiToken);
        leaveList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, leaveList.size());
        String leaveId2 = leaveList.get(0).getId();
        // 部门经理登录 -> 部门审批通过
        mvcResult = mockPost("/api/v1/flow/task/list", "{}", liubeiToken);
        List<FlowTask> tasks = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(tasks.isEmpty());
        String taskId = tasks.get(0).getTaskId();
        body = """
                {
                    "taskId": "%s",
                    "variables": {
                        "deptAapproveResult": "true",
                        "hrApprover": "cowave-sys-liubei"
                    },
                    "comment": "同意"
                }
                """.formatted(taskId);
        mockPost("/api/v1/flow/task/complete", body, liubeiToken);
        // 人事登录 -> 人事审批通过
        mvcResult = mockPost("/api/v1/flow/task/list", "{}", liubeiToken);
        tasks = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(tasks.isEmpty());
        taskId = tasks.get(0).getTaskId();
        body = """
                {
                    "taskId": "%s",
                    "variables": {"hrApproveResult": "true"},
                    "comment": "同意"
                }
                """.formatted(taskId);
        mockPost("/api/v1/flow/task/complete", body, liubeiToken);
        // 销假serviceTask自动执行，流程结束
        mvcResult = mockPost("/api/v1/flow/task/list", "{}", liubeiToken);
        tasks = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertTrue(tasks.isEmpty());
        // 删除请假2
        mockGet("/api/v1/flow/leave/delete/" + leaveId2, liubeiToken);
        // 列表，验证全删
        mvcResult = mockPost("/api/v1/flow/leave/list", "{}", liubeiToken);
        leaveList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(0, leaveList.size());
        // 退出登录
        mockDelete("/api/v1/auth/logout", liubeiToken);
    }
}
