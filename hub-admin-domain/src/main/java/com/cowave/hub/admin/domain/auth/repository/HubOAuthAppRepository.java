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
import com.cowave.hub.admin.domain.auth.entity.HubOAuthApp;
import com.cowave.hub.admin.domain.rbac.entity.HubRoleApp;
import com.cowave.hub.admin.domain.auth.repository.facade.HubOAuthAppRepositoryFacade;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubOAuthAppRepository extends HubOAuthAppRepositoryFacade, IService<HubOAuthApp> {

    /**
     * 删除应用
     */
    void removeByIds(String tenantId, List<Integer> ids);

    /**
     * 删除应用菜单
     */
    void removeMenusByAppIds(List<Integer> appIds);

    /**
     * 删除角色应用（按角色）
     */
    void removeRoleAppsByRoleId(Integer roleId);

    /**
     * 删除角色应用（按应用）
     */
    void removeRoleAppsByAppIds(List<Integer> appIds);

    /**
     * 删除角色应用菜单
     */
    void removeRoleAppMenusByAppIds(List<Integer> appIds);

    /**
     * 批量保存角色应用
     */
    void saveRoleAppsBatch(List<HubRoleApp> list);
}
