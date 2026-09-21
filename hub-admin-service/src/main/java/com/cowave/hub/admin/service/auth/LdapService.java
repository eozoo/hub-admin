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
package com.cowave.hub.admin.service.auth;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;
import com.cowave.hub.admin.domain.auth.entity.vo.LoginVo;
import com.cowave.hub.admin.domain.auth.entity.command.AccountBind;
import com.cowave.hub.admin.domain.auth.entity.SysAuthLdap;
import com.cowave.hub.admin.domain.auth.entity.vo.LdapUserVo;

/**
 * @author shanhuiming
 */
public interface LdapService {

    /**
     * Ldap认证
     */
    LoginVo authenticate(String userAccount, String passWord);

    /**
     * 绑定本地账号
     */
    LoginVo bindAccount(AccountBind command);

    /**
     * 获取配置
     */
    SysAuthLdap getLdap();

    /**
     * 修改配置
     */
    void editLdap(SysAuthLdap config);

    /**
     * 测试配置
     */
    void validConfig(SysAuthLdap config);

    /**
     * 用户列表
     */
    Page<LdapUserVo> listUser(String ldapAccount);

    /**
     * 修改LDAP身份状态
     */
    void updateIdentityStatus(Long identityId, EnableStatus status);

    /**
     * 删除LDAP身份绑定
     */
    void deleteIdentity(Long identityId);
}
