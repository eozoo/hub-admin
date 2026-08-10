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
package com.cowave.hub.admin.infra.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rbac.entity.HubUserRole;
import com.cowave.hub.admin.domain.rbac.entity.command.RoleUserUpdate;
import com.cowave.hub.admin.domain.rbac.entity.pto.RoleUserPto;
import com.cowave.hub.admin.domain.rbac.entity.query.RoleUserQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author shanhuiming
 */
@Mapper
public interface HubUserRoleMapper extends BaseMapper<HubUserRole> {

    /**
     * 授权用户
     */
    void addRoleUser(@Param("tenantId") String tenantId, @Param("role") RoleUserUpdate roleUpdate);

    /**
     * 用户列表（已授权）
     */
    Page<RoleUserPto> getAuthedUser(@Param("tenantId") String tenantId, @Param("query") RoleUserQuery query, Page<RoleUserPto> page);

    /**
     * 用户列表（未授权）
     */
    Page<RoleUserPto> getUnAuthedUser(@Param("tenantId") String tenantId, @Param("query") RoleUserQuery query, Page<RoleUserPto> page);

}
