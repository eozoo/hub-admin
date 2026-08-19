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
package com.cowave.hub.admin.service.member.impl;

import com.cowave.hub.admin.domain.auth.remote.GitlabRemote;
import com.cowave.zoo.framework.access.operation.OperationInfo;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.access.security.BearerTokenService;
import com.cowave.hub.admin.domain.auth.entity.bo.GitlabUser;
import com.cowave.hub.admin.domain.auth.repository.facade.UserDetailsRepositoryFacade;
import com.cowave.hub.admin.domain.member.biz.HubMemberBiz;
import com.cowave.hub.admin.domain.member.entity.HubMember;
import com.cowave.hub.admin.domain.member.entity.HubOAuth;
import com.cowave.hub.admin.domain.member.entity.vo.HubOAuthVo;
import com.cowave.hub.admin.domain.member.repository.facade.HubMemberRepositoryFacade;
import com.cowave.hub.admin.domain.member.repository.facade.HubOAuthRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.entity.SysRole;
import com.cowave.hub.admin.domain.rbac.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysRoleRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysTenantRepositoryFacade;
import com.cowave.hub.admin.domain.sys.biz.SysOperationBiz;
import com.cowave.hub.admin.service.member.MemberOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cowave.hub.admin.domain.rbac.enums.EnableStatus.ENABLE;
import static com.cowave.hub.admin.domain.rbac.enums.UserType.GITLAB;
import static com.cowave.hub.admin.domain.sys.enums.OpAction.LOGIN;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_AUTH;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class MemberOAuthServiceImpl implements MemberOAuthService {
    private final BearerTokenService bearerTokenService;
    private final HubOAuthRepositoryFacade hubOAuthRepositoryFacade;
    private final HubMemberRepositoryFacade hubMemberRepositoryFacade;
    private final HubMemberBiz hubMemberBiz;
    private final SysRoleRepositoryFacade roleRepositoryFacade;
    private final SysTenantRepositoryFacade tenantRepositoryFacade;
    private final UserDetailsRepositoryFacade userDetailsRepositoryFacade;
    private final SysOperationBiz operationBiz;
    private final GitlabRemote gitlabRemote;

    @Override
    public List<HubOAuthVo> memberOauthList(String tenantId) {
        List<HubOAuth> oauthList = hubOAuthRepositoryFacade.queryListByTenantId(tenantId);
        List<HubOAuthVo> voList = new ArrayList<>();
        for (HubOAuth oauth : oauthList) {
            HubOAuthVo vo = new HubOAuthVo();
            vo.setServerType(oauth.getServerType());
            vo.setOauthName(oauth.getOauthName());
            vo.setOauthIcon(oauth.getOauthIcon());
            vo.setOauthTip(oauth.getOauthTip());
            vo.setAuthorizeUrl(oauth.gitlabAuthorizeUrl());
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public AccessUserDetails memberGitlabCallback(String tenantId, String code) {
        HubOAuth oauth = hubOAuthRepositoryFacade.queryByServerType(tenantId, GITLAB.getVal());
        GitlabUser gitlabUser = gitlabRemote.getGitlabUser(
                oauth.getAuthUrl(), oauth.getAppId(), oauth.getAppSecret(),
                oauth.getRedirectUrl(), oauth.getGrantType(), oauth.getAuthScope(), code);
        // 会员用户
        assert gitlabUser != null;
        String userCode = GITLAB.newCode(tenantId, gitlabUser.getUsername());
        HubMember hubMember = hubMemberRepositoryFacade.queryByCode(userCode);
        if (hubMember == null) {
            hubMember = new HubMember();
            hubMember.setMemberCode(userCode);
            hubMember.setTenantId(tenantId);
            hubMember.setMemberType(GITLAB);
            hubMember.setMemberAccount(gitlabUser.getUsername());
            hubMember.setMemberName(gitlabUser.getName());
            hubMember.setMemberAvatar(gitlabUser.getAvatarUrl());
            hubMember.setMemberEmail(gitlabUser.getEmail());
            hubMember.setMemberStatus(ENABLE);
            hubMember.setCreateTime(new Date());
            hubMember.setUpdateTime(new Date());
            hubMemberBiz.saveMember(hubMember);
            // 默认角色
            SysRole sysRole = roleRepositoryFacade.queryByCode(tenantId, oauth.getRoleCode());
            if (sysRole != null) {
                hubMemberBiz.saveMemberRole(hubMember.getMemberId(), sysRole.getRoleId());
            }
        } else {
            hubMember.setMemberName(gitlabUser.getName());
            hubMember.setMemberAvatar(gitlabUser.getAvatarUrl());
            hubMember.setMemberEmail(gitlabUser.getEmail());
            hubMember.setUpdateTime(new Date());
            hubMemberBiz.updateMember(hubMember);
        }

        // 创建令牌
        SysTenant sysTenant = tenantRepositoryFacade.queryById(tenantId);
        AccessUserDetails userDetails = userDetailsRepositoryFacade.queryMemberDetails(sysTenant, hubMember);
        bearerTokenService.assignAccessRefreshToken(userDetails);

        // 登录日志
        OperationInfo operationInfo = OperationInfo.builder()
                .success(true)
                .opModule(SYSTEM)
                .opType(SYSTEM_AUTH)
                .opAction(LOGIN)
                .desc("会员登录：" + hubMember.getMemberAccount())
                .build();
        operationBiz.createOperation(operationInfo, null);
        return userDetails;
    }
}
