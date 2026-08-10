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

import com.cowave.hub.admin.domain.sys.entity.HubDict;
import com.cowave.hub.admin.domain.sys.entity.command.DictCreate;
import com.cowave.hub.admin.domain.sys.entity.pto.DictPto;
import com.cowave.hub.admin.domain.sys.entity.query.DictQuery;
import com.cowave.hub.admin.domain.sys.entity.vo.SelectOptionVo;

import java.util.Collection;
import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubDictService {

	/**
	 * 列表
	 */
	List<DictPto> queryList(DictQuery query);

	/**
	 * 详情
	 */
	DictPto info(Long dictId);

	/**
	 * 新增
	 */
	void add(DictCreate dictCreate);

	/**
	 * 删除
	 */
	void delete(List<Integer> dictIds);

	/**
	 * 修改
	 */
	void edit(DictCreate dictCreate);

	/**
	 * 获取字典
	 */
	HubDict queryByCode(String dictCode);

	/**
	 * 获取类型字典
	 */
	List<HubDict> queryListByType(String typeCode);

	/**
	 * 获取分组字典
	 */
	List<HubDict> queryListByGroup(String groupCode);

	/**
	 * 获取分组选项
	 */
	Collection<SelectOptionVo> queryListTypeByGroup(String groupCode);
}
