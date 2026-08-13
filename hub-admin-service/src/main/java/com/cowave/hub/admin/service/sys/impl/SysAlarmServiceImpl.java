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
package com.cowave.hub.admin.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.biz.SysAlarmBiz;
import com.cowave.hub.admin.domain.sys.entity.command.AlarmHandles;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmPto;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmTypePto;
import com.cowave.hub.admin.domain.sys.repository.facade.SysAlarmRepositoryFacade;
import com.cowave.hub.admin.service.sys.SysAlarmService;
import com.cowave.zoo.framework.access.Access;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class SysAlarmServiceImpl implements SysAlarmService {

    private final SysAlarmBiz alarmBiz;

    private final SysAlarmRepositoryFacade alarmRepositoryFacade;

    @Override
    public void add(AlarmPto alarmPto) {
        alarmBiz.recordAlarm(alarmPto);
    }

    @Override
    public Page<AlarmTypePto> typeList(AlarmTypePto alarmTypePto) {
        return alarmRepositoryFacade.queryTypePage(Access.page(), alarmTypePto);
    }

    @Override
    public void typeAdd(AlarmTypePto alarmTypePto) {
        alarmBiz.createAlarmType(alarmTypePto);
    }

    @Override
    public void typeEdit(AlarmTypePto alarmTypePto) {
        alarmBiz.updateAlarmType(alarmTypePto);
    }

    @Override
    public void typeDelete(Long id) {
        alarmBiz.deleteAlarmType(id);
    }

    @Override
    public Page<AlarmPto> list(AlarmPto alarmPto) {
        return alarmRepositoryFacade.queryAlarmPage(Access.page(), alarmPto);
    }

    @Override
    public AlarmPto info(Long id) {
        return alarmRepositoryFacade.queryAlarmById(id);
    }

    @Override
    public void delete(Long id) {
        alarmBiz.deleteAlarm(id);
    }

    @Override
    public void handle(AlarmHandles alarmHandles) {
        alarmBiz.handleAlarm(alarmHandles);
    }
}
