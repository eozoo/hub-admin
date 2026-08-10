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
import com.cowave.hub.admin.domain.sys.entity.HubDict;
import com.cowave.hub.admin.domain.sys.entity.command.DictCreate;
import com.cowave.hub.admin.domain.sys.entity.pto.DictPto;
import com.cowave.hub.admin.domain.sys.repository.facade.HubDictRepositoryFacade;

/**
 * @author shanhuiming
 */
public interface HubDictRepository extends HubDictRepositoryFacade, IService<HubDict> {

    /**
     * 删除分组字典
     */
    void removeByGroup(String groupCode);

    /**
     * 删除类型字典
     */
    void removeByType(String parentCode);

    /**
     * 更新字典
     */
    void updateDict(DictCreate dictCreate);

    /**
     * 更新上级字典码
     */
    void updateParentCode(String newParent, String oldParent);
}
