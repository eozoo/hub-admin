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
import com.cowave.hub.admin.domain.flow.entity.FlowLeave;
import com.cowave.hub.admin.domain.flow.entity.pto.LeaveInfoPto;
import com.cowave.hub.admin.domain.flow.repository.FlowLeaveRepository;
import com.cowave.hub.admin.infra.flow.mapper.FlowLeaveMapper;
import com.cowave.zoo.framework.access.Access;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class FlowLeaveDao extends ServiceImpl<FlowLeaveMapper, FlowLeave> implements FlowLeaveRepository {

    private final FlowLeaveMapper leaveMapper;

    @Override
    public Page<LeaveInfoPto> queryPage(LeaveInfoPto leaveInfoPto) {
        return leaveMapper.list(Access.page(), leaveInfoPto);
    }

    @Override
    public LeaveInfoPto queryById(String id) {
        return leaveMapper.info(id);
    }

    @Override
    public void insert(FlowLeave flowLeave) {
        save(flowLeave);
    }

    @Override
    public int update(FlowLeave flowLeave) {
        return lambdaUpdate()
                .eq(FlowLeave::getId, flowLeave.getId())
                .set(FlowLeave::getBeginTime, flowLeave.getBeginTime())
                .set(FlowLeave::getEndTime, flowLeave.getEndTime())
                .set(FlowLeave::getLeaveType, flowLeave.getLeaveType())
                .set(FlowLeave::getReason, flowLeave.getReason())
                .update() ? 1 : 0;
    }

    @Override
    public int delete(String[] ids) {
        return baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void changeProcessStatus(String id, Integer processStatus) {
        lambdaUpdate()
                .eq(FlowLeave::getId, id)
                .set(FlowLeave::getProcessStatus, processStatus)
                .update();
    }
}
