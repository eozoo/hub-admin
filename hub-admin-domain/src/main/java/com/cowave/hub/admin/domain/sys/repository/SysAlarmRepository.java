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
package com.cowave.hub.admin.domain.sys.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cowave.hub.admin.domain.sys.entity.SysAlarm;
import com.cowave.hub.admin.domain.sys.entity.command.AlarmHandles;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmPto;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmTypePto;
import com.cowave.hub.admin.domain.sys.repository.facade.SysAlarmRepositoryFacade;

/**
 * @author shanhuiming
 */
public interface SysAlarmRepository extends SysAlarmRepositoryFacade, IService<SysAlarm> {

    /**
     * 类型新增
     */
    void insertType(AlarmTypePto alarmTypePto);

    /**
     * 类型更新
     */
    void updateType(AlarmTypePto alarmTypePto);

    /**
     * 类型删除
     */
    void deleteType(Long id);

    /**
     * 告警累计（累加 alarm_times + 1）
     */
    int alarmIncrease(AlarmPto alarmPto);

    /**
     * 新增告警
     */
    void insertAlarm(AlarmPto alarmPto);

    /**
     * 删除告警
     */
    void deleteAlarm(Long id);

    /**
     * 处理告警
     */
    void updateHandle(AlarmHandles alarmHandles);
}
