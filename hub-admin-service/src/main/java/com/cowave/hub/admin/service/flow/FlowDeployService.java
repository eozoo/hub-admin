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
package com.cowave.hub.admin.service.flow;

import com.cowave.zoo.http.client.asserts.AssertsException;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.Access;
import com.cowave.hub.admin.domain.flow.entity.FlowDeploy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.constants.ModelDataJsonConstants;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.image.ProcessDiagramGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class FlowDeployService {

    private final RepositoryService repositoryService;

    private final ProcessEngineConfiguration configuration;

    /**
     * 流程选项
     */
    public List<FlowDeploy> options() {
        ProcessDefinitionQuery processQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> list = processQuery.orderByDeploymentId().desc().list();
        List<FlowDeploy> processList = new ArrayList<>();
        for (ProcessDefinition definition : list) {
            FlowDeploy deploy = new FlowDeploy();
            deploy.setId(definition.getId());
            deploy.setKey(definition.getKey());
            deploy.setName(definition.getName());
            processList.add(deploy);
        }
        return processList;
    }

    /**
     * 列表
     */
    public Response.Page<FlowDeploy> list(FlowDeploy flowDeploy) {
        ProcessDefinitionQuery processQuery = repositoryService.createProcessDefinitionQuery();
        flowDeploy.fillProcessQuery(processQuery);
        List<ProcessDefinition> list = processQuery.orderByDeploymentId().desc().listPage(Access.pageOffset(), Access.pageSize());
        List<FlowDeploy> processList = new ArrayList<>();
        for (ProcessDefinition definition : list) {
            FlowDeploy deploy = new FlowDeploy();
            deploy.setId(definition.getId());
            deploy.setKey(definition.getKey());
            deploy.setName(definition.getName());
            deploy.setVersion(definition.getVersion());
            deploy.setDeploymentId(definition.getDeploymentId());
            deploy.setResourceName(definition.getResourceName());
            deploy.setDiagramresourceName(definition.getDiagramResourceName());
            processList.add(deploy);
        }
        return new Response.Page<>(processList, processQuery.count());
    }

    /**
     * 部署
     */
    public void upload(MultipartFile file, String filename) throws IOException {
        try(InputStream input = file.getInputStream()){
            if (filename.endsWith("zip")) {
                repositoryService.createDeployment().name(filename).addZipInputStream(new ZipInputStream(input)).deploy();
            } else if (filename.endsWith("bpmn") || filename.endsWith("xml")) {
                repositoryService.createDeployment().name(filename).addInputStream(filename, input).deploy();
            } else {
                throw new AssertsException("文件格式错误");
            }
        }
    }

    /**
     * 流程定义
     */
    public Map<String, String> definition(String deploymentId, String resourceName) throws Exception {
        try(InputStream input = repositoryService.getResourceAsStream(deploymentId, resourceName)){
            Map<String, String> map = new HashMap<>();
            map.put(resourceName, IOUtils.toString(input, StandardCharsets.UTF_8));
            return map;
        }
    }

    /**
     * 流程图
     */
    public String diagram(String id) throws Exception {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(id);
        ProcessDiagramGenerator diagramGenerator = configuration.getProcessDiagramGenerator();
        try(InputStream input = diagramGenerator.generateDiagram(bpmnModel,
                "png",  "宋体", "宋体", "宋体",
                configuration.getClassLoader(), true);
            ByteArrayOutputStream output = new ByteArrayOutputStream()){
            IOUtils.copy(input, output);
            return Base64.getEncoder().encodeToString(output.toByteArray());
        }
    }

    /**
     * 转为模型
     */
    public String translate(String id) {
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
        Model modelData = repositoryService.newModel();
        modelData.setKey(definition.getKey());
        modelData.setName(definition.getName());
        modelData.setCategory(definition.getCategory());

        ObjectNode modelJson = new ObjectMapper().createObjectNode();
        modelJson.put(ModelDataJsonConstants.MODEL_NAME, definition.getName());
        modelJson.put(ModelDataJsonConstants.MODEL_DESCRIPTION, definition.getDescription());
        List<Model> models = repositoryService.createModelQuery().modelKey(definition.getKey()).list();
        if (!models.isEmpty()) {
            Integer version = models.get(0).getVersion() + 1;
            modelJson.put(ModelDataJsonConstants.MODEL_REVISION, version);
            repositoryService.deleteModel(models.get(0).getId()); // 删除旧模型
            modelData.setVersion(version);
        } else {
            modelJson.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        }
        modelData.setMetaInfo(modelJson.toString());
        modelData.setDeploymentId(definition.getDeploymentId());
        // 保存新模型
        repositoryService.saveModel(modelData);
        // 保存模型json
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definition.getId());
        ObjectNode objectNode = new BpmnJsonConverter().convertToJson(bpmnModel);
        repositoryService.addModelEditorSource(modelData.getId(), objectNode.toString().getBytes(StandardCharsets.UTF_8));
        return definition.getName();
    }

    /**
     * 删除部署
     */
    public void delete(String[] deploymentIds) {
        for(String deploymentId : deploymentIds){
            repositoryService.deleteDeployment(deploymentId, true);
        }
    }
}
