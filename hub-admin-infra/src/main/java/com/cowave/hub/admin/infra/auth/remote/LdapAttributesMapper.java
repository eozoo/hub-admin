package com.cowave.hub.admin.infra.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.SysAuthIdentity;
import com.cowave.hub.admin.domain.auth.entity.SysAuthLdap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
public class LdapAttributesMapper implements AttributesMapper<SysAuthIdentity> {

    private static final Pattern PATTERN_CN = Pattern.compile("CN=([^,]+)");

    private final SysAuthLdap config;

    @Override
    public SysAuthIdentity mapFromAttributes(Attributes attributes) throws NamingException {
        SysAuthIdentity ldapUser = new SysAuthIdentity();
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
        ldapUser.setExternalSubject(subject(attributes));
        return ldapUser;
    }

    /**
     * 读取 LDAP 目录对象的稳定标识，不使用可变的账号或 DN 兜底
     */
    private String subject(Attributes attributes) throws NamingException {
        if (StringUtils.isBlank(config.getSubjectProperty())) {
            throw new NamingException("LDAP subject property is not configured");
        }
        Attribute attribute = attributes.get(config.getSubjectProperty());
        if (attribute == null || attribute.get() == null) {
            throw new NamingException("LDAP subject attribute is missing: " + config.getSubjectProperty());
        }
        Object value = attribute.get();
        if ("objectGUID".equalsIgnoreCase(config.getSubjectProperty())) {
            if (!(value instanceof byte[] bytes) || bytes.length != 16) {
                throw new NamingException("Invalid LDAP objectGUID");
            }
            return objectGuid(bytes);
        }
        if (!(value instanceof String subject) || StringUtils.isBlank(subject)) {
            throw new NamingException("Invalid LDAP subject: " + config.getSubjectProperty());
        }
        return subject.toLowerCase(Locale.ROOT);
    }

    /**
     * 将 AD objectGUID 的混合字节序转换为标准 UUID 字符串
     */
    private String objectGuid(byte[] bytes) {
        ByteBuffer first = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        long mostSignificantBits = (Integer.toUnsignedLong(first.getInt()) << 32)
                | (Short.toUnsignedLong(first.getShort()) << 16)
                | Short.toUnsignedLong(first.getShort());
        long leastSignificantBits = ByteBuffer.wrap(bytes, 8, 8).getLong();
        return new UUID(mostSignificantBits, leastSignificantBits).toString();
    }

    private void setUserAccount(Attribute attribute, SysAuthIdentity ldapUser) throws NamingException {
        if (config.getAccountProperty().equals(attribute.getID())) {
            ldapUser.setUserAccount(attribute.get().toString());
        }
    }

    private void setUserName(Attribute attribute, SysAuthIdentity ldapUser) throws NamingException {
        if (StringUtils.isNotBlank(config.getNameProperty()) && config.getNameProperty().equals(attribute.getID())) {
            ldapUser.setUserName(attribute.get().toString());
        }
    }

    private void setUserEmail(Attribute attribute, SysAuthIdentity ldapUser) throws NamingException {
        if (StringUtils.isNotBlank(config.getEmailProperty()) && config.getEmailProperty().equals(attribute.getID())) {
            ldapUser.setUserEmail(attribute.get().toString());
        }
    }

    private void setUserPhone(Attribute attribute, SysAuthIdentity ldapUser) throws NamingException {
        if (StringUtils.isNotBlank(config.getPhoneProperty()) && config.getPhoneProperty().equals(attribute.getID())) {
            ldapUser.setUserPhone(attribute.get().toString());
        }
    }

    private void setUserPost(Attribute attribute, SysAuthIdentity ldapUser) throws NamingException {
        if (StringUtils.isNotBlank(config.getPostProperty()) && config.getPostProperty().equals(attribute.getID())) {
            ldapUser.setUserPost(attribute.get().toString());
        }
    }

    private void setUserDept(Attribute attribute, SysAuthIdentity ldapUser) throws NamingException {
        if (StringUtils.isNotBlank(config.getDeptProperty()) && config.getDeptProperty().equals(attribute.getID())) {
            ldapUser.setUserDept(attribute.get().toString());
        }
    }

    private void setUserLeader(Attribute attribute, SysAuthIdentity ldapUser) throws NamingException {
        if (StringUtils.isNotBlank(config.getLeaderProperty()) && config.getLeaderProperty().equals(attribute.getID())) {
            String manager = attribute.get().toString();
            Matcher matcher = PATTERN_CN.matcher(manager);
            if (matcher.find()) {
                ldapUser.setUserLeader(matcher.group(1));
            }
        }
    }

    private void setUserInfo(Attribute attribute, SysAuthIdentity ldapUser) throws NamingException {
        if (StringUtils.isNotBlank(config.getInfoProperty()) && config.getInfoProperty().equals(attribute.getID())) {
            ldapUser.setIdentityInfo(Map.of("userInfo", attribute.get().toString()));
        }
    }
}
