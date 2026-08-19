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

import com.cowave.hub.admin.domain.home.entity.HubMember;
import com.cowave.hub.admin.domain.rbac.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac.entity.SysUser;
import com.cowave.zoo.framework.access.security.AccessUserDetails;

/**
 * @author shanhuiming
 */
public interface UserDetailsRepositoryFacade {

    /**
     * 查询用户认证详情（含部门、角色、权限）
     */
    AccessUserDetails queryUserDetails(SysTenant sysTenant, SysUser sysUser, boolean validAccess);

    /**
     * 查询会员认证详情（含角色、权限）
     */
    AccessUserDetails queryMemberDetails(SysTenant sysTenant, HubMember hubMember);
}

