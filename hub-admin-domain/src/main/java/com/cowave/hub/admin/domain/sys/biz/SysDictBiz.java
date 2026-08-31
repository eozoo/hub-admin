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

import com.cowave.hub.admin.domain.sys.entity.command.DictCreate;
import com.cowave.hub.admin.domain.sys.entity.command.DictTypeCreate;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysDictBiz {

    /**
     * 新增类型
     */
    void saveType(String tenantId, DictTypeCreate typeCreate);

    /**
     * 修改类型
     */
    void editType(String tenantId, DictTypeCreate typeCreate);

    /**
     * 删除类型（级联删除类型下字典）
     */
    void deleteTypes(String tenantId, List<Integer> typeIds);

    /**
     * 修改类型状态
     */
    void updateTypeStatus(String tenantId, Integer typeId, Integer status);

    /**
     * 新增字典
     */
    void saveDict(String tenantId, DictCreate dictCreate);

    /**
     * 修改字典
     */
    void editDict(String tenantId, DictCreate dictCreate);

    /**
     * 删除字典
     */
    void deleteDicts(String tenantId, List<Long> dictIds);

    /**
     * 修改字典状态
     */
    void updateDictStatus(String tenantId, Long dictId, Integer status);
}
