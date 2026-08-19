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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.auth.entity.HubApp;
import com.cowave.hub.admin.domain.auth.remote.GitlabRemote;
import com.cowave.hub.admin.domain.rbac.entity.*;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.AccessProperties;
import com.cowave.zoo.framework.access.operation.OperationInfo;
import com.cowave.zoo.framework.access.security.AccessInfo;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.access.security.BearerTokenService;
import com.cowave.zoo.framework.helper.redis.RedisHelper;
import com.cowave.zoo.tools.Collections;
import com.cowave.hub.admin.domain.auth.biz.SysOAuthBiz;
import com.cowave.hub.admin.domain.auth.repository.facade.SysOAuthRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.biz.SysUserBiz;
import com.cowave.hub.admin.domain.auth.repository.facade.HubAppRepositoryFacade;
import com.cowave.hub.admin.domain.member.entity.HubMember;
import com.cowave.hub.admin.domain.member.repository.facade.HubMemberRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysRoleRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.domain.auth.entity.bo.GitlabUser;
import com.cowave.hub.admin.domain.auth.entity.SysOAuth;
import com.cowave.hub.admin.domain.auth.entity.SysOAuthUser;
import com.cowave.hub.admin.domain.auth.entity.bo.OAuth2CodeBo;
import com.cowave.hub.admin.domain.auth.entity.command.OAuth2CodeReq;
import com.cowave.hub.admin.domain.auth.entity.command.OAuth2TokenReq;
import com.cowave.hub.admin.domain.auth.entity.query.OAuthUserQuery;
import com.cowave.hub.admin.domain.auth.entity.vo.OAuth2CodeVo;
import com.cowave.hub.admin.domain.sys.entity.SysOperation;
import com.cowave.hub.admin.domain.rbac.enums.SuccessStatus;
import com.cowave.hub.admin.domain.sys.biz.SysOperationBiz;
import com.cowave.hub.admin.domain.auth.repository.facade.UserDetailsRepositoryFacade;
import com.cowave.hub.admin.service.auth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.cowave.hub.admin.domain.auth.enums.AuthType.MEMBER;
import static com.cowave.hub.admin.domain.auth.enums.AuthType.OAUTH;
import static com.cowave.hub.admin.domain.rbac.enums.UserType.GITLAB;
import static com.cowave.zoo.http.client.constants.HttpCode.*;
import static com.cowave.hub.admin.domain.AdminRedisKeys.*;
import static com.cowave.hub.admin.domain.sys.enums.OpAction.LOGIN;
import static com.cowave.hub.admin.domain.sys.enums.OpAction.LOGIN_OAUTH;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_AUTH;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class OAuthServiceImpl implements OAuthService {
    private final BearerTokenService bearerTokenService;
    private final RedisHelper redisHelper;
    private final SysUserBiz userBiz;
    private final SysOAuthBiz oauthBiz;
    private final SysUserRepositoryFacade userRepositoryFacade;
    private final SysRoleRepositoryFacade roleRepositoryFacade;
    private final SysTenantRepositoryFacade tenantRepositoryFacade;
    private final SysOAuthRepositoryFacade oauthRepositoryFacade;
    private final HubAppRepositoryFacade oauthAppRepositoryFacade;
    private final SysOperationBiz operationBiz;
    private final GitlabRemote gitlabRemote;
    private final UserDetailsRepositoryFacade userDetailsRepositoryFacade;
    private final HubMemberRepositoryFacade memberRepositoryFacade;
    private final AccessProperties accessProperties;

    @Override
    public AccessUserDetails gitlabCallback(String tenantId, String code) {
        SysOAuth oauth = oauthRepositoryFacade.queryByServerType(tenantId, GITLAB.getVal());
        GitlabUser gitlabUser = gitlabRemote.getGitlabUser(
                oauth.getAuthUrl(), oauth.getAppId(), oauth.getAppSecret(),
                oauth.getRedirectUrl(), oauth.getGrantType(), oauth.getAuthScope(), code);
        // Gitlab用户信息
        assert gitlabUser != null;
        SysOAuthUser oauthUserHub = oauthRepositoryFacade.queryUserByAccount(tenantId, GITLAB.getVal(), gitlabUser.getUsername());
        if (oauthUserHub != null) {
            oauthUserHub.setUserName(gitlabUser.getName());
            oauthUserHub.setUserEmail(gitlabUser.getEmail());
            oauthUserHub.setUserAvatar(gitlabUser.getAvatarUrl());
            oauthUserHub.setUpdateTime(new Date());
            if (CollectionUtils.isNotEmpty(gitlabUser.getIdentities())) {
                GitlabUser.LdapInfo ldap = gitlabUser.getIdentities().get(0);
                oauthUserHub.setUserDept(ldap.getExternUid());
            }
            oauthBiz.updateOauthUserById(oauthUserHub);
        } else {
            oauthUserHub = GitlabUser.oAuthUser(gitlabUser);
            oauthUserHub.setTenantId(tenantId);
            oauthBiz.saveOauthUser(oauthUserHub);
        }

        // 对应系统用户
        String userCode = GITLAB.newCode(tenantId, oauthUserHub.getUserAccount());
        SysUser sysUser = userRepositoryFacade.queryByCode(userCode);
        if(sysUser == null){
            sysUser = new SysUser();
            sysUser.setUserCode(userCode);
            sysUser.setTenantId(tenantId);
            sysUser.setUserType(GITLAB);
            sysUser.setUserAccount(oauthUserHub.getUserAccount());
            sysUser.setUserName(oauthUserHub.getUserName());
            sysUser.setUserEmail(oauthUserHub.getUserEmail());
            userBiz.saveUser(sysUser);
            // role
            SysRole sysRole = roleRepositoryFacade.queryByCode(tenantId, oauth.getRoleCode());
            if(sysRole != null) {
                userBiz.saveUserRole(sysUser.getUserId(), sysRole.getRoleId());
            }
            // 用户关系
            userBiz.saveUserDiagram(sysUser.getUserId(), 0, tenantId);
        }else{
            sysUser.setUserName(oauthUserHub.getUserName());
            sysUser.setUserEmail(oauthUserHub.getUserEmail());
            userBiz.updateGitlabUser(sysUser);
        }

        // 创建令牌
        SysTenant sysTenant = tenantRepositoryFacade.queryById(tenantId);
        AccessUserDetails userDetails = userDetailsRepositoryFacade.queryUserDetails(sysTenant, sysUser, true);
        bearerTokenService.assignAccessRefreshToken(userDetails);

        // 登录日志
        OperationInfo operationInfo = OperationInfo.builder()
                .success(true)
                .opModule(SYSTEM)
                .opType(SYSTEM_AUTH)
                .opAction(LOGIN)
                .desc("Gitlab登录：" + oauthUserHub.getUserAccount())
                .build();
        operationBiz.createOperation(operationInfo, null);
        return userDetails;
    }

    @Override
    public SysOAuth getOauth(String tenantId, String serverType) {
        return oauthRepositoryFacade.queryByServerType(tenantId, serverType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editOauth(String tenantId, SysOAuth oauth) {
        oauth.setTenantId(tenantId);
        oauthBiz.editOauth(oauth);
    }

    @Override
    public Page<SysOAuthUser> listUser(String tenantId, OAuthUserQuery query) {
        return oauthRepositoryFacade.queryUserPage(tenantId, query.getServerType(), query.getUserAccount());
    }

    @Override
    public OAuth2CodeVo getClientCode(OAuth2CodeReq codeReq) {
        // 验证应用id
        HubApp oauthApp = oauthAppRepositoryFacade.queryByClientId(codeReq.getClientId());
        HttpAsserts.notNull(oauthApp, BAD_REQUEST, "{admin.oauth.name.not.exist}");

        // 所属租户是否允许授权这个应用
        HttpAsserts.equals(Access.tenantId(), oauthApp.getTenantId(),
                FORBIDDEN, "{admin.oauth.tenant.forbidden}");

        // 所属角色是否允许授权这个应用
        List<Integer> roleIdList;
        if (MEMBER.getVal().equals(Access.userDetails().getAuthType())) {
            // member用户
            roleIdList = memberRepositoryFacade.queryMemberRoleIdsByMemberId(Access.userId());
        } else {
            // sys用户
            roleIdList = userRepositoryFacade.queryUserRoleIdsByUserId(Access.userId());
        }
        if (!roleIdList.contains(1)) {
            List<HubRoleApp> roleAppList = oauthAppRepositoryFacade.queryRoleAppsByRoleIdList(roleIdList);
            Set<Integer> appIdSet = Collections.copyToSet(roleAppList, HubRoleApp::getAppId);
            HttpAsserts.isTrue(appIdSet.contains(oauthApp.getId()), FORBIDDEN, "{admin.oauth.role.forbidden}");
        }

        // 校验返回类型
        HttpAsserts.isTrue(StringUtils.equalsIgnoreCase("code", codeReq.getResponseType()),
                BAD_REQUEST, "{admin.oauth.resp.invalid}");

        // 验证授权类型
        HttpAsserts.isTrue(oauthApp.getGrantType().contains("authorization_code"),
                BAD_REQUEST, "{admin.oauth.grant.invalid}");

        // 验证回调地址
        HttpAsserts.isTrue(StringUtils.equalsIgnoreCase(oauthApp.getRedirectUrl(), codeReq.getRedirectUri()),
                BAD_REQUEST, "{admin.oauth.redirect.invalid}");

        // 生成随机code
        String code = UUID.randomUUID().toString().replace("-", "");

        OAuth2CodeBo oAuth2CodeBo = new OAuth2CodeBo();
        oAuth2CodeBo.setUserCode(Access.userCode());
        oAuth2CodeBo.setAuthType(Access.userDetails().getAuthType());
        oAuth2CodeBo.setClientId(codeReq.getClientId());
        oAuth2CodeBo.setState(codeReq.getState());
        oAuth2CodeBo.setRedirectUri(oauthApp.getRedirectUrl());

        // PKCE: 存储 code_challenge 和 method（S256 / plain），在换 token 时验证 code_verifier
        if (StringUtils.isNotBlank(codeReq.getCodeChallenge())) {
            oAuth2CodeBo.setCodeChallenge(codeReq.getCodeChallenge());
            oAuth2CodeBo.setCodeChallengeMethod(
                    StringUtils.defaultIfBlank(codeReq.getCodeChallengeMethod(), "plain"));
        }

        // 绑定用户信息
        redisHelper.putExpire(OAUTH_CODE.formatted(code), oAuth2CodeBo, 60, TimeUnit.SECONDS);
        return new OAuth2CodeVo(code, oauthApp.getClientName(), oauthApp.getAuthScope());
    }

    @Override
    public void clientRedirect(String code, HttpServletResponse response) throws IOException {
        OAuth2CodeBo oAuth2CodeBo = redisHelper.getValue(OAUTH_CODE.formatted(code));
        HttpAsserts.notNull(oAuth2CodeBo, BAD_REQUEST, "{admin.oauth.code.expire}");
        // 回调
        String redirectUrl = oAuth2CodeBo.getRedirectUri();
        redirectUrl += (redirectUrl.contains("?") ? "&" : "?") + "code=" + code + "&state=" + oAuth2CodeBo.getState();
        response.sendRedirect(redirectUrl);
    }

    @Override
    public AccessUserDetails getClientToken(OAuth2TokenReq tokenReq) {
        // 获取授权code
        OAuth2CodeBo oAuth2CodeBo = redisHelper.getValue(OAUTH_CODE.formatted(tokenReq.getCode()));
        HttpAsserts.notNull(oAuth2CodeBo, BAD_REQUEST, "{admin.oauth.code.expire}");

        // 验证应用id
        HubApp oauthApp = oauthAppRepositoryFacade.queryByClientId(tokenReq.getClientId());
        HttpAsserts.notNull(oauthApp, BAD_REQUEST, "{admin.oauth.name.not.exist}");

        // 验证授权码是否发放给该应用（授权码绑定 client_id，防止跨应用使用）
        HttpAsserts.equals(oAuth2CodeBo.getClientId(), tokenReq.getClientId(),
                BAD_REQUEST, "{admin.oauth.client.invalid}");

        // 验证回调地址
        HttpAsserts.isTrue(StringUtils.equalsIgnoreCase(oauthApp.getRedirectUrl(), tokenReq.getRedirectUri()),
                BAD_REQUEST, "{admin.oauth.redirect.invalid}");

        // 验证应用密钥
        HttpAsserts.isTrue(StringUtils.equals(tokenReq.getClientSecret(), oauthApp.getClientSecret()),
                BAD_REQUEST, "{admin.oauth.secret.invalid}");

        // PKCE 校验：用 code_verifier 重新计算 challenge，与授权请求时存储的值比对
        if (StringUtils.isNotBlank(oAuth2CodeBo.getCodeChallenge())) {
            String computedChallenge;
            if ("S256".equalsIgnoreCase(oAuth2CodeBo.getCodeChallengeMethod())) {
                computedChallenge = computeS256Challenge(tokenReq.getCodeVerifier());
            } else {
                // plain: challenge == verifier
                computedChallenge = tokenReq.getCodeVerifier();
            }
            HttpAsserts.isTrue(StringUtils.equals(oAuth2CodeBo.getCodeChallenge(), computedChallenge),
                    BAD_REQUEST, "{admin.oauth.pkce.invalid}");
        }

        // 授权码一次性使用：校验全部通过后立即删除，防止重放
        redisHelper.delete(OAUTH_CODE.formatted(tokenReq.getCode()));

        // 创建令牌
        SysTenant sysTenant;
        AccessUserDetails userDetails;
        if (MEMBER.getVal().equals(oAuth2CodeBo.getAuthType())) {
            // 会员身份签发应用令牌
            HubMember hubMember = memberRepositoryFacade.queryByCode(oAuth2CodeBo.getUserCode());
            sysTenant = tenantRepositoryFacade.queryById(hubMember.getTenantId());
            userDetails = userDetailsRepositoryFacade.queryMemberDetails(sysTenant, hubMember);
        } else {
            SysUser sysUser = userRepositoryFacade.queryByCode(oAuth2CodeBo.getUserCode());
            sysTenant = tenantRepositoryFacade.queryById(sysUser.getTenantId());
            userDetails = userDetailsRepositoryFacade.queryUserDetails(sysTenant, sysUser, false);
        }
        userDetails.setOauthId(oauthApp.getClientId());
        userDetails.setOauthName(oauthApp.getClientName());
        userDetails.setAuthType(OAUTH.getVal());
        // 授权访问的应用列表
        List<String> apps = new ArrayList<>();
        // 申请方（oauthId）
        apps.add(oauthApp.getClientId());
        // 签发方（hub-admin）
        String selfAppId = accessProperties.oauthAppId();
        if (StringUtils.isNotBlank(selfAppId)) {
            apps.add(selfAppId);
        }
        // TODO 其它授权访问的应用
        userDetails.setApps(apps);
        bearerTokenService.assignOauthToken(userDetails);

        // 授权日志
        SysOperation sysOperation = new SysOperation();
        sysOperation.setOpStatus(SuccessStatus.SUCCESS);
        sysOperation.setOpModule(SYSTEM);
        sysOperation.setOpType(SYSTEM_AUTH);
        sysOperation.setOpAction(LOGIN_OAUTH);
        sysOperation.setOpDesc("授权应用'" + oauthApp.getClientName() + "'访问");
        sysOperation.setAccess(new AccessInfo(userDetails));
        sysOperation.setIp(Access.accessIp());
        sysOperation.setUrl(Access.accessMethod() + " " + Access.accessUrl());
        sysOperation.setOpTime(Access.accessTime());
        operationBiz.save(sysOperation);
        return userDetails;
    }

    @Override
    public AccessUserDetails refreshClientToken(String refreshToken) {
        return bearerTokenService.refreshOauthToken(refreshToken);
    }

    @Override
    public void revokeClientToken(String tenantId, String authType, String userAccount, String appId) {
        bearerTokenService.revokeOauthToken(tenantId, authType, userAccount, appId);
    }

    /**
     * PKCE S256: code_challenge = BASE64URL(SHA256(code_verifier))
     */
    private String computeS256Challenge(String codeVerifier) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}
