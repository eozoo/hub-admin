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
package com.cowave.hub.admin.service.auth;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.auth.entity.HubApp;
import com.cowave.hub.admin.domain.auth.entity.command.RoleAppGrant;
import com.cowave.hub.admin.domain.auth.entity.HubAppMenu;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.domain.auth.entity.vo.OAuthAppCard;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface OAuthAppService {

    /**
     * 授权应用列表
     */
    Page<HubApp> listOauthApp(String tenantId, String clientName);

    /**
     * 新增授权应用
     */
    HubApp createOauthApp(String tenantId, HubApp oauthApp);

    /**
     * 删除授权应用
     */
    void deleteOauthApp(String tenantId, List<Integer> ids);

    /**
     * 获取授权应用选项
     */
    List<OAuthAppCard> queryOauthAppOptions(String tenantId);

    /**
     * 获取人员授权应用
     */
    List<OAuthAppCard> queryOauthAppCards();

    /**
     * 给角色授权应用
     */
    void grantRoleOauthApp(RoleAppGrant appGrant);

    /**
     * 获取角色授权应用
     */
    List<Integer> queryRoleOauthApp(Integer roleId);

    /**
     * 获取授权应用的菜单列表
     */
    List<HubAppMenu> listAppMenus(Integer appId, String menuName, EnableStatus menuStatus);
}
