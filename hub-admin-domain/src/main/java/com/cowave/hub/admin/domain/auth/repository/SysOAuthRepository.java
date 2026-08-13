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
import com.cowave.hub.admin.domain.auth.entity.SysOAuth;
import com.cowave.hub.admin.domain.auth.entity.SysOAuthUser;
import com.cowave.hub.admin.domain.auth.repository.facade.SysOAuthRepositoryFacade;

/**
 * @author shanhuiming
 */
public interface SysOAuthRepository extends SysOAuthRepositoryFacade, IService<SysOAuth> {

    /**
     * 删除OAuth配置
     */
    void removeByServerType(String tenantId, String serverType);

    /**
     * 新增OAuth用户
     */
    void saveOauthUser(SysOAuthUser oauthUser);

    /**
     * 更新OAuth用户
     */
    void updateOauthUserById(SysOAuthUser oauthUser);
}
