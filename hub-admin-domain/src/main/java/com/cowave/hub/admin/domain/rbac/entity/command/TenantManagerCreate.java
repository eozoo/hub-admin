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
package com.cowave.hub.admin.domain.rbac.entity.command;

import com.cowave.hub.admin.domain.rbac.enums.UserType;
import com.cowave.hub.admin.domain.rbac.entity.HubUser;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class TenantManagerCreate {

    /**
     * 租户id
     */
    @NotBlank(message = "{admin.tenant.id.null}")
    private String tenantId;

    /**
     * 用户名称
     */
    @NotBlank(message = "{admin.user.name.null}")
    private String userName;

    /**
     * 用户账号
     */
    @NotBlank(message = "{admin.user.account.null}")
    private String userAccount;

    /**
     * 用户密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "{admin.user.passwd.null}")
    private String userPasswd;

    public HubUser newSysUser() {
        HubUser hubUser = new HubUser();
        hubUser.setUserType(UserType.SYS);
        hubUser.setUserCode(UserType.SYS.newCode(tenantId, userAccount));
        hubUser.setTenantId(tenantId);
        hubUser.setUserName(userName);
        hubUser.setUserAccount(userAccount);
        return hubUser;
    }
}
