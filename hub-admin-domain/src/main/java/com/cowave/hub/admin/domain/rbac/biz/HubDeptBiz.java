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
package com.cowave.hub.admin.domain.rbac.biz;

import com.cowave.hub.admin.domain.rbac.entity.HubDept;
import com.cowave.hub.admin.domain.rbac.entity.HubDeptDiagram;
import com.cowave.hub.admin.domain.rbac.entity.HubDeptPost;
import com.cowave.hub.admin.domain.rbac.entity.HubUserDept;
import com.cowave.hub.admin.domain.rbac.entity.command.DeptCreate;

import java.util.List;

/**
 * HubDept聚合根Command操作
 *
 * @see HubDept
 * @see HubDeptDiagram
 * @see HubDeptPost
 * @see HubUserDept
 *
 * @author shanhuiming
 */
public interface HubDeptBiz {

    /**
     * 新增部门
     */
    void createDept(String tenantId, DeptCreate deptCreate);

    /**
     * 删除部门
     */
    void deleteDepts(String tenantId, List<Integer> deptIds);

    /**
     * 修改部门
     */
    void editDept(String tenantId, DeptCreate deptCreate);

    /**
     * 添加部门岗位
     */
    void addPosts(String tenantId, List<HubDeptPost> list);

    /**
     * 移除部门岗位
     */
    void removePosts(String tenantId, Integer deptId, List<Integer> postIds);

    /**
     * 添加部门成员
     */
    void addMembers(String tenantId, List<HubUserDept> list);

    /**
     * 移除部门成员
     */
    void removeMembers(String tenantId, Integer deptId, List<Integer> userIds);
}
