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
import com.cowave.hub.admin.domain.sys.entity.HubAttach;
import com.cowave.hub.admin.domain.sys.enums.AttachType;
import com.cowave.hub.admin.domain.sys.repository.facade.HubAttachRepositoryFacade;

/**
 * @author shanhuiming
 */
public interface HubAttachRepository extends HubAttachRepositoryFacade, IService<HubAttach> {

    /**
     * 宿主最新附件
     */
    HubAttach queryLatestByOwner(String ownerId, String ownerModule, AttachType attachType);

    /**
     * 更新附件Owner
     */
    void updateOwner(String ownerId, Long attachId);

    /**
     * 清除附件Owner
     */
    void clearOwner(String ownerId, String ownerModule, AttachType attachType, Long attachId);
}
