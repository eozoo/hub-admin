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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.entity.HubAttach;
import com.cowave.hub.admin.domain.sys.enums.AttachType;
import com.cowave.hub.admin.domain.sys.entity.query.AttachQuery;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubAttachRepositoryFacade {

    /**
     * 分页查询
     */
    Page<HubAttach> queryPage(AttachQuery query);

    /**
     * 列表查询(id)
     */
    List<HubAttach> queryByIds(List<Long> attachIds);

    /**
     * 详情
     */
    HubAttach queryById(Long attachId);

    /**
     * 宿主附件列表
     */
    List<HubAttach> listByOwner(String ownerId, String ownerModule, AttachType attachType);
}
