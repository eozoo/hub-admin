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

import com.cowave.hub.admin.domain.rbac.biz.HubDeptBiz;
import com.cowave.hub.admin.domain.rbac.entity.HubDept;
import com.cowave.hub.admin.domain.rbac.entity.HubDeptPost;
import com.cowave.hub.admin.domain.rbac.entity.HubUserDept;
import com.cowave.hub.admin.domain.rbac.entity.command.DeptCreate;
import com.cowave.hub.admin.domain.rbac.entity.pto.DeptInfoPto;
import com.cowave.hub.admin.domain.rbac.repository.HubDeptRepository;
import com.cowave.zoo.framework.access.operation.OperationContext;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.tools.Collections;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.hub.admin.domain.AdminRedisKeys.*;
import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.zoo.http.client.constants.HttpCode.NOT_FOUND;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubDeptBizImpl implements HubDeptBiz {

    private final HubDeptRepository deptRepository;

    @CacheEvict(value = {DEPT_DIAGRAM, DEPT_USER_DIAGRAM, DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void createDept(String tenantId, DeptCreate deptCreate) {
        deptRepository.save(deptCreate);
        deptRepository.saveDiagramBatch(deptCreate.getDeptParents());
    }

    @CacheEvict(value = {DEPT_DIAGRAM, DEPT_USER_DIAGRAM, DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void deleteDepts(String tenantId, List<Integer> deptIds) {
        HttpAsserts.isTrue(deptRepository.countChildDepts(deptIds) == 0, BAD_REQUEST, "{admin.dept.child.forbid.delete}");

        List<HubDept> list = deptRepository.queryListOfTenantByIds(tenantId, deptIds);
        OperationContext.prepareContent(list);

        List<Integer> deleteList = Collections.copyToList(list, HubDept::getDeptId);
        if (!deleteList.isEmpty()) {
            deptRepository.removeByIds(deleteList);
            deptRepository.removePostsByDeptIds(deleteList);
            deptRepository.removeUserDeptByDeptIds(deleteList);
            deptRepository.deleteDiagramParentsByDeptIds(deleteList);
            deptRepository.clearUserDeptByDeptIds(deleteList);
        }
    }

    @CacheEvict(value = {DEPT_DIAGRAM, DEPT_USER_DIAGRAM, DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void editDept(String tenantId, DeptCreate deptCreate) {
        Integer deptId = deptCreate.getDeptId();
        HttpAsserts.notNull(deptId, BAD_REQUEST, "{admin.dept.id.null}");

        DeptInfoPto preDept = deptRepository.queryDeptInfo(tenantId, deptId);
        HttpAsserts.notNull(preDept, NOT_FOUND, "{admin.dept.not.exist}");
        OperationContext.prepareContent(preDept);

        deptRepository.updateDept(deptCreate);

        deptRepository.deleteDiagramParentsByDeptId(deptId);
        List<Integer> childIds = deptRepository.queryChildDeptIds(deptId);
        childIds.add(deptId);
        List<Integer> parentIds = deptCreate.getParentIds();
        HttpAsserts.isTrue(java.util.Collections.disjoint(childIds, parentIds), BAD_REQUEST, "{admin.user.tree.cycle}");
        deptRepository.saveDiagramBatch(deptCreate.getDeptParents());
    }

    @CacheEvict(value = {DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void addPosts(String tenantId, List<HubDeptPost> list) {
        if (list.isEmpty()) {
            return;
        }
        deptRepository.insertDeptPosts(tenantId, list);
        List<HubDeptPost> insertList = deptRepository.queryDeptPosts(list);

        List<Integer> postIds = Collections.copyToList(insertList, HubDeptPost::getPostId);
        deptRepository.removeUserDeptByDeptPosts(0, postIds);

        List<Integer> deptIdList = deptRepository.queryDeptsWithMultiDefaultPost();
        HttpAsserts.isEmpty(deptIdList, BAD_REQUEST, "{admin.dept.post.default.max}");
    }

    @CacheEvict(value = {DEPT_POST_DIAGRAM}, key = "#tenantId")
    @Override
    public void removePosts(String tenantId, Integer deptId, List<Integer> postIds) {
        if (CollectionUtils.isEmpty(postIds)) {
            return;
        }
        HubDept hubDept = deptRepository.queryOfTenantById(tenantId, deptId);
        HttpAsserts.notNull(hubDept, NOT_FOUND, "{admin.dept.not.exist}");

        deptRepository.removePostOfDept(deptId, postIds);
        deptRepository.removeUserDeptByDeptPosts(deptId, postIds);
    }

    @CacheEvict(value = {DEPT_USER_DIAGRAM}, key = "#tenantId")
    @Override
    public void addMembers(String tenantId, List<HubUserDept> list) {
        if (list.isEmpty()) {
            return;
        }
        deptRepository.insertDeptUsers(tenantId, list);
    }

    @CacheEvict(value = {DEPT_USER_DIAGRAM}, key = "#tenantId")
    @Override
    public void removeMembers(String tenantId, Integer deptId, List<Integer> userIds) {
        HubDept hubDept = deptRepository.queryOfTenantById(tenantId, deptId);
        if (hubDept == null) {
            return;
        }
        deptRepository.removeUserOfDept(deptId, userIds);
    }
}
