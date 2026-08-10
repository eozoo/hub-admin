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

import com.cowave.hub.admin.domain.flow.entity.command.LeaveCreate;
import com.cowave.hub.admin.domain.flow.entity.pto.LeaveInfoPto;
import com.cowave.hub.admin.domain.flow.entity.query.LeaveQuery;
import com.cowave.hub.admin.service.flow.LeaveFlowService;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.http.client.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 请假申请
 * @order 24
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/leave")
public class LeaveFlowController {

    private final LeaveFlowService leaveFlowService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Response<Response.Page<LeaveInfoPto>> list(@RequestBody LeaveQuery query) {
        return Response.page(leaveFlowService.list(query));
    }

    /**
     * 我的请假
     */
    @PostMapping("/list/my")
    public Response<Response.Page<LeaveInfoPto>> mylist(@RequestBody LeaveQuery query) {
        query.setApplyUser(Access.userCode());
        return Response.page(leaveFlowService.list(query));
    }

    /**
     * 详情
     */
    @GetMapping("/info/{id}")
    public Response<LeaveInfoPto> info(@PathVariable String id) {
        return Response.success(leaveFlowService.info(id));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@Validated @RequestBody LeaveCreate cmd) {
        leaveFlowService.add(cmd);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<Void> edit(@Validated @RequestBody LeaveCreate cmd) {
        leaveFlowService.edit(cmd);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping( "/delete/{ids}")
    public Response<Void> delete(@PathVariable String[] ids) {
        leaveFlowService.delete(ids);
        return Response.success();
    }

    /**
     * 撤销
     */
    @GetMapping( "/revocate/{id}")
    public Response<Void> revocate(@PathVariable String id) {
        leaveFlowService.revocate(id);
        return Response.success();
    }
}
