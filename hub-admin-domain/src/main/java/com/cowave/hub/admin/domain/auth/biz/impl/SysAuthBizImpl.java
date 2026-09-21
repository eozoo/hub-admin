package com.cowave.hub.admin.domain.auth.biz.impl;

import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;

import com.cowave.hub.admin.domain.auth.biz.SysAuthBiz;
import com.cowave.hub.admin.domain.auth.entity.SysAuthPasswd;
import com.cowave.hub.admin.domain.auth.entity.SysAuthIdentity;
import com.cowave.hub.admin.domain.auth.entity.SysAuthLdap;
import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.repository.SysAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class SysAuthBizImpl implements SysAuthBiz {
    private final SysAuthRepository authRepository;

    @Override
    public void saveOauthProvider(SysAuthProvider provider) {
        authRepository.saveOauthProvider(provider);
    }

    @Override
    public void saveLdap(SysAuthLdap config) {
        authRepository.saveLdap(config);
    }

    @Override
    public void createPasswd(SysAuthPasswd passwd) {
        authRepository.createPasswd(passwd);
    }

    @Override
    public void changePasswd(Integer userId, String encodedPasswd, String userAccount) {
        authRepository.changePasswd(userId, encodedPasswd, userAccount);
    }

    @Override
    public void createExternalIdentity(SysAuthIdentity identity) {
        authRepository.createExternalIdentity(identity);
    }

    @Override
    public void updateLdapIdentity(SysAuthIdentity identity) {
        authRepository.updateLdapIdentity(identity);
    }

    @Override
    public void updateProviderIdentity(SysAuthIdentity identity) {
        authRepository.updateProviderIdentity(identity);
    }

    @Override
    public void updateProviderIdentityStatus(Integer providerId, Long identityId, EnableStatus status) {
        authRepository.updateProviderIdentityStatus(providerId, identityId, status);
    }

    @Override
    public void deleteProviderIdentity(Integer providerId, Long identityId) {
        authRepository.deleteProviderIdentity(providerId, identityId);
    }

    @Override
    public void updateLdapIdentityStatus(Integer ldapId, Long identityId, EnableStatus status) {
        authRepository.updateLdapIdentityStatus(ldapId, identityId, status);
    }

    @Override
    public void deleteLdapIdentity(Integer ldapId, Long identityId) {
        authRepository.deleteLdapIdentity(ldapId, identityId);
    }
}
