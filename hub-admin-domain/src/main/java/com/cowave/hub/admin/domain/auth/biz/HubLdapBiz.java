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
package com.cowave.hub.admin.domain.auth.biz;

import com.cowave.hub.admin.domain.auth.entity.HubLdap;
import com.cowave.hub.admin.domain.auth.entity.HubLdapUser;

/**
 * HubLdap聚合根Command操作
 *
 * @see HubLdap
 * @see HubLdapUser
 *
 * @author shanhuiming
 */
public interface HubLdapBiz {

    /**
     * 修改LDAP配置
     */
    void editLdap(HubLdap hubLdap);

    /**
     * 新增LDAP用户
     */
    void saveLdapUser(HubLdapUser ldapUser);

    /**
     * 更新LDAP用户
     */
    void updateLdapUserById(HubLdapUser ldapUser);
}
