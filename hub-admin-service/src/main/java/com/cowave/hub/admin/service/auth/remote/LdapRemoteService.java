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
package com.cowave.hub.admin.service.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.HubLdap;
import com.cowave.hub.admin.domain.auth.entity.HubLdapUser;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface LdapRemoteService {

    /**
     * LDAP 认证
     *
     * @param config   LDAP 配置
     * @param filter   查询过滤器
     * @param password 用户密码
     * @return 认证是否通过
     */
    boolean authenticate(HubLdap config, String filter, String password);

    /**
     * 搜索 LDAP 用户
     *
     * @param config LDAP 配置
     * @param filter 查询过滤器
     * @return 用户属性列表
     */
    List<HubLdapUser> searchUser(HubLdap config, String filter);
}
