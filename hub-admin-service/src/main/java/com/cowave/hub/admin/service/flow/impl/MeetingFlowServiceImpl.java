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
import com.cowave.hub.admin.domain.flow.entity.FlowMeeting;
import com.cowave.hub.admin.domain.flow.entity.command.MeetingCreate;
import com.cowave.hub.admin.domain.flow.entity.pto.MeetingInfoPto;
import com.cowave.hub.admin.domain.flow.entity.query.MeetingQuery;
import com.cowave.hub.admin.domain.flow.biz.FlowMeetingBiz;
import com.cowave.hub.admin.domain.flow.repository.facade.FlowMeetingRepositoryFacade;
import com.cowave.hub.admin.service.flow.MeetingFlowService;
import com.cowave.zoo.framework.access.Access;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Transactional
@Service
public class MeetingFlowServiceImpl implements MeetingFlowService {
    private final RuntimeService runtimeService;
    private final IdentityService identityService;
    private final HistoryService historyService;
    private final FlowMeetingBiz meetingBiz;
    private final FlowMeetingRepositoryFacade meetingRepositoryFacade;

    @Override
    public Page<MeetingInfoPto> list(MeetingQuery query) {
        MeetingInfoPto meetingInfoPto = new MeetingInfoPto();
        meetingInfoPto.setMeetingRoom(query.getMeetingRoom());
        meetingInfoPto.setProcessStatus(query.getProcessStatus());
        meetingInfoPto.setBeginTime(query.getBeginTime());
        meetingInfoPto.setEndTime(query.getEndTime());
        return meetingRepositoryFacade.queryPage(meetingInfoPto);
    }

    @Override
    public MeetingInfoPto info(String id) {
        return meetingRepositoryFacade.queryById(id);
    }

    @Override
    public void add(MeetingCreate cmd) {
        FlowMeeting flowMeeting = new FlowMeeting();
        flowMeeting.setMeetingTopic(cmd.getMeetingTopic());
        flowMeeting.setMeetingRoom(cmd.getMeetingRoom());
        flowMeeting.setMembers(cmd.getMembers());
        flowMeeting.setBeginTime(cmd.getBeginTime());
        flowMeeting.setEndTime(cmd.getEndTime());

        String meetingId = UUID.randomUUID().toString();
        identityService.setAuthenticatedUserId(Access.userCode());
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("applyUser", Access.userCode());
        variables.put("members", cmd.getMembers());
        ProcessInstance process = runtimeService.startProcessInstanceByKey("meeting", String.valueOf(meetingId), variables);
        flowMeeting.setId(meetingId);
        flowMeeting.setApplyUser(Access.userCode());
        flowMeeting.setApplyTime(new Date());
        flowMeeting.setProcessId(process.getProcessInstanceId());
        meetingBiz.create(flowMeeting);
    }

    @Override
    public void edit(MeetingCreate cmd) {
        FlowMeeting flowMeeting = new FlowMeeting();
        flowMeeting.setId(cmd.getId());
        flowMeeting.setContent(cmd.getContent());
        flowMeeting.setMeetingTopic(cmd.getMeetingTopic());
        flowMeeting.setMeetingRoom(cmd.getMeetingRoom());
        meetingBiz.edit(flowMeeting);
    }

    @Override
    public void delete(String[] ids) {
        for (String id : ids) {
            MeetingInfoPto meetingInfoPto = meetingRepositoryFacade.queryById(id);
            ProcessInstance process =
                    runtimeService.createProcessInstanceQuery().processInstanceId(meetingInfoPto.getProcessId()).singleResult();
            if (process != null) {
                runtimeService.deleteProcessInstance(meetingInfoPto.getProcessId(), "删除");
            }
            HistoricProcessInstance history =
                    historyService.createHistoricProcessInstanceQuery().processInstanceId(meetingInfoPto.getProcessId()).singleResult();
            if (history != null) {
                historyService.deleteHistoricProcessInstance(meetingInfoPto.getProcessId());
            }
        }
        meetingBiz.delete(ids);
    }
}
