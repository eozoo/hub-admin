package com.cowave.hub.admin.domain.auth.repository.facade;

import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.SysAuthPasswd;
import com.cowave.hub.admin.domain.auth.entity.SysAuthIdentity;
import com.cowave.hub.admin.domain.auth.entity.SysAuthLdap;

import java.util.List;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.auth.enums.ProviderCode;

/**
 * @author shanhuiming
 */
public interface SysAuthRepositoryFacade {

    /**
     * LDAP配置
     */
    SysAuthLdap queryLdap();

    /**
     * LDAP用户列表
     */
    Page<SysAuthIdentity> queryLdapUsers(String userAccount);

    /**
     * 查询已启用的OAuth提供方
     */
    List<SysAuthProvider> queryEnabledOauthProviders();

    /**
     * OAuth提供方，包含停用配置
     */
    List<SysAuthProvider> queryOauthProviders();

    /**
     * 获取账号绑定信息
     */
    List<SysAuthIdentity> queryUserProviderIdentities(Integer userId);

    /**
     * 查询用户当前有效密码
     */
    SysAuthPasswd queryCurrentPasswd(Integer userId);

    /**
     * 查询启用的 LDAP 配置
     */
    SysAuthLdap queryEnabledLdap();

    /**
     * 按 LDAP 配置和对象标识查询身份
     */
    SysAuthIdentity queryLdapIdentity(Integer ldapId, String externalSubject);

    /**
     * 查询用户LDAP身份
     */
    SysAuthIdentity queryUserLdapIdentity(Integer ldapId, Integer userId);

    /**
     * 按提供方编码查询配置
     */
    SysAuthProvider queryOauthProviderByCode(ProviderCode providerCode);

    /**
     * 按提供方ID查询OAuth配置
     */
    SysAuthProvider queryOauthProviderById(Integer providerId);

    /**
     * 查询提供方OAuth身份列表
     */
    Page<SysAuthIdentity> queryProviderUsers(Integer providerId, String userAccount);

    /**
     * 按提供方和身份标识查询
     */
    SysAuthIdentity queryProviderIdentity(Integer providerId, String externalSubject);

    /**
     * 按id查询外部身份
     */
    SysAuthIdentity queryIdentityById(Long identityId);

}
