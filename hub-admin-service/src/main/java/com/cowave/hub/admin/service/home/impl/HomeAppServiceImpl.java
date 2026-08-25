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
package com.cowave.hub.admin.service.home.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.home.biz.HubAppBiz;
import com.cowave.hub.admin.domain.home.entity.HubApp;
import com.cowave.hub.admin.domain.home.entity.HubAppMenu;
import com.cowave.hub.admin.domain.home.entity.command.RoleAppGrant;
import com.cowave.hub.admin.domain.home.entity.vo.OAuthAppCard;
import com.cowave.hub.admin.domain.auth.enums.AuthType;
import com.cowave.hub.admin.domain.home.repository.facade.HubAppRepositoryFacade;
import com.cowave.hub.admin.domain.home.repository.facade.HubMemberRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.entity.HubRoleApp;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.service.home.HomeAppService;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
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
public class HomeAppServiceImpl implements HomeAppService {
    private final HubAppBiz hubAppBiz;
    private final HubAppRepositoryFacade hubAppRepositoryFacade;
    private final SysUserRepositoryFacade userRepositoryFacade;
    private final HubMemberRepositoryFacade memberRepositoryFacade;

    @Override
    public List<OAuthAppCard> queryAppNav(String tenantId) {
        AccessUserDetails userDetails = Access.userDetails();
        if (tenantId == null && userDetails != null) {
            tenantId = userDetails.getTenantId();
        }

        List<HubApp> appList;
        if (userDetails != null) {
            // 登录
            List<Integer> roleIdList;
            if (AuthType.MEMBER.getVal().equals(userDetails.getAuthType())) {
                roleIdList = memberRepositoryFacade.queryMemberRoleIdsByMemberId(Access.userId());
            } else {
                roleIdList = userRepositoryFacade.queryUserRoleIdsByUserId(Access.userId());
            }

            if (roleIdList.isEmpty()) {
                appList = hubAppRepositoryFacade.queryPublicNavByTenantId(tenantId);
            } else if (roleIdList.contains(1)) {
                appList = hubAppRepositoryFacade.queryNavByTenantId(tenantId);
            } else {
                List<HubRoleApp> roleAppList = hubAppRepositoryFacade.queryRoleAppsByRoleIdList(roleIdList);
                Set<Integer> appIdSet = Collections.copyToSet(roleAppList, HubRoleApp::getAppId);
                if (appIdSet.isEmpty()) {
                    return new ArrayList<>();
                }
                appList = hubAppRepositoryFacade.queryListByIds(appIdSet);
            }
        } else {
            // 匿名
            appList = hubAppRepositoryFacade.queryPublicNavByTenantId(tenantId);
        }
        return Collections.convertToList(appList, OAuthAppCard.class);
    }

    @Override
    public Page<HubApp> listOauthApp(String tenantId, String clientName) {
        return hubAppRepositoryFacade.queryPage(tenantId, clientName);
    }

    @Override
    public HubApp createOauthApp(String tenantId, HubApp oauthApp) {
        return hubAppBiz.createApp(tenantId, oauthApp);
    }

    @Override
    public void deleteOauthApp(String tenantId, List<Integer> ids) {
        hubAppBiz.deleteApp(tenantId, ids);
    }

    @Override
    public List<OAuthAppCard> queryOauthAppOptions(String tenantId) {
        List<HubApp> appList = hubAppRepositoryFacade.queryListByTenantId(tenantId);
        return Collections.convertToList(appList, OAuthAppCard.class);
    }

    @Override
    public void grantRoleOauthApp(RoleAppGrant appGrant) {
        hubAppBiz.grantRoleApps(appGrant);
    }

    @Override
    public List<Integer> queryRoleOauthApp(Integer roleId) {
        return hubAppRepositoryFacade.queryRoleAppIdsByRoleId(roleId);
    }

    @Override
    public List<HubAppMenu> listAppMenus(Integer appId, String menuName, EnableStatus menuStatus) {
        return hubAppRepositoryFacade.queryMenuList(appId, menuName, menuStatus);
    }
}
