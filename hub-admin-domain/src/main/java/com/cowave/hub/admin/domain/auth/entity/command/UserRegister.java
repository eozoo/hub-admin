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

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class UserRegister {

    /**
     * 租户id
     */
    @NotBlank(message = "{admin.tenant.id.null}")
    private String tenantId;

    /**
     * 验证码
     */
    @NotBlank(message = "{admin.captcha.failed}")
    private String captcha;

    /**
     * 用户邮箱
     */
    @NotBlank(message = "{admin.user.email.null}")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "{admin.user.email.invalid}")
    private String userEmail;

    /**
     * 用户账号
     */
    @NotBlank(message = "{admin.user.account.null}")
    private String userAccount;

    /**
     * 用户名称
     */
    @NotBlank(message = "{admin.user.name.null}")
    private String userName;
}
