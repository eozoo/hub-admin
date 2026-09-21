package com.cowave.hub.admin.domain.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.SysAuthIdentity;
import com.cowave.hub.admin.domain.auth.entity.SysAuthLdap;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface LdapRemote {

    /**
     * 验证LDAP配置
     */
    void validConfig(SysAuthLdap config);

    /**
     * 验证用户密码
     */
    boolean authenticate(SysAuthLdap config, String userAccount, String password);

    /**
     * 查询LDAP用户
     */
    List<SysAuthIdentity> searchUser(SysAuthLdap config, String userAccount);
}
