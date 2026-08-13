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
package com.cowave.hub.admin.domain.auth.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cowave.hub.admin.domain.auth.entity.SysLdap;
import com.cowave.hub.admin.domain.auth.entity.SysLdapUser;
import com.cowave.hub.admin.domain.auth.repository.facade.SysLdapRepositoryFacade;

/**
 * @author shanhuiming
 */
public interface SysLdapRepository extends SysLdapRepositoryFacade, IService<SysLdap> {

    /**
     * 新增LDAP用户
     */
    void saveLdapUser(SysLdapUser ldapUser);

    /**
     * 更新LDAP用户
     */
    void updateLdapUserById(SysLdapUser ldapUser);
}
