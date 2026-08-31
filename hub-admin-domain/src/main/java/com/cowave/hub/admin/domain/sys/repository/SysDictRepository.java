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
import com.cowave.hub.admin.domain.sys.entity.SysDict;
import com.cowave.hub.admin.domain.sys.entity.SysDictType;
import com.cowave.hub.admin.domain.sys.repository.facade.SysDictRepositoryFacade;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysDictRepository extends SysDictRepositoryFacade, IService<SysDict> {

    /**
     * 新增类型
     */
    void saveType(SysDictType type);

    /**
     * 修改类型
     */
    void updateType(SysDictType type);

    /**
     * 修改类型状态
     */
    void updateTypeStatus(Integer typeId, Integer status);

    /**
     * 删除类型
     */
    void deleteTypes(List<Integer> typeIds);

    /**
     * 按类型批量查询
     */
    List<SysDictType> listTypesByIds(List<Integer> typeIds);

    /**
     * 按类型删除字典项
     */
    void removeDictsByType(String tenantId, String typeCode);

    /**
     * 修改字典状态
     */
    void updateDictStatus(Long dictId, Integer status);
}
