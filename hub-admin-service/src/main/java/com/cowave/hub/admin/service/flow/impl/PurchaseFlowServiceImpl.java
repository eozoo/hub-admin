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
package com.cowave.hub.admin.service.flow.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.flow.entity.FlowPurchase;
import com.cowave.hub.admin.domain.flow.entity.command.PurchaseCreate;
import com.cowave.hub.admin.domain.flow.entity.pto.PurchaseInfoPto;
import com.cowave.hub.admin.domain.flow.entity.query.PurchaseQuery;
import com.cowave.hub.admin.domain.flow.biz.FlowPurchaseBiz;
import com.cowave.hub.admin.domain.flow.repository.facade.FlowPurchaseRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubUserRepositoryFacade;
import com.cowave.hub.admin.service.flow.PurchaseFlowService;
import com.cowave.zoo.framework.access.Access;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Transactional
@Service
public class PurchaseFlowServiceImpl implements PurchaseFlowService {
    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final HistoryService historyService;
    private final IdentityService identityService;
    private final FlowPurchaseBiz purchaseBiz;
    private final FlowPurchaseRepositoryFacade purchaseRepositoryFacade;
    private final HubUserRepositoryFacade userRepositoryFacade;

    @Override
    public Page<PurchaseInfoPto> list(PurchaseQuery query) {
        PurchaseInfoPto purchaseInfoPto = new PurchaseInfoPto();
        purchaseInfoPto.setApplyUser(query.getApplyUser());
        purchaseInfoPto.setProcessStatus(query.getProcessStatus());
        purchaseInfoPto.setBeginTime(query.getBeginTime());
        purchaseInfoPto.setEndTime(query.getEndTime());
        Page<PurchaseInfoPto> page = purchaseRepositoryFacade.queryPage(purchaseInfoPto);
        for (PurchaseInfoPto p : page.getRecords()) {
            Task activeTask = taskService.createTaskQuery().processInstanceId(p.getProcessId()).active().singleResult();
            if (activeTask != null) {
                p.setTaskId(activeTask.getId());
                p.setProcessTaskUser(userRepositoryFacade.queryNameByCode(activeTask.getAssignee()));
            }
        }
        return page;
    }

    @Override
    public PurchaseInfoPto info(String id) {
        PurchaseInfoPto purchaseInfoPto = purchaseRepositoryFacade.queryById(id);
        List<HistoricVariableInstance> variables =
                historyService.createHistoricVariableInstanceQuery().processInstanceId(purchaseInfoPto.getProcessId()).list();
        Map<String, Object> variableMap = new HashMap<>();
        variables.forEach(v -> variableMap.put(v.getVariableName(), v.getValue()));
        purchaseInfoPto.setProcessVariables(variableMap);
        return purchaseInfoPto;
    }

    @Override
    public void add(PurchaseCreate cmd) {
        FlowPurchase flowPurchase = new FlowPurchase();
        flowPurchase.setContent(cmd.getContent());
        flowPurchase.setMoney(cmd.getMoney());

        String purchaseId = UUID.randomUUID().toString();
        identityService.setAuthenticatedUserId(Access.userCode());
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("starter", Access.userCode());
        variables.put("manager", cmd.getManager());
        variables.put("finance", cmd.getFinance());
        variables.put("cashier", cmd.getCashier());
        variables.put("general", cmd.getGeneral());
        variables.put("money", cmd.getMoney());
        ProcessInstance process = runtimeService.startProcessInstanceByKey("purchase", purchaseId, variables);
        flowPurchase.setId(purchaseId);
        flowPurchase.setApplyUser(Access.userCode());
        flowPurchase.setApplyTime(new Date());
        flowPurchase.setProcessId(process.getProcessInstanceId());
        purchaseBiz.create(flowPurchase);
    }

    @Override
    public void edit(PurchaseCreate cmd) {
        FlowPurchase flowPurchase = new FlowPurchase();
        flowPurchase.setId(cmd.getId());
        flowPurchase.setContent(cmd.getContent());
        flowPurchase.setMoney(cmd.getMoney());
        purchaseBiz.edit(flowPurchase);
    }

    @Override
    public void delete(String[] ids) {
        for (String id : ids) {
            PurchaseInfoPto purchaseInfoPto = info(id);
            ProcessInstance process =
                    runtimeService.createProcessInstanceQuery().processInstanceId(purchaseInfoPto.getProcessId()).singleResult();
            if (process != null) {
                runtimeService.deleteProcessInstance(process.getId(), "删除");
            }
            HistoricProcessInstance history =
                    historyService.createHistoricProcessInstanceQuery().processInstanceId(purchaseInfoPto.getProcessId()).singleResult();
            if (history != null) {
                historyService.deleteHistoricProcessInstance(history.getId());
            }
        }
        purchaseBiz.delete(ids);
    }
}
