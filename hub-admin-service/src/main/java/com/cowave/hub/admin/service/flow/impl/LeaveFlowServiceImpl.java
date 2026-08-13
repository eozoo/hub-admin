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
import com.cowave.hub.admin.domain.flow.entity.FlowLeave;
import com.cowave.hub.admin.domain.flow.entity.command.LeaveCreate;
import com.cowave.hub.admin.domain.flow.entity.pto.LeaveInfoPto;
import com.cowave.hub.admin.domain.flow.entity.query.LeaveQuery;
import com.cowave.hub.admin.domain.flow.biz.FlowLeaveBiz;
import com.cowave.hub.admin.domain.flow.repository.facade.FlowLeaveRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.service.flow.FlowInstanceService;
import com.cowave.hub.admin.service.flow.LeaveFlowService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Transactional
@Service
public class LeaveFlowServiceImpl implements LeaveFlowService {
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final IdentityService identityService;
    private final HistoryService historyService;
    private final FlowInstanceService flowInstanceService;
    private final FlowLeaveBiz leaveBiz;
    private final FlowLeaveRepositoryFacade leaveRepositoryFacade;
    private final SysUserRepositoryFacade userRepositoryFacade;

    @Override
    public Page<LeaveInfoPto> list(LeaveQuery query) {
        LeaveInfoPto leaveInfoPto = new LeaveInfoPto();
        leaveInfoPto.setApplyUser(query.getApplyUser());
        leaveInfoPto.setLeaveType(query.getLeaveType());
        leaveInfoPto.setProcessStatus(query.getProcessStatus());
        leaveInfoPto.setBeginTime(query.getBeginTime());
        leaveInfoPto.setEndTime(query.getEndTime());
        Page<LeaveInfoPto> page = leaveRepositoryFacade.queryPage(leaveInfoPto);
        for (LeaveInfoPto l : page.getRecords()) {
            if (FlowLeave.PROCESS_STATUS_APPROVING.equals(l.getProcessStatus())) {
                Task activeTask = taskService.createTaskQuery().processInstanceId(l.getProcessId()).active().singleResult();
                if (activeTask != null) {
                    l.setTaskId(activeTask.getId());
                    l.setProcessTask(activeTask.getName());
                    l.setProcessTaskUser(userRepositoryFacade.queryNameByCode(activeTask.getAssignee()));
                }
            }
        }
        return page;
    }

    @Override
    public LeaveInfoPto info(String id) {
        LeaveInfoPto leaveInfoPto = leaveRepositoryFacade.queryById(id);
        List<HistoricVariableInstance> variables =
                historyService.createHistoricVariableInstanceQuery().processInstanceId(leaveInfoPto.getProcessId()).list();
        Map<String, Object> variableMap = new HashMap<>();
        variables.forEach(v -> variableMap.put(v.getVariableName(), v.getValue()));
        leaveInfoPto.setProcessVariables(variableMap);
        return leaveInfoPto;
    }

    @Override
    public void add(LeaveCreate cmd) {
        FlowLeave flowLeave = new FlowLeave();
        flowLeave.setLeaveType(cmd.getLeaveType());
        flowLeave.setReason(cmd.getReason());
        flowLeave.setBeginTime(cmd.getBeginTime());
        flowLeave.setEndTime(cmd.getEndTime());

        String leaveId = UUID.randomUUID().toString();
        identityService.setAuthenticatedUserId(Access.userCode());
        Map<String, Object> variables = new HashMap<>();
        variables.put("applyUser", Access.userCode());
        variables.put("deptApprover", cmd.getDeptApprover());
        ProcessInstance process = runtimeService.startProcessInstanceByKey("leave", String.valueOf(leaveId), variables);
        String processId = process.getProcessInstanceId();
        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        taskService.addComment(task.getId(), processId, "comment", "申请");
        taskService.complete(task.getId());
        flowLeave.setId(leaveId);
        flowLeave.setProcessId(processId);
        flowLeave.setApplyUser(Access.userCode());
        flowLeave.setApplyTime(process.getStartTime());
        leaveBiz.create(flowLeave);
    }

    @Override
    public int edit(LeaveCreate cmd) {
        FlowLeave flowLeave = new FlowLeave();
        flowLeave.setId(cmd.getId());
        flowLeave.setLeaveType(cmd.getLeaveType());
        flowLeave.setReason(cmd.getReason());
        flowLeave.setBeginTime(cmd.getBeginTime());
        flowLeave.setEndTime(cmd.getEndTime());
        return leaveBiz.edit(flowLeave);
    }

    @Override
    public void delete(String[] ids) {
        for (String id : ids) {
            LeaveInfoPto leaveInfoPto = leaveRepositoryFacade.queryById(id);
            ProcessInstance process =
                    runtimeService.createProcessInstanceQuery().processInstanceId(leaveInfoPto.getProcessId()).singleResult();
            if (process != null) {
                runtimeService.deleteProcessInstance(leaveInfoPto.getProcessId(), "删除");
            }
            HistoricProcessInstance history =
                    historyService.createHistoricProcessInstanceQuery().processInstanceId(leaveInfoPto.getProcessId()).singleResult();
            if (history != null) {
                historyService.deleteHistoricProcessInstance(leaveInfoPto.getProcessId());
            }
        }
        leaveBiz.delete(ids);
    }

    @Override
    public void revocate(String id) {
        LeaveInfoPto leaveInfoPto = leaveRepositoryFacade.queryById(id);
        flowInstanceService.revocate(leaveInfoPto.getProcessId(), "leaveRevocatedEnd");
    }
}
