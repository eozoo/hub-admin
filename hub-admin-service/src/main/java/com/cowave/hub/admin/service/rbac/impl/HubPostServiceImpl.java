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
package com.cowave.hub.admin.service.rbac.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rbac.biz.HubPostBiz;
import com.cowave.hub.admin.domain.rbac.entity.HubPost;
import com.cowave.hub.admin.domain.rbac.entity.command.PostCreate;
import com.cowave.hub.admin.domain.rbac.entity.pto.PostInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserNamePto;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptPostQuery;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubPostRepositoryFacade;
import com.cowave.hub.admin.service.rbac.HubPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
public class HubPostServiceImpl implements HubPostService {

    private final HubPostBiz postBiz;

    private final HubPostRepositoryFacade postRepositoryFacade;

    @Override
    public Page<HubPost> pageList(String tenantId, DeptPostQuery query) {
        return postRepositoryFacade.queryPage(tenantId, query);
    }

    @Override
    public List<HubPost> list(String tenantId, DeptPostQuery query) {
        return postRepositoryFacade.queryList(tenantId, query);
    }

    @Override
    public PostInfoPto info(String tenantId, Integer postId) {
        return postRepositoryFacade.queryInfo(tenantId, postId);
    }

    @Override
    public void create(String tenantId, PostCreate post) {
        postBiz.createPost(tenantId, post);
    }

    @Override
    public void delete(String tenantId, List<Integer> postIds) {
        postBiz.deletePosts(tenantId, postIds);
    }

    @Override
    public void edit(String tenantId, PostCreate post) {
        postBiz.editPost(tenantId, post);
    }

    @Override
    public Tree<Integer> queryDiagram(String tenantId) {
        return postRepositoryFacade.queryPostDiagram(tenantId);
    }

    @Override
    public List<UserNamePto> queryCandidatesByCode(String tenantId, String postCode) {
        return postRepositoryFacade.queryCandidatesByCode(tenantId, postCode);
    }

    @Override
    public String queryNameById(String tenantId, Integer postId) {
        return postRepositoryFacade.queryNameById(tenantId, postId);
    }

    @Override
    public List<String> queryNameOfDeptPost(String tenantId, List<String> deptPosts) {
        List<String> list = new ArrayList<>();
        for (String deptPostId : deptPosts) {
            String[] arr = deptPostId.split("-");
            list.add(postRepositoryFacade.queryNameOfDeptPost(tenantId,
                    Integer.parseInt(arr[0]), Integer.parseInt(arr[1])));
        }
        return list;
    }
}
