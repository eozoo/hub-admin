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
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.hub.admin.domain.auth.entity.SysLdap;
import com.cowave.hub.admin.domain.auth.entity.SysLdapUser;

/**
 * @author shanhuiming
 */
public interface LdapService {

    /**
     * Ldap认证
     */
    AccessUserDetails authenticate(String tenantId, String userAccount, String passWord);

    /**
     * 获取配置
     */
    SysLdap getLdap(String tenantId);

    /**
     * 修改配置
     */
    void editLdap(String tenantId, SysLdap sysLdap);

    /**
     * 测试配置
     */
    void validConfig(SysLdap sysLdap);

    /**
     * 用户列表
     */
    Page<SysLdapUser> listUser(String tenantId, String ldapAccount);
}
