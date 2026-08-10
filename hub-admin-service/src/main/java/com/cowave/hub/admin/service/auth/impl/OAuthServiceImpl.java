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

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.auth.entity.HubOAuthApp;
import com.cowave.hub.admin.domain.rbac.entity.*;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.http.client.response.HttpResponse;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.operation.OperationInfo;
import com.cowave.zoo.framework.access.security.AccessInfo;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.access.security.BearerTokenService;
import com.cowave.zoo.framework.helper.redis.RedisHelper;
import com.cowave.zoo.tools.Collections;
import com.cowave.hub.admin.domain.auth.biz.HubOAuthBiz;
import com.cowave.hub.admin.domain.auth.repository.facade.HubOAuthRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.biz.HubUserBiz;
import com.cowave.hub.admin.domain.auth.repository.facade.HubOAuthAppRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubRoleRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubUserRepositoryFacade;
import com.cowave.hub.admin.domain.auth.entity.bo.GitlabUser;
import com.cowave.hub.admin.domain.auth.entity.HubOAuth;
import com.cowave.hub.admin.domain.auth.entity.bo.GitlabToken;
import com.cowave.hub.admin.domain.auth.entity.HubOAuthUser;
import com.cowave.hub.admin.domain.auth.entity.bo.OAuth2CodeBo;
import com.cowave.hub.admin.domain.auth.entity.command.OAuth2CodeReq;
import com.cowave.hub.admin.domain.auth.entity.command.OAuth2TokenReq;
import com.cowave.hub.admin.domain.auth.entity.query.OAuthUserQuery;
import com.cowave.hub.admin.domain.auth.entity.vo.OAuth2CodeVo;
import com.cowave.hub.admin.domain.sys.entity.HubOperation;
import com.cowave.hub.admin.domain.rbac.enums.SuccessStatus;
import com.cowave.hub.admin.domain.sys.biz.HubOperationBiz;
import com.cowave.hub.admin.domain.auth.repository.facade.UserDetailsRepositoryFacade;
import com.cowave.hub.admin.service.auth.remote.GitlabRemoteService;
import com.cowave.hub.admin.service.auth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private final HubUserBiz userBiz;
    private final HubOAuthBiz oauthBiz;
    private final HubUserRepositoryFacade userRepositoryFacade;
    private final HubRoleRepositoryFacade roleRepositoryFacade;
    private final HubTenantRepositoryFacade tenantRepositoryFacade;
    private final HubOAuthRepositoryFacade oauthRepositoryFacade;
    private final HubOAuthAppRepositoryFacade oauthAppRepositoryFacade;
    private final HubOperationBiz operationBiz;
    private final GitlabRemoteService gitlabRemoteService;
    private final UserDetailsRepositoryFacade userDetailsRepositoryFacade;

    @Override
    public AccessUserDetails gitlabCallback(String tenantId, String code) {
        // 根据授权码兑换令牌
        HubOAuth oauth = oauthRepositoryFacade.queryByServerType(tenantId, GITLAB.getVal());

        HttpResponse<GitlabToken> tokenResponse = gitlabRemoteService.getGitlabToken(oauth.getAuthUrl(),
                oauth.getAppId(), oauth.getAppSecret(),
                oauth.getRedirectUrl(), oauth.getGrantType(),
                oauth.getAuthScope(), code);
        HttpAsserts.isTrue(tokenResponse.isSuccess(), INTERNAL_SERVER_ERROR, tokenResponse.getMessage());

        GitlabToken gitlabToken = tokenResponse.getBody();
        assert gitlabToken != null;
        HttpResponse<GitlabUser> userResponse = gitlabRemoteService.getGitlabUser(oauth.getAuthUrl(), gitlabToken.getAccessToken());
        HttpAsserts.isTrue(userResponse.isSuccess(), INTERNAL_SERVER_ERROR, userResponse.getMessage());
        GitlabUser gitlabUser = userResponse.getBody();

        // Gitlab用户信息
        assert gitlabUser != null;
        HubOAuthUser oauthUserHub = oauthRepositoryFacade.queryUserByAccount(tenantId, GITLAB.getVal(), gitlabUser.getUsername());
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
        HubUser hubUser = userRepositoryFacade.queryByCode(userCode);
        if(hubUser == null){
            hubUser = new HubUser();
            hubUser.setUserCode(userCode);
            hubUser.setTenantId(tenantId);
            hubUser.setUserType(GITLAB);
            hubUser.setUserAccount(oauthUserHub.getUserAccount());
            hubUser.setUserName(oauthUserHub.getUserName());
            hubUser.setUserEmail(oauthUserHub.getUserEmail());
            userBiz.createTenantManager(hubUser);
            // role
            HubRole hubRole = roleRepositoryFacade.queryByCode(tenantId, oauth.getRoleCode());
            if(hubRole != null) {
                userBiz.saveUserRole(hubUser.getUserId(), hubRole.getRoleId());
            }
            // 用户关系
            userBiz.saveUserDiagram(hubUser.getUserId(), 0, tenantId);
        }else{
            hubUser.setUserName(oauthUserHub.getUserName());
            hubUser.setUserEmail(oauthUserHub.getUserEmail());
            userBiz.updateGitlabUser(hubUser);
        }

        // 创建令牌
        HubTenant hubTenant = tenantRepositoryFacade.queryById(tenantId);
        AccessUserDetails userDetails = userDetailsRepositoryFacade.queryUserDetails(GITLAB, hubTenant, hubUser, true);
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
    public HubOAuth getOauth(String tenantId, String serverType) {
        return oauthRepositoryFacade.queryByServerType(tenantId, serverType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editOauth(String tenantId, HubOAuth oauth) {
        oauth.setTenantId(tenantId);
        oauthBiz.editOauth(oauth);
    }

    @Override
    public Page<HubOAuthUser> listUser(String tenantId, OAuthUserQuery query) {
        return oauthRepositoryFacade.queryUserPage(tenantId, query.getServerType(), query.getUserAccount());
    }

    @Override
    public OAuth2CodeVo getClientCode(OAuth2CodeReq codeCreate) {
        // 验证应用id
        HubOAuthApp oauthApp = oauthAppRepositoryFacade.queryByClientId(codeCreate.getClientId());
        HttpAsserts.notNull(oauthApp, BAD_REQUEST, "{admin.oauth.name.not.exist}");

        // 所属租户是否允许授权这个应用
        HttpAsserts.equals(Access.tenantId(), oauthApp.getTenantId(),
                FORBIDDEN, "{admin.oauth.tenant.forbidden}");

        // 所属角色是否允许授权这个应用
        List<Integer> roleIdList = userRepositoryFacade.queryUserRoleIdsByUserId(Access.userId());
        if (!roleIdList.contains(1)) {
            List<HubRoleApp> roleAppList = oauthAppRepositoryFacade.queryRoleAppsByRoleIdList(roleIdList);
            Set<Integer> appIdSet = Collections.copyToSet(roleAppList, HubRoleApp::getAppId);
            HttpAsserts.isTrue(appIdSet.contains(oauthApp.getId()), FORBIDDEN, "{admin.oauth.role.forbidden}");
        }

        // 校验返回类型
        HttpAsserts.isTrue(StringUtils.equalsIgnoreCase("code", codeCreate.getResponseType()),
                BAD_REQUEST, "{admin.oauth.resp.invalid}");

        // 验证授权类型
        HttpAsserts.isTrue(oauthApp.getGrantType().contains("authorization_code"),
                BAD_REQUEST, "{admin.oauth.grant.invalid}");

        // 验证回调地址
        HttpAsserts.isTrue(StringUtils.equalsIgnoreCase(oauthApp.getRedirectUrl(), codeCreate.getRedirectUri()),
                BAD_REQUEST, "{admin.oauth.redirect.invalid}");

        // 生成随机code
        String code = UUID.randomUUID().toString().replace("-", "");

        OAuth2CodeBo oAuth2CodeBo = new OAuth2CodeBo();
        oAuth2CodeBo.setUserCode(Access.userCode());
        oAuth2CodeBo.setState(codeCreate.getState());
        oAuth2CodeBo.setRedirectUri(oauthApp.getRedirectUrl());

        // PKCE校验（这里简单示意下只支持md5）
        if(StringUtils.isNotBlank(codeCreate.getCodeChallenge())
                && "md5".equalsIgnoreCase(codeCreate.getCodeChallengeMethod())){
            String codeVerifier = SecureUtil.md5(codeCreate.getCodeChallenge());
            oAuth2CodeBo.setCodeVerifier(codeVerifier);
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
        String redirectUrl = oAuth2CodeBo.getRedirectUri() + "?code=" + code + "&state=" + oAuth2CodeBo.getState();
        response.sendRedirect(redirectUrl);
    }

    @Override
    public AccessUserDetails getClientToken(OAuth2TokenReq tokenCreate) {
        // 获取授权code
        OAuth2CodeBo oAuth2CodeBo = redisHelper.getValue(OAUTH_CODE.formatted(tokenCreate.getCode()));
        HttpAsserts.notNull(oAuth2CodeBo, BAD_REQUEST, "{admin.oauth.code.expire}");

        // 验证应用id
        HubOAuthApp oauthApp = oauthAppRepositoryFacade.queryByClientId(tokenCreate.getClientId());
        HttpAsserts.notNull(oauthApp, BAD_REQUEST, "{admin.oauth.name.not.exist}");

        // 验证回调地址
        HttpAsserts.isTrue(StringUtils.equalsIgnoreCase(oauthApp.getRedirectUrl(), tokenCreate.getRedirectUri()),
                BAD_REQUEST, "{admin.oauth.redirect.invalid}");

        // 验证应用密钥
        HttpAsserts.isTrue(StringUtils.equals(tokenCreate.getClientSecret(), oauthApp.getClientSecret()),
                BAD_REQUEST, "{admin.oauth.secret.invalid}");

        // PKCE校验
        if(StringUtils.isNotBlank(oAuth2CodeBo.getCodeVerifier())){
            HttpAsserts.isTrue(StringUtils.equals(oAuth2CodeBo.getCodeVerifier(), tokenCreate.getCodeVerifier()),
                BAD_REQUEST, "{admin.oauth.pkce.invalid}");
        }

        // 创建令牌
        HubUser hubUser = userRepositoryFacade.queryByCode(oAuth2CodeBo.getUserCode());
        HubTenant hubTenant = tenantRepositoryFacade.queryById(hubUser.getTenantId());
        AccessUserDetails userDetails = userDetailsRepositoryFacade.queryUserDetails(hubUser.getUserType(), hubTenant, hubUser, false);
        userDetails.setOauthId(oauthApp.getClientId());
        userDetails.setOauthName(oauthApp.getClientName());
        bearerTokenService.assignOauthToken(userDetails);

        // 授权日志
        HubOperation hubOperation = new HubOperation();
        hubOperation.setOpStatus(SuccessStatus.SUCCESS);
        hubOperation.setOpModule(SYSTEM);
        hubOperation.setOpType(SYSTEM_AUTH);
        hubOperation.setOpAction(LOGIN_OAUTH);
        hubOperation.setOpDesc("授权应用'" + oauthApp.getClientName() + "'访问");
        hubOperation.setAccess(new AccessInfo(userDetails));
        hubOperation.setIp(Access.accessIp());
        hubOperation.setUrl(Access.accessMethod() + " " + Access.accessUrl());
        hubOperation.setOpTime(Access.accessTime());
        operationBiz.save(hubOperation);
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
}
