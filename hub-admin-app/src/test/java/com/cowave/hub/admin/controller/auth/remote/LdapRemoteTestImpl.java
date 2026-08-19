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
package com.cowave.hub.admin.controller.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.SysLdap;
import com.cowave.hub.admin.domain.auth.entity.SysLdapUser;
import com.cowave.hub.admin.domain.auth.remote.LdapRemote;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shanhuiming
 */
@Primary
@Component
public class LdapRemoteTestImpl implements LdapRemote {

    @Override
    public boolean authenticate(SysLdap config, String filter, String password) {
        return true;
    }

    @Override
    public List<SysLdapUser> searchUser(SysLdap config, String filter) {
        SysLdapUser user = new SysLdapUser();
        user.setUserAccount("ldaptest");
        user.setUserName("LDAP测试用户");
        user.setUserEmail("ldaptest@cowave.com");
        user.setUserPhone("13288888888");
        user.setUserDept("研发部门");
        user.setUserPost("Java工程师");
        return List.of(user);
    }
}
