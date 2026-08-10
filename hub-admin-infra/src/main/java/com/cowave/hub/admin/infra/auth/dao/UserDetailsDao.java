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
package com.cowave.hub.admin.infra.auth.dao;

import com.cowave.hub.admin.domain.auth.repository.facade.UserDetailsRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.entity.HubDept;
import com.cowave.hub.admin.domain.rbac.entity.HubTenant;
import com.cowave.hub.admin.domain.rbac.entity.HubUser;
import com.cowave.hub.admin.domain.rbac.enums.UserType;
import com.cowave.hub.admin.infra.rbac.mapper.HubDeptMapper;
import com.cowave.hub.admin.infra.rbac.mapper.HubMenuMapper;
import com.cowave.hub.admin.infra.rbac.mapper.HubRoleMapper;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.configuration.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cowave.zoo.framework.access.security.Permission.PERMIT_ADMIN;
import static com.cowave.zoo.framework.access.security.Permission.ROLE_ADMIN;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class UserDetailsDao implements UserDetailsRepositoryFacade {
    private final ApplicationProperties applicationProperties;
    private final HubDeptMapper hubDeptMapper;
    private final HubMenuMapper hubMenuMapper;
    private final HubRoleMapper hubRoleMapper;

    @Override
    public AccessUserDetails queryUserDetails(UserType userType, HubTenant hubTenant, HubUser hubUser, boolean validAccess) {
        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setAccessValid(validAccess);
        userDetails.setAuthType(userType.getVal());
        // 用户信息
        userDetails.setUserType(hubUser.getUserType().getVal());
        userDetails.setTenantId(hubUser.getTenantId());
        userDetails.setUserId(hubUser.getUserId());
        userDetails.setUserCode(hubUser.getUserCode());
        userDetails.setUsername(hubUser.getUserAccount());
        userDetails.setUserNick(hubUser.getUserName());
        userDetails.setUserPasswd(hubUser.getUserPasswd());
        // 部门信息
        HubDept userDept = hubDeptMapper.getPrimaryDeptByUserId(hubUser.getUserId());
        if (userDept != null) {
            userDetails.setDeptId(userDept.getDeptId());
            userDetails.setDeptCode(userDept.getDeptCode());
            userDetails.setDeptName(userDept.getDeptName());
        }
        // 角色信息
        List<String> roleCodes = hubRoleMapper.getRoleCodesByUserId(hubUser.getUserId());
        userDetails.setRoles(roleCodes);
        // 权限信息
        if(roleCodes.contains(ROLE_ADMIN)){
            userDetails.setPermissions(List.of(PERMIT_ADMIN));
        }else{
            List<String> permitCodes = hubMenuMapper.listPermitsByUserId(hubUser.getTenantId(), hubUser.getUserId());
            userDetails.setPermissions(permitCodes);
        }
        // 集群信息
        userDetails.setClusterId(applicationProperties.getClusterId());
        userDetails.setClusterLevel(applicationProperties.getClusterLevel());
        userDetails.setClusterName(applicationProperties.getClusterName());
        // 租户首页
        userDetails.setTenantIndex(hubTenant.getViewIndex());
        return userDetails;
    }
}
