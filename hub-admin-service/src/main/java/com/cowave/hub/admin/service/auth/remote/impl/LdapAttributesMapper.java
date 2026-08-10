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

import com.cowave.hub.admin.domain.auth.entity.HubLdap;
import com.cowave.hub.admin.domain.auth.entity.HubLdapUser;
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
public class LdapAttributesMapper implements AttributesMapper<HubLdapUser> {

    private static final Pattern PATTERN_CN = Pattern.compile("CN=([^,]+)");

    private final HubLdap hubLdap;

    @Override
    public HubLdapUser mapFromAttributes(Attributes attributes) throws NamingException {
        HubLdapUser hubLdapUser = new HubLdapUser();
        NamingEnumeration<? extends Attribute> attributeEnum = attributes.getAll();
        while (attributeEnum.hasMore()) {
            Attribute attribute = attributeEnum.next();
            setUserAccount(attribute, hubLdapUser);
            setUserName(attribute, hubLdapUser);
            setUserEmail(attribute, hubLdapUser);
            setUserPhone(attribute, hubLdapUser);
            setUserPost(attribute, hubLdapUser);
            setUserDept(attribute, hubLdapUser);
            setUserLeader(attribute, hubLdapUser);
            setUserInfo(attribute, hubLdapUser);
        }
        return hubLdapUser;
    }

    private void setUserAccount(Attribute attribute, HubLdapUser hubLdapUser) throws NamingException {
        if(hubLdap.getAccountProperty().equals(attribute.getID())){
            hubLdapUser.setUserAccount(attribute.get().toString());
        }
    }

    public void setUserName(Attribute attribute, HubLdapUser hubLdapUser) throws NamingException {
        if(StringUtils.isBlank(hubLdap.getNameProperty())){
            return;
        }
        if(hubLdap.getNameProperty().equals(attribute.getID())){
            hubLdapUser.setUserName(attribute.get().toString());
        }
    }

    public void setUserEmail(Attribute attribute, HubLdapUser hubLdapUser) throws NamingException {
        if(StringUtils.isBlank(hubLdap.getEmailProperty())){
            return;
        }
        if(hubLdap.getEmailProperty().equals(attribute.getID())){
            hubLdapUser.setUserEmail(attribute.get().toString());
        }
    }

    public void setUserPhone(Attribute attribute, HubLdapUser hubLdapUser) throws NamingException {
        if(StringUtils.isBlank(hubLdap.getPhoneProperty())){
            return;
        }
        if(hubLdap.getPhoneProperty().equals(attribute.getID())){
            hubLdapUser.setUserPhone(attribute.get().toString());
        }
    }

    public void setUserPost(Attribute attribute, HubLdapUser hubLdapUser) throws NamingException {
        if(StringUtils.isBlank(hubLdap.getPostProperty())){
            return;
        }
        if(hubLdap.getPostProperty().equals(attribute.getID())){
            hubLdapUser.setUserPost(attribute.get().toString());
        }
    }

    public void setUserDept(Attribute attribute, HubLdapUser hubLdapUser) throws NamingException {
        if(StringUtils.isBlank(hubLdap.getDeptProperty())){
            return;
        }
        if(hubLdap.getDeptProperty().equals(attribute.getID())){
            hubLdapUser.setUserDept(attribute.get().toString());
        }
    }

    public void setUserLeader(Attribute attribute, HubLdapUser hubLdapUser) throws NamingException {
        if(StringUtils.isBlank(hubLdap.getLeaderProperty())){
            return;
        }
        if(hubLdap.getLeaderProperty().equals(attribute.getID())){
            String manager = attribute.get().toString();
            Matcher matcher = PATTERN_CN.matcher(manager);
            if (matcher.find()) {
                hubLdapUser.setUserLeader(matcher.group(1));
            }
        }
    }

    public void setUserInfo(Attribute attribute, HubLdapUser hubLdapUser) throws NamingException {
        if(StringUtils.isBlank(hubLdap.getInfoProperty())){
            return;
        }
        if(hubLdap.getInfoProperty().equals(attribute.getID())){
            hubLdapUser.setUserInfo(attribute.get().toString());
        }
    }
}
