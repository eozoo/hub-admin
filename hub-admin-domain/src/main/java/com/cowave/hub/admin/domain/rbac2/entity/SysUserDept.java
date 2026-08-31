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
 * 用户部门
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysUserDept {

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 部门id
     */
    private Integer deptId;

    /**
     * 岗位id
     */
    private Integer postId;

    /**
     * 是否用户主部门
     */
    private Integer isPrimary;

    /**
     * 是否部门负责人
     */
    private Integer isLeader;
}
