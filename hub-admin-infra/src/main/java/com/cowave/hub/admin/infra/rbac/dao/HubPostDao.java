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
package com.cowave.hub.admin.infra.rbac.dao;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.rbac.entity.*;
import com.cowave.hub.admin.domain.rbac.entity.pto.PostInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserNamePto;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptPostQuery;
import com.cowave.hub.admin.domain.rbac.entity.pto.DiagramNode;
import com.cowave.hub.admin.domain.rbac.repository.HubPostRepository;
import com.cowave.hub.admin.infra.rbac.mapper.*;
import com.cowave.zoo.framework.access.Access;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

import static com.cowave.hub.admin.domain.AdminRedisKeys.POST_DIAGRAM;
import static com.cowave.hub.admin.domain.rbac.entity.pto.DiagramNode.DIAGRAM_CONFIG;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class HubPostDao extends ServiceImpl<HubPostMapper, HubPost> implements HubPostRepository {
    private final HubPostDiagramMapper postDiagramMapper;
    private final HubDeptPostMapper deptPostMapper;
    private final HubUserDeptMapper userDeptMapper;
    private final HubTenantMapper tenantMapper;

    @Override
    public Page<HubPost> queryPage(String tenantId, DeptPostQuery query) {
        return baseMapper.pageList(tenantId, query, Access.page());
    }

    @Override
    public List<HubPost> queryList(String tenantId, DeptPostQuery query) {
        return baseMapper.list(tenantId, query);
    }

    @Override
    public List<HubPost> queryListByIds(String tenantId, List<Integer> postIds) {
        return lambdaQuery()
                .eq(HubPost::getTenantId, tenantId)
                .in(HubPost::getPostId, postIds)
                .list();
    }

    @Override
    public PostInfoPto queryInfo(String tenantId, Integer postId) {
        return baseMapper.info(tenantId, postId);
    }

    @Override
    public String queryNameById(String tenantId, Integer postId) {
        return lambdaQuery()
                .eq(HubPost::getTenantId, tenantId)
                .eq(HubPost::getPostId, postId)
                .select(HubPost::getPostName)
                .oneOpt().map(HubPost::getPostName).orElse(null);
    }

    @Override
    public String queryNameOfDeptPost(String tenantId, Integer deptId, Integer postId) {
        return baseMapper.getNameOfDeptPost(tenantId, deptId, postId);
    }

    @Cacheable(value = POST_DIAGRAM, key = "#tenantId")
    @Override
    public Tree<Integer> queryPostDiagram(String tenantId) {
        List<DiagramNode> list = postDiagramMapper.listDiagramNodes(tenantId);
        HubTenant hubTenant = tenantMapper.selectById(tenantId);
        list.add(DiagramNode.newRootNode(hubTenant.getTenantName()));
        return TreeUtil.build(list, -1, DIAGRAM_CONFIG, (u, node) -> {
            node.setId(u.getId());
            node.setParentId(u.getPid());
            node.setName(u.getLabel());
        }).get(0);
    }

    @Override
    public List<Integer> queryChildPostIds(Integer postId) {
        return postDiagramMapper.childIds(postId);
    }

    @Override
    public List<UserNamePto> queryCandidatesByCode(String tenantId, String postCode) {
        return baseMapper.getCandidatesByCode(tenantId, postCode);
    }

    @Override
    public void updatePost(HubPost hubPost) {
        lambdaUpdate()
                .eq(HubPost::getPostId, hubPost.getPostId())
                .set(HubPost::getUpdateBy, Access.userCode())
                .set(HubPost::getUpdateTime, new Date())
                .set(HubPost::getPostCode, hubPost.getPostCode())
                .set(HubPost::getPostType, hubPost.getPostType())
                .set(HubPost::getPostName, hubPost.getPostName())
                .set(HubPost::getPostLevel, hubPost.getPostLevel())
                .set(HubPost::getPostStatus, hubPost.getPostStatus())
                .set(HubPost::getRemark, hubPost.getRemark())
                .update();
    }

    @Override
    public void saveDiagram(HubPostDiagram diagram) {
        postDiagramMapper.insert(diagram);
    }

    @Override
    public void deleteDiagramParentsByPostId(Integer postId) {
        postDiagramMapper.delete(new LambdaUpdateWrapper<HubPostDiagram>()
                .eq(HubPostDiagram::getPostId, postId));
    }

    @Override
    public void deleteDiagramParentsByPostIds(List<Integer> postIds) {
        postDiagramMapper.delete(new LambdaUpdateWrapper<HubPostDiagram>()
                .in(HubPostDiagram::getPostId, postIds));
    }

    @Override
    public void deleteDiagramChildrenByPostIds(List<Integer> postIds) {
        postDiagramMapper.delete(new LambdaUpdateWrapper<HubPostDiagram>()
                .in(HubPostDiagram::getParentId, postIds));
    }

    @Override
    public void removeDeptPostsByPostIds(List<Integer> postIds) {
        deptPostMapper.delete(new LambdaUpdateWrapper<HubDeptPost>()
                .in(HubDeptPost::getPostId, postIds));
    }

    @Override
    public long countUsersByPostIds(List<Integer> postIds) {
        return userDeptMapper.selectCount(new LambdaQueryWrapper<HubUserDept>()
                .in(HubUserDept::getPostId, postIds));
    }

    @Override
    public void removeUserDeptsByPostIds(List<Integer> postIds) {
        userDeptMapper.delete(new LambdaUpdateWrapper<HubUserDept>()
                .in(HubUserDept::getPostId, postIds));
    }
}
