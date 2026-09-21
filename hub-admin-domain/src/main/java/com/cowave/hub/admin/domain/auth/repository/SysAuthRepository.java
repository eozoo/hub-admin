package com.cowave.hub.admin.domain.auth.repository;

import com.cowave.hub.admin.domain.auth.entity.SysAuthPasswd;
import com.cowave.hub.admin.domain.auth.entity.SysAuthIdentity;
import com.cowave.hub.admin.domain.auth.entity.SysAuthLdap;
import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;
import com.cowave.hub.admin.domain.auth.repository.facade.SysAuthRepositoryFacade;

/**
 * @author shanhuiming
 */
public interface SysAuthRepository extends SysAuthRepositoryFacade {

    /**
     * 保存OAuth提供方配置
     */
    void saveOauthProvider(SysAuthProvider provider);

    /**
     * 保存LDAP配置
     */
    void saveLdap(SysAuthLdap config);

    /**
     * 保存初始密码
     */
    void createPasswd(SysAuthPasswd passwd);

    /**
     * 失效当前密码并保存新密码记录
     */
    void changePasswd(Integer userId, String encodedPasswd, String userAccount);

    /**
     * 保存外部身份
     */
    void createExternalIdentity(SysAuthIdentity identity);

    /**
     * 更新LDAP身份
     */
    void updateLdapIdentity(SysAuthIdentity identity);

    /**
     * 更新OAuth2身份
     */
    void updateProviderIdentity(SysAuthIdentity identity);

    /**
     * 修改指定提供方的身份状态
     */
    void updateProviderIdentityStatus(Integer providerId, Long identityId, EnableStatus status);

    /**
     * 删除指定提供方的身份绑定
     */
    void deleteProviderIdentity(Integer providerId, Long identityId);

    /**
     * 修改LDAP身份状态
     */
    void updateLdapIdentityStatus(Integer ldapId, Long identityId, EnableStatus status);

    /**
     * 删除LDAP身份绑定
     */
    void deleteLdapIdentity(Integer ldapId, Long identityId);
}
