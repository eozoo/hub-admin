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

import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;

import com.cowave.hub.admin.domain.auth.enums.LoginSource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.auth.biz.SysAuthBiz;
import com.cowave.hub.admin.domain.auth.entity.SysAuthIdentity;
import com.cowave.hub.admin.domain.auth.entity.SysAuthLdap;
import com.cowave.hub.admin.domain.auth.entity.SysAuthPasswd;
import com.cowave.hub.admin.domain.auth.entity.command.AccountBind;
import com.cowave.hub.admin.domain.auth.entity.vo.LoginVo;
import com.cowave.hub.admin.domain.auth.entity.vo.LdapUserVo;
import com.cowave.hub.admin.domain.auth.enums.PasswdAlgo;
import com.cowave.hub.admin.domain.auth.enums.PasswdSource;
import com.cowave.hub.admin.domain.auth.enums.ProviderType;
import com.cowave.hub.admin.domain.auth.remote.LdapRemote;
import com.cowave.hub.admin.domain.auth.repository.facade.SysAuthRepositoryFacade;
import com.cowave.hub.admin.domain.rbac2.biz.SysTenantUserBiz;
import com.cowave.hub.admin.domain.rbac2.biz.SysUserBiz;
import com.cowave.hub.admin.domain.rbac2.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac2.entity.SysTenantUser;
import com.cowave.hub.admin.domain.rbac2.entity.SysUser;
import com.cowave.hub.admin.domain.rbac2.entity.SysUserRole;
import com.cowave.hub.admin.domain.rbac2.repository.facade.SysTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac2.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.domain.rbac2.enums.RoleGrant;
import com.cowave.hub.admin.domain.sys.biz.SysOperationBiz;
import com.cowave.hub.admin.service.auth.LdapService;
import com.cowave.zoo.framework.access.Access;
import com.cowave.hub.admin.service.auth.support.MfaConfiguration;
import com.cowave.hub.admin.service.auth.support.SysUserDetailsServiceImpl;
import com.cowave.zoo.framework.access.operation.OperationInfo;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.access.security.BearerTokenService;
import com.cowave.zoo.framework.helper.redis.RedisHelper;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_EXTERNAL_BIND;
import static com.cowave.hub.admin.domain.auth.enums.AuthType.SYS;
import static com.cowave.hub.admin.domain.rbac2.enums.EnableStatus.ENABLE;
import static com.cowave.hub.admin.domain.sys.enums.OpAction.LOGIN;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_AUTH;
import static com.cowave.zoo.http.client.constants.HttpCode.*;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class LdapServiceImpl implements LdapService {
    private static final long BIND_EXPIRATION_MINUTES = 10;
    private final LdapRemote ldapRemote2;
    private final BearerTokenService bearerTokenService;
    private final SysOperationBiz operationBiz;
    private final SysAuthBiz authBiz;
    private final SysTenantUserBiz tenantUserBiz;
    private final SysUserBiz userBiz;
    private final SysTenantRepositoryFacade tenantRepositoryFacade;
    private final SysUserRepositoryFacade userRepositoryFacade;
    private final SysAuthRepositoryFacade authRepositoryFacade;
    private final SysUserDetailsServiceImpl userDetailsService;
    private final MfaConfiguration mfaConfiguration;
    private final RedisHelper redisHelper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginVo authenticate(String userAccount, String passWord) {
        SysAuthLdap ldapConfig = authRepositoryFacade.queryEnabledLdap();
        HttpAsserts.notNull(ldapConfig, FORBIDDEN, "{admin.auth.ldap.unavailable}");
        // Ldap认证
        boolean isAuthenticated = ldapRemote2.authenticate(ldapConfig, userAccount, passWord);
        HttpAsserts.isTrue(isAuthenticated, UNAUTHORIZED, "{frame.auth.pass.invalid}");
        // Ldap获取用户
        List<SysAuthIdentity> matches = ldapRemote2.searchUser(ldapConfig, userAccount);
        HttpAsserts.isTrue(matches.size() == 1, FORBIDDEN, "{admin.ldap.failed.user}");
        SysAuthIdentity ldapUser = matches.get(0);
        HttpAsserts.isTrue(StringUtils.isNotBlank(ldapUser.getUserAccount())
                && StringUtils.isNotBlank(ldapUser.getExternalSubject())
                && userAccount.equalsIgnoreCase(ldapUser.getUserAccount()), FORBIDDEN, "{admin.ldap.failed.user}");
        // 获取本地Ldap身份信息
        SysAuthIdentity ldapIdentity = authRepositoryFacade.queryLdapIdentity(ldapConfig.getLdapId(), ldapUser.getExternalSubject());
        // 没有绑定过，则返回页面填写绑定账号信息
        if (ldapIdentity == null) {
            ldapUser.setLdapId(ldapConfig.getLdapId());
            ldapUser.setIdentityType(ProviderType.LDAP);
            String bindToken = UUID.randomUUID().toString();
            redisHelper.putExpire(AUTH_EXTERNAL_BIND.formatted(bindToken), ldapUser, BIND_EXPIRATION_MINUTES, TimeUnit.MINUTES);
            LoginVo login = new LoginVo();
            login.setBindRequired(true);
            login.setBindToken(bindToken);
            login.setBindType(ProviderType.LDAP);
            login.setBindAccount(ldapUser.getUserAccount());
            login.setBindName(ldapUser.getUserName());
            return login;
        }
        // Ldap身份禁用
        HttpAsserts.isTrue(ldapIdentity.getAuthStatus() == ENABLE,
                FORBIDDEN, "{admin.user.account.disable}", userAccount);
        // 获取本地账号
        SysUser sysUser = userRepositoryFacade.queryById(ldapIdentity.getUserId());
        HttpAsserts.notNull(sysUser, FORBIDDEN, "{admin.user.not.exist}", userAccount);
        // 本地账号禁用
        HttpAsserts.equals(ENABLE, sysUser.getUserStatus(),
                FORBIDDEN, "{admin.user.account.disable}", userAccount);
        // 更新Ldap身份信息
        Date now = new Date();
        ldapUser.setId(ldapIdentity.getId());
        ldapUser.setUserId(sysUser.getUserId());
        ldapUser.setLastSyncTime(now);
        if (StringUtils.isBlank(sysUser.getMfa())) {
            ldapUser.setLastLoginTime(now);
        }
        ldapUser.setUpdateTime(now);
        authBiz.updateLdapIdentity(ldapUser);
        // 签发令牌
        return issueLogin(sysUser, ldapUser);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginVo bindAccount(AccountBind bind) {
        // 账号/密码校验，失败允许重试
        HttpAsserts.isTrue(bind.getPassWord().getBytes(StandardCharsets.UTF_8).length <= 72,
                BAD_REQUEST, "{admin.auth.bind.passwd.invalid}");
        HttpAsserts.isFalse(userRepositoryFacade.existsAccountIncludingDeleted(bind.getUserAccount()),
                BAD_REQUEST, "{admin.user.account.conflict}", bind.getUserAccount());
        // Ldap身份信息
        String key = AUTH_EXTERNAL_BIND.formatted(bind.getBindToken());
        SysAuthIdentity ldapUser = redisHelper.getValueAndDelete(key);
        HttpAsserts.notNull(ldapUser, UNAUTHORIZED, "{admin.auth.bind.expired}");
        // 绑定类型
        HttpAsserts.isTrue(bind.getBindType() == ProviderType.LDAP,
                UNAUTHORIZED, "{admin.auth.bind.type.invalid}");
        HttpAsserts.isTrue(ldapUser.getIdentityType() == ProviderType.LDAP,
                UNAUTHORIZED, "{admin.auth.bind.type.invalid}");
        // Ldap配置
        SysAuthLdap ldapConfig = authRepositoryFacade.queryEnabledLdap();
        HttpAsserts.isTrue(ldapConfig != null && Objects.equals(ldapConfig.getLdapId(), ldapUser.getLdapId()),
                FORBIDDEN, "{admin.auth.bind.ldap.unavailable}");
        // 没有绑定过
        HttpAsserts.isTrue(authRepositoryFacade.queryLdapIdentity(ldapUser.getLdapId(), ldapUser.getExternalSubject()) == null,
                BAD_REQUEST, "{admin.auth.bind.already}");
        // 获取公共租户
        SysTenant publicTenant = tenantRepositoryFacade.queryPublicTenant();
        HttpAsserts.notNull(publicTenant, FORBIDDEN, "{admin.tenant.user.invalid}");
        Date now = new Date();
        // 本地账号
        SysUser sysUser = new SysUser();
        sysUser.setUserAccount(bind.getUserAccount());
        sysUser.setUserName(bind.getUserName());
        sysUser.setUserStatus(ENABLE);
        sysUser.setIsDelete(0);
        sysUser.setCreateBy(sysUser.getUserAccount());
        sysUser.setCreateTime(now);
        userBiz.createUser(sysUser);
        // 本地账号密码
        SysAuthPasswd passwd = new SysAuthPasswd();
        passwd.setUserId(sysUser.getUserId());
        passwd.setPasswdHash(passwordEncoder.encode(bind.getPassWord()));
        passwd.setPasswdAlgo(PasswdAlgo.BCRYPT);
        passwd.setIsCurrent(1);
        passwd.setNeedChange(0);
        passwd.setEffectiveTime(now);
        passwd.setChangeSource(PasswdSource.INITIAL);
        passwd.setCreateBy(sysUser.getUserAccount());
        passwd.setCreateTime(now);
        authBiz.createPasswd(passwd);
        // 本地账号权限
        SysTenantUser member = new SysTenantUser();
        member.setTenantId(publicTenant.getTenantId());
        member.setUserId(sysUser.getUserId());
        member.setUserType("external");
        member.setUserCode("open-visitor-" + sysUser.getUserId());
        member.setDisplayName(sysUser.getUserName());
        member.setStatus(ENABLE);
        member.setIsDefault(tenantRepositoryFacade.queryLoginTenant(sysUser.getUserId()) == null ? 1 : 0);
        member.setJoinTime(now);
        member.setCreateBy(sysUser.getUserAccount());
        member.setCreateTime(now);
        tenantUserBiz.createMember(member);
        Integer roleId = tenantRepositoryFacade.queryVisitorRoleId(publicTenant.getTenantId());
        if (roleId != null) {
            SysUserRole role = new SysUserRole();
            role.setTenantId(publicTenant.getTenantId());
            role.setUserId(sysUser.getUserId());
            role.setRoleId(roleId);
            role.setGrantType(RoleGrant.DIRECT);
            role.setGrantedBy(sysUser.getUserAccount());
            role.setGrantedTime(now);
            tenantUserBiz.grantRole(role);
        }
        // Ldap绑定身份
        ldapUser.setUserId(sysUser.getUserId());
        ldapUser.setAuthStatus(ENABLE);
        ldapUser.setCreateTime(now);
        ldapUser.setLastSyncTime(now);
        ldapUser.setLastLoginTime(now);
        authBiz.createExternalIdentity(ldapUser);
        // 签发令牌
        return issueLogin(sysUser, ldapUser);
    }

    private LoginVo issueLogin(SysUser user, SysAuthIdentity identity) {
        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setAccessValid(true);
        userDetails.setAuthType(SYS.getVal());
        userDetails.setUserId(user.getUserId());
        userDetails.setUsername(user.getUserAccount());
        userDetails.setUserNick(user.getUserName());
        userDetails.setLoginSource(LoginSource.LDAP.getVal());
        // 需要MFA二次认证
        if (StringUtils.isNotBlank(user.getMfa())) {
            userDetails.setAccessValid(false);
            userDetails.setMfaRequired(true);
            userDetails.setAccessToken(mfaConfiguration.buildMfaToken(user.getUserAccount(), identity.getId()));
            return LoginVo.from(userDetails);
        }
        // 加载账号权限
        userDetailsService.loadUserAccess(userDetails);
        // 创建令牌
        bearerTokenService.assignAccessRefreshToken(userDetails);
        // 操作日志
        OperationInfo operationInfo = OperationInfo.builder()
                .success(true)
                .opModule(SYSTEM)
                .opType(SYSTEM_AUTH)
                .opAction(LOGIN)
                .desc("LDAP登录：" + identity.getUserAccount())
                .build();
        operationBiz.createOperation(operationInfo, null);
        return LoginVo.from(userDetails);
    }

    @Override
    public void validConfig(SysAuthLdap config) {
        ldapRemote2.validConfig(config);
    }

    @Override
    public SysAuthLdap getLdap() {
        return authRepositoryFacade.queryLdap();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editLdap(SysAuthLdap config) {
        SysAuthLdap existing = authRepositoryFacade.queryLdap();
        Date now = new Date();
        if (existing == null) {
            config.setLdapId(null);
            config.setLdapStatus(config.getLdapStatus() == null ? ENABLE : config.getLdapStatus());
            config.setReadonly(config.getReadonly() == null ? 0 : config.getReadonly());
            config.setSubjectProperty(StringUtils.defaultIfBlank(config.getSubjectProperty(), "objectGUID"));
            config.setEnvironment(config.getEnvironment() == null ? Map.of() : config.getEnvironment());
            config.setCreateBy(Access.userAccount());
            config.setCreateTime(now);
        } else {
            config.setLdapId(existing.getLdapId());
            config.setLdapStatus(config.getLdapStatus() == null ? existing.getLdapStatus() : config.getLdapStatus());
            config.setReadonly(config.getReadonly() == null ? existing.getReadonly() : config.getReadonly());
            config.setSubjectProperty(StringUtils.defaultIfBlank(config.getSubjectProperty(), existing.getSubjectProperty()));
            config.setEnvironment(config.getEnvironment() == null ? existing.getEnvironment() : config.getEnvironment());
            config.setCreateBy(existing.getCreateBy());
            config.setCreateTime(existing.getCreateTime());
        }
        config.setUpdateBy(Access.userAccount());
        config.setUpdateTime(now);
        authBiz.saveLdap(config);
    }

    @Override
    public Page<LdapUserVo> listUser(String ldapAccount) {
        Page<SysAuthIdentity> identities = authRepositoryFacade.queryLdapUsers(ldapAccount);
        Page<LdapUserVo> result = new Page<>(identities.getCurrent(), identities.getSize(), identities.getTotal());
        result.setRecords(identities.getRecords().stream().map(LdapUserVo::from).toList());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateIdentityStatus(Long identityId, EnableStatus status) {
        SysAuthLdap config = authRepositoryFacade.queryLdap();
        HttpAsserts.notNull(config, BAD_REQUEST, "{admin.auth.ldap.unavailable}");
        authBiz.updateLdapIdentityStatus(config.getLdapId(), identityId, status);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteIdentity(Long identityId) {
        SysAuthLdap config = authRepositoryFacade.queryLdap();
        HttpAsserts.notNull(config, BAD_REQUEST, "{admin.auth.ldap.unavailable}");
        authBiz.deleteLdapIdentity(config.getLdapId(), identityId);
    }
}
