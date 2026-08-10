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
import com.cowave.hub.admin.domain.flow.entity.FlowDeploy;
import com.cowave.hub.admin.service.flow.FlowDeployService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 流程部署
 * @order 20
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/deploy")
public class FlowDeployController {

    private final FlowDeployService flowDeployService;

    /**
     * 流程选项
     */
    @GetMapping(value = "/options")
    public Response<List<FlowDeploy>> options() {
        return Response.success(flowDeployService.options());
    }

    /**
     * 列表
     */
    @PostMapping(value = "/list")
    public Response<Response.Page<FlowDeploy>> list(@Validated @RequestBody FlowDeploy flowDeploy) {
        return Response.success(flowDeployService.list(flowDeploy));
    }

    /**
     * 部署
     */
    @RequestMapping(value = "/upload")
    public Response<Void> upload(@RequestParam MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        flowDeployService.upload(file, filename);
        return Response.success(null, "导入成功: " + filename);
    }

    /**
     * 流程定义
     */
    @GetMapping("/definition")
    public Response<Map<String, String>> definition(String deploymentId, String resourceName) throws Exception {
        return Response.success(flowDeployService.definition(deploymentId, resourceName));
    }

    /**
     * 流程图
     */
    @GetMapping("/diagram/{id}")
    public Response<String> diagram(@PathVariable("id") String id) throws Exception {
        return Response.success(flowDeployService.diagram(id));
    }

    /**
     * 转为模型
     */
    @GetMapping("/translate/{id}")
    public Response<Void> translate(@PathVariable("id") String id) {
        String definitionName = flowDeployService.translate(id);
        return Response.success(null, "[" + definitionName + "]流程转为模型成功");
    }

    /**
     * 删除部署
     */
    @GetMapping("/delete/{deploymentIds}")
    public Response<Void> delete(@PathVariable String[] deploymentIds) {
        flowDeployService.delete(deploymentIds);
        return Response.success();
    }
}
