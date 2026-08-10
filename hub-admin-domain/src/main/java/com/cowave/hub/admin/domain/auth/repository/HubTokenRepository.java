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
package com.cowave.hub.admin.domain.auth.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cowave.hub.admin.domain.auth.entity.HubToken;
import com.cowave.hub.admin.domain.auth.entity.HubTokenMenu;
import com.cowave.hub.admin.domain.auth.repository.facade.HubTokenRepositoryFacade;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubTokenRepository extends HubTokenRepositoryFacade, IService<HubToken> {

    /**
     * 批量保存令牌菜单
     */
    void saveTokenMenusBatch(List<HubTokenMenu> list);

    /**
     * 删除令牌菜单
     */
    void removeTokenMenusByTokenId(Integer tokenId);
}
