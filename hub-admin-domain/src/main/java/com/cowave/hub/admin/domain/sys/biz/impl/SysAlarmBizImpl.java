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
package com.cowave.hub.admin.domain.sys.biz.impl;

import com.cowave.hub.admin.domain.sys.biz.SysAlarmBiz;
import com.cowave.hub.admin.domain.sys.entity.command.AlarmHandles;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmPto;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmTypePto;
import com.cowave.hub.admin.domain.sys.repository.SysAlarmRepository;
import com.cowave.zoo.http.client.asserts.Asserts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysAlarmBizImpl implements SysAlarmBiz {

    private final SysAlarmRepository alarmRepository;

    @Override
    public void createAlarmType(AlarmTypePto alarmTypePto) {
        alarmRepository.insertType(alarmTypePto);
    }

    @Override
    public void updateAlarmType(AlarmTypePto alarmTypePto) {
        alarmRepository.updateType(alarmTypePto);
    }

    @Override
    public void deleteAlarmType(Long id) {
        Asserts.isNull(alarmRepository.queryAlarmExistsByTypeId(id), "{admin.alarm.type.has.alarm}");
        alarmRepository.deleteType(id);
    }

    @Override
    public void recordAlarm(AlarmPto alarmPto) {
        if (alarmRepository.alarmIncrease(alarmPto) == 0) {
            alarmRepository.insertAlarm(alarmPto);
        }
    }

    @Override
    public void handleAlarm(AlarmHandles alarmHandles) {
        alarmRepository.updateHandle(alarmHandles);
    }

    @Override
    public void deleteAlarm(Long id) {
        alarmRepository.deleteAlarm(id);
    }
}
