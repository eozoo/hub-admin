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
package com.cowave.hub.admin.domain.rbac.repository.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rbac.entity.HubRole;
import com.cowave.hub.admin.domain.rbac.entity.HubRoleMenu;
import com.cowave.hub.admin.domain.rbac.entity.HubUserRole;
import com.cowave.hub.admin.domain.rbac.entity.pto.RoleInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.RoleUserPto;
import com.cowave.hub.admin.domain.rbac.entity.query.RoleQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.RoleUserQuery;

import java.util.List;

/**
 * HubRole聚合根Query操作
 *
 * @see HubRole
 * @see HubRoleMenu
 * @see HubUserRole
 *
 * @author shanhuiming
 */
public interface HubRoleRepositoryFacade {

    /**
     * 分页列表
     */
    Page<HubRole> queryPage(String tenantId, RoleQuery query);

    /**
     * 详情（含菜单权限）
     */
    RoleInfoPto queryInfo(String tenantId, Integer roleId);

    /**
     * 角色查询（角色id）
     */
    HubRole queryById(String tenantId, Integer roleId);

    /**
     * 角色查询（角色编码，含#全局角色）
     */
    HubRole queryByCode(String tenantId, String roleCode);

    /**
     * 列表查询（角色id）
     */
    List<HubRole> queryListByIds(String tenantId, List<Integer> roleIds);

    /**
     * 角色名称查询
     */
    List<String> queryNameByIds(String tenantId, List<Integer> roleIds);

    /**
     * 角色编码冲突检测
     */
    long countRoleCode(String tenantId, String roleCode, Integer roleId);

    /**
     * 角色已授权用户
     */
    Page<RoleUserPto> queryAuthedUser(String tenantId, RoleUserQuery query);

    /**
     * 角色未授权用户
     */
    Page<RoleUserPto> queryUnAuthedUser(String tenantId, RoleUserQuery query);
}
