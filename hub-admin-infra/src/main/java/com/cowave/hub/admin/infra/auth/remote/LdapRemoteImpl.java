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
package com.cowave.hub.admin.infra.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.SysLdap;
import com.cowave.hub.admin.domain.auth.entity.SysLdapUser;
import com.cowave.hub.admin.domain.auth.remote.LdapRemote;
import com.cowave.zoo.http.client.asserts.HttpException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DirContextAuthenticationStrategy;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Component;

import javax.naming.directory.SearchControls;
import java.util.Collections;
import java.util.List;

import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class LdapRemoteImpl implements LdapRemote {

    private final ObjectProvider<DirContextAuthenticationStrategy> dirContextAuthenticationStrategy;

    @Override
    public boolean authenticate(SysLdap config, String filter, String password) {
        LdapTemplate ldapTemplate = getLdapTemplate(config);
        return ldapTemplate.authenticate("", filter, password);
    }

    @Override
    public List<SysLdapUser> searchUser(SysLdap config, String filter) {
        LdapTemplate ldapTemplate = getLdapTemplate(config);
        return ldapTemplate.search(config.getUserDn(), filter,
                SearchControls.SUBTREE_SCOPE, new LdapAttributesMapper(config));
    }

    private LdapTemplate getLdapTemplate(SysLdap sysLdap) {
        LdapContextSource source = new LdapContextSource();
        dirContextAuthenticationStrategy.ifUnique(source::setAuthenticationStrategy);
        PropertyMapper propertyMapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        try {
            propertyMapper.from(sysLdap.getLdapUser()).to(source::setUserDn);
            propertyMapper.from(sysLdap.getLdapPasswd()).to(source::setPassword);
            propertyMapper.from(sysLdap.anonymousReadOnly()).to(source::setAnonymousReadOnly);
            propertyMapper.from(sysLdap.getBaseDn()).to(source::setBase);
            propertyMapper.from(sysLdap.determineUrls()).to(source::setUrls);
            propertyMapper.from(sysLdap.getEnvironment()).to(
                    baseEnvironment -> source.setBaseEnvironmentProperties(Collections.unmodifiableMap(baseEnvironment)));
            source.afterPropertiesSet();
        } catch (Exception e) {
            throw new HttpException(e, BAD_REQUEST, "{admin.ldap.invalid}");
        }
        return new LdapTemplate(source);
    }
}
