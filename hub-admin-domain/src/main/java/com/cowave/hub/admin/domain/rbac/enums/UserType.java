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
package com.cowave.hub.admin.domain.rbac.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.cowave.zoo.tools.EnumVal;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author shanhuiming
 */
@Getter
@RequiredArgsConstructor
public enum UserType implements EnumVal<String> {

    /**
     * 系统用户
     */
    SYS("sys"),

    /**
     * Ldap用户
     */
    LDAP("ldap"),

    /**
     * Gitlab用户
     */
    GITLAB("gitlab");

    @EnumValue
    @JsonValue
    private final String val;

    private final Pattern pattern = Pattern.compile("^[^-]+-([^-]+)-");

    public String newCode(String tenantId, String userAccount) {
        return tenantId + "-" + val + "-" + userAccount;
    }

    public boolean equalsType(String userCode){
        Matcher matcher = pattern.matcher(userCode);
        if (matcher.find()) {
            return val.equals(matcher.group(1));
        }
        return false;
    }
}
