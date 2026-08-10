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
package com.cowave.hub.admin.domain.sys.repository.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.entity.HubAlarm;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmPto;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmTypePto;

/**
 * @author shanhuiming
 */
public interface HubAlarmRepositoryFacade {

    /**
     * 告警类型分页列表
     */
    Page<AlarmTypePto> queryTypePage(Page<AlarmTypePto> page, AlarmTypePto alarmTypePto);

    /**
     * 告警分页列表
     */
    Page<AlarmPto> queryAlarmPage(Page<AlarmPto> page, AlarmPto alarmPto);

    /**
     * 告警详情
     */
    AlarmPto queryAlarmById(long id);

    /**
     * 查询类型下是否存在告警（删除类型前检查）
     */
    HubAlarm queryAlarmExistsByTypeId(Long typeId);
}
