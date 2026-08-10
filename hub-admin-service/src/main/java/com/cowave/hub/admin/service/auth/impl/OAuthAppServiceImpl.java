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
package com.cowave.hub.admin.service.auth.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.auth.entity.command.RoleAppGrant;
import com.cowave.hub.admin.domain.auth.biz.HubOAuthAppBiz;
import com.cowave.hub.admin.domain.auth.entity.HubOAuthApp;
import com.cowave.hub.admin.domain.auth.entity.HubOAuthAppMenu;
import com.cowave.hub.admin.domain.rbac.entity.HubRoleApp;
import com.cowave.hub.admin.domain.auth.entity.vo.OAuthAppCard;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.domain.auth.repository.facade.HubOAuthAppRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubUserRepositoryFacade;
import com.cowave.hub.admin.service.auth.OAuthAppService;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.tools.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
public class OAuthAppServiceImpl implements OAuthAppService {
    private final HubOAuthAppBiz oauthAppBiz;
    private final HubOAuthAppRepositoryFacade oauthAppRepositoryFacade;
    private final HubUserRepositoryFacade userRepositoryFacade;

    @Override
    public Page<HubOAuthApp> listOauthApp(String tenantId, String clientName) {
        return oauthAppRepositoryFacade.queryPage(tenantId, clientName);
    }

    @Override
    public HubOAuthApp createOauthApp(String tenantId, HubOAuthApp oauthApp) {
        return oauthAppBiz.createApp(tenantId, oauthApp);
    }

    @Override
    public void deleteOauthApp(String tenantId, List<Integer> ids) {
        oauthAppBiz.deleteApp(tenantId, ids);
    }

    @Override
    public List<OAuthAppCard> queryOauthAppOptions(String tenantId) {
        List<HubOAuthApp> appList = oauthAppRepositoryFacade.queryListByTenantId(tenantId);
        return Collections.convertToList(appList, OAuthAppCard.class);
    }

    @Override
    public List<OAuthAppCard> queryOauthAppCards() {
        List<Integer> roleIdList = userRepositoryFacade.queryUserRoleIdsByUserId(Access.userId());
        if (roleIdList.isEmpty()) {
            return new ArrayList<>();
        }

        List<HubOAuthApp> appList;
        if (roleIdList.contains(1)) {
            appList = oauthAppRepositoryFacade.queryListByTenantId(Access.tenantId());
        } else {
            List<HubRoleApp> roleAppList = oauthAppRepositoryFacade.queryRoleAppsByRoleIdList(roleIdList);
            Set<Integer> appIdSet = Collections.copyToSet(roleAppList, HubRoleApp::getAppId);
            if (appIdSet.isEmpty()) {
                return new ArrayList<>();
            }
            appList = oauthAppRepositoryFacade.queryListByIds(appIdSet);
        }
        return Collections.convertToList(appList, OAuthAppCard.class);
    }

    @Override
    public void grantRoleOauthApp(RoleAppGrant appGrant) {
        oauthAppBiz.grantRoleApps(appGrant);
    }

    @Override
    public List<Integer> queryRoleOauthApp(Integer roleId) {
        return oauthAppRepositoryFacade.queryRoleAppIdsByRoleId(roleId);
    }

    @Override
    public List<HubOAuthAppMenu> listAppMenus(Integer appId, String menuName, EnableStatus menuStatus) {
        return oauthAppRepositoryFacade.queryListMenus(appId, menuName, menuStatus);
    }
}
