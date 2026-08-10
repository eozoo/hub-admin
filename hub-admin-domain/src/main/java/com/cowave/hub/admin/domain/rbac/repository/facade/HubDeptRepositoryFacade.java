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
package com.cowave.hub.admin.domain.rbac.repository.facade;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rbac.entity.HubDept;
import com.cowave.hub.admin.domain.rbac.entity.HubDeptDiagram;
import com.cowave.hub.admin.domain.rbac.entity.HubDeptPost;
import com.cowave.hub.admin.domain.rbac.entity.HubUserDept;
import com.cowave.hub.admin.domain.rbac.entity.pto.*;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptPostQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptUserQuery;
import com.cowave.hub.admin.domain.rbac.entity.pto.TreeNode;

import java.util.List;

/**
 * HubDept聚合根Query操作
 *
 * @see HubDept
 * @see HubDeptDiagram
 * @see HubDeptPost
 * @see HubUserDept
 *
 * @author shanhuiming
 */
public interface HubDeptRepositoryFacade {

    /**
     * 部门查询（部门id）
     */
    HubDept queryOfTenantById(String tenantId, Integer deptId);

    /**
     * 部门列表
     */
    List<HubDept> queryListOfTenant(String tenantId);

    /**
     * 部门列表（部门id）
     */
    List<HubDept> queryListOfTenantByIds(String tenantId, List<Integer> deptIds);

    /**
     * 部门名称列表
     */
    List<String> queryNamesOfTenantById(String tenantId, List<Integer> deptIds);

    /**
     * 部门列表视图（联查上级部门、负责人）
     */
    List<DeptListPto> queryDeptList(String tenantId, DeptQuery query);

    /**
     * 部门详情视图（联查上级部门、负责人）
     */
    DeptInfoPto queryDeptInfo(String tenantId, Integer deptId);

    /**
     * 部门组织结构树
     */
    Tree<Integer> queryDeptDiagram(String tenantId);

    /**
     * 下级部门id列表（递归）
     */
    List<Integer> queryChildDeptIds(Integer deptId);

    /**
     * 统计下级部门数
     */
    long countChildDepts(List<Integer> deptIds);

    /**
     * 部门岗位列表（已设置）
     */
    Page<DeptPostPto> queryConfiguredPosts(String tenantId, DeptPostQuery query);

    /**
     * 部门岗位列表（未设置）
     */
    Page<DeptPostPto> queryUnConfiguredPosts(String tenantId, DeptPostQuery query);

    /**
     * 部门岗位树节点
     */
    List<TreeNode> listDeptPostDiagramNode(String tenantId);

    /**
     * 查询部门岗位
     */
    List<HubDeptPost> queryDeptPosts(List<HubDeptPost> list);

    /**
     * 存在多个默认岗位的部门id列表
     */
    List<Integer> queryDeptsWithMultiDefaultPost();

    /**
     * 部门成员（已加入）
     */
    Page<DeptUserPto> queryJoinedMembers(String tenantId, DeptUserQuery query);

    /**
     * 部门成员（未加入）
     */
    Page<DeptUserPto> queryUnJoinedMembers(String tenantId, DeptUserQuery query);

    /**
     * 部门用户树节点
     */
    List<TreeNode> listDeptUserDiagramNode(String tenantId);

    /**
     * 部门流程候选人
     */
    List<UserNamePto> queryCandidatesByCode(String tenantId, String deptCode);
}
