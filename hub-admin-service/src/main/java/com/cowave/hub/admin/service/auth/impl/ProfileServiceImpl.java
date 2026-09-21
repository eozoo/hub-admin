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

import com.cowave.hub.admin.domain.auth.entity.vo.UserProfileVo;
import com.cowave.hub.admin.domain.auth.entity.SysAuthPasswd;
import com.cowave.hub.admin.domain.auth.entity.SysAuthLdap;
import com.cowave.hub.admin.domain.auth.entity.command.LdapLogin;
import com.cowave.hub.admin.domain.auth.remote.LdapRemote;
import com.cowave.hub.admin.domain.auth.biz.SysAuthBiz;
import com.cowave.hub.admin.domain.auth.repository.facade.SysAuthRepositoryFacade;
import com.cowave.hub.admin.domain.auth.entity.command.MfaBind;
import com.cowave.hub.admin.domain.auth.entity.command.MfaDisable;
import com.cowave.hub.admin.domain.auth.entity.command.PasswdReset;
import com.cowave.hub.admin.domain.auth.entity.command.ProfileUpdate;
import com.cowave.hub.admin.domain.auth.entity.vo.MfaVo;
import com.cowave.hub.admin.domain.auth.entity.vo.IdentityBindingVo;
import com.cowave.hub.admin.domain.auth.entity.SysAuthIdentity;
import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.bo.GitlabUser;
import com.cowave.hub.admin.domain.auth.entity.bo.GithubUser;
import com.cowave.hub.admin.domain.auth.entity.bo.WechatUser;
import com.cowave.hub.admin.domain.auth.entity.bo.QqUser;
import com.cowave.hub.admin.domain.auth.entity.bo.BilibiliUser;
import com.cowave.hub.admin.domain.auth.remote.BilibiliRemote;
import com.cowave.hub.admin.domain.auth.remote.QqRemote;
import com.cowave.hub.admin.domain.auth.entity.bo.IdentityBindState;
import com.cowave.hub.admin.domain.auth.enums.ProviderType;
import com.cowave.hub.admin.domain.auth.enums.ProviderCode;
import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;
import com.cowave.hub.admin.domain.auth.remote.GitlabRemote;
import com.cowave.hub.admin.domain.auth.remote.GithubRemote;
import com.cowave.hub.admin.domain.auth.remote.WechatRemote;
import com.cowave.zoo.framework.helper.redis.RedisHelper;
import com.cowave.hub.admin.domain.rbac2.biz.SysUserBiz;
import com.cowave.hub.admin.domain.rbac2.entity.SysUser;
import com.cowave.hub.admin.domain.rbac2.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.domain.rbac2.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac2.repository.facade.SysTenantRepositoryFacade;
import com.cowave.hub.admin.domain.sys2.biz.SysAttachBiz;
import com.cowave.hub.admin.service.auth.ProfileService;
import com.cowave.hub.admin.service.auth.support.MfaAuthVerifier;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.access.security.BearerTokenService;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.http.client.asserts.HttpException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_OAUTH_BIND_STATE;
import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.zoo.http.client.constants.HttpCode.FORBIDDEN;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final PasswordEncoder passwordEncoder;
    private final BearerTokenService bearerTokenService;
    private final LdapRemote ldapRemote;
    private final QqRemote qqRemote;
    private final GitlabRemote gitlabRemote;
    private final GithubRemote githubRemote;
    private final WechatRemote wechatRemote;
    private final BilibiliRemote bilibiliRemote;
    private final RedisHelper redisHelper;
    private final SysAttachBiz attachBiz;
    private final SysUserBiz userBiz;
    private final SysAuthBiz authBiz;
    private final SysAuthRepositoryFacade authRepositoryFacade;
    private final SysUserRepositoryFacade userRepositoryFacade;
    private final SysTenantRepositoryFacade tenantRepositoryFacade;

    @Override
    public UserProfileVo info() throws Exception {
        AccessUserDetails userDetails = Access.userDetails();
        // 用户信息
        Integer userId = userDetails.getUserId();
        SysUser user = userRepositoryFacade.queryById(userId);
        HttpAsserts.notNull(user, BAD_REQUEST, "{admin.user.not.exist}");
        // 租户信息
        Integer tenantId = userDetails.getTenantId();
        SysTenant tenant = tenantRepositoryFacade.queryTenantById(tenantId);
        HttpAsserts.notNull(tenant, BAD_REQUEST, "{admin.tenant.not.exist}");
        // 角色
        List<String> roles = tenantRepositoryFacade.queryRoleNames(tenantId, userId);
        // 部门岗位
        List<String> depts = tenantRepositoryFacade.queryDeptPostNames(tenantId, userId);
        // 汇报人
        List<String> parents = tenantRepositoryFacade.queryParentNames(tenantId, userId);
        UserProfileVo profile = new UserProfileVo();
        profile.setTenantId(tenantId);
        profile.setTenantName(tenant.getTenantName());
        profile.setUserId(userId);
        profile.setUserCode(Access.userCode());
        profile.setUserType(userDetails.getUserType());
        profile.setUserName(user.getUserName());
        profile.setUserAccount(user.getUserAccount());
        profile.setUserSex(user.getUserSex());
        profile.setUserPhone(user.getUserPhone());
        profile.setUserEmail(user.getUserEmail());
        profile.setCreateTime(user.getCreateTime());
        profile.setRoles(roles);
        profile.setDepts(depts);
        profile.setParents(parents);
        // 用户头像
        String avatarUrl = attachBiz.previewLatestAvatar(user.getUserAccount());
        profile.setAvatar(avatarUrl != null ? avatarUrl : user.getUserAvatar());
        return profile;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(ProfileUpdate profile) throws Exception {
        Integer userId = Access.userId();
        SysUser user = userRepositoryFacade.queryById(userId);
        HttpAsserts.notNull(user, BAD_REQUEST, "{admin.user.not.exist}");
        user.setUserName(profile.getUserName());
        user.setUserSex(profile.getUserSex() == null ? null : profile.getUserSex().getVal());
        user.setUserPhone(profile.getUserPhone());
        user.setUserEmail(profile.getUserEmail());
        userBiz.updateProfile(user);
        attachBiz.reserveUserAvatars(user.getUserAccount(), 3);
    }

    @Override
    public void resetPasswd(PasswdReset passwdReset) {
        Integer userId = Access.userId();
        SysAuthPasswd currentPasswd = authRepositoryFacade.queryCurrentPasswd(userId);
        HttpAsserts.notNull(currentPasswd, BAD_REQUEST, "{admin.user.not.exist}");
        String passwd = currentPasswd.getPasswdHash();
        HttpAsserts.isTrue(passwordEncoder.matches(passwdReset.getOldPasswd(), passwd), BAD_REQUEST, "{admin.user.passwd.failed}");
        HttpAsserts.isFalse(passwordEncoder.matches(passwdReset.getNewPasswd(), passwd), BAD_REQUEST, "{admin.user.passwd.repeat}");
        authBiz.changePasswd(userId, passwordEncoder.encode(passwdReset.getNewPasswd()), Access.userAccount());
        // 撤销全部授权
        bearerTokenService.revokeUserTokens(Access.userAccount());
    }

    @Override
    public MfaVo generateMfa() {
        MfaVo mfaVo = new MfaVo();
        SysUser sysUser = userRepositoryFacade.queryById(Access.userId());
        String mfaKey = sysUser.getMfa();
        mfaVo.setEnabled(StringUtils.isNotBlank(mfaKey));
        if (!mfaVo.isEnabled()) {
            mfaKey = MfaAuthVerifier.generateKey();
            String mfaUrl = MfaAuthVerifier.generateAuthUrl(Access.userAccount(), mfaKey);
            mfaVo.setMfaUrl(mfaUrl);
            mfaVo.setMfaKey(mfaKey);
        }
        return mfaVo;
    }

    @Override
    public void enableMfa(MfaBind mfaBind) {
        SysUser user = userRepositoryFacade.queryById(Access.userId());
        HttpAsserts.notNull(user, BAD_REQUEST, "{admin.user.not.exist}");
        HttpAsserts.isTrue(StringUtils.isBlank(user.getMfa()),
                BAD_REQUEST, "{admin.mfa.code.invalid}");
        HttpAsserts.isTrue(MfaAuthVerifier.validateCode(mfaBind.getMfaKey(), mfaBind.getMfaCode()),
                BAD_REQUEST, "{admin.mfa.code.invalid}");
        userBiz.updateMfa(Access.userId(), mfaBind.getMfaKey());
    }

    @Override
    public void disableMfa(MfaDisable mfaDisable) {
        SysUser user = userRepositoryFacade.queryById(Access.userId());
        HttpAsserts.notNull(user, BAD_REQUEST, "{admin.user.not.exist}");
        HttpAsserts.isTrue(StringUtils.isNotBlank(user.getMfa()),
                BAD_REQUEST, "{admin.mfa.code.invalid}");
        HttpAsserts.isTrue(MfaAuthVerifier.validateCode(user.getMfa(), mfaDisable.getMfaCode()),
                BAD_REQUEST, "{admin.mfa.code.invalid}");
        userBiz.updateMfa(Access.userId(), null);
    }

    @Override
    public List<IdentityBindingVo> identities() {
        // OAuth身份
        Map<Integer, SysAuthIdentity> oauthIdentities = new HashMap<>();
        for (SysAuthIdentity identity : authRepositoryFacade.queryUserProviderIdentities(Access.userId())) {
            oauthIdentities.putIfAbsent(identity.getProviderId(), identity);
        }
        // Ldap身份
        SysAuthLdap ldap = authRepositoryFacade.queryLdap();
        SysAuthIdentity ldapIdentity = null;
        if(ldap != null) {
            ldapIdentity = authRepositoryFacade.queryUserLdapIdentity(ldap.getLdapId(), Access.userId());
        }
        // OAuth提供方
        List<SysAuthProvider> providerList = authRepositoryFacade.queryOauthProviders();
        List<IdentityBindingVo> voList = new ArrayList<>();
        voList.add(IdentityBindingVo.from(ProviderCode.LDAP,
                ldap == null ? EnableStatus.DISABLE : ldap.getLdapStatus(), ldapIdentity));
        providerList.stream().map(provider -> IdentityBindingVo.from(
                provider.getProviderCode(), provider.getStatus(), oauthIdentities.get(provider.getProviderId())))
                .forEach(voList::add);
        return voList;
    }

    @Override
    public String oauthBind(ProviderCode providerCode) {
        // provider检查
        SysAuthProvider provider = authRepositoryFacade.queryOauthProviderByCode(providerCode);
        HttpAsserts.isTrue(provider != null && provider.getStatus() == EnableStatus.ENABLE,
                FORBIDDEN, "{admin.auth.provider.unavailable}");
        HttpAsserts.isTrue(StringUtils.isNoneBlank(provider.getAuthUrl(), provider.getClientId(),
                        provider.getRedirectUrl(), provider.getResponseType()),
                FORBIDDEN, "{admin.auth.provider.config.invalid}");
        // 授权url
        String state = UUID.randomUUID().toString();
        String authorizeUrl = provider.buildAuthorizeUrl(state);
        // 保存绑定凭证
        IdentityBindState bindState = new IdentityBindState();
        bindState.setUserId(Access.userId());
        bindState.setProviderId(provider.getProviderId());
        redisHelper.putExpire(AUTH_OAUTH_BIND_STATE.formatted(state), bindState, 10, TimeUnit.MINUTES);
        return authorizeUrl;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public IdentityBindingVo ldapBind(LdapLogin login) {
        // Ldap启用
        SysAuthLdap ldap = authRepositoryFacade.queryEnabledLdap();
        HttpAsserts.notNull(ldap, FORBIDDEN, "{admin.auth.ldap.unavailable}");
        // Ldap登录获取身份信息
        HttpAsserts.isTrue(ldapRemote.authenticate(ldap, login.getUserAccount(), login.getPassWord()),
                BAD_REQUEST, "{frame.auth.pass.invalid}");
        List<SysAuthIdentity> matches = ldapRemote.searchUser(ldap, login.getUserAccount());
        HttpAsserts.isTrue(matches.size() == 1, BAD_REQUEST, "{admin.ldap.failed.user}");
        SysAuthIdentity identity = matches.get(0);
        HttpAsserts.isTrue(StringUtils.isNotBlank(identity.getExternalSubject())
                        && login.getUserAccount().equalsIgnoreCase(identity.getUserAccount()),
                BAD_REQUEST, "{admin.ldap.failed.user}");
        // 更新身份信息
        SysAuthIdentity existing = authRepositoryFacade.queryLdapIdentity(ldap.getLdapId(), identity.getExternalSubject());
        Date now = new Date();
        identity.setUserId(Access.userId());
        identity.setLdapId(ldap.getLdapId());
        identity.setIdentityType(ProviderType.LDAP);
        identity.setAuthStatus(EnableStatus.ENABLE);
        identity.setLastSyncTime(now);
        identity.setUpdateTime(now);
        if (existing == null) {
            identity.setCreateTime(now);
            authBiz.createExternalIdentity(identity);
        } else {
            identity.setId(existing.getId());
            identity.setCreateTime(existing.getCreateTime());
            authBiz.updateLdapIdentity(identity);
        }
        return IdentityBindingVo.from(ProviderCode.LDAP, ldap.getLdapStatus(), identity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public IdentityBindingVo identityCallback(ProviderCode providerCode, String code, String state) {
        HttpAsserts.isTrue(StringUtils.isNotBlank(code), BAD_REQUEST, "{admin.auth.provider.code.invalid}");
        HttpAsserts.isTrue(StringUtils.isNotBlank(state), BAD_REQUEST, "{admin.auth.provider.state.invalid}");
        // provider支持
        SysAuthProvider provider = authRepositoryFacade.queryOauthProviderByCode(providerCode);
        HttpAsserts.isTrue(provider != null && provider.getStatus() == EnableStatus.ENABLE,
                FORBIDDEN, "{admin.auth.provider.unavailable}");
        // 获取绑定凭证
        IdentityBindState bindState = redisHelper.getValueAndDelete(AUTH_OAUTH_BIND_STATE.formatted(state));
        HttpAsserts.notNull(bindState, BAD_REQUEST, "{admin.auth.provider.state.invalid}");
        HttpAsserts.isTrue(Objects.equals(Access.userId(), bindState.getUserId())
                        && Objects.equals(provider.getProviderId(), bindState.getProviderId()),
                BAD_REQUEST, "{admin.auth.provider.state.invalid}");
        // provider获取用户
        String externalSubject;
        String userAccount;
        String userName;
        String userEmail;
        String userAvatar;
        switch (providerCode) {
            case GITLAB: {
                GitlabUser gitlabUser = gitlabRemote.getUser(provider, code);
                HttpAsserts.isTrue(gitlabUser != null && gitlabUser.getId() != null
                                && StringUtils.isNotBlank(gitlabUser.getUsername()),
                        FORBIDDEN, "{admin.auth.gitlab.user.invalid}");
                externalSubject = gitlabUser.getId().toString();
                userAccount = gitlabUser.getUsername();
                userName = gitlabUser.getName();
                userEmail = gitlabUser.getEmail();
                userAvatar = gitlabUser.getAvatarUrl();
                break;
            }
            case GITHUB: {
                GithubUser githubUser = githubRemote.getUser(provider, code);
                HttpAsserts.isTrue(githubUser != null && githubUser.getId() != null
                                && StringUtils.isNotBlank(githubUser.getLogin()),
                        FORBIDDEN, "{admin.auth.github.user.invalid}");
                externalSubject = githubUser.getId().toString();
                userAccount = githubUser.getLogin();
                userName = StringUtils.defaultIfBlank(githubUser.getName(), githubUser.getLogin());
                userEmail = githubUser.getEmail();
                userAvatar = githubUser.getAvatarUrl();
                break;
            }
            case WECHAT: {
                WechatUser wechatUser = wechatRemote.getUser(provider, code);
                HttpAsserts.isTrue(wechatUser != null && StringUtils.isNotBlank(wechatUser.getOpenid()),
                        FORBIDDEN, "{admin.auth.wechat.user.invalid}");
                externalSubject = StringUtils.defaultIfBlank(wechatUser.getUnionid(), wechatUser.getOpenid());
                userAccount = externalSubject;
                userName = StringUtils.defaultIfBlank(wechatUser.getNickname(), "微信用户");
                userEmail = null;
                userAvatar = wechatUser.getAvatarUrl();
                break;
            }
            case QQ: {
                QqUser qqUser = qqRemote.getUser(provider, code);
                HttpAsserts.isTrue(qqUser != null && StringUtils.isNotBlank(qqUser.getOpenid()),
                        FORBIDDEN, "{admin.auth.qq.user.invalid}");
                externalSubject = qqUser.getOpenid();
                userAccount = externalSubject;
                userName = StringUtils.defaultIfBlank(qqUser.getNickname(), "QQ用户");
                userEmail = null;
                userAvatar = qqUser.getAvatarUrl();
                break;
            }
            case BILIBILI: {
                BilibiliUser bilibiliUser = bilibiliRemote.getUser(provider, code);
                HttpAsserts.isTrue(bilibiliUser != null && StringUtils.isNotBlank(bilibiliUser.getOpenid()),
                        FORBIDDEN, "{admin.auth.bilibili.user.invalid}");
                externalSubject = bilibiliUser.getOpenid();
                userAccount = externalSubject;
                userName = StringUtils.defaultIfBlank(bilibiliUser.getName(), "B站用户");
                userEmail = null;
                userAvatar = bilibiliUser.getAvatarUrl();
                break;
            }
            default:
                throw new HttpException(
                        BAD_REQUEST, "{admin.auth.provider.unsupported}");
        }
        // 获取本地provider身份信息
        SysAuthIdentity existing = authRepositoryFacade.queryProviderIdentity(provider.getProviderId(), externalSubject);
        // 如果身份已绑定过账号，那么重新绑定到当前账号
        SysAuthIdentity identity = existing == null ? new SysAuthIdentity() : existing;
        identity.setUserId(Access.userId());
        identity.setProviderId(provider.getProviderId());
        identity.setIdentityType(ProviderType.OAUTH);
        identity.setExternalSubject(externalSubject);
        identity.setUserAccount(userAccount);
        identity.setUserName(userName);
        identity.setUserEmail(userEmail);
        identity.setUserAvatar(userAvatar);
        identity.setAuthStatus(EnableStatus.ENABLE);
        Date now = new Date();
        identity.setLastSyncTime(now);
        identity.setUpdateTime(now);
        if (existing == null) {
            identity.setCreateTime(now);
            authBiz.createExternalIdentity(identity);
        } else {
            authBiz.updateProviderIdentity(identity);
        }
        return IdentityBindingVo.from(providerCode, provider.getStatus(), identity);
    }
}
