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
import com.cowave.hub.admin.domain.flow.entity.pto.MeetingInfoPto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

/**
 * @author shanhuiming
 */
public class MeetingFlowControllerTest extends SpringTest {

    /**
     * 申请人登录 -> 新增会议 -> 列表 -> 详情 -> 修改 ->
     * 参会人登录 -> 会议签到 ->
     * 申请人登录 -> 会议纪要 -> 流程结束 ->
     * 删除 -> 列表验证全删 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/flow/meeting/add
     * post /api/v1/flow/meeting/list
     * get /api/v1/flow/meeting/info/{id}
     * post /api/v1/flow/meeting/edit
     * get /api/v1/flow/meeting/delete/{ids}
     * post /api/v1/flow/task/list
     * post /api/v1/flow/task/complete
     * delete /api/v1/auth/logout
     */
    @Test
    public void meetingCrud() throws Exception {
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
        // 新增会议（使用预置meeting流程）
        body = """
                {
                    "meetingTopic": "单元测试会议",
                    "meetingRoom": "会议室A",
                    "members": ["cowave-sys-liubei"],
                    "beginTime": "2026-08-09 09:00:00",
                    "endTime": "2026-08-09 10:00:00"
                }
                """;
        mockPost("/api/v1/flow/meeting/add", body, liubeiToken);
        // 列表，验证新增
        mvcResult = mockPost("/api/v1/flow/meeting/list", "{}", liubeiToken);
        List<MeetingInfoPto> meetingList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, meetingList.size());
        MeetingInfoPto meeting = meetingList.get(0);
        String meetingId = meeting.getId();
        Assertions.assertEquals("单元测试会议", meeting.getMeetingTopic());
        // 详情
        mvcResult = mockGet("/api/v1/flow/meeting/info/" + meetingId, liubeiToken);
        MeetingInfoPto info = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(meetingId, info.getId());
        Assertions.assertEquals("单元测试会议", info.getMeetingTopic());
        // 修改
        body = """
                {
                    "id": "%s",
                    "meetingTopic": "单元测试会议-已修改",
                    "meetingRoom": "会议室B"
                }
                """.formatted(meetingId);
        mockPost("/api/v1/flow/meeting/edit", body, liubeiToken);
        // 详情，验证修改
        mvcResult = mockGet("/api/v1/flow/meeting/info/" + meetingId, liubeiToken);
        info = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals("单元测试会议-已修改", info.getMeetingTopic());
        // 参会人登录 -> 会议签到（多实例任务，每人一个签到）
        body = """
                {}
                """;
        mvcResult = mockPost("/api/v1/flow/task/list", body, liubeiToken);
        List<FlowTask> tasks = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(tasks.isEmpty());
        String taskId = tasks.get(0).getTaskId();
        body = """
                {
                    "taskId": "%s",
                    "comment": "已签到"
                }
                """.formatted(taskId);
        mockPost("/api/v1/flow/task/complete", body, liubeiToken);
        // 申请人登录 -> 会议纪要
        mvcResult = mockPost("/api/v1/flow/task/list", "{}", liubeiToken);
        tasks = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(tasks.isEmpty());
        taskId = tasks.get(0).getTaskId();
        body = """
                {
                    "taskId": "%s",
                    "comment": "会议纪要已填写"
                }
                """.formatted(taskId);
        mockPost("/api/v1/flow/task/complete", body, liubeiToken);
        // 流程结束
        mvcResult = mockPost("/api/v1/flow/task/list", "{}", liubeiToken);
        tasks = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertTrue(tasks.isEmpty());
        // 删除会议
        mockGet("/api/v1/flow/meeting/delete/" + meetingId, liubeiToken);
        // 列表，验证删除
        mvcResult = mockPost("/api/v1/flow/meeting/list", "{}", liubeiToken);
        meetingList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(0, meetingList.size());
        // 退出登录
        mockDelete("/api/v1/auth/logout", liubeiToken);
    }
}
