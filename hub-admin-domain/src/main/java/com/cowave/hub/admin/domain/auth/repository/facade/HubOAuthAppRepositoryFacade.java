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
import com.cowave.hub.admin.domain.auth.entity.HubOAuthApp;
import com.cowave.hub.admin.domain.auth.entity.HubOAuthAppMenu;
import com.cowave.hub.admin.domain.rbac.entity.HubRoleApp;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;

import java.util.List;
import java.util.Set;

/**
 * HubOAuthApp聚合根Query操作
 *
 * @see HubOAuthApp
 * @see HubOAuthAppMenu
 * @see HubRoleApp
 *
 * @author shanhuiming
 */
public interface HubOAuthAppRepositoryFacade {

    /**
     * 分页列表
     */
    Page<HubOAuthApp> queryPage(String tenantId, String clientName);

    /**
     * 按clientId查询
     */
    HubOAuthApp queryByClientId(String clientId);

    /**
     * 租户应用列表
     */
    List<HubOAuthApp> queryListByTenantId(String tenantId);

    /**
     * 按id列表查询
     */
    List<HubOAuthApp> queryListByIds(Set<Integer> appIds);

    /**
     * 应用菜单列表
     */
    List<HubOAuthAppMenu> queryListMenus(Integer appId, String menuName, EnableStatus menuStatus);

    /**
     * 角色应用id列表
     */
    List<Integer> queryRoleAppIdsByRoleId(Integer roleId);

    /**
     * 角色应用对象列表
     */
    List<HubRoleApp> queryRoleAppsByRoleIdList(List<Integer> roleIdList);
}
