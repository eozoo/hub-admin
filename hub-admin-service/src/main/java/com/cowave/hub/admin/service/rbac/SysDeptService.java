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
package com.cowave.hub.admin.service.rbac;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rbac.entity.SysDept;
import com.cowave.hub.admin.domain.rbac.entity.SysDeptPost;
import com.cowave.hub.admin.domain.rbac.entity.SysUserDept;
import com.cowave.hub.admin.domain.rbac.entity.pto.*;
import com.cowave.hub.admin.domain.rbac.entity.command.DeptCreate;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptPostQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptUserQuery;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysDeptService {

	/**
	 * 列表
	 */
	List<DeptListPto> list(String tenantId, DeptQuery query);

	/**
	 * 详情
	 */
	DeptInfoPto info(String tenantId, Integer deptId);

	/**
	 * 新增
	 */
	void create(String tenantId, DeptCreate dept);

	/**
	 * 删除
	 */
	void delete(String tenantId, List<Integer> deptIds);

	/**
	 * 修改
	 */
	void edit(String tenantId, DeptCreate dept);

	/**
	 * 导出列表
	 */
	List<SysDept> queryListForExport(String tenantId);

	/**
	 * 部门组织架构
	 */
	List<Tree<Integer>> queryDiagram(String tenantId, Integer deptId);

	/**
	 * 部门岗位树
	 */
	Tree<String> queryPostDiagram(String tenantId);

	/**
	 * 部门用户树
	 */
	Tree<String> queryUserDiagram(String tenantId);

	/**
	 * 添加部门岗位
	 */
	void addPosts(String tenantId, List<SysDeptPost> list);

	/**
     * 移除部门岗位
     */
	void removePosts(String tenantId, Integer deptId, List<Integer> postIds);

	/**
	 * 获取部门岗位（已设置）
	 */
	Page<DeptPostPto> queryConfiguredPosts(String tenantId, DeptPostQuery query);

	/**
     * 获取部门岗位（未设置）
     */
    Page<DeptPostPto> queryUnConfiguredPosts(String tenantId, DeptPostQuery query);

	/**
	 * 添加部门成员
	 */
	void addMembers(String tenantId, List<SysUserDept> list);

	/**
     * 移除部门成员
     */
	void removeMembers(String tenantId, Integer deptId, List<Integer> userIds);

	/**
	 * 获取部门成员（已加入）
	 */
	Page<DeptUserPto> queryJoinedMembers(String tenantId, DeptUserQuery query);

	/**
	 * 获取部门成员（未加入）
	 */
	Page<DeptUserPto> queryUnJoinedMembers(String tenantId, DeptUserQuery query);

	/**
	 * 部门流程候选人
	 */
	List<UserNamePto> queryCandidatesByCode(String tenantId, String deptCode);

	/**
     * 部门名称查询
     */
    List<String> queryNamesById(String tenantId, List<Integer> deptIds);
}
