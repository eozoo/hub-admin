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

import com.cowave.hub.admin.domain.rbac.entity.SysPost;
import com.cowave.hub.admin.domain.rbac.entity.SysPostDiagram;
import com.cowave.hub.admin.domain.rbac.entity.command.PostCreate;

import java.util.List;

/**
 * HubPost聚合根Command操作
 *
 * @see SysPost
 * @see SysPostDiagram
 *
 * @author shanhuiming
 */
public interface SysPostBiz {

    /**
     * 新增岗位
     */
    void createPost(String tenantId, PostCreate post);

    /**
     * 删除岗位
     */
    void deletePosts(String tenantId, List<Integer> postIds);

    /**
     * 修改岗位
     */
    void editPost(String tenantId, PostCreate post);
}
