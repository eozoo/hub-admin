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
package com.cowave.hub.admin.domain.home.biz.impl;

import com.cowave.hub.admin.domain.home.entity.command.RoleAppGrant;
import com.cowave.hub.admin.domain.home.biz.HubAppBiz;
import com.cowave.hub.admin.domain.home.entity.HubApp;
import com.cowave.hub.admin.domain.rbac.entity.HubRoleApp;
import com.cowave.hub.admin.domain.home.repository.HubAppRepository;
import com.cowave.zoo.tools.Collections;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubAppBizImpl implements HubAppBiz {

    private final HubAppRepository hubAppRepository;

    @Override
    public HubApp createApp(String tenantId, HubApp oauthApp) {
        oauthApp.setTenantId(tenantId);
        oauthApp.setClientId(UUID.randomUUID().toString().replace("-", ""));
        oauthApp.setClientSecret(UUID.randomUUID().toString().replace("-", ""));
        hubAppRepository.save(oauthApp);
        return oauthApp;
    }

    @Override
    public void deleteApp(String tenantId, List<Integer> ids) {
        hubAppRepository.removeByIds(tenantId, ids);
        hubAppRepository.removeMenusByAppIds(ids);
        hubAppRepository.removeRoleAppsByAppIds(ids);
        hubAppRepository.removeRoleAppMenusByAppIds(ids);
    }

    @Override
    public void grantRoleApps(RoleAppGrant appGrant) {
        hubAppRepository.removeRoleAppsByRoleId(appGrant.getRoleId());
        List<Integer> appIdList = appGrant.getAppIdList();
        if (CollectionUtils.isNotEmpty(appIdList)) {
            List<HubRoleApp> roleAppList = Collections.copyToList(appIdList,
                    v -> new HubRoleApp(appGrant.getRoleId(), v));
            hubAppRepository.saveRoleAppsBatch(roleAppList);
        }
    }
}
