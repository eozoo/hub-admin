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
package com.cowave.hub.admin.domain.auth.biz.impl;

import com.cowave.hub.admin.domain.auth.biz.SysOAuthBiz;
import com.cowave.hub.admin.domain.auth.entity.SysOAuth;
import com.cowave.hub.admin.domain.auth.entity.SysOAuthUser;
import com.cowave.hub.admin.domain.auth.repository.SysOAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysOAuthBizImpl implements SysOAuthBiz {

    private final SysOAuthRepository oauthRepository;

    @Override
    public void editOauth(SysOAuth oauth) {
        oauthRepository.removeByServerType(oauth.getTenantId(), oauth.getServerType());
        oauthRepository.save(oauth);
    }

    @Override
    public void saveOauthUser(SysOAuthUser oauthUser) {
        oauthRepository.saveOauthUser(oauthUser);
    }

    @Override
    public void updateOauthUserById(SysOAuthUser oauthUser) {
        oauthRepository.updateOauthUserById(oauthUser);
    }
}
