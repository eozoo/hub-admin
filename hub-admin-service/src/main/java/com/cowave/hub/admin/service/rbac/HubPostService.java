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
import com.cowave.hub.admin.domain.rbac.entity.HubPost;
import com.cowave.hub.admin.domain.rbac.entity.command.PostCreate;
import com.cowave.hub.admin.domain.rbac.entity.pto.PostInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserNamePto;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptPostQuery;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubPostService {

	/**
	 * 分页列表
	 */
	Page<HubPost> pageList(String tenantId, DeptPostQuery query);

	/**
	 * 列表
	 */
	List<HubPost> list(String tenantId, DeptPostQuery query);

	/**
	 * 详情
	 */
	PostInfoPto info(String tenantId, Integer postId);

	/**
	 * 新增
	 */
	void create(String tenantId, PostCreate post);

	/**
	 * 删除
	 */
	void delete(String tenantId, List<Integer> postIds);

	/**
	 * 修改
	 */
	void edit(String tenantId, PostCreate post);

	/**
	 * 岗位组织架构
	 */
	Tree<Integer> queryDiagram(String tenantId);

	/**
	 * 岗位流程候选人
	 */
	List<UserNamePto> queryCandidatesByCode(String tenantId, String postCode);

	/**
     * 岗位名称查询
     */
	String queryNameById(String tenantId, Integer postId);

	/**
     * 部门岗位名称查询
     */
    List<String> queryNameOfDeptPost(String tenantId, List<String> deptPosts);
}
