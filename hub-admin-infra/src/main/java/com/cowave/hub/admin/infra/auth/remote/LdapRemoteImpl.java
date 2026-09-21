/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.SysAuthIdentity;
import com.cowave.hub.admin.domain.auth.entity.SysAuthLdap;
import com.cowave.hub.admin.domain.auth.remote.LdapRemote;
import com.cowave.zoo.http.client.asserts.HttpException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DirContextAuthenticationStrategy;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.stereotype.Component;

import javax.naming.directory.SearchControls;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class LdapRemoteImpl implements LdapRemote {

    private final ObjectProvider<DirContextAuthenticationStrategy> dirContextAuthenticationStrategy;

    @Override
    public void validConfig(SysAuthLdap config) {
        getLdapTemplate(config).authenticate("", "(objectClass=*)", config.getLdapPasswd());
    }

    @Override
    public boolean authenticate(SysAuthLdap config, String userAccount, String password) {
        LdapTemplate ldapTemplate = getLdapTemplate(config);
        return ldapTemplate.authenticate("", filter(config, userAccount), password);
    }

    @Override
    public List<SysAuthIdentity> searchUser(SysAuthLdap config, String userAccount) {
        LdapTemplate ldapTemplate = getLdapTemplate(config);
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(Stream.of(config.getAccountProperty(), config.getSubjectProperty(),
                        config.getNameProperty(), config.getEmailProperty(), config.getPhoneProperty(),
                        config.getPostProperty(), config.getDeptProperty(), config.getLeaderProperty(),
                        config.getInfoProperty())
                .filter(StringUtils::isNotBlank)
                .distinct()
                .toArray(String[]::new));
        return ldapTemplate.search(StringUtils.defaultString(config.getUserDn()), filter(config, userAccount),
                controls, new LdapAttributesMapper(config));
    }

    private String filter(SysAuthLdap config, String userAccount) {
        String encoded = userAccount.replace("\\", "\\5c").replace("*", "\\2a")
                .replace("(", "\\28").replace(")", "\\29").replace("\u0000", "\\00");
        return "(&(objectClass=" + config.getUserClass() + ")(" + config.getAccountProperty() + "=" + encoded + "))";
    }

    private LdapTemplate getLdapTemplate(SysAuthLdap config) {
        LdapContextSource source = new LdapContextSource();
        dirContextAuthenticationStrategy.ifUnique(source::setAuthenticationStrategy);
        PropertyMapper propertyMapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        try {
            propertyMapper.from(config.getLdapUser()).to(source::setUserDn);
            propertyMapper.from(config.getLdapPasswd()).to(source::setPassword);
            propertyMapper.from(Integer.valueOf(1).equals(config.getReadonly())).to(source::setAnonymousReadOnly);
            propertyMapper.from(config.getBaseDn()).to(source::setBase);
            propertyMapper.from(new String[]{config.getLdapUrl()}).to(source::setUrls);
            propertyMapper.from(config.getEnvironment()).to(
                    environment -> source.setBaseEnvironmentProperties(Collections.unmodifiableMap(environment)));
            source.afterPropertiesSet();
        } catch (Exception e) {
            throw new HttpException(e, BAD_REQUEST, "{admin.ldap.invalid}");
        }
        return new LdapTemplate(source);
    }
}
