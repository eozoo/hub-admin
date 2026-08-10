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
package com.cowave.hub.admin.infra.sys.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.entity.HubAlarm;
import com.cowave.hub.admin.domain.sys.entity.command.AlarmHandles;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmPto;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmTypePto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author shanhuiming
 */
@Mapper
public interface HubAlarmMapper extends BaseMapper<HubAlarm> {

	/**
	 * 类型列表
	 */
    Page<AlarmTypePto> typeList(Page<AlarmTypePto> page, @Param("type") AlarmTypePto alarmTypePto);

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
     * 累计
     */
    int alarmIncrease(AlarmPto alarmPto);

	/**
     * 新增
     */
    void insert(AlarmPto alarmPto);

    /**
     * 列表
     */
    Page<AlarmPto> list(Page<AlarmPto> page, @Param("alarm") AlarmPto alarmPto);

    /**
     * 详情
     */
    AlarmPto info(long id);

    /**
     * 删除
     */
    void delete(Long id);

    /**
     * 处理
     */
    void updateHandle(AlarmHandles alarmHandles);
}
