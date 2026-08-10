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
import com.cowave.hub.admin.domain.rbac.entity.HubPost;
import com.cowave.hub.admin.domain.rbac.entity.HubPostDiagram;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubPostRepositoryFacade;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubPostRepository extends HubPostRepositoryFacade, IService<HubPost> {

    /**
     * 修改岗位信息
     */
    void updatePost(HubPost hubPost);

    /**
     * 新增上下级关系
     */
    void saveDiagram(HubPostDiagram diagram);

    /**
     * 删除上级岗位（按postId）
     */
    void deleteDiagramParentsByPostId(Integer postId);

    /**
     * 批量删除上级岗位（按postId）
     */
    void deleteDiagramParentsByPostIds(List<Integer> postIds);

    /**
     * 批量删除下级岗位（按parentId）
     */
    void deleteDiagramChildrenByPostIds(List<Integer> postIds);

    /**
     * 删除部门岗位（按postId）
     */
    void removeDeptPostsByPostIds(List<Integer> postIds);

    /**
     * 统计岗位用户数
     */
    long countUsersByPostIds(List<Integer> postIds);

    /**
     * 删除用户部门岗位（按postId）
     */
    void removeUserDeptsByPostIds(List<Integer> postIds);
}
