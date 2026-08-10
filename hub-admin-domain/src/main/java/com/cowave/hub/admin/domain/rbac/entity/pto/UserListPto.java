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

import com.cowave.hub.admin.domain.rbac.enums.YesNo;
import com.cowave.hub.admin.domain.rbac.entity.HubUser;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.cowave.hub.admin.domain.rbac.enums.YesNo.NO;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class UserListPto extends HubUser {

    /**
     * 部门岗位
     */
    private List<SysUserDeptPost> deptPosts;

    @Getter
    @Setter
    public static class SysUserDeptPost {

        /**
         * 部门id
         */
        private Integer deptId;

        /**
         * 部门名称
         */
        private String deptName;

        /**
         * 是否用户默认单位
         */
        private YesNo isDefault = NO;

        /**
         * 是否单位负责人
         */
        private YesNo isLeader = NO;

        /**
         * 岗位id
         */
        private Integer postId;

        /**
         * 岗位名称
         */
        private String postName;
    }
}
