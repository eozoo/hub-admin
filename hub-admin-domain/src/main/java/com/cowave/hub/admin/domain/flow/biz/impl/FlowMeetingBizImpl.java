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
package com.cowave.hub.admin.domain.flow.biz.impl;

import com.cowave.hub.admin.domain.flow.entity.FlowMeeting;
import com.cowave.hub.admin.domain.flow.biz.FlowMeetingBiz;
import com.cowave.hub.admin.domain.flow.repository.FlowMeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class FlowMeetingBizImpl implements FlowMeetingBiz {

    private final FlowMeetingRepository flowMeetingRepository;

    @Override
    public void create(FlowMeeting flowMeeting) {
        flowMeetingRepository.insert(flowMeeting);
    }

    @Override
    public void edit(FlowMeeting flowMeeting) {
        flowMeetingRepository.update(flowMeeting);
    }

    @Override
    public void delete(String[] ids) {
        flowMeetingRepository.delete(ids);
    }

    @Override
    public void changeProcessStatus(String id, Integer processStatus) {
        flowMeetingRepository.changeProcessStatus(id, processStatus);
    }
}
