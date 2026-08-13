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

import com.cowave.zoo.tools.Collections;
import com.cowave.hub.admin.domain.rbac.entity.SysUserRole;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class UserRoleUpdate {

    /**
     * 用户id
     */
    @NotNull(message = "{admin.user.id.null}")
    private Integer userId;

    /**
     * 用户名称
     */
    @NotBlank(message = "{admin.user.name.null}")
    private String userName;

    /**
     * 角色id列表
     */
    @NotEmpty(message = "{admin.role.ids.null}")
    private List<Integer> roleIds;

    public List<SysUserRole> getUserRoles(){
        return Collections.copyToList(roleIds, v -> new SysUserRole(userId, v));
    }
}
