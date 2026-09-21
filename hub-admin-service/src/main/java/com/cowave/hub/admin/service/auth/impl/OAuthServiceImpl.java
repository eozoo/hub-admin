/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.auth.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.auth.biz.SysAuthBiz;
import com.cowave.hub.admin.domain.auth.entity.SysAuthIdentity;
import com.cowave.hub.admin.domain.auth.entity.SysAuthPasswd;
import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.bo.GitlabUser;
import com.cowave.hub.admin.domain.auth.entity.bo.GithubUser;
import com.cowave.hub.admin.domain.auth.entity.bo.WechatUser;
import com.cowave.hub.admin.domain.auth.entity.bo.QqUser;
import com.cowave.hub.admin.domain.auth.entity.bo.BilibiliUser;
import com.cowave.hub.admin.domain.auth.remote.BilibiliRemote;
import com.cowave.hub.admin.domain.auth.remote.QqRemote;
import com.cowave.hub.admin.domain.auth.entity.command.AccountBind;
import com.cowave.hub.admin.domain.auth.entity.command.OAuthConfigUpdate;
import com.cowave.hub.admin.domain.auth.entity.query.OAuthUserQuery;
import com.cowave.hub.admin.domain.auth.entity.vo.OAuthUserVo;
import com.cowave.hub.admin.domain.auth.entity.vo.LoginVo;
import com.cowave.hub.admin.domain.auth.enums.ProviderType;
import com.cowave.hub.admin.domain.auth.enums.ProviderCode;
import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;
import com.cowave.hub.admin.domain.auth.enums.PasswdAlgo;
import com.cowave.hub.admin.domain.auth.enums.PasswdSource;
import com.cowave.hub.admin.domain.auth.remote.GitlabRemote;
import com.cowave.hub.admin.domain.auth.remote.GithubRemote;
import com.cowave.hub.admin.domain.auth.remote.WechatRemote;
import com.cowave.hub.admin.domain.auth.repository.facade.SysAuthRepositoryFacade;
import com.cowave.hub.admin.domain.rbac2.biz.SysTenantUserBiz;
import com.cowave.hub.admin.domain.rbac2.biz.SysUserBiz;
import com.cowave.hub.admin.domain.rbac2.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac2.entity.SysTenantUser;
import com.cowave.hub.admin.domain.rbac2.entity.SysUser;
import com.cowave.hub.admin.domain.rbac2.entity.SysUserRole;
import com.cowave.hub.admin.domain.rbac2.enums.RoleGrant;
import com.cowave.hub.admin.domain.rbac2.repository.facade.SysTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac2.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.domain.sys.biz.SysOperationBiz;
import com.cowave.hub.admin.service.auth.OAuthService;
import com.cowave.hub.admin.service.auth.support.MfaConfiguration;
import com.cowave.hub.admin.service.auth.support.SysUserDetailsServiceImpl;
import com.cowave.zoo.framework.access.operation.OperationInfo;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.access.security.BearerTokenService;
import com.cowave.zoo.framework.helper.redis.RedisHelper;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_EXTERNAL_BIND;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_OAUTH_STATE;
import static com.cowave.hub.admin.domain.auth.enums.AuthType.SYS;
import static com.cowave.hub.admin.domain.rbac2.enums.EnableStatus.ENABLE;
import static com.cowave.hub.admin.domain.sys.enums.OpAction.LOGIN;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_AUTH;
import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.zoo.http.client.constants.HttpCode.FORBIDDEN;
import static com.cowave.zoo.http.client.constants.HttpCode.UNAUTHORIZED;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class OAuthServiceImpl implements OAuthService {
    private static final long BIND_EXPIRATION_MINUTES = 10;
    private final PasswordEncoder passwordEncoder;
    private final RedisHelper redisHelper;
    private final MfaConfiguration mfaConfiguration;
    private final SysOperationBiz operationBiz;
    private final SysAuthBiz authBiz;
    private final SysUserBiz userBiz;
    private final SysTenantUserBiz tenantUserBiz;
    private final QqRemote qqRemote;
    private final GitlabRemote gitlabRemote;
    private final GithubRemote githubRemote;
    private final WechatRemote wechatRemote;
    private final BilibiliRemote bilibiliRemote;
    private final SysAuthRepositoryFacade authRepositoryFacade;
    private final SysUserRepositoryFacade userRepositoryFacade;
    private final SysTenantRepositoryFacade tenantRepositoryFacade;
    private final SysUserDetailsServiceImpl userDetailsService;
    private final BearerTokenService bearerTokenService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginVo gitlabCallback(String code, String state) {
        HttpAsserts.isTrue(StringUtils.isNotBlank(code), BAD_REQUEST, "{admin.auth.gitlab.code.invalid}");
        HttpAsserts.isTrue(StringUtils.isNotBlank(state), UNAUTHORIZED, "{admin.auth.gitlab.state.invalid}");
        // gitlab开启
        SysAuthProvider provider = authRepositoryFacade.queryOauthProviderByCode(ProviderCode.GITLAB);
        HttpAsserts.isTrue(provider != null && provider.getStatus() == ENABLE,
                FORBIDDEN, "{admin.auth.gitlab.unavailable}");
        // state校验
        Integer providerId = redisHelper.getValueAndDelete(AUTH_OAUTH_STATE.formatted(state));
        HttpAsserts.isTrue(Objects.equals(provider.getProviderId(), providerId),
                UNAUTHORIZED, "{admin.auth.gitlab.state.invalid}");
        // gitlab获取用户
        GitlabUser gitlabUser = gitlabRemote.getUser(provider, code);
        HttpAsserts.isTrue(gitlabUser != null && gitlabUser.getId() != null
                        && StringUtils.isNotBlank(gitlabUser.getUsername()),
                FORBIDDEN, "{admin.auth.gitlab.user.invalid}");
        // 获取本地gitlab身份信息
        String subject = gitlabUser.getId().toString();
        SysAuthIdentity identity = authRepositoryFacade.queryProviderIdentity(provider.getProviderId(), subject);
        // 没有绑定过，则返回页面填写绑定账号信息
        if (identity == null) {
            identity = new SysAuthIdentity();
            identity.setProviderId(provider.getProviderId());
            identity.setIdentityType(ProviderType.OAUTH);
            identity.setExternalSubject(subject);
            identity.setUserAccount(gitlabUser.getUsername());
            identity.setUserName(gitlabUser.getName());
            identity.setUserEmail(gitlabUser.getEmail());
            identity.setUserAvatar(gitlabUser.getAvatarUrl());
            String bindToken = UUID.randomUUID().toString();
            redisHelper.putExpire(AUTH_EXTERNAL_BIND.formatted(bindToken), identity, BIND_EXPIRATION_MINUTES, TimeUnit.MINUTES);
            LoginVo login = new LoginVo();
            login.setBindRequired(true);
            login.setBindToken(bindToken);
            login.setBindType(ProviderType.OAUTH);
            login.setBindAccount(gitlabUser.getUsername());
            login.setBindName(gitlabUser.getName());
            return login;
        }
        // gitlab身份禁用
        HttpAsserts.isTrue(identity.getAuthStatus() == ENABLE,
                FORBIDDEN, "{admin.user.account.disable}", gitlabUser.getUsername());
        // 获取本地账号
        SysUser sysUser = userRepositoryFacade.queryById(identity.getUserId());
        HttpAsserts.notNull(sysUser, FORBIDDEN, "{admin.user.not.exist}", gitlabUser.getUsername());
        HttpAsserts.equals(ENABLE, sysUser.getUserStatus(),
                FORBIDDEN, "{admin.user.account.disable}", sysUser.getUserAccount());
        // 更新gitlab身份信息
        Date now = new Date();
        identity.setUserAccount(gitlabUser.getUsername());
        identity.setUserName(gitlabUser.getName());
        identity.setUserEmail(gitlabUser.getEmail());
        identity.setUserAvatar(gitlabUser.getAvatarUrl());
        identity.setLastSyncTime(now);
        if (StringUtils.isBlank(sysUser.getMfa())) {
            identity.setLastLoginTime(now);
        }
        identity.setUpdateTime(now);
        authBiz.updateProviderIdentity(identity);
        // 签发令牌
        return issueLogin(sysUser, identity, ProviderCode.GITLAB);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginVo githubCallback(String code, String state) {
        HttpAsserts.isTrue(StringUtils.isNotBlank(code), BAD_REQUEST, "{admin.auth.github.code.invalid}");
        HttpAsserts.isTrue(StringUtils.isNotBlank(state), UNAUTHORIZED, "{admin.auth.github.state.invalid}");
        // github开启
        SysAuthProvider provider = authRepositoryFacade.queryOauthProviderByCode(ProviderCode.GITHUB);
        HttpAsserts.isTrue(provider != null && provider.getStatus() == ENABLE,
                FORBIDDEN, "{admin.auth.github.unavailable}");
        // state校验
        Integer providerId = redisHelper.getValueAndDelete(AUTH_OAUTH_STATE.formatted(state));
        HttpAsserts.isTrue(Objects.equals(provider.getProviderId(), providerId), UNAUTHORIZED,
                "{admin.auth.github.state.invalid}");
        // github获取用户
        GithubUser githubUser = githubRemote.getUser(provider, code);
        HttpAsserts.isTrue(githubUser != null && githubUser.getId() != null
                        && StringUtils.isNotBlank(githubUser.getLogin()), FORBIDDEN,
                "{admin.auth.github.user.invalid}");
        // 获取本地github身份信息
        String subject = githubUser.getId().toString();
        SysAuthIdentity identity = authRepositoryFacade.queryProviderIdentity(provider.getProviderId(), subject);
        // 没有绑定过，则返回页面填写绑定账号信息
        if (identity == null) {
            identity = new SysAuthIdentity();
            identity.setProviderId(provider.getProviderId());
            identity.setIdentityType(ProviderType.OAUTH);
            identity.setExternalSubject(subject);
            identity.setUserAccount(githubUser.getLogin());
            identity.setUserName(StringUtils.defaultIfBlank(githubUser.getName(), githubUser.getLogin()));
            identity.setUserEmail(githubUser.getEmail());
            identity.setUserAvatar(githubUser.getAvatarUrl());
            String bindToken = UUID.randomUUID().toString();
            redisHelper.putExpire(AUTH_EXTERNAL_BIND.formatted(bindToken), identity, BIND_EXPIRATION_MINUTES, TimeUnit.MINUTES);
            LoginVo login = new LoginVo();
            login.setBindRequired(true);
            login.setBindToken(bindToken);
            login.setBindType(ProviderType.OAUTH);
            login.setBindAccount(githubUser.getLogin());
            login.setBindName(identity.getUserName());
            return login;
        }
        // github身份禁用
        HttpAsserts.isTrue(identity.getAuthStatus() == ENABLE,
                FORBIDDEN, "{admin.user.account.disable}", githubUser.getLogin());
        // 获取本地账号
        SysUser user = userRepositoryFacade.queryById(identity.getUserId());
        HttpAsserts.notNull(user, FORBIDDEN, "{admin.user.not.exist}", githubUser.getLogin());
        HttpAsserts.equals(ENABLE, user.getUserStatus(),
                FORBIDDEN, "{admin.user.account.disable}", user.getUserAccount());
        // 更新github身份信息
        Date now = new Date();
        identity.setUserAccount(githubUser.getLogin());
        identity.setUserName(StringUtils.defaultIfBlank(githubUser.getName(), githubUser.getLogin()));
        identity.setUserEmail(githubUser.getEmail());
        identity.setUserAvatar(githubUser.getAvatarUrl());
        identity.setLastSyncTime(now);
        if (StringUtils.isBlank(user.getMfa())) {
            identity.setLastLoginTime(now);
        }
        identity.setUpdateTime(now);
        authBiz.updateProviderIdentity(identity);
        // 签发令牌
        return issueLogin(user, identity, ProviderCode.GITHUB);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginVo wechatCallback(String code, String state) {
        HttpAsserts.isTrue(StringUtils.isNotBlank(code), BAD_REQUEST, "{admin.auth.wechat.code.invalid}");
        HttpAsserts.isTrue(StringUtils.isNotBlank(state), UNAUTHORIZED, "{admin.auth.wechat.state.invalid}");
        // wechat开启
        SysAuthProvider provider = authRepositoryFacade.queryOauthProviderByCode(ProviderCode.WECHAT);
        HttpAsserts.isTrue(provider != null && provider.getStatus() == ENABLE,
                FORBIDDEN, "{admin.auth.wechat.unavailable}");
        // state校验
        Integer providerId = redisHelper.getValueAndDelete(AUTH_OAUTH_STATE.formatted(state));
        HttpAsserts.isTrue(Objects.equals(provider.getProviderId(), providerId), UNAUTHORIZED,
                "{admin.auth.wechat.state.invalid}");
        // wechat获取用户
        WechatUser wechatUser = wechatRemote.getUser(provider, code);
        HttpAsserts.isTrue(wechatUser != null && StringUtils.isNotBlank(wechatUser.getOpenid()), FORBIDDEN,
                "{admin.auth.wechat.user.invalid}");
        // 获取本地wechat身份信息
        String subject = StringUtils.defaultIfBlank(wechatUser.getUnionid(), wechatUser.getOpenid());
        SysAuthIdentity identity = authRepositoryFacade.queryProviderIdentity(provider.getProviderId(), subject);
        String userName = StringUtils.defaultIfBlank(wechatUser.getNickname(), "微信用户");
        // 没有绑定过，则返回页面填写绑定账号信息
        if (identity == null) {
            identity = new SysAuthIdentity();
            identity.setProviderId(provider.getProviderId());
            identity.setIdentityType(ProviderType.OAUTH);
            identity.setExternalSubject(subject);
            identity.setUserAccount(subject);
            identity.setUserName(userName);
            identity.setUserAvatar(wechatUser.getAvatarUrl());
            String bindToken = UUID.randomUUID().toString();
            redisHelper.putExpire(AUTH_EXTERNAL_BIND.formatted(bindToken), identity, BIND_EXPIRATION_MINUTES, TimeUnit.MINUTES);
            LoginVo login = new LoginVo();
            login.setBindRequired(true);
            login.setBindToken(bindToken);
            login.setBindType(ProviderType.OAUTH);
            login.setBindAccount(subject);
            login.setBindName(userName);
            return login;
        }
        // wechat身份禁用
        HttpAsserts.isTrue(identity.getAuthStatus() == ENABLE, FORBIDDEN,
                "{admin.user.account.disable}", identity.getUserAccount());
        // 获取本地账号
        SysUser user = userRepositoryFacade.queryById(identity.getUserId());
        HttpAsserts.notNull(user, FORBIDDEN, "{admin.user.not.exist}", identity.getUserAccount());
        HttpAsserts.equals(ENABLE, user.getUserStatus(), FORBIDDEN,
                "{admin.user.account.disable}", user.getUserAccount());
        // 更新wechat身份信息
        Date now = new Date();
        identity.setUserName(userName);
        identity.setUserAvatar(wechatUser.getAvatarUrl());
        identity.setLastSyncTime(now);
        if (StringUtils.isBlank(user.getMfa())) {
            identity.setLastLoginTime(now);
        }
        identity.setUpdateTime(now);
        authBiz.updateProviderIdentity(identity);
        // 签发令牌
        return issueLogin(user, identity, ProviderCode.WECHAT);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginVo qqCallback(String code, String state) {
        HttpAsserts.isTrue(StringUtils.isNotBlank(code), BAD_REQUEST, "{admin.auth.qq.code.invalid}");
        HttpAsserts.isTrue(StringUtils.isNotBlank(state), UNAUTHORIZED, "{admin.auth.qq.state.invalid}");
        // qq开启
        SysAuthProvider provider = authRepositoryFacade.queryOauthProviderByCode(ProviderCode.QQ);
        HttpAsserts.isTrue(provider != null && provider.getStatus() == ENABLE,
                FORBIDDEN, "{admin.auth.qq.unavailable}");
        // state校验
        Integer providerId = redisHelper.getValueAndDelete(AUTH_OAUTH_STATE.formatted(state));
        HttpAsserts.isTrue(Objects.equals(provider.getProviderId(), providerId), UNAUTHORIZED,
                "{admin.auth.qq.state.invalid}");
        // qq获取用户
        QqUser qqUser = qqRemote.getUser(provider, code);
        HttpAsserts.isTrue(qqUser != null && StringUtils.isNotBlank(qqUser.getOpenid()), FORBIDDEN,
                "{admin.auth.qq.user.invalid}");
        // 获取本地qq身份信息
        String subject = qqUser.getOpenid();
        SysAuthIdentity identity = authRepositoryFacade.queryProviderIdentity(provider.getProviderId(), subject);
        String userName = StringUtils.defaultIfBlank(qqUser.getNickname(), "QQ用户");
        // 没有绑定过，则返回页面填写绑定账号信息
        if (identity == null) {
            identity = new SysAuthIdentity();
            identity.setProviderId(provider.getProviderId());
            identity.setIdentityType(ProviderType.OAUTH);
            identity.setExternalSubject(subject);
            identity.setUserAccount(subject);
            identity.setUserName(userName);
            identity.setUserAvatar(qqUser.getAvatarUrl());
            String bindToken = UUID.randomUUID().toString();
            redisHelper.putExpire(AUTH_EXTERNAL_BIND.formatted(bindToken), identity, BIND_EXPIRATION_MINUTES, TimeUnit.MINUTES);
            LoginVo login = new LoginVo();
            login.setBindRequired(true);
            login.setBindToken(bindToken);
            login.setBindType(ProviderType.OAUTH);
            login.setBindAccount(subject);
            login.setBindName(userName);
            return login;
        }
        // qq身份禁用
        HttpAsserts.isTrue(identity.getAuthStatus() == ENABLE, FORBIDDEN,
                "{admin.user.account.disable}", identity.getUserAccount());
        // 获取本地账号
        SysUser user = userRepositoryFacade.queryById(identity.getUserId());
        HttpAsserts.notNull(user, FORBIDDEN, "{admin.user.not.exist}", identity.getUserAccount());
        HttpAsserts.equals(ENABLE, user.getUserStatus(), FORBIDDEN,
                "{admin.user.account.disable}", user.getUserAccount());
        // 更新qq身份信息
        Date now = new Date();
        identity.setUserName(userName);
        identity.setUserAvatar(qqUser.getAvatarUrl());
        identity.setLastSyncTime(now);
        if (StringUtils.isBlank(user.getMfa())) {
            identity.setLastLoginTime(now);
        }
        identity.setUpdateTime(now);
        authBiz.updateProviderIdentity(identity);
        // 签发令牌
        return issueLogin(user, identity, ProviderCode.QQ);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginVo bilibiliCallback(String code, String state) {
        HttpAsserts.isTrue(StringUtils.isNotBlank(code), BAD_REQUEST, "{admin.auth.bilibili.code.invalid}");
        HttpAsserts.isTrue(StringUtils.isNotBlank(state), UNAUTHORIZED, "{admin.auth.bilibili.state.invalid}");
        // bilibili开启
        SysAuthProvider provider = authRepositoryFacade.queryOauthProviderByCode(ProviderCode.BILIBILI);
        HttpAsserts.isTrue(provider != null && provider.getStatus() == ENABLE,
                FORBIDDEN, "{admin.auth.bilibili.unavailable}");
        // state校验
        Integer providerId = redisHelper.getValueAndDelete(AUTH_OAUTH_STATE.formatted(state));
        HttpAsserts.isTrue(Objects.equals(provider.getProviderId(), providerId), UNAUTHORIZED,
                "{admin.auth.bilibili.state.invalid}");
        // bilibili获取用户
        BilibiliUser bilibiliUser = bilibiliRemote.getUser(provider, code);
        HttpAsserts.isTrue(bilibiliUser != null && StringUtils.isNotBlank(bilibiliUser.getOpenid()), FORBIDDEN,
                "{admin.auth.bilibili.user.invalid}");
        // 获取本地bilibili身份信息
        String subject = bilibiliUser.getOpenid();
        SysAuthIdentity identity = authRepositoryFacade.queryProviderIdentity(provider.getProviderId(), subject);
        String userName = StringUtils.defaultIfBlank(bilibiliUser.getName(), "B站用户");
        // 没有绑定过，则返回页面填写绑定账号信息
        if (identity == null) {
            identity = new SysAuthIdentity();
            identity.setProviderId(provider.getProviderId());
            identity.setIdentityType(ProviderType.OAUTH);
            identity.setExternalSubject(subject);
            identity.setUserAccount(subject);
            identity.setUserName(userName);
            identity.setUserAvatar(bilibiliUser.getAvatarUrl());
            String bindToken = UUID.randomUUID().toString();
            redisHelper.putExpire(AUTH_EXTERNAL_BIND.formatted(bindToken), identity, BIND_EXPIRATION_MINUTES, TimeUnit.MINUTES);
            LoginVo login = new LoginVo();
            login.setBindRequired(true);
            login.setBindToken(bindToken);
            login.setBindType(ProviderType.OAUTH);
            login.setBindAccount(subject);
            login.setBindName(userName);
            return login;
        }
        // bilibili身份禁用
        HttpAsserts.isTrue(identity.getAuthStatus() == ENABLE, FORBIDDEN,
                "{admin.user.account.disable}", identity.getUserAccount());
        // 获取本地账号
        SysUser user = userRepositoryFacade.queryById(identity.getUserId());
        HttpAsserts.notNull(user, FORBIDDEN, "{admin.user.not.exist}", identity.getUserAccount());
        HttpAsserts.equals(ENABLE, user.getUserStatus(), FORBIDDEN,
                "{admin.user.account.disable}", user.getUserAccount());
        // 更新bilibili身份信息
        Date now = new Date();
        identity.setUserName(userName);
        identity.setUserAvatar(bilibiliUser.getAvatarUrl());
        identity.setLastSyncTime(now);
        if (StringUtils.isBlank(user.getMfa())) {
            identity.setLastLoginTime(now);
        }
        identity.setUpdateTime(now);
        authBiz.updateProviderIdentity(identity);
        // 签发令牌
        return issueLogin(user, identity, ProviderCode.BILIBILI);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LoginVo bindAccount(AccountBind bind) {
        // 账号/密码校验，失败允许重试
        HttpAsserts.isTrue(bind.getPassWord().getBytes(StandardCharsets.UTF_8).length <= 72,
                BAD_REQUEST, "{admin.auth.bind.passwd.invalid}");
        HttpAsserts.isFalse(userRepositoryFacade.existsAccountIncludingDeleted(bind.getUserAccount()),
                BAD_REQUEST, "{admin.user.account.conflict}", bind.getUserAccount());
        // Provider身份信息
        String key = AUTH_EXTERNAL_BIND.formatted(bind.getBindToken());
        SysAuthIdentity identity = redisHelper.getValueAndDelete(key);
        HttpAsserts.notNull(identity, UNAUTHORIZED, "{admin.auth.bind.expired}");
        // 绑定类型
        HttpAsserts.isTrue(bind.getBindType() == ProviderType.OAUTH,
                UNAUTHORIZED, "{admin.auth.bind.type.invalid}");
        // Provider配置
        SysAuthProvider provider = authRepositoryFacade.queryOauthProviderById(identity.getProviderId());
        HttpAsserts.isTrue(provider != null && provider.getStatus() == ENABLE,
                FORBIDDEN, "{admin.auth.provider.unavailable}");
        // 没有绑定过
        HttpAsserts.isTrue(authRepositoryFacade.queryProviderIdentity(identity.getProviderId(), identity.getExternalSubject()) == null,
                BAD_REQUEST, "{admin.auth.bind.already}");
        // 获取公共租户
        SysTenant publicTenant = tenantRepositoryFacade.queryPublicTenant();
        HttpAsserts.notNull(publicTenant, FORBIDDEN, "{admin.tenant.user.invalid}");
        // 本地账号
        Date now = new Date();
        SysUser user = new SysUser();
        user.setUserAccount(bind.getUserAccount());
        user.setUserName(bind.getUserName());
        user.setUserAvatar(identity.getUserAvatar());
        user.setUserStatus(ENABLE);
        user.setIsDelete(0);
        user.setCreateBy(user.getUserAccount());
        user.setCreateTime(now);
        userBiz.createUser(user);
        // 本地账号密码
        SysAuthPasswd passwd = new SysAuthPasswd();
        passwd.setUserId(user.getUserId());
        passwd.setPasswdHash(passwordEncoder.encode(bind.getPassWord()));
        passwd.setPasswdAlgo(PasswdAlgo.BCRYPT);
        passwd.setIsCurrent(1);
        passwd.setNeedChange(0);
        passwd.setEffectiveTime(now);
        passwd.setChangeSource(PasswdSource.INITIAL);
        passwd.setCreateBy(user.getUserAccount());
        passwd.setCreateTime(now);
        authBiz.createPasswd(passwd);
        // 本地账号权限
        SysTenantUser member = new SysTenantUser();
        member.setTenantId(publicTenant.getTenantId());
        member.setUserId(user.getUserId());
        member.setUserType("external");
        member.setUserCode("open-visitor-" + user.getUserId());
        member.setDisplayName(user.getUserName());
        member.setStatus(ENABLE);
        member.setIsDefault(tenantRepositoryFacade.queryLoginTenant(user.getUserId()) == null ? 1 : 0);
        member.setJoinTime(now);
        member.setCreateBy(user.getUserAccount());
        member.setCreateTime(now);
        tenantUserBiz.createMember(member);
        Integer roleId = tenantRepositoryFacade.queryVisitorRoleId(publicTenant.getTenantId());
        if (roleId != null) {
            SysUserRole role = new SysUserRole();
            role.setTenantId(publicTenant.getTenantId());
            role.setUserId(user.getUserId());
            role.setRoleId(roleId);
            role.setGrantType(RoleGrant.DIRECT);
            role.setGrantedBy(user.getUserAccount());
            role.setGrantedTime(now);
            tenantUserBiz.grantRole(role);
        }
        // Provider绑定身份
        identity.setUserId(user.getUserId());
        identity.setAuthStatus(ENABLE);
        identity.setCreateTime(now);
        identity.setLastSyncTime(now);
        identity.setLastLoginTime(now);
        authBiz.createExternalIdentity(identity);
        // 签发令牌
        return issueLogin(user, identity, provider.getProviderCode());
    }

    private LoginVo issueLogin(SysUser sysUser, SysAuthIdentity identity, ProviderCode providerCode) {
        AccessUserDetails userDetails = AccessUserDetails.newUserDetails();
        userDetails.setAccessValid(true);
        userDetails.setAuthType(SYS.getVal());
        userDetails.setUserId(sysUser.getUserId());
        userDetails.setUsername(sysUser.getUserAccount());
        userDetails.setUserNick(sysUser.getUserName());
        userDetails.setLoginSource(providerCode.getVal());
        // 需要MFA二次认证
        if (StringUtils.isNotBlank(sysUser.getMfa())) {
            userDetails.setAccessValid(false);
            userDetails.setMfaRequired(true);
            userDetails.setAccessToken(mfaConfiguration.buildMfaToken(sysUser.getUserAccount(), identity.getId()));
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
                .desc(providerCode.getVal() + "登录：" + identity.getUserAccount())
                .build();
        operationBiz.createOperation(operationInfo, null);
        return LoginVo.from(userDetails);
    }

    @Override
    public SysAuthProvider getOauth(ProviderCode providerCode) {
        return authRepositoryFacade.queryOauthProviderByCode(providerCode);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editOauth(ProviderCode providerCode, OAuthConfigUpdate oauthConfig) {
        SysAuthProvider provider = authRepositoryFacade.queryOauthProviderByCode(providerCode);
        if (provider == null) {
            provider = new SysAuthProvider();
            provider.setProviderCode(providerCode);
            provider.setProviderType(ProviderType.OAUTH);
            provider.setProviderName(providerCode.getVal());
            provider.setProviderSort(0);
            provider.setGrantType("authorization_code");
            provider.setResponseType("code");
            provider.setCreateBy(Access.userAccount());
            provider.setCreateTime(new Date());
        }
        provider.setClientId(oauthConfig.getClientId());
        provider.setClientSecret(oauthConfig.getClientSecret());
        provider.setAuthUrl(oauthConfig.getAuthUrl());
        provider.setRedirectUrl(oauthConfig.getRedirectUrl());
        provider.setAuthScope(oauthConfig.getAuthScope());
        provider.setStatus(oauthConfig.getStatus());
        provider.setUpdateBy(Access.userAccount());
        provider.setUpdateTime(new Date());
        authBiz.saveOauthProvider(provider);
    }

    @Override
    public Page<OAuthUserVo> listUser(ProviderCode providerCode, OAuthUserQuery userQuery) {
        SysAuthProvider provider = authRepositoryFacade.queryOauthProviderByCode(providerCode);
        if (provider == null) {
            return Access.page();
        }
        Page<SysAuthIdentity> identities =
                authRepositoryFacade.queryProviderUsers(provider.getProviderId(), userQuery.getUserAccount());
        Page<OAuthUserVo> result = new Page<>(identities.getCurrent(), identities.getSize(), identities.getTotal());
        result.setRecords(identities.getRecords().stream().map(OAuthUserVo::from).toList());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateIdentityStatus(ProviderCode providerCode, Long identityId, EnableStatus status) {
        SysAuthProvider provider = authRepositoryFacade.queryOauthProviderByCode(providerCode);
        HttpAsserts.notNull(provider, BAD_REQUEST, "{admin.auth.provider.unavailable}");
        authBiz.updateProviderIdentityStatus(provider.getProviderId(), identityId, status);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteIdentity(ProviderCode providerCode, Long identityId) {
        SysAuthProvider provider = authRepositoryFacade.queryOauthProviderByCode(providerCode);
        HttpAsserts.notNull(provider, BAD_REQUEST, "{admin.auth.provider.unavailable}");
        authBiz.deleteProviderIdentity(provider.getProviderId(), identityId);
    }
}
