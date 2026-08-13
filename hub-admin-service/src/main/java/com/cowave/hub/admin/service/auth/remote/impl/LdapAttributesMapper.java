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
package com.cowave.hub.admin.service.auth.remote.impl;

import com.cowave.hub.admin.domain.auth.entity.SysLdap;
import com.cowave.hub.admin.domain.auth.entity.SysLdapUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
public class LdapAttributesMapper implements AttributesMapper<SysLdapUser> {

    private static final Pattern PATTERN_CN = Pattern.compile("CN=([^,]+)");

    private final SysLdap sysLdap;

    @Override
    public SysLdapUser mapFromAttributes(Attributes attributes) throws NamingException {
        SysLdapUser ldapUser = new SysLdapUser();
        NamingEnumeration<? extends Attribute> attributeEnum = attributes.getAll();
        while (attributeEnum.hasMore()) {
            Attribute attribute = attributeEnum.next();
            setUserAccount(attribute, ldapUser);
            setUserName(attribute, ldapUser);
            setUserEmail(attribute, ldapUser);
            setUserPhone(attribute, ldapUser);
            setUserPost(attribute, ldapUser);
            setUserDept(attribute, ldapUser);
            setUserLeader(attribute, ldapUser);
            setUserInfo(attribute, ldapUser);
        }
        return ldapUser;
    }

    private void setUserAccount(Attribute attribute, SysLdapUser ldapUser) throws NamingException {
        if(sysLdap.getAccountProperty().equals(attribute.getID())){
            ldapUser.setUserAccount(attribute.get().toString());
        }
    }

    public void setUserName(Attribute attribute, SysLdapUser ldapUser) throws NamingException {
        if(StringUtils.isBlank(sysLdap.getNameProperty())){
            return;
        }
        if(sysLdap.getNameProperty().equals(attribute.getID())){
            ldapUser.setUserName(attribute.get().toString());
        }
    }

    public void setUserEmail(Attribute attribute, SysLdapUser ldapUser) throws NamingException {
        if(StringUtils.isBlank(sysLdap.getEmailProperty())){
            return;
        }
        if(sysLdap.getEmailProperty().equals(attribute.getID())){
            ldapUser.setUserEmail(attribute.get().toString());
        }
    }

    public void setUserPhone(Attribute attribute, SysLdapUser ldapUser) throws NamingException {
        if(StringUtils.isBlank(sysLdap.getPhoneProperty())){
            return;
        }
        if(sysLdap.getPhoneProperty().equals(attribute.getID())){
            ldapUser.setUserPhone(attribute.get().toString());
        }
    }

    public void setUserPost(Attribute attribute, SysLdapUser ldapUser) throws NamingException {
        if(StringUtils.isBlank(sysLdap.getPostProperty())){
            return;
        }
        if(sysLdap.getPostProperty().equals(attribute.getID())){
            ldapUser.setUserPost(attribute.get().toString());
        }
    }

    public void setUserDept(Attribute attribute, SysLdapUser ldapUser) throws NamingException {
        if(StringUtils.isBlank(sysLdap.getDeptProperty())){
            return;
        }
        if(sysLdap.getDeptProperty().equals(attribute.getID())){
            ldapUser.setUserDept(attribute.get().toString());
        }
    }

    public void setUserLeader(Attribute attribute, SysLdapUser ldapUser) throws NamingException {
        if(StringUtils.isBlank(sysLdap.getLeaderProperty())){
            return;
        }
        if(sysLdap.getLeaderProperty().equals(attribute.getID())){
            String manager = attribute.get().toString();
            Matcher matcher = PATTERN_CN.matcher(manager);
            if (matcher.find()) {
                ldapUser.setUserLeader(matcher.group(1));
            }
        }
    }

    public void setUserInfo(Attribute attribute, SysLdapUser ldapUser) throws NamingException {
        if(StringUtils.isBlank(sysLdap.getInfoProperty())){
            return;
        }
        if(sysLdap.getInfoProperty().equals(attribute.getID())){
            ldapUser.setUserInfo(attribute.get().toString());
        }
    }
}
