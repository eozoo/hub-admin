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
import com.cowave.hub.admin.domain.rbac.biz.HubDeptBiz;
import com.cowave.hub.admin.domain.rbac.entity.HubDept;
import com.cowave.hub.admin.domain.rbac.entity.HubDeptPost;
import com.cowave.hub.admin.domain.rbac.entity.HubUserDept;
import com.cowave.hub.admin.domain.rbac.entity.command.DeptCreate;
import com.cowave.hub.admin.domain.rbac.entity.pto.*;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptPostQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptUserQuery;
import com.cowave.hub.admin.domain.rbac.entity.pto.TreeNode;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubDeptRepositoryFacade;
import com.cowave.hub.admin.service.rbac.HubDeptService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.cowave.hub.admin.domain.AdminRedisKeys.*;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
public class HubDeptServiceImpl implements HubDeptService {

    private final HubDeptBiz deptBiz;

    private final HubDeptRepositoryFacade deptRepositoryFacade;

    @Override
    public List<DeptListPto> list(String tenantId, DeptQuery query) {
        return deptRepositoryFacade.queryDeptList(tenantId, query);
    }

    @Override
    public DeptInfoPto info(String tenantId, Integer deptId) {
        return deptRepositoryFacade.queryDeptInfo(tenantId, deptId);
    }

    @Override
    public void create(String tenantId, DeptCreate deptCreate) {
        deptBiz.createDept(tenantId, deptCreate);
    }

    @Override
    public void delete(String tenantId, List<Integer> deptIds) {
        deptBiz.deleteDepts(tenantId, deptIds);
    }

    @Override
    public void edit(String tenantId, DeptCreate deptCreate) {
        deptBiz.editDept(tenantId, deptCreate);
    }

    @Override
    public List<HubDept> queryListForExport(String tenantId) {
        return deptRepositoryFacade.queryListOfTenant(tenantId);
    }

    @Override
    public List<Tree<Integer>> queryDiagram(String tenantId, Integer deptId) {
        Tree<Integer> tree = deptRepositoryFacade.queryDeptDiagram(tenantId);
        if (deptId == null || deptId.equals(tree.getId())) {
            return List.of(tree);
        }
        if (CollectionUtils.isEmpty(tree.getChildren())) {
            return List.of(new Tree<>());
        }
        Deque<Tree<Integer>> queue = new LinkedList<>(tree.getChildren());
        while (!queue.isEmpty()) {
            tree = queue.pop();
            if (Objects.equals(deptId, tree.getId())) {
                return List.of(tree);
            }
            if (CollectionUtils.isNotEmpty(tree.getChildren())) {
                queue.addAll(tree.getChildren());
            }
        }
        return List.of(new Tree<>());
    }

    @Cacheable(value = DEPT_POST_DIAGRAM, key = "#tenantId")
    @Override
    public Tree<String> queryPostDiagram(String tenantId) {
        Tree<Integer> tree = deptRepositoryFacade.queryDeptDiagram(tenantId);
        Tree<String> deptTree = convertTree(tree);

        List<TreeNode> list = deptRepositoryFacade.listDeptPostDiagramNode(tenantId);
        Map<String, List<Tree<String>>> deptPostMap = new HashMap<>();
        for (TreeNode option : list) {
            deptPostMap.put(option.getPid(), option.getChildren());
        }

        Tree<String> root = deptTree;
        Deque<Tree<String>> queue = new LinkedList<>(List.of(root));
        while (!queue.isEmpty()) {
            root = queue.pop();
            List<Tree<String>> postList = deptPostMap.get(root.getId().split("-")[0]);
            List<Tree<String>> children = root.getChildren();
            if (children != null) {
                queue.addAll(root.getChildren());
                if (CollectionUtils.isNotEmpty(postList)) {
                    children.addAll(postList);
                }
            } else {
                if (CollectionUtils.isNotEmpty(postList)) {
                    root.setChildren(postList);
                } else {
                    root.put("isDisabled", true);
                }
            }
        }
        return deptTree;
    }

    @Cacheable(value = DEPT_USER_DIAGRAM, key = "#tenantId")
    @Override
    public Tree<String> queryUserDiagram(String tenantId) {
        Tree<Integer> tree = deptRepositoryFacade.queryDeptDiagram(tenantId);
        Tree<String> deptTree = convertTree(tree);

        List<TreeNode> deptUserList = deptRepositoryFacade.listDeptUserDiagramNode(tenantId);
        Map<String, List<Tree<String>>> deptUserMap = new HashMap<>();
        for (TreeNode deptUser : deptUserList) {
            deptUserMap.put(deptUser.getPid(), deptUser.getChildren());
        }

        Tree<String> root = deptTree;
        Deque<Tree<String>> queue = new LinkedList<>(List.of(root));
        while (!queue.isEmpty()) {
            root = queue.pop();
            List<Tree<String>> userList = deptUserMap.get(root.getId().split("-")[0]);
            List<Tree<String>> children = root.getChildren();
            if (children != null) {
                queue.addAll(root.getChildren());
                if (CollectionUtils.isNotEmpty(userList)) {
                    children.addAll(userList);
                }
            } else {
                if (CollectionUtils.isNotEmpty(userList)) {
                    root.setChildren(userList);
                }
            }
        }
        return deptTree;
    }

    private Tree<String> convertTree(Tree<Integer> tree) {
        Tree<String> strTree = new Tree<>();
        strTree.setId(tree.getId() + "-0");
        strTree.put("isDept", true);
        strTree.setParentId(String.valueOf(tree.get("pid")));
        strTree.put("label", String.valueOf(tree.get("label")));
        if (tree.hasChild()) {
            List<Tree<String>> strChildren = tree.getChildren().stream()
                    .map(this::convertTree).collect(Collectors.toList());
            strTree.setChildren(strChildren);
        }
        return strTree;
    }

    @Override
    public void addPosts(String tenantId, List<HubDeptPost> list) {
        deptBiz.addPosts(tenantId, list);
    }

    @Override
    public void removePosts(String tenantId, Integer deptId, List<Integer> postIds) {
        deptBiz.removePosts(tenantId, deptId, postIds);
    }

    @Override
    public Page<DeptPostPto> queryConfiguredPosts(String tenantId, DeptPostQuery query) {
        return deptRepositoryFacade.queryConfiguredPosts(tenantId, query);
    }

    @Override
    public Page<DeptPostPto> queryUnConfiguredPosts(String tenantId, DeptPostQuery query) {
        return deptRepositoryFacade.queryUnConfiguredPosts(tenantId, query);
    }

    @Override
    public void addMembers(String tenantId, List<HubUserDept> list) {
        deptBiz.addMembers(tenantId, list);
    }

    @Override
    public void removeMembers(String tenantId, Integer deptId, List<Integer> userIds) {
        deptBiz.removeMembers(tenantId, deptId, userIds);
    }

    @Override
    public Page<DeptUserPto> queryJoinedMembers(String tenantId, DeptUserQuery query) {
        return deptRepositoryFacade.queryJoinedMembers(tenantId, query);
    }

    @Override
    public Page<DeptUserPto> queryUnJoinedMembers(String tenantId, DeptUserQuery query) {
        return deptRepositoryFacade.queryUnJoinedMembers(tenantId, query);
    }

    @Override
    public List<UserNamePto> queryCandidatesByCode(String tenantId, String deptCode) {
        return deptRepositoryFacade.queryCandidatesByCode(tenantId, deptCode);
    }

    @Override
    public List<String> queryNamesById(String tenantId, List<Integer> deptIds) {
        return deptRepositoryFacade.queryNamesOfTenantById(tenantId, deptIds);
    }
}
