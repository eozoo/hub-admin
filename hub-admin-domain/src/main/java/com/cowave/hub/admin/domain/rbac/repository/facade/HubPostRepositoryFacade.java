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
import com.cowave.hub.admin.domain.rbac.entity.HubPost;
import com.cowave.hub.admin.domain.rbac.entity.HubPostDiagram;
import com.cowave.hub.admin.domain.rbac.entity.pto.PostInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserNamePto;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptPostQuery;

import java.util.List;

/**
 * HubPost聚合根Query操作
 *
 * @see HubPost
 * @see HubPostDiagram
 *
 * @author shanhuiming
 */
public interface HubPostRepositoryFacade {

    /**
     * 分页列表
     */
    Page<HubPost> queryPage(String tenantId, DeptPostQuery query);

    /**
     * 列表
     */
    List<HubPost> queryList(String tenantId, DeptPostQuery query);

    /**
     * 列表（岗位id）
     */
    List<HubPost> queryListByIds(String tenantId, List<Integer> postIds);

    /**
     * 详情
     */
    PostInfoPto queryInfo(String tenantId, Integer postId);

    /**
     * 岗位名称
     */
    String queryNameById(String tenantId, Integer postId);

    /**
     * 部门岗位组合名称
     */
    String queryNameOfDeptPost(String tenantId, Integer deptId, Integer postId);

    /**
     * 岗位组织架构树
     */
    Tree<Integer> queryPostDiagram(String tenantId);

    /**
     * 下级岗位id列表（递归）
     */
    List<Integer> queryChildPostIds(Integer postId);

    /**
     * 岗位流程候选人
     */
    List<UserNamePto> queryCandidatesByCode(String tenantId, String postCode);
}
