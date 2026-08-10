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
package com.cowave.hub.admin.domain.rbac.entity.pto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class TenantManagerPto {

    /**
     * 租户id
     */
    @NotBlank(message = "{admin.tenant.id.null}")
    private String tenantId;

    /**
     * 用户id
     */
    private Integer userId;

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
	private String userPasswd;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
