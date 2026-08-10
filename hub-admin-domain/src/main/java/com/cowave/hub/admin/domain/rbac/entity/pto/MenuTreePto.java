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

import com.cowave.hub.admin.domain.rbac.entity.HubMenu;
import com.cowave.hub.admin.domain.rbac.entity.HubScope;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class MenuTreePto extends HubMenu {

    /**
     * 数据权限id
     */
    private Integer scopeId;

    /**
     * 数据权限列表
     */
    private List<HubScope> scopes;
}
