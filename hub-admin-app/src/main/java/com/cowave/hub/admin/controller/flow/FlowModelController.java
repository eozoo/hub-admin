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
import com.cowave.hub.admin.domain.flow.entity.FlowModel;
import com.cowave.hub.admin.service.flow.FlowModelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.repository.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 流程模型
 * @order 21
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/model")
public class FlowModelController {

    private final FlowModelService flowModelService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Response<Response.Page<Model>> list(@RequestBody FlowModel flowModel) {
        return Response.success(flowModelService.list(flowModel));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@Validated @RequestBody FlowModel flowModel) throws JsonProcessingException {
        flowModelService.add(flowModel);
        return Response.success();
    }

    /**
     * 发布
     * @param modelId 流程id
     */
    @GetMapping("/deploy/{modelId}")
    public Response<Void> publish(@PathVariable String modelId) throws IOException {
        flowModelService.publish(modelId);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping("/delete/{modelIds}")
    public Response<Void> delete(@PathVariable String[] modelIds) {
        flowModelService.delete(modelIds);
        return Response.success();
    }

    /**
     * 导出
     */
    @RequestMapping("/export/{modelId}")
    public void export(@PathVariable String modelId, HttpServletResponse response) throws IOException {
        flowModelService.export(modelId, response);
    }
}
