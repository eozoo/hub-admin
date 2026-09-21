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
package com.cowave.hub.admin.service.auth.impl;

import com.cowave.hub.admin.domain.auth.entity.vo.OnlineAccess;
import com.cowave.hub.admin.domain.auth.entity.SysAuthPasswd;
import com.cowave.hub.admin.domain.auth.entity.SysAuthIdentity;
import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.SysAuthLdap;
import com.cowave.hub.admin.domain.auth.entity.bo.MfaChallenge;
import com.cowave.hub.admin.domain.auth.enums.ProviderType;
import com.cowave.hub.admin.domain.auth.enums.LoginSource;
import com.cowave.hub.admin.domain.auth.biz.SysAuthBiz;
import com.cowave.hub.admin.domain.auth.enums.PasswdAlgo;
import com.cowave.hub.admin.domain.auth.enums.PasswdSource;
import com.cowave.hub.admin.domain.rbac2.biz.SysTenantUserBiz;
import com.cowave.hub.admin.domain.rbac2.entity.SysTenantUser;
import com.cowave.hub.admin.domain.rbac2.entity.SysUserRole;
import com.cowave.hub.admin.domain.rbac2.entity.SysUser;
import com.cowave.hub.admin.domain.rbac2.enums.RoleGrant;
import com.cowave.hub.admin.domain.sys2.biz.SysAttachBiz;
import com.cowave.hub.admin.service.auth.AuthService;

import com.cowave.hub.admin.domain.rbac2.biz.SysMenuBiz;
import com.cowave.hub.admin.domain.sys2.repository.facade.SysConfigRepositoryFacade;
import com.cowave.hub.admin.service.auth.support.MfaConfiguration;
import com.cowave.hub.admin.service.auth.support.SysUserDetailsServiceImpl;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.http.client.asserts.HttpHintException;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.operation.OperationInfo;
import com.cowave.zoo.framework.access.security.*;
import com.cowave.zoo.framework.helper.redis.RedisHelper;
import com.cowave.hub.admin.domain.auth.entity.command.UserRegister;
import com.cowave.hub.admin.domain.auth.entity.query.OnlineQuery;
import com.cowave.hub.admin.domain.auth.entity.vo.AuthVo;
import com.cowave.hub.admin.domain.auth.entity.vo.LoginVo;
import com.cowave.hub.admin.domain.auth.repository.facade.SysAuthRepositoryFacade;
import com.cowave.hub.admin.domain.auth.entity.vo.OnlineVo;
import com.cowave.hub.admin.domain.sys.biz.SysOperationBiz;
import com.cowave.hub.admin.domain.rbac2.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac2.repository.facade.SysTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac2.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.domain.rbac2.biz.SysUserBiz;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.cowave.zoo.framework.access.security.Permission.ROLE_ADMIN;
import static com.cowave.zoo.framework.access.security.BearerTokenDelegate.CLAIM_USER_ACCOUNT;
import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.zoo.http.client.constants.HttpCode.FORBIDDEN;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_FAILS;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_LOCK;
import static com.cowave.hub.admin.domain.rbac2.enums.EnableStatus.ENABLE;
import static com.cowave.hub.admin.domain.sys.enums.OpAction.LOGIN;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.*;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final BearerTokenService bearerTokenService;
    private final AuthenticationManager authenticationManager;
    private final RedisHelper redisHelper;
    private final MfaConfiguration mfaConfiguration;
    private final SysOperationBiz operationBiz;
    private final SysUserBiz userBiz;
    private final SysMenuBiz menuBiz;
    private final SysAuthBiz authBiz;
    private final SysAttachBiz attachBiz;
    private final SysTenantUserBiz tenantUserBiz;
    private final SysConfigRepositoryFacade configRepositoryFacade;
    private final SysAuthRepositoryFacade authRepositoryFacade;
    private final SysUserRepositoryFacade userRepositoryFacade;
    private final SysTenantRepositoryFacade tenantAccessRepositoryFacade;
    private final SysUserDetailsServiceImpl userDetailsService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String register(UserRegister userRegister) {
        // 注册开关
        Boolean registerOnOff = configRepositoryFacade.queryConfigValue("hub.registerOnOff");
        HttpAsserts.isTrue(Boolean.TRUE.equals(registerOnOff), FORBIDDEN, "{admin.register.disable}");
        // 默认租户
        SysTenant publicTenant = tenantAccessRepositoryFacade.queryPublicTenant();
        HttpAsserts.notNull(publicTenant, BAD_REQUEST, "{admin.tenant.user.invalid}");
        // 账号重复检查
        String userAccount = userRegister.getUserAccount();
        HttpAsserts.isFalse(userRepositoryFacade.existsAccountIncludingDeleted(userAccount),
                BAD_REQUEST, "{admin.user.account.conflict}", userAccount);
        // 默认密码
        String initPasswd = configRepositoryFacade.queryConfigValue("hub.initPassword");
        HttpAsserts.notNull(initPasswd, BAD_REQUEST, "{admin.user.passwd.null}");
        // 用户信息
        Date now = new Date();
        SysUser user = new SysUser();
        user.setUserAccount(userAccount);
        user.setUserName(userRegister.getUserName());
        user.setUserEmail(userRegister.getUserEmail());
        user.setUserStatus(ENABLE);
        user.setIsDelete(0);
        user.setCreateBy(userAccount);
        user.setCreateTime(now);
        userBiz.createUser(user);
        // 密码信息
        SysAuthPasswd passwd = new SysAuthPasswd();
        passwd.setUserId(user.getUserId());
        passwd.setPasswdHash(passwordEncoder.encode(initPasswd));
        passwd.setPasswdAlgo(PasswdAlgo.BCRYPT);
        passwd.setIsCurrent(1);
        passwd.setNeedChange(1);
        passwd.setEffectiveTime(now);
        passwd.setChangeSource(PasswdSource.INITIAL);
        passwd.setCreateBy(userAccount);
        passwd.setCreateTime(now);
        authBiz.createPasswd(passwd);
        // 租户成员
        SysTenantUser member = new SysTenantUser();
        member.setTenantId(publicTenant.getTenantId());
        member.setUserId(user.getUserId());
        member.setUserType("external");
        member.setUserCode("open-visitor-" + user.getUserId());
        member.setDisplayName(user.getUserName());
        member.setStatus(ENABLE);
        member.setIsDefault(1);
        member.setJoinTime(now);
        member.setCreateBy(userAccount);
        member.setCreateTime(now);
        tenantUserBiz.createMember(member);
        // 租户角色
        Integer roleId = tenantAccessRepositoryFacade.queryVisitorRoleId(publicTenant.getTenantId());
        if (roleId != null) {
            SysUserRole userRole = new SysUserRole();
            userRole.setTenantId(publicTenant.getTenantId());
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(roleId);
            userRole.setGrantType(RoleGrant.DIRECT);
            userRole.setGrantedBy(userAccount);
            userRole.setGrantedTime(now);
            tenantUserBiz.grantRole(userRole);
        }
        return initPasswd;
    }

    @Override
    public LoginVo login(String userAccount, String passwd) {
        Long lockTime = redisHelper.getExpire(AUTH_LOCK.formatted(userAccount));
        if (lockTime != null && lockTime > 0) {
            long minutes = (lockTime + 59) / 60;
            throw new HttpHintException(BAD_REQUEST, "{admin.auth.locked}", minutes);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new TenantUsernamePasswordAuthenticationToken(null, userAccount, passwd));
            AccessUserDetails userDetails = (AccessUserDetails) authentication.getPrincipal();
            // 如果有MFA，需要二次认证
            if (!userDetails.isMfaRequired()) {
                completeLogin(userDetails, "用户登录：" + userAccount, null);
            }
            return LoginVo.from(userDetails);
        } catch (BadCredentialsException e) {
            // 5min内最多允许尝试5次密码，否则锁定30min
            Long failCount = redisHelper.incrementValue(AUTH_FAILS.formatted(userAccount), 1);
            if (failCount == 1) {
                redisHelper.expire(AUTH_FAILS.formatted(userAccount), 300, TimeUnit.SECONDS);
            }
            if (failCount >= 5) {
                redisHelper.putExpire(AUTH_LOCK.formatted(userAccount), "-", 1800, TimeUnit.SECONDS);
                throw new HttpHintException(BAD_REQUEST, "{admin.auth.locked}", 30);
            }
            throw new HttpHintException(BAD_REQUEST, "{admin.auth.failed}", 5 - failCount);
        }
    }

    @Override
    public LoginVo mfa(String mfaToken, String mfaCode) {
        Claims claims = mfaConfiguration.parseMfaToken(mfaToken);
        String userAccount = (String) claims.get(CLAIM_USER_ACCOUNT);
        AccessUserDetails userDetails = userDetailsService.loadMfaUser(userAccount, mfaCode);
        MfaChallenge challenge = mfaConfiguration.consume(claims);
        SysAuthIdentity identity = null;
        String loginDescription = "用户登录：" + userAccount;
        if (challenge.getIdentityId() != null) {
            identity = authRepositoryFacade.queryIdentityById(challenge.getIdentityId());
            HttpAsserts.isTrue(identity != null && Objects.equals(identity.getUserId(), userDetails.getUserId())
                    && identity.getAuthStatus() == ENABLE, FORBIDDEN, "{admin.auth.provider.unavailable}");
            if (identity.getIdentityType() == ProviderType.LDAP) {
                SysAuthLdap ldap = authRepositoryFacade.queryEnabledLdap();
                HttpAsserts.isTrue(ldap != null && Objects.equals(ldap.getLdapId(), identity.getLdapId()),
                        FORBIDDEN, "{admin.auth.ldap.unavailable}");
                loginDescription = "LDAP登录：" + identity.getUserAccount();
                userDetails.setLoginSource(LoginSource.LDAP.getVal());
            } else {
                SysAuthProvider provider = authRepositoryFacade.queryOauthProviderById(identity.getProviderId());
                HttpAsserts.isTrue(provider != null && provider.getStatus() == ENABLE,
                        FORBIDDEN, "{admin.auth.provider.unavailable}");
                loginDescription = provider.getProviderCode().getVal() + "登录：" + identity.getUserAccount();
                userDetails.setLoginSource(provider.getProviderCode().getVal());
            }
        }
        completeLogin(userDetails, loginDescription, identity);
        return LoginVo.from(userDetails);
    }

    private void completeLogin(AccessUserDetails userDetails, String loginDescription, SysAuthIdentity identity) {
        // 租户权限信息
        userDetailsService.loadUserAccess(userDetails);
        // 生成令牌
        bearerTokenService.assignAccessRefreshToken(userDetails);
        redisHelper.delete(AUTH_FAILS.formatted(userDetails.getUsername()), AUTH_LOCK.formatted(userDetails.getUsername()));
        if (identity != null) {
            identity.setLastLoginTime(new Date());
            if (identity.getIdentityType() == ProviderType.LDAP) {
                authBiz.updateLdapIdentity(identity);
            } else {
                authBiz.updateProviderIdentity(identity);
            }
        }
        // 操作日志
        OperationInfo operationInfo = OperationInfo.builder()
                .success(true)
                .opModule(SYSTEM)
                .opType(SYSTEM_AUTH)
                .opAction(LOGIN)
                .desc(loginDescription)
                .build();
        operationBiz.createOperation(operationInfo, null);
    }

    @Override
    public void logout() throws IOException {
        bearerTokenService.revoke();
    }

    @Override
    public Response.Page<OnlineVo> onlineList(OnlineQuery query) {
        // 检索令牌索引
        List<OnlineIndex> indexes = bearerTokenService.listTenantOnlineIndex(
                Access.tenantCode(), query.getBeginTime(), query.getEndTime());
        if (StringUtils.isNotBlank(query.getUserAccount())) {
            indexes = indexes.stream().filter(index -> StringUtils.containsIgnoreCase(
                    index.getUserAccount(), query.getUserAccount().trim())).toList();
        }
        // 令牌信息
        List<OnlineVo> onlineList = new ArrayList<>();
        for (OnlineToken onlineToken : bearerTokenService.listOnlineToken(indexes)) {
            // Refresh令牌
            RefreshTokenInfo refresh = onlineToken.getRefreshToken();
            // 授权列表
            List<OnlineAccess> grants = new ArrayList<>();
            // Access授权
            for (AccessTokenInfo accessToken : onlineToken.getAccessTokens()) {
                if (Objects.equals(refresh.getTenantCode(), accessToken.getTenantCode())) {
                    grants.add(new OnlineAccess(accessToken));
                }
            }
            // OAuth授权
            for (RefreshTokenInfo oauthToken : onlineToken.getOauthTokens()) {
                if (Objects.equals(refresh.getTenantCode(), oauthToken.getTenantCode())) {
                    grants.add(new OnlineAccess(oauthToken));
                }
            }
            onlineList.add(OnlineVo.builder()
                    .sessionId(refresh.getSessionId())
                    .tenantCode(refresh.getTenantCode())
                    .refreshId(refresh.getRefreshId())
                    .authType(refresh.getAuthType())
                    .loginSource(LoginSource.of(refresh.getLoginSource()))
                    .userAccount(refresh.getUserAccount())
                    .userName(refresh.getUserName())
                    .cluster(refresh.getClusterName())
                    .loginIp(refresh.getLoginIp())
                    .loginTime(refresh.getLoginTime())
                    .accessList(grants)
                    .build());
        }
        int total = onlineList.size();
        int pageSize = Math.max(1, Access.pageSize());
        long offset = (long) (Math.max(1, Access.pageIndex()) - 1) * pageSize;
        int from = (int) Math.min(offset, total);
        int to = Math.min(from + pageSize, total);
        return new Response.Page<>(onlineList.subList(from, to), total);
    }

    @Override
    public void revokeAccess(String userAccount, String sessionId, String accessId) {
        requireCurrentTenantSession(userAccount, sessionId);
        bearerTokenService.revokeAccessToken(userAccount, sessionId, Access.tenantCode(), accessId);
    }

    @Override
    public void revokeRefresh(String userAccount, String sessionId) {
        requireCurrentTenantSession(userAccount, sessionId);
        bearerTokenService.revokeRefreshToken(userAccount, sessionId);
    }

    private void requireCurrentTenantSession(String userAccount, String sessionId) {
        boolean found = bearerTokenService.listTenantOnlineIndex(
                Access.tenantCode(), null, null).stream().anyMatch(
                        index -> Objects.equals(index.getUserAccount(), userAccount)
                                && Objects.equals(index.getSessionId(), sessionId));
        HttpAsserts.isTrue(found, FORBIDDEN, "{frame.auth.access.denied}");
    }

    @Override
    public LoginVo refresh(String refreshToken) throws Exception{
        return LoginVo.from(bearerTokenService.refreshAccessRefreshToken(refreshToken));
    }

    @Override
    public AuthVo getAuth() throws Exception {
        AccessUserDetails userDetails = Access.userDetails();
        Integer userId = userDetails.getUserId();
        Integer tenantId = userDetails.getTenantId();
        // 用户信息
        AuthVo authVo = new AuthVo();
        authVo.setUserId(userId);
        authVo.setUserName(userDetails.getUserNick());
        authVo.setRoles(userDetails.getRoles());
        authVo.setPermissions(userDetails.getPermissions());
        authVo.setTenantId(tenantId);
        authVo.setTenantCode(userDetails.getTenantCode());
        // 修改密码提示
        SysAuthPasswd authPasswd = authRepositoryFacade.queryCurrentPasswd(userId);
        authVo.setNeedChangePasswd(authPasswd == null ? 0 : authPasswd.getNeedChange());
        // 菜单权限
        SysTenant tenant = tenantAccessRepositoryFacade.queryTenantById(tenantId);
        authVo.setTenantTitle(tenant == null ? null : tenant.getTitle());
        authVo.setMenus(menuBiz.buildRoutes(tenantId, userId, userDetails.getRoles().contains(ROLE_ADMIN)));
        // 用户头像
        String avatarUrl = attachBiz.previewLatestAvatar(Access.userAccount());
        if (avatarUrl != null) {
            authVo.setAvatar(avatarUrl);
        } else {
            var user = userRepositoryFacade.queryById(userId);
            authVo.setAvatar(user == null ? null : user.getUserAvatar());
        }
        return authVo;
    }
}
