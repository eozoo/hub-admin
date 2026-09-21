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
package com.cowave.hub.admin.service.auth.support;

import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.access.security.TenantUserDetailsService;
import com.cowave.hub.admin.domain.auth.entity.SysAuthPasswd;
import com.cowave.hub.admin.domain.auth.enums.LoginSource;
import com.cowave.hub.admin.domain.auth.repository.facade.SysAuthRepositoryFacade;
import com.cowave.hub.admin.domain.rbac2.entity.SysUser;
import com.cowave.hub.admin.domain.rbac2.entity.pto.PermitScopePto;
import com.cowave.hub.admin.domain.rbac2.entity.pto.TenantAccessPto;
import com.cowave.hub.admin.domain.rbac2.repository.facade.SysTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac2.repository.facade.SysUserRepositoryFacade;
import com.cowave.zoo.framework.configuration.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;

import static com.cowave.zoo.framework.access.security.Permission.PERMIT_ADMIN;
import static com.cowave.zoo.framework.access.security.Permission.ROLE_ADMIN;

import static com.cowave.hub.admin.domain.auth.enums.AuthType.SYS;
import static com.cowave.hub.admin.domain.rbac2.enums.EnableStatus.ENABLE;
import static com.cowave.zoo.http.client.constants.HttpCode.FORBIDDEN;
import static com.cowave.zoo.http.client.constants.HttpCode.UNAUTHORIZED;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class SysUserDetailsServiceImpl implements TenantUserDetailsService {
    private final MfaConfiguration mfaConfiguration;
    private final SysUserRepositoryFacade userRepositoryFacade;
    private final SysAuthRepositoryFacade authRepositoryFacade;
    private final SysTenantRepositoryFacade tenantAccessRepositoryFacade;
    private final ApplicationProperties applicationProperties;

    @Override
    public UserDetails loadTenantUserByUsername(String tenantId, String userAccount) {
        // 用户信息
        SysUser sysUser = userRepositoryFacade.queryByAccount(userAccount);
        if (sysUser == null) {
            return null;
        }
        HttpAsserts.equals(ENABLE, sysUser.getUserStatus(), FORBIDDEN, "{admin.user.account.disable}", userAccount);
        // 密码
        SysAuthPasswd authPasswd = authRepositoryFacade.queryCurrentPasswd(sysUser.getUserId());
        if (authPasswd == null) {
            return null;
        }
        // MFA
        String mfaKey = sysUser.getMfa();
        AccessUserDetails userDetails = buildUserDetails(sysUser, authPasswd);
        if (StringUtils.isNotBlank(mfaKey)) {
            userDetails.setAccessValid(false);
            userDetails.setMfaRequired(true);
            userDetails.setAccessToken(mfaConfiguration.buildMfaToken(userAccount, null));
        }
        return userDetails;
    }

    public AccessUserDetails loadMfaUser(String userAccount, String mfaCode) {
        SysUser sysUser = userRepositoryFacade.queryByAccount(userAccount);
        HttpAsserts.notNull(sysUser, FORBIDDEN, "{admin.mfa.code.invalid}");
        HttpAsserts.equals(ENABLE, sysUser.getUserStatus(), FORBIDDEN, "{admin.user.account.disable}", userAccount);
        HttpAsserts.isTrue(StringUtils.isNotBlank(sysUser.getMfa()), FORBIDDEN, "{admin.mfa.code.invalid}");
        HttpAsserts.isTrue(MfaAuthVerifier.validateCode(sysUser.getMfa(), mfaCode), FORBIDDEN, "{admin.mfa.code.invalid}");
        return buildUserDetails(sysUser, null);
    }

    private AccessUserDetails buildUserDetails(SysUser sysUser, SysAuthPasswd authPasswd) {
        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setAccessValid(true);
        userDetails.setAuthType(SYS.getVal());
        userDetails.setUserId(sysUser.getUserId());
        userDetails.setUsername(sysUser.getUserAccount());
        userDetails.setUserNick(sysUser.getUserName());
        userDetails.setLoginSource(LoginSource.PASSWORD.getVal());
        if (authPasswd != null) {
            userDetails.setUserPasswd(authPasswd.getPasswdHash());
        }
        return userDetails;
    }

    public void loadUserAccess(AccessUserDetails userDetails) {
        TenantAccessPto tenantAccess = tenantAccessRepositoryFacade.queryLoginTenant(userDetails.getUserId());
        HttpAsserts.notNull(tenantAccess, FORBIDDEN, "{admin.tenant.user.invalid}");
        applyUserAccess(userDetails, tenantAccess);
    }

    public void reloadRefreshUserAccess(AccessUserDetails userDetails) {
        Integer userId = userDetails.getUserId();
        Integer tenantId = userDetails.getTenantId();
        SysUser user = userRepositoryFacade.queryById(userId);
        HttpAsserts.notNull(user, UNAUTHORIZED, "{frame.auth.refresh.invalid}");
        HttpAsserts.isTrue(Objects.equals(user.getUserAccount(), userDetails.getUsername())
                        && ENABLE.equals(user.getUserStatus()), UNAUTHORIZED, "{frame.auth.refresh.invalid}");

        TenantAccessPto tenantAccess = tenantAccessRepositoryFacade.queryTenantAccess(userId, tenantId);
        HttpAsserts.notNull(tenantAccess, UNAUTHORIZED, "{frame.auth.refresh.invalid}");
        HttpAsserts.isTrue(Objects.equals(tenantAccess.getTenantCode(), userDetails.getTenantCode()),
                UNAUTHORIZED, "{frame.auth.refresh.invalid}");
        applyUserAccess(userDetails, tenantAccess);
    }

    private void applyUserAccess(AccessUserDetails userDetails, TenantAccessPto tenantAccess) {
        Integer tenantId = tenantAccess.getTenantId();
        // 集群信息
        userDetails.setClusterId(applicationProperties.getClusterId());
        userDetails.setClusterLevel(applicationProperties.getClusterLevel());
        userDetails.setClusterName(applicationProperties.getClusterName());
        // 租户信息
        userDetails.setTenantId(tenantId);
        userDetails.setTenantCode(tenantAccess.getTenantCode());
        userDetails.setUserType(tenantAccess.getUserType());
        userDetails.setUserCode(tenantAccess.getUserCode());
        if (StringUtils.isNotBlank(tenantAccess.getDisplayName())) {
            userDetails.setUserNick(tenantAccess.getDisplayName());
        }
        // 主部门信息
        userDetails.setDeptId(tenantAccess.getDeptId());
        userDetails.setDeptCode(tenantAccess.getDeptCode());
        userDetails.setDeptName(tenantAccess.getDeptName());
        // 角色信息
        List<String> roles = tenantAccessRepositoryFacade.queryRoleCodes(tenantId, userDetails.getUserId());
        userDetails.setRoles(roles);
        if (roles.contains(ROLE_ADMIN)) {
            userDetails.setPermissions(List.of(PERMIT_ADMIN));
            userDetails.setPermitScopes(Map.of());
            return;
        }
        // 权限信息
        List<PermitScopePto> permitScopes = tenantAccessRepositoryFacade.queryPermitScopes(tenantId, userDetails.getUserId());
        Set<String> permissions = new LinkedHashSet<>();
        Map<String, List<Integer>> scopes = new HashMap<>();
        for (PermitScopePto permitScope : permitScopes) {
            permissions.add(permitScope.getPermit());
            if (permitScope.getScopeId() != null) {
                scopes.computeIfAbsent(permitScope.getPermit(), key -> new ArrayList<>())
                        .add(permitScope.getScopeId());
            }
        }
        userDetails.setPermissions(new ArrayList<>(permissions));
        userDetails.setPermitScopes(scopes);
    }
}
