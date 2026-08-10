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
package com.cowave.hub.admin.domain.rbac.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * 角色应用菜单
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HubRoleAppMenu {

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 应用id
     */
    private Integer appId;

    /**
     * 菜单id
     */
    private Integer menuId;
}
