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

import com.cowave.hub.admin.domain.sys.entity.HubDict;
import com.cowave.hub.admin.domain.sys.entity.pto.DictPto;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubDictRepositoryFacade {

    /**
     * 字典列表
     */
    List<DictPto> queryList(String dictCode, String dictName);

    /**
     * 批量查询
     */
    List<DictPto> queryByIds(List<Integer> list);

    /**
     * 详情
     */
    DictPto queryById(long id);

    /**
     * 按编码查询
     */
    HubDict queryByCode(String dictCode);

    /**
     * 按类型查询
     */
    List<HubDict> listByType(String typeCode);

    /**
     * 按分组查询
     */
    List<HubDict> listByGroup(String groupCode);

    /**
     * 查询分组类型
     */
    List<DictPto> listTypeByGroup(String groupCode);
}
