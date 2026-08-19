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
package com.cowave.hub.admin.domain.home.biz;

import com.cowave.hub.admin.domain.home.entity.command.RoleAppGrant;
import com.cowave.hub.admin.domain.home.entity.HubApp;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubAppBiz {

    /**
     * 新增应用
     */
    HubApp createApp(String tenantId, HubApp oauthApp);

    /**
     * 删除应用（级联删除菜单、角色关联）
     */
    void deleteApp(String tenantId, List<Integer> ids);

    /**
     * 角色授权应用
     */
    void grantRoleApps(RoleAppGrant appGrant);
}
