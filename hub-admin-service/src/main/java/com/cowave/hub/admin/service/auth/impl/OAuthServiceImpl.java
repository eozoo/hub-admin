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
import com.cowave.hub.admin.domain.auth.biz.SysOAuthBiz;
import com.cowave.hub.admin.domain.auth.entity.SysOAuth;
import com.cowave.hub.admin.domain.auth.entity.SysOAuthUser;
import com.cowave.hub.admin.domain.auth.entity.bo.GitlabUser;
import com.cowave.hub.admin.domain.auth.entity.query.OAuthUserQuery;
import com.cowave.hub.admin.domain.auth.remote.GitlabRemote;
import com.cowave.hub.admin.domain.auth.repository.facade.SysOAuthRepositoryFacade;
import com.cowave.hub.admin.domain.auth.repository.facade.UserDetailsRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.biz.SysUserBiz;
import com.cowave.hub.admin.domain.rbac.entity.SysRole;
import com.cowave.hub.admin.domain.rbac.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac.entity.SysUser;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysRoleRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.domain.sys.biz.SysOperationBiz;
import com.cowave.hub.admin.service.auth.OAuthService;
import com.cowave.zoo.framework.access.operation.OperationInfo;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.access.security.BearerTokenService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.cowave.hub.admin.domain.rbac.enums.UserType.GITLAB;
import static com.cowave.hub.admin.domain.sys.enums.OpAction.LOGIN;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_AUTH;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class OAuthServiceImpl implements OAuthService {
    private final BearerTokenService bearerTokenService;
    private final SysUserBiz userBiz;
    private final SysOAuthBiz oauthBiz;
    private final SysUserRepositoryFacade userRepositoryFacade;
    private final SysRoleRepositoryFacade roleRepositoryFacade;
    private final SysTenantRepositoryFacade tenantRepositoryFacade;
    private final SysOAuthRepositoryFacade oauthRepositoryFacade;
    private final SysOperationBiz operationBiz;
    private final GitlabRemote gitlabRemote;
    private final UserDetailsRepositoryFacade userDetailsRepositoryFacade;

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
}
