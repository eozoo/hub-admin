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

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.editor.constants.ModelDataJsonConstants;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.validation.ProcessValidator;
import org.flowable.validation.ProcessValidatorFactory;
import org.flowable.validation.ValidationError;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class FlowDesignerService {

    private final ObjectMapper objectMapper;

    private final RepositoryService repositoryService;

    /**
     * 流程信息
     */
    public ObjectNode info(String modelId) throws IOException {
        Model model = repositoryService.getModel(modelId);
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put("modelId", model.getId());
        modelNode.put("name", model.getName());
        modelNode.put("key", model.getKey());
        modelNode.put("description", JSONObject.parseObject(model.getMetaInfo()).getString("description"));
        modelNode.putPOJO("lastUpdated", model.getLastUpdateTime());
        byte[] modelEditorSource = repositoryService.getModelEditorSource(modelId);
        ObjectNode editorJsonNode;
        if (null != modelEditorSource && modelEditorSource.length > 0) {
            editorJsonNode = (ObjectNode) objectMapper.readTree(modelEditorSource);
        } else {
            editorJsonNode = objectMapper.createObjectNode();
            editorJsonNode.put("id", "canvas");
            editorJsonNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        }
        editorJsonNode.put("modelType", "model");
        modelNode.replace("model", editorJsonNode);
        return modelNode;
    }

    /**
     * 保存
     */
    public void save(String modelId, MultiValueMap<String, String> values) throws JsonProcessingException {
        String json = values.getFirst("json_xml");
        String name = values.getFirst("name");
        String key = values.getFirst("key");
        String description = values.getFirst("description");
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, StringUtils.defaultString(description));

        Model model = repositoryService.getModel(modelId);
        if (null == model) {
            model = repositoryService.newModel();
        }
        model.setName(name);
        model.setDeploymentId(null);  // 显示发布按钮
        model.setKey(StringUtils.defaultString(key));
        model.setMetaInfo(modelNode.toString());
        repositoryService.saveModel(model);
        ObjectNode modelJson = (ObjectNode) new ObjectMapper().readTree(json);
        repositoryService.addModelEditorSource(model.getId(), modelJson.toString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 校验
     */
    public List<ValidationError> validate(JsonNode body) {
        if (body != null && body.has("stencilset")) {
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(body);
            ProcessValidator validator = new ProcessValidatorFactory().createDefaultProcessValidator();
            return validator.validate(bpmnModel);
        }
        return Collections.emptyList();
    }

    /**
     * 流程创建
     */
    public String create(String flowKey) throws IOException {
        // 节点
        StartEvent startEvent = new StartEvent();
        startEvent.setId("start");
        startEvent.setName("start");
        UserTask userTask = new UserTask();
        userTask.setId("userTask");
        userTask.setName("审批任务");
        EndEvent endEvent = new EndEvent();
        endEvent.setId("end");
        endEvent.setName("end");
        // 连线
        SequenceFlow sequence1 = new SequenceFlow();
        sequence1.setId("flow1");
        sequence1.setName("flow1");
        sequence1.setSourceRef(startEvent.getId());
        sequence1.setTargetRef(userTask.getId());
        SequenceFlow sequence2 = new SequenceFlow();
        sequence2.setId("flow2");
        sequence2.setName("flow2");
        sequence2.setSourceRef(userTask.getId());
        sequence2.setTargetRef(endEvent.getId());
        List<SequenceFlow> sequenceList1 = new ArrayList<>();
        List<SequenceFlow> sequenceList2 = new ArrayList<>();
        sequenceList1.add(sequence1);
        sequenceList2.add(sequence2);
        // 连接节点
        startEvent.setOutgoingFlows(sequenceList1);
        userTask.setOutgoingFlows(sequenceList2);
        // 流程对象
        Process process = new Process();
        process.setId(flowKey);
        process.setName("流程创建测试");
        process.addFlowElement(startEvent);
        process.addFlowElement(sequence1);
        process.addFlowElement(userTask);
        process.addFlowElement(sequence2);
        process.addFlowElement(endEvent);
        // 模型对象
        BpmnModel bpmnModel=new BpmnModel();
        bpmnModel.addProcess(process);
        // 流程图自动布局
        new BpmnAutoLayout(bpmnModel).execute();
        // 合法性校验
        List<ValidationError> validationErrorList = repositoryService.validateProcess(bpmnModel);
        if (validationErrorList.isEmpty()) {
            // 转换为BPMN JSON（含BPMNDI图形数据）
            ObjectNode editorJson = new BpmnJsonConverter().convertToJson(bpmnModel);
            // 创建Model
            Model model = repositoryService.newModel();
            model.setKey(flowKey);
            model.setName("流程创建测试");
            model.setCategory("dynamic");
            model.setVersion(1);
            ObjectNode metaInfo = objectMapper.createObjectNode();
            metaInfo.put(ModelDataJsonConstants.MODEL_NAME, "流程创建测试");
            metaInfo.put(ModelDataJsonConstants.MODEL_REVISION, 1);
            metaInfo.put(ModelDataJsonConstants.MODEL_DESCRIPTION, "");
            model.setMetaInfo(metaInfo.toString());
            repositoryService.saveModel(model);
            // 保存模型文件（含BPMNDI）
            repositoryService.addModelEditorSource(model.getId(), editorJson.toString().getBytes(StandardCharsets.UTF_8));
            // 部署
            Deployment deploy = repositoryService.createDeployment().category("dynamic").key(flowKey)
                    .addBpmnModel(flowKey + ".bpmn20.xml", bpmnModel).deploy();
            // 重新获取model关联deploymentId（避免乐观锁冲突）
            model = repositoryService.getModel(model.getId());
            model.setDeploymentId(deploy.getId());
            repositoryService.saveModel(model);
            return model.getId();
        }
        return null;
    }
}
