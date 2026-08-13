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
package com.cowave.hub.admin.infra.sys.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.sys.entity.SysAlarm;
import com.cowave.hub.admin.domain.sys.entity.command.AlarmHandles;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmPto;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmTypePto;
import com.cowave.hub.admin.domain.sys.repository.SysAlarmRepository;
import com.cowave.hub.admin.infra.sys.mapper.SysAlarmMapper;
import org.springframework.stereotype.Repository;

/**
 * @author shanhuiming
 */
@Repository
public class SysAlarmDao extends ServiceImpl<SysAlarmMapper, SysAlarm> implements SysAlarmRepository {

    @Override
    public Page<AlarmTypePto> queryTypePage(Page<AlarmTypePto> page, AlarmTypePto alarmTypePto) {
        return baseMapper.typeList(page, alarmTypePto);
    }

    @Override
    public Page<AlarmPto> queryAlarmPage(Page<AlarmPto> page, AlarmPto alarmPto) {
        return baseMapper.list(page, alarmPto);
    }

    @Override
    public AlarmPto queryAlarmById(long id) {
        return baseMapper.info(id);
    }

    @Override
    public SysAlarm queryAlarmExistsByTypeId(Long typeId) {
        return baseMapper.selectOne(new LambdaQueryWrapper<SysAlarm>()
                .eq(SysAlarm::getAlarmType, typeId).last("limit 1"));
    }

    @Override
    public void insertType(AlarmTypePto alarmTypePto) {
        baseMapper.insertType(alarmTypePto);
    }

    @Override
    public void updateType(AlarmTypePto alarmTypePto) {
        baseMapper.updateType(alarmTypePto);
    }

    @Override
    public void deleteType(Long id) {
        baseMapper.deleteType(id);
    }

    @Override
    public int alarmIncrease(AlarmPto alarmPto) {
        return baseMapper.alarmIncrease(alarmPto);
    }

    @Override
    public void insertAlarm(AlarmPto alarmPto) {
        baseMapper.insert(alarmPto);
    }

    @Override
    public void deleteAlarm(Long id) {
        baseMapper.delete(id);
    }

    @Override
    public void updateHandle(AlarmHandles alarmHandles) {
        baseMapper.updateHandle(alarmHandles);
    }
}
