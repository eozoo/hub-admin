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
import com.cowave.hub.admin.domain.sys.entity.HubDict;
import com.cowave.hub.admin.domain.sys.entity.pto.DictPto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface HubDictMapper extends BaseMapper<HubDict> {

    /**
	 * 列表
	 */
	List<DictPto> queryList(@Param("dictCode") String dictCode, @Param("dictName") String dictName);

	/**
	 * 列表
	 */
	List<DictPto> queryByIds(List<Integer> list);

	/**
	 * 详情
	 */
	DictPto queryById(long id);

	/**
	 * 删除分组字典
	 */
	void removeByGroup(String groupCode);

	/**
	 * 获取分组字典
	 */
	List<HubDict> listByGroup(String groupCode);

	/**
	 * 获取分组类型
	 */
	List<DictPto> listTypeByGroup(String groupCode);
}
