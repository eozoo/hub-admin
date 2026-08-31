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
import com.cowave.hub.admin.domain.sys.entity.SysDict;
import com.cowave.hub.admin.domain.sys.entity.SysDictType;
import com.cowave.hub.admin.domain.sys.entity.command.DictCreate;
import com.cowave.hub.admin.domain.sys.entity.command.DictTypeCreate;
import com.cowave.hub.admin.domain.sys.entity.pto.DictPto;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysDictService {

	/**
	 * 获取类型字典
	 */
	List<SysDict> queryListByType(String tenantId, String typeCode);

	/**
	 * 类型列表（分页）
	 */
	Page<SysDictType> queryTypePageByModule(String tenantId, String moduleCode, int pageNum, int pageSize);

	/**
	 * 新增类型
	 */
	void addType(String tenantId, DictTypeCreate typeCreate);

	/**
	 * 修改类型
	 */
	void editType(String tenantId, DictTypeCreate typeCreate);

	/**
	 * 删除类型
	 */
	void deleteType(String tenantId, List<Integer> typeIds);

	/**
	 * 修改类型状态
	 */
	void updateTypeStatus(String tenantId, Integer typeId, Integer status);

	/**
	 * 获取字典
	 */
	SysDict queryByCode(String tenantId, String dictCode);

	/**
	 * 字典列表
	 */
	List<DictPto> queryList(String tenantId, String typeCode, String moduleCode);

	/**
	 * 字典详情
	 */
	DictPto info(String tenantId, Long id);

	/**
	 * 新增字典
	 */
	void add(String tenantId, DictCreate dictCreate);

	/**
	 * 修改字典
	 */
	void edit(String tenantId, DictCreate dictCreate);

	/**
	 * 删除字典
	 */
	void delete(String tenantId, List<Long> dictIds);

	/**
	 * 修改字典状态
	 */
	void updateDictStatus(String tenantId, Long dictId, Integer status);
}
