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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.auth.entity.HubOAuth;
import com.cowave.hub.admin.domain.auth.entity.HubOAuthUser;

/**
 * HubOAuth聚合根Query操作
 *
 * @see HubOAuth
 * @see HubOAuthUser
 *
 * @author shanhuiming
 */
public interface HubOAuthRepositoryFacade {

    /**
     * 按服务类型查询OAuth配置
     */
    HubOAuth queryByServerType(String tenantId, String serverType);

    /**
     * OAuth用户分页列表
     */
    Page<HubOAuthUser> queryUserPage(String tenantId, String serverType, String userAccount);

    /**
     * 按账号查询OAuth用户
     */
    HubOAuthUser queryUserByAccount(String tenantId, String serverType, String userAccount);
}
