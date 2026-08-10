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
package com.cowave.hub.admin.domain.auth.entity.bo;

import com.cowave.hub.admin.domain.auth.entity.HubOAuthUser;
import com.cowave.hub.admin.domain.rbac.enums.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class GitlabUser {

    /**
     * 用户名称
     */
    private String name;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户头像
     */
    @JsonProperty("avatar_url")
    private String avatarUrl;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * Ldap信息
     */
    private List<LdapInfo> identities;

    @Getter
    @Setter
    public static class LdapInfo {

        private String provider;

        @JsonProperty("extern_uid")
        private String externUid;
    }

    public static HubOAuthUser oAuthUser(GitlabUser gitlabUser){
        HubOAuthUser oauthUserHub = new HubOAuthUser();
        oauthUserHub.setServerType(UserType.GITLAB.getVal());
        oauthUserHub.setUserName(gitlabUser.name);
        oauthUserHub.setUserAccount(gitlabUser.username);
        oauthUserHub.setUserEmail(gitlabUser.email);
        oauthUserHub.setUserAvatar(gitlabUser.avatarUrl);
        if(CollectionUtils.isNotEmpty(gitlabUser.identities)){
            LdapInfo ldap = gitlabUser.identities.get(0);
            oauthUserHub.setUserDept(ldap.externUid);
        }
        return oauthUserHub;
    }
}
