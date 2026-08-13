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
import com.cowave.hub.admin.domain.rbac.repository.SysPostRepository;
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
public class SysPostDao extends ServiceImpl<SysPostMapper, SysPost> implements SysPostRepository {
    private final SysPostDiagramMapper postDiagramMapper;
    private final SysDeptPostMapper deptPostMapper;
    private final SysUserDeptMapper userDeptMapper;
    private final SysTenantMapper tenantMapper;

    @Override
    public Page<SysPost> queryPage(String tenantId, DeptPostQuery query) {
        return baseMapper.pageList(tenantId, query, Access.page());
    }

    @Override
    public List<SysPost> queryList(String tenantId, DeptPostQuery query) {
        return baseMapper.list(tenantId, query);
    }

    @Override
    public List<SysPost> queryListByIds(String tenantId, List<Integer> postIds) {
        return lambdaQuery()
                .eq(SysPost::getTenantId, tenantId)
                .in(SysPost::getPostId, postIds)
                .list();
    }

    @Override
    public PostInfoPto queryInfo(String tenantId, Integer postId) {
        return baseMapper.info(tenantId, postId);
    }

    @Override
    public String queryNameById(String tenantId, Integer postId) {
        return lambdaQuery()
                .eq(SysPost::getTenantId, tenantId)
                .eq(SysPost::getPostId, postId)
                .select(SysPost::getPostName)
                .oneOpt().map(SysPost::getPostName).orElse(null);
    }

    @Override
    public String queryNameOfDeptPost(String tenantId, Integer deptId, Integer postId) {
        return baseMapper.getNameOfDeptPost(tenantId, deptId, postId);
    }

    @Cacheable(value = POST_DIAGRAM, key = "#tenantId")
    @Override
    public Tree<Integer> queryPostDiagram(String tenantId) {
        List<DiagramNode> list = postDiagramMapper.listDiagramNodes(tenantId);
        SysTenant sysTenant = tenantMapper.selectById(tenantId);
        list.add(DiagramNode.newRootNode(sysTenant.getTenantName()));
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
    public void updatePost(SysPost sysPost) {
        lambdaUpdate()
                .eq(SysPost::getPostId, sysPost.getPostId())
                .set(SysPost::getUpdateBy, Access.userCode())
                .set(SysPost::getUpdateTime, new Date())
                .set(SysPost::getPostCode, sysPost.getPostCode())
                .set(SysPost::getPostType, sysPost.getPostType())
                .set(SysPost::getPostName, sysPost.getPostName())
                .set(SysPost::getPostLevel, sysPost.getPostLevel())
                .set(SysPost::getPostStatus, sysPost.getPostStatus())
                .set(SysPost::getRemark, sysPost.getRemark())
                .update();
    }

    @Override
    public void saveDiagram(SysPostDiagram diagram) {
        postDiagramMapper.insert(diagram);
    }

    @Override
    public void deleteDiagramParentsByPostId(Integer postId) {
        postDiagramMapper.delete(new LambdaUpdateWrapper<SysPostDiagram>()
                .eq(SysPostDiagram::getPostId, postId));
    }

    @Override
    public void deleteDiagramParentsByPostIds(List<Integer> postIds) {
        postDiagramMapper.delete(new LambdaUpdateWrapper<SysPostDiagram>()
                .in(SysPostDiagram::getPostId, postIds));
    }

    @Override
    public void deleteDiagramChildrenByPostIds(List<Integer> postIds) {
        postDiagramMapper.delete(new LambdaUpdateWrapper<SysPostDiagram>()
                .in(SysPostDiagram::getParentId, postIds));
    }

    @Override
    public void removeDeptPostsByPostIds(List<Integer> postIds) {
        deptPostMapper.delete(new LambdaUpdateWrapper<SysDeptPost>()
                .in(SysDeptPost::getPostId, postIds));
    }

    @Override
    public long countUsersByPostIds(List<Integer> postIds) {
        return userDeptMapper.selectCount(new LambdaQueryWrapper<SysUserDept>()
                .in(SysUserDept::getPostId, postIds));
    }

    @Override
    public void removeUserDeptsByPostIds(List<Integer> postIds) {
        userDeptMapper.delete(new LambdaUpdateWrapper<SysUserDept>()
                .in(SysUserDept::getPostId, postIds));
    }
}
