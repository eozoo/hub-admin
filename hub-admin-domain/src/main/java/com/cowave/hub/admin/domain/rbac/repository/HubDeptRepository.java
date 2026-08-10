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
package com.cowave.hub.admin.domain.rbac.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cowave.hub.admin.domain.rbac.entity.HubDept;
import com.cowave.hub.admin.domain.rbac.entity.HubDeptDiagram;
import com.cowave.hub.admin.domain.rbac.entity.HubDeptPost;
import com.cowave.hub.admin.domain.rbac.entity.HubUserDept;
import com.cowave.hub.admin.domain.rbac.entity.command.DeptCreate;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubDeptRepositoryFacade;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubDeptRepository extends HubDeptRepositoryFacade, IService<HubDept> {

    /**
     * 修改部门信息
     */
    void updateDept(DeptCreate dept);

    /**
     * 批量新增上下级关系
     */
    void saveDiagramBatch(List<HubDeptDiagram> list);

    /**
     * 删除上级部门
     */
    void deleteDiagramParentsByDeptId(Integer deptId);

    /**
     * 批量删除上级部门
     */
    void deleteDiagramParentsByDeptIds(List<Integer> deptIds);

    /**
     * 插入部门岗位
     */
    void insertDeptPosts(String tenantId, List<HubDeptPost> list);

    /**
     * 从部门中移除岗位
     */
    void removePostOfDept(Integer deptId, List<Integer> postIds);

    /**
     * 删除部门-关联删除岗位
     */
    void removePostsByDeptIds(List<Integer> deptIds);

    /**
     * 删除部门岗位-关联删除用户岗位
     */
    void removeUserDeptByDeptPosts(Integer deptId, List<Integer> postIds);

    /**
     * 插入部门人员
     */
    void insertDeptUsers(String tenantId, List<HubUserDept> list);

    /**
     * 从部门中移除用户
     */
    void removeUserOfDept(Integer deptId, List<Integer> userIds);

    /**
     * 删除部门-关联删除用户岗位
     */
    void removeUserDeptByDeptIds(List<Integer> deptIds);

    /**
     * 清除部门用户
     */
    void clearUserDeptByDeptIds(List<Integer> deptIds);
}
