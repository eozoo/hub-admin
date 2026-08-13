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
package com.cowave.hub.admin.service.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.entity.command.AlarmHandles;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmPto;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmTypePto;

/**
 * @author shanhuiming
 */
public interface SysAlarmService {

	/**
	 * 新增
	 */
	void add(AlarmPto alarmPto);

	/**
	 * 类型列表
	 */
	Page<AlarmTypePto> typeList(AlarmTypePto alarmTypePto);

	/**
	 * 类型新增
	 */
	void typeAdd(AlarmTypePto alarmTypePto);

	/**
	 * 类型修改
	 */
	void typeEdit(AlarmTypePto alarmTypePto);

	/**
	 * 类型删除
	 */
	void typeDelete(Long id);

	/**
	 * 列表
	 */
	Page<AlarmPto> list(AlarmPto alarmPto);

	/**
	 * 详情
	 */
	AlarmPto info(Long id);

	/**
	 * 删除
	 */
	void delete(Long id);

	/**
	 * 处理
	 */
	void handle(AlarmHandles alarmHandles);
}
