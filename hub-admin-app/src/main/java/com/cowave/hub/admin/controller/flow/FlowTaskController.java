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

import com.cowave.zoo.http.client.response.Response;
import com.cowave.hub.admin.domain.flow.entity.FlowTask;
import com.cowave.hub.admin.domain.flow.entity.command.TaskComplete;
import com.cowave.hub.admin.service.flow.FlowTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程任务
 * @order 23
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/task")
public class FlowTaskController {

    private final FlowTaskService flowTaskService;

    /**
     * 全部待办
     */
    @PostMapping("/list/all")
    public Response<Response.Page<FlowTask>> listAll(@Validated @RequestBody FlowTask flowTask) {
        return Response.success(flowTaskService.listAll(flowTask));
    }

    /**
     * 我的待办
     */
    @PostMapping("/list")
    public Response<Response.Page<FlowTask>> list(@Validated @RequestBody FlowTask flowTask) {
        return Response.success(flowTaskService.list(flowTask));
    }

    /**
     * 我办理的
     */
    @PostMapping("/list/history")
    public Response<Response.Page<FlowTask>> listHistory(@Validated @RequestBody FlowTask flowTask) {
        return Response.success(flowTaskService.listHistory(flowTask));
    }

    /**
     * 任务表单
     */
    @PostMapping("/form/{taskId}")
    public Response<String> form(@PathVariable String taskId) {
        return Response.success(flowTaskService.form(taskId));
    }

    /**
     * 任务办理
     */
    @PostMapping("/complete")
    public Response<Void> complete(@Validated @RequestBody TaskComplete taskComplete) {
        flowTaskService.complete(taskComplete);
        return Response.success();
    }

    /**
     * 办理过程
     */
    @GetMapping("/records/{taskId}")
    public Response<List<FlowTask>> records(@PathVariable String taskId) {
        return Response.success(flowTaskService.records(taskId));
    }

    /**
     * 修改办理人
     */
    @GetMapping("/assignee/{taskId}/{userId}")
    public Response<Void> assignee(@PathVariable String taskId, @PathVariable String userId) {
        flowTaskService.assignee(taskId, userId);
        return Response.success();
    }

    /**
     * 催办
     */
    @GetMapping("/press/{taskId}")
    public Response<Void> press(@PathVariable String taskId) {
        flowTaskService.press(taskId);
        return Response.success();
    }
}
