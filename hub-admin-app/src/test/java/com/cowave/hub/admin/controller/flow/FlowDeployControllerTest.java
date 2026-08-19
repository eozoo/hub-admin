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
import com.cowave.hub.admin.domain.flow.entity.FlowDeploy;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

/**
 * @author shanhuiming
 */
public class FlowDeployControllerTest extends SpringTest {

    /**
     * 登录 -> 获取选项 -> 创建图形流程 -> 流程图 -> 上传BPMN -> 列表验证 -> 流程定义 -> 转为模型 -> 删除部署 -> 退出登录
     * post /api/v1/auth/public/logon
     * get /api/v1/flow/deploy/options
     * get /api/v1/flow/designer/create/{flowKey}
     * get /api/v1/flow/deploy/diagram/{id}
     * post /api/v1/flow/deploy/upload
     * post /api/v1/flow/deploy/list
     * get /api/v1/flow/deploy/definition
     * get /api/v1/flow/deploy/translate/{id}
     * get /api/v1/flow/deploy/delete/{deploymentIds}
     * delete /api/v1/auth/logout
     */
    @Test
    public void deployUploadAndManage() throws Exception {
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
        // 获取流程选项
        mockGet("/api/v1/flow/deploy/options", accessToken);
        // designer创建带BPMNDI的流程（用于测试流程图接口）
        mvcResult = mockGet("/api/v1/flow/designer/create/diagram-test");
        String diagramModelId = readString(mvcResult, "/data");
        // 查询部署获取processDefinitionId
        body = """
                {"key": "diagram-test"}
                """;
        mvcResult = mockPost("/api/v1/flow/deploy/list", body, accessToken);
        List<FlowDeploy> diagramDeployList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(diagramDeployList.isEmpty());
        FlowDeploy diagramDeploy = diagramDeployList.get(0);
        String diagramProcessId = diagramDeploy.getId();
        String diagramDeploymentId = diagramDeploy.getDeploymentId();
        // 流程图
        mvcResult = mockGet("/api/v1/flow/deploy/diagram/" + diagramProcessId, accessToken);
        String diagramBase64 = readString(mvcResult, "/data");
        Assertions.assertFalse(diagramBase64.isEmpty());
        // 上传BPMN文件部署
        String bpmnXml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL"
                             xmlns:flowable="http://flowable.org/bpmn"
                             xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI"
                             xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC"
                             xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI"
                             targetNamespace="http://flowable.org/bpmn">
                  <process id="test-deploy-process" name="测试部署流程" isExecutable="true">
                    <startEvent id="start" />
                    <userTask id="task1" name="审批" flowable:assignee="1" />
                    <endEvent id="end" />
                    <sequenceFlow id="flow1" sourceRef="start" targetRef="task1" />
                    <sequenceFlow id="flow2" sourceRef="task1" targetRef="end" />
                  </process>
                  <bpmndi:BPMNDiagram id="BPMNDiagram_test-deploy-process">
                    <bpmndi:BPMNPlane id="BPMNPlane_test-deploy-process" bpmnElement="test-deploy-process">
                      <bpmndi:BPMNShape id="BPMNShape_start" bpmnElement="start">
                        <omgdc:Bounds x="100" y="100" width="36" height="36" />
                      </bpmndi:BPMNShape>
                      <bpmndi:BPMNShape id="BPMNShape_task1" bpmnElement="task1">
                        <omgdc:Bounds x="200" y="100" width="100" height="80" />
                      </bpmndi:BPMNShape>
                      <bpmndi:BPMNShape id="BPMNShape_end" bpmnElement="end">
                        <omgdc:Bounds x="360" y="100" width="36" height="36" />
                      </bpmndi:BPMNShape>
                      <bpmndi:BPMNEdge id="BPMNEdge_flow1" bpmnElement="flow1">
                        <omgdi:waypoint x="136" y="118" />
                        <omgdi:waypoint x="200" y="140" />
                      </bpmndi:BPMNEdge>
                      <bpmndi:BPMNEdge id="BPMNEdge_flow2" bpmnElement="flow2">
                        <omgdi:waypoint x="300" y="140" />
                        <omgdi:waypoint x="360" y="118" />
                      </bpmndi:BPMNEdge>
                    </bpmndi:BPMNPlane>
                  </bpmndi:BPMNDiagram>
                </definitions>
                """;
        MockMultipartFile file = new MockMultipartFile(
                "file", "test-deploy.bpmn20.xml", "text/xml", bpmnXml.getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/flow/deploy/upload")
                        .file(file)
                        .header("Authorization", accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        // 列表，验证部署
        body = """
                {"key": "test-deploy-process", "latest": true}
                """;
        mvcResult = mockPost("/api/v1/flow/deploy/list", body, accessToken);
        List<FlowDeploy> deployList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, deployList.size());
        FlowDeploy deploy = deployList.get(0);
        String deploymentId = deploy.getDeploymentId();
        // 流程定义XML
        mvcResult = mockGet("/api/v1/flow/deploy/definition?deploymentId=" + deploymentId
                + "&resourceName=test-deploy.bpmn20.xml", accessToken);
        Assertions.assertNotNull(mvcResult.getResponse().getContentAsString());
        // 转为模型
        mvcResult = mockGet("/api/v1/flow/deploy/translate/" + deploy.getId(), accessToken);
        String msg = readString(mvcResult, "/msg");
        Assertions.assertTrue(msg.contains("转为模型成功"));
        // 删除上传的部署
        mockGet("/api/v1/flow/deploy/delete/" + deploymentId, accessToken);
        // 删除diagram测试的部署
        mockGet("/api/v1/flow/deploy/delete/" + diagramDeploymentId, accessToken);
        // 清理diagram测试模型
        mockGet("/api/v1/flow/model/delete/" + diagramModelId, accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
