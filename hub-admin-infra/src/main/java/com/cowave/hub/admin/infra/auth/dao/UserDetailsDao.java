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
import com.cowave.hub.admin.domain.rbac.entity.SysDept;
import com.cowave.hub.admin.domain.rbac.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac.entity.SysUser;
import com.cowave.hub.admin.domain.rbac.enums.UserType;
import com.cowave.hub.admin.infra.rbac.mapper.SysDeptMapper;
import com.cowave.hub.admin.infra.rbac.mapper.SysMenuMapper;
import com.cowave.hub.admin.infra.rbac.mapper.SysRoleMapper;
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
    private final SysDeptMapper sysDeptMapper;
    private final SysMenuMapper sysMenuMapper;
    private final SysRoleMapper sysRoleMapper;

    @Override
    public AccessUserDetails queryUserDetails(UserType userType, SysTenant sysTenant, SysUser sysUser, boolean validAccess) {
        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setAccessValid(validAccess);
        userDetails.setAuthType(userType.getVal());
        // 用户信息
        userDetails.setUserType(sysUser.getUserType().getVal());
        userDetails.setTenantId(sysUser.getTenantId());
        userDetails.setUserId(sysUser.getUserId());
        userDetails.setUserCode(sysUser.getUserCode());
        userDetails.setUsername(sysUser.getUserAccount());
        userDetails.setUserNick(sysUser.getUserName());
        userDetails.setUserPasswd(sysUser.getUserPasswd());
        // 部门信息
        SysDept userDept = sysDeptMapper.getPrimaryDeptByUserId(sysUser.getUserId());
        if (userDept != null) {
            userDetails.setDeptId(userDept.getDeptId());
            userDetails.setDeptCode(userDept.getDeptCode());
            userDetails.setDeptName(userDept.getDeptName());
        }
        // 角色信息
        List<String> roleCodes = sysRoleMapper.getRoleCodesByUserId(sysUser.getUserId());
        userDetails.setRoles(roleCodes);
        // 权限信息
        if(roleCodes.contains(ROLE_ADMIN)){
            userDetails.setPermissions(List.of(PERMIT_ADMIN));
        }else{
            List<String> permitCodes = sysMenuMapper.listPermitsByUserId(sysUser.getTenantId(), sysUser.getUserId());
            userDetails.setPermissions(permitCodes);
        }
        // 集群信息
        userDetails.setClusterId(applicationProperties.getClusterId());
        userDetails.setClusterLevel(applicationProperties.getClusterLevel());
        userDetails.setClusterName(applicationProperties.getClusterName());
        // 租户首页
        userDetails.setTenantIndex(sysTenant.getViewIndex());
        return userDetails;
    }
}
