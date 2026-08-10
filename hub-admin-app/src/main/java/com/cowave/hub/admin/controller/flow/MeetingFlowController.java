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

import com.cowave.hub.admin.domain.flow.entity.command.MeetingCreate;
import com.cowave.hub.admin.domain.flow.entity.pto.MeetingInfoPto;
import com.cowave.hub.admin.domain.flow.entity.query.MeetingQuery;
import com.cowave.hub.admin.service.flow.MeetingFlowService;
import com.cowave.zoo.http.client.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 会议预约
 * @order 25
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/meeting")
public class MeetingFlowController {

    private final MeetingFlowService meetingFlowService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Response<Response.Page<MeetingInfoPto>> list(@RequestBody MeetingQuery query) {
        return Response.page(meetingFlowService.list(query));
    }

    /**
     * 详情
     */
    @GetMapping( "/info/{id}")
    public Response<MeetingInfoPto> info(@PathVariable String id) {
        return Response.success(meetingFlowService.info(id));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@RequestBody MeetingCreate cmd) {
        meetingFlowService.add(cmd);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<Void> edit(@RequestBody MeetingCreate cmd) {
        meetingFlowService.edit(cmd);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping( "/delete/{ids}")
    public Response<Void> delete(@PathVariable String[] ids) {
        meetingFlowService.delete(ids);
        return Response.success();
    }
}
