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
package com.cowave.hub.admin.infra.flow.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.flow.entity.FlowMeeting;
import com.cowave.hub.admin.domain.flow.entity.pto.MeetingInfoPto;
import com.cowave.hub.admin.domain.flow.repository.FlowMeetingRepository;
import com.cowave.hub.admin.infra.flow.mapper.FlowMeetingMapper;
import com.cowave.zoo.framework.access.Access;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class FlowMeetingDao extends ServiceImpl<FlowMeetingMapper, FlowMeeting> implements FlowMeetingRepository {

    private final FlowMeetingMapper flowMeetingMapper;

    @Override
    public Page<MeetingInfoPto> queryPage(MeetingInfoPto meetingInfoPto) {
        return flowMeetingMapper.list(Access.page(), meetingInfoPto);
    }

    @Override
    public MeetingInfoPto queryById(String id) {
        return flowMeetingMapper.info(id);
    }

    @Override
    public void insert(FlowMeeting flowMeeting) {
        flowMeetingMapper.insert(flowMeeting);
    }

    @Override
    public void update(FlowMeeting flowMeeting) {
        lambdaUpdate()
                .eq(FlowMeeting::getId, flowMeeting.getId())
                .set(FlowMeeting::getContent, flowMeeting.getContent())
                .set(FlowMeeting::getMeetingTopic, flowMeeting.getMeetingTopic())
                .set(FlowMeeting::getMeetingRoom, flowMeeting.getMeetingRoom())
                .update();
    }

    @Override
    public int delete(String[] ids) {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void changeProcessStatus(String id, Integer processStatus) {
        lambdaUpdate()
                .eq(FlowMeeting::getId, id)
                .set(FlowMeeting::getProcessStatus, processStatus)
                .update();
    }
}
