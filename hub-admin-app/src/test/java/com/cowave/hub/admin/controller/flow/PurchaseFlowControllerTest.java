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
import com.cowave.hub.admin.domain.flow.entity.pto.PurchaseInfoPto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

/**
 * @author shanhuiming
 */
public class PurchaseFlowControllerTest extends SpringTest {

    /**
     * 申请人登录 -> 新增采购(指定部门/财务/出纳/总经理审批人) -> 列表验证 -> 详情 ->
     * 部门经理登录 -> 部门审批通过 ->
     * 财务登录 -> 财务审批通过 ->
     * 出纳登录 -> 出纳付款 ->
     * 申请人登录 -> 收货确认 -> 流程结束 ->
     * 修改 -> 删除 -> 列表验证删除 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/flow/purchase/add
     * post /api/v1/flow/purchase/list
     * get /api/v1/flow/purchase/info/{id}
     * post /api/v1/flow/task/list
     * post /api/v1/flow/task/complete
     * post /api/v1/flow/purchase/edit
     * get /api/v1/flow/purchase/delete/{ids}
     * post /api/v1/flow/purchase/list
     * delete /api/v1/auth/logout
     */
    @Test
    public void purchaseCrud() throws Exception {
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
        // 新增采购（指定各环节审批人，money=9999<10000 不触发总经理审批）
        body = """
                {
                    "content": "单元测试采购内容",
                    "money": 9999.00,
                    "manager": "cowave-sys-liubei",
                    "finance": "cowave-sys-liubei",
                    "cashier": "cowave-sys-liubei",
                    "general": "cowave-sys-liubei"
                }
                """;
        mockPost("/api/v1/flow/purchase/add", body, liubeiToken);
        // 列表，验证新增
        mvcResult = mockPost("/api/v1/flow/purchase/list", "{}", liubeiToken);
        List<PurchaseInfoPto> purchaseList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, purchaseList.size());
        PurchaseInfoPto purchase = purchaseList.get(0);
        String purchaseId = purchase.getId();
        Assertions.assertEquals("单元测试采购内容", purchase.getContent());
        // 详情
        mvcResult = mockGet("/api/v1/flow/purchase/info/" + purchaseId, liubeiToken);
        PurchaseInfoPto info = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(purchaseId, info.getId());
        Assertions.assertEquals("单元测试采购内容", info.getContent());
        // 部门经理登录 -> 部门审批通过
        mvcResult = mockPost("/api/v1/flow/task/list", "{}", liubeiToken);
        List<FlowTask> tasks = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(tasks.isEmpty());
        String taskId = tasks.get(0).getTaskId();
        body = """
                {
                    "taskId": "%s",
                    "variables": {"manageResult": "true"},
                    "comment": "同意"
                }
                """.formatted(taskId);
        mockPost("/api/v1/flow/task/complete", body, liubeiToken);
        // 财务登录 -> 财务审批通过
        String financeToken = liubeiToken; // 财务=liubei，复用token
        mvcResult = mockPost("/api/v1/flow/task/list", "{}", financeToken);
        tasks = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(tasks.isEmpty());
        taskId = tasks.get(0).getTaskId();
        body = """
                {
                    "taskId": "%s",
                    "variables": {"financeResult": "true"},
                    "comment": "同意"
                }
                """.formatted(taskId);
        mockPost("/api/v1/flow/task/complete", body, financeToken);
        // 出纳登录 -> 出纳付款（金额9999<10000，不经过总经理审批）
        String cashierToken = liubeiToken; // 出纳=liubei，复用token
        mvcResult = mockPost("/api/v1/flow/task/list", "{}", cashierToken);
        tasks = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(tasks.isEmpty());
        taskId = tasks.get(0).getTaskId();
        body = """
                {
                    "taskId": "%s",
                    "comment": "已付款"
                }
                """.formatted(taskId);
        mockPost("/api/v1/flow/task/complete", body, cashierToken);
        // 申请人登录 -> 收货确认
        mvcResult = mockPost("/api/v1/flow/task/list", "{}", liubeiToken);
        tasks = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(tasks.isEmpty());
        taskId = tasks.get(0).getTaskId();
        body = """
                {
                    "taskId": "%s",
                    "comment": "已收货"
                }
                """.formatted(taskId);
        mockPost("/api/v1/flow/task/complete", body, liubeiToken);
        // 流程已结束
        mvcResult = mockPost("/api/v1/flow/task/list", "{}", liubeiToken);
        tasks = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertTrue(tasks.isEmpty());
        // 修改
        body = """
                {
                    "id": "%s",
                    "content": "单元测试采购内容-已修改"
                }
                """.formatted(purchaseId);
        mockPost("/api/v1/flow/purchase/edit", body, liubeiToken);
        // 详情，验证修改
        mvcResult = mockGet("/api/v1/flow/purchase/info/" + purchaseId, liubeiToken);
        info = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals("单元测试采购内容-已修改", info.getContent());
        // 删除采购
        mockGet("/api/v1/flow/purchase/delete/" + purchaseId, liubeiToken);
        // 列表，验证删除
        mvcResult = mockPost("/api/v1/flow/purchase/list", "{}", liubeiToken);
        purchaseList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(0, purchaseList.size());
        // 退出登录
        mockDelete("/api/v1/auth/logout", liubeiToken);
    }
}
