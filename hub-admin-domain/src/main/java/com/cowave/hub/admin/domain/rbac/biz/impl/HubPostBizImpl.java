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
package com.cowave.hub.admin.domain.rbac.biz.impl;

import com.cowave.hub.admin.domain.rbac.biz.HubPostBiz;
import com.cowave.hub.admin.domain.rbac.entity.HubPost;
import com.cowave.hub.admin.domain.rbac.entity.HubPostDiagram;
import com.cowave.hub.admin.domain.rbac.entity.command.PostCreate;
import com.cowave.hub.admin.domain.rbac.entity.pto.PostInfoPto;
import com.cowave.hub.admin.domain.rbac.repository.HubPostRepository;
import com.cowave.zoo.framework.access.operation.OperationContext;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.tools.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.hub.admin.domain.AdminRedisKeys.DEPT_POST_DIAGRAM;
import static com.cowave.hub.admin.domain.AdminRedisKeys.POST_DIAGRAM;
import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.zoo.http.client.constants.HttpCode.NOT_FOUND;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubPostBizImpl implements HubPostBiz {

    private final HubPostRepository postRepository;

    @CacheEvict(value = {POST_DIAGRAM, DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void createPost(String tenantId, PostCreate post) {
        postRepository.save(post);
        inputPostParents(tenantId, post, false);
    }

    @CacheEvict(value = {POST_DIAGRAM, DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void deletePosts(String tenantId, List<Integer> postIds) {
        long postUserCount = postRepository.countUsersByPostIds(postIds);
        HttpAsserts.isTrue(postUserCount == 0, BAD_REQUEST, "{admin.post.forbid.user.delete}");

        List<HubPost> list = postRepository.queryListByIds(tenantId, postIds);
        OperationContext.prepareContent(list);

        List<Integer> deleteList = Collections.copyToList(list, HubPost::getPostId);
        if (!deleteList.isEmpty()) {
            postRepository.removeByIds(deleteList);
            postRepository.removeDeptPostsByPostIds(deleteList);
            postRepository.removeUserDeptsByPostIds(deleteList);
            postRepository.deleteDiagramParentsByPostIds(deleteList);
            postRepository.deleteDiagramChildrenByPostIds(deleteList);
        }
    }

    @CacheEvict(value = {POST_DIAGRAM, DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void editPost(String tenantId, PostCreate post) {
        HttpAsserts.notNull(post.getPostId(), BAD_REQUEST, "{admin.post.id.null}");

        PostInfoPto prePost = postRepository.queryInfo(tenantId, post.getPostId());
        HttpAsserts.notNull(prePost, NOT_FOUND, "{admin.post.not.exist}", post.getPostId());
        OperationContext.prepareContent(prePost);

        postRepository.updatePost(post);
        inputPostParents(tenantId, post, true);
    }

    private void inputPostParents(String tenantId, PostCreate post, boolean overwrite) {
        if (overwrite) {
            postRepository.deleteDiagramParentsByPostId(post.getPostId());
        }
        int parentId = post.getParentId();
        if (parentId > 0 && overwrite) {
            List<Integer> childIds = postRepository.queryChildPostIds(post.getPostId());
            childIds.add(post.getPostId());
            HttpAsserts.isFalse(childIds.contains(parentId), BAD_REQUEST, "{admin.post.tree.cycle}");
        }
        postRepository.saveDiagram(new HubPostDiagram(post.getPostId(), parentId, tenantId));
    }
}
