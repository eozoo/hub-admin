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
package com.cowave.hub.admin.domain.rbac2.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 部门关系
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysDeptDiagram {

    /**
     * 上级部门id
     */
    private Integer parentId;

    /**
     * 部门id
     */
    private Integer deptId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 部门关系类型，默认行政隶属
     */
    private String relationType;
}
