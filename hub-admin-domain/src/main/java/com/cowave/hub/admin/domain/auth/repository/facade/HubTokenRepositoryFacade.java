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
package com.cowave.hub.admin.domain.auth.repository.facade;

import com.cowave.hub.admin.domain.auth.entity.HubToken;

import java.util.List;

/**
 * HubToken聚合根Query操作
 *
 * @author shanhuiming
 */
public interface HubTokenRepositoryFacade {

    /**
     * 全部令牌列表
     */
    List<HubToken> queryList();

    /**
     * 按用户编码查询令牌列表
     */
    List<HubToken> queryListByUserCode(String userCode);

    /**
     * 查询令牌关联的权限符列表
     */
    List<String> queryPermitsByTokenId(Integer tokenId);
}
