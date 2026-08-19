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

import com.cowave.hub.admin.domain.auth.enums.AuthType;
import com.cowave.hub.admin.domain.auth.repository.UserDetailsRepository;
import com.cowave.hub.admin.domain.home.entity.HubMember;
import com.cowave.hub.admin.domain.rbac.entity.SysDept;
import com.cowave.hub.admin.domain.rbac.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac.entity.SysUser;
import com.cowave.hub.admin.domain.rbac.entity.pto.PermitScopePto;
import com.cowave.hub.admin.infra.home.mapper.HubMemberRoleMapper;
import com.cowave.hub.admin.infra.rbac.mapper.SysDeptMapper;
import com.cowave.hub.admin.infra.rbac.mapper.SysMenuMapper;
import com.cowave.hub.admin.infra.rbac.mapper.SysRoleMapper;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.configuration.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.cowave.zoo.framework.access.security.Permission.PERMIT_ADMIN;
import static com.cowave.zoo.framework.access.security.Permission.ROLE_ADMIN;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class UserDetailsDao implements UserDetailsRepository {
    private final ApplicationProperties applicationProperties;
    private final SysDeptMapper sysDeptMapper;
    private final SysMenuMapper sysMenuMapper;
    private final SysRoleMapper sysRoleMapper;
    private final HubMemberRoleMapper hubMemberRoleMapper;

    @Override
    public AccessUserDetails queryUserDetails(SysTenant sysTenant, SysUser sysUser, boolean validAccess) {
        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setAccessValid(validAccess);
        userDetails.setAuthType(AuthType.SYS.getVal());
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
            // 操作权限 + 数据权限
            List<PermitScopePto> permitScopes =
                    sysMenuMapper.listPermitScopesByUserId(sysUser.getTenantId(), sysUser.getUserId());
            userDetails.setPermissions(getPermits(permitScopes));
            userDetails.setPermitScopes(getPermitScopes(permitScopes));
        }
        // 集群信息
        userDetails.setClusterId(applicationProperties.getClusterId());
        userDetails.setClusterLevel(applicationProperties.getClusterLevel());
        userDetails.setClusterName(applicationProperties.getClusterName());
        // 租户首页
        userDetails.setTenantIndex(sysTenant.getViewIndex());
        return userDetails;
    }

    @Override
    public AccessUserDetails queryMemberDetails(SysTenant sysTenant, HubMember hubMember) {
        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setAuthType(AuthType.MEMBER.getVal());
        // 用户信息
        userDetails.setUserType(hubMember.getMemberType().getVal());
        userDetails.setTenantId(hubMember.getTenantId());
        userDetails.setUserId(hubMember.getMemberId());
        userDetails.setUserCode(hubMember.getMemberCode());
        userDetails.setUsername(hubMember.getMemberAccount());
        userDetails.setUserNick(hubMember.getMemberName());
        // 角色信息
        List<String> roleCodes = hubMemberRoleMapper.getRoleCodesByMemberId(hubMember.getMemberId());
        userDetails.setRoles(roleCodes);
        // 操作权限 + 数据权限
        if (roleCodes.contains(ROLE_ADMIN)) {
            userDetails.setPermissions(List.of(PERMIT_ADMIN));
        } else {
            List<PermitScopePto> permitScopes =
                    sysMenuMapper.listPermitScopesByMemberId(hubMember.getTenantId(), hubMember.getMemberId());
            userDetails.setPermissions(getPermits(permitScopes));
            userDetails.setPermitScopes(getPermitScopes(permitScopes));
        }
        // 集群信息
        userDetails.setClusterId(applicationProperties.getClusterId());
        userDetails.setClusterLevel(applicationProperties.getClusterLevel());
        userDetails.setClusterName(applicationProperties.getClusterName());
        // 租户首页
        userDetails.setTenantIndex(sysTenant.getViewIndex());
        return userDetails;
    }

    private List<String> getPermits(List<PermitScopePto> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        Set<String> permits = new HashSet<>();
        for (PermitScopePto pto : list) {
                permits.add(pto.getPermit());
        }
        return permits.stream().toList();
    }

    private Map<String, List<Integer>> getPermitScopes(List<PermitScopePto> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }

        Map<String, List<Integer>> map = new HashMap<>();
        for (PermitScopePto permitScope : list) {
            if (permitScope.getScopeId() == null) {
                continue;
            }
            map.computeIfAbsent(permitScope.getPermit(), k -> new ArrayList<>()).add(permitScope.getScopeId());
        }
        return map;
    }
}
