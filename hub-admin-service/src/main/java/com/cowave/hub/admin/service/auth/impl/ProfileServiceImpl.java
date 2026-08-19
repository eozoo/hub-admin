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

import com.cowave.hub.admin.domain.auth.entity.SysOAuthUser;
import com.cowave.hub.admin.domain.auth.entity.pto.UserProfile;
import com.cowave.hub.admin.domain.auth.entity.command.MfaBind;
import com.cowave.hub.admin.domain.auth.entity.command.PasswdReset;
import com.cowave.hub.admin.domain.auth.entity.command.ProfileUpdate;
import com.cowave.hub.admin.domain.auth.entity.vo.MfaVo;
import com.cowave.hub.admin.domain.auth.enums.AuthType;
import com.cowave.hub.admin.domain.rbac.biz.SysUserBiz;
import com.cowave.hub.admin.domain.member.entity.HubMember;
import com.cowave.hub.admin.domain.rbac.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac.entity.SysUser;
import com.cowave.hub.admin.domain.rbac.enums.UserType;
import com.cowave.hub.admin.domain.member.repository.facade.HubMemberRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.domain.sys.entity.SysAttach;
import com.cowave.hub.admin.domain.auth.repository.facade.SysOAuthRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysTenantRepositoryFacade;
import com.cowave.hub.admin.domain.sys.biz.SysAttachBiz;
import com.cowave.hub.admin.service.auth.ProfileService;
import com.cowave.hub.admin.service.auth.support.MfaAuthVerifier;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.cowave.hub.admin.domain.sys.enums.AttachType.AVATAR;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_USER;
import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {
    private final PasswordEncoder passwordEncoder;
    private final SysUserBiz userBiz;
    private final SysAttachBiz attachBiz;
    private final SysUserRepositoryFacade userRepositoryFacade;
    private final SysOAuthRepositoryFacade oauthRepositoryFacade;
    private final SysTenantRepositoryFacade tenantRepositoryFacade;
    private final HubMemberRepositoryFacade memberRepositoryFacade;

    @Override
    public UserProfile info() throws Exception {
        AccessUserDetails userDetails = Access.userDetails();
        String tenantId = userDetails.getTenantId();
        Integer userId = userDetails.getUserId();
        String userCode = userDetails.getUserCode();
        // member令牌
        if (AuthType.MEMBER.getVal().equals(userDetails.getAuthType())) {
            // member信息
            HubMember hubMember = memberRepositoryFacade.queryByCode(userCode);
            UserProfile memberProfile = new UserProfile();
            memberProfile.setUserId(hubMember.getMemberId());
            memberProfile.setUserCode(hubMember.getMemberCode());
            memberProfile.setUserType(hubMember.getMemberType().getVal());
            memberProfile.setUserName(hubMember.getMemberName());
            memberProfile.setUserAccount(hubMember.getMemberAccount());
            memberProfile.setUserEmail(hubMember.getMemberEmail());
            memberProfile.setAvatar(hubMember.getMemberAvatar());
            // 租户信息
            SysTenant sysTenant = tenantRepositoryFacade.queryById(tenantId);
            memberProfile.setTenantId(sysTenant.getTenantId());
            memberProfile.setTenantName(sysTenant.getTenantName());
            return memberProfile;
        }

        UserProfile userProfile = userRepositoryFacade.queryUserProfile(userId);
        // Avatar
        if (UserType.GITLAB.equalsType(userCode)) {
            SysOAuthUser oauthUser =
                    oauthRepositoryFacade.queryUserByAccount(tenantId, UserType.GITLAB.getVal(), userDetails.getUsername());
            userProfile.setAvatar(oauthUser.getUserAvatar());
        } else if (UserType.SYS.equalsType(userCode)) {
            SysAttach avatar = attachBiz.previewLatestByOwner(String.valueOf(userId), SYSTEM_USER, AVATAR);
            if (avatar != null) {
                userProfile.setAvatar(avatar.getViewUrl());
            }
        }
        // 租户信息
        SysTenant sysTenant = tenantRepositoryFacade.queryById(tenantId);
        userProfile.setTenantId(sysTenant.getTenantId());
        userProfile.setTenantName(sysTenant.getTenantName());
        return userProfile;
    }

    @Override
    public void edit(ProfileUpdate profile) throws Exception {
        Integer userId = Access.userId();
        userBiz.updateProfile(userId, profile);
        attachBiz.reserveByOwner(String.valueOf(userId), SYSTEM_USER, AVATAR, 3);
    }

    @Override
    public void resetPasswd(PasswdReset passwdReset) {
        String userCode = Access.userCode();
        String passwd = userRepositoryFacade.queryByCode(userCode).getUserPasswd();
        HttpAsserts.isTrue(passwordEncoder.matches(passwdReset.getOldPasswd(), passwd), BAD_REQUEST, "{admin.user.passwd.failed}");
        HttpAsserts.isFalse(passwordEncoder.matches(passwdReset.getNewPasswd(), passwd), BAD_REQUEST, "{admin.user.passwd.repeat}");
        userBiz.changePasswd(Access.userId(), passwordEncoder.encode(passwdReset.getNewPasswd()));
    }

    @Override
    public MfaVo generateMfa() {
        MfaVo mfaVo = new MfaVo();
        SysUser sysUser = userRepositoryFacade.queryByCode(Access.userCode());
        if (sysUser != null) {
            String mfaKey = sysUser.getMfa();
            if (StringUtils.isBlank(mfaKey)) {
                mfaKey = MfaAuthVerifier.generateKey();
                String mfaUrl = MfaAuthVerifier.generateAuthUrl(Access.tenantId(), Access.userAccount(), mfaKey);
                mfaVo.setMfaUrl(mfaUrl);
            }
            mfaVo.setMfaKey(mfaKey);
        }
        return mfaVo;
    }

    @Override
    public void enableMfa(MfaBind mfaBind) {
        HttpAsserts.isTrue(MfaAuthVerifier.validateCode(
                mfaBind.getMfaKey(), mfaBind.getMfaCode()), BAD_REQUEST, "{admin.mfa.code.invalid}");
        userBiz.enableMfa(Access.userId(), mfaBind.getMfaKey());
    }

    @Override
    public void disableMfa(MfaBind mfaBind) {
        HttpAsserts.isTrue(MfaAuthVerifier.validateCode(
                mfaBind.getMfaKey(), mfaBind.getMfaCode()), BAD_REQUEST, "{admin.mfa.code.invalid}");
        userBiz.disableMfa(Access.userId());
    }
}
