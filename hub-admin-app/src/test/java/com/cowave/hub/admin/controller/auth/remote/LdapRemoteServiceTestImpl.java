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

import com.cowave.hub.admin.domain.auth.entity.HubLdap;
import com.cowave.hub.admin.domain.auth.entity.HubLdapUser;
import com.cowave.hub.admin.service.auth.remote.LdapRemoteService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shanhuiming
 */
@Primary
@Component
public class LdapRemoteServiceTestImpl implements LdapRemoteService {

    @Override
    public boolean authenticate(HubLdap config, String filter, String password) {
        return true;
    }

    @Override
    public List<HubLdapUser> searchUser(HubLdap config, String filter) {
        HubLdapUser user = new HubLdapUser();
        user.setUserAccount("ldaptest");
        user.setUserName("LDAP测试用户");
        user.setUserEmail("ldaptest@cowave.com");
        user.setUserPhone("13288888888");
        user.setUserDept("研发部门");
        user.setUserPost("Java工程师");
        return List.of(user);
    }
}
