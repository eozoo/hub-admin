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
package com.cowave.hub.admin.domain.auth.biz;

import com.cowave.hub.admin.domain.auth.entity.HubToken;
import com.cowave.hub.admin.domain.auth.entity.HubTokenMenu;

import java.util.List;

/**
 * HubToken聚合根Command操作
 *
 * @author shanhuiming
 */
public interface HubTokenBiz {

    /**
     * 创建API令牌
     */
    void saveToken(HubToken token);

    /**
     * 更新令牌
     */
    void updateToken(HubToken token);

    /**
     * 保存令牌菜单
     */
    void saveTokenMenus(List<HubTokenMenu> list);

    /**
     * 删除API令牌
     */
    void deleteApiToken(Integer tokenId);
}
