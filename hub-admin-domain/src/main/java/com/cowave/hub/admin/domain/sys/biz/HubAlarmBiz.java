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
package com.cowave.hub.admin.domain.sys.biz;

import com.cowave.hub.admin.domain.sys.entity.HubAlarm;
import com.cowave.hub.admin.domain.sys.entity.command.AlarmHandles;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmPto;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmTypePto;

/**
 * HubAlarm聚合根Command操作
 *
 * @see HubAlarm
 *
 * @author shanhuiming
 */
public interface HubAlarmBiz {

    /**
     * 新增告警类型
     */
    void createAlarmType(AlarmTypePto alarmTypePto);

    /**
     * 修改告警类型
     */
    void updateAlarmType(AlarmTypePto alarmTypePto);

    /**
     * 删除告警类型
     */
    void deleteAlarmType(Long id);

    /**
     * 记录告警（存在则累加，不存在则新增）
     */
    void recordAlarm(AlarmPto alarmPto);

    /**
     * 处理告警
     */
    void handleAlarm(AlarmHandles alarmHandles);

    /**
     * 删除告警
     */
    void deleteAlarm(Long id);
}
