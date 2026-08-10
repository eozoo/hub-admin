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
package com.cowave.hub.admin.domain.auth.entity.command;

import com.cowave.zoo.framework.access.annotation.Sensitive;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class LdapLogin {

    /**
     * 租户id
     */
    @NotBlank(message = "{admin.tenant.id.null}")
    private String tenantId;

    /**
     * 用户名
     */
	@NotBlank(message = "{admin.user.account.null}")
    private String userAccount;

    /**
     * 用户密码
     */
    @Sensitive
    @NotBlank(message = "{admin.user.passwd.null}")
    private String passWord;
}
