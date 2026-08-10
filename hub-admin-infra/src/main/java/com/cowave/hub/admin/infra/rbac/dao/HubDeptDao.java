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
import com.cowave.hub.admin.domain.rbac.entity.command.DeptCreate;
import com.cowave.hub.admin.domain.rbac.entity.pto.*;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptPostQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptUserQuery;
import com.cowave.hub.admin.domain.rbac.entity.pto.DeptDiagramPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.TreeNode;
import com.cowave.hub.admin.domain.rbac.repository.HubDeptRepository;
import com.cowave.hub.admin.infra.rbac.mapper.*;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.tools.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static com.cowave.hub.admin.domain.AdminRedisKeys.DEPT_DIAGRAM;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class HubDeptDao extends ServiceImpl<HubDeptMapper, HubDept> implements HubDeptRepository {
    private final HubDeptDiagramMapper deptDiagramMapper;
    private final HubDeptPostMapper deptPostMapper;
    private final HubUserDeptMapper userDeptMapper;
    private final HubTenantMapper tenantMapper;

    @Override
    public HubDept queryOfTenantById(String tenantId, Integer deptId) {
        return lambdaQuery()
                .eq(HubDept::getTenantId, tenantId)
                .eq(HubDept::getDeptId, deptId)
                .one();
    }

    @Override
    public List<HubDept> queryListOfTenant(String tenantId) {
        return lambdaQuery().eq(HubDept::getTenantId, tenantId).list();
    }

    @Override
    public List<HubDept> queryListOfTenantByIds(String tenantId, List<Integer> deptIds) {
        return lambdaQuery()
                .eq(HubDept::getTenantId, tenantId)
                .in(HubDept::getDeptId, deptIds)
                .list();
    }

    @Override
    public List<String> queryNamesOfTenantById(String tenantId, List<Integer> deptIds) {
        if (deptIds.isEmpty()) {
            return List.of();
        }
        List<HubDept> list = lambdaQuery()
                .eq(HubDept::getTenantId, tenantId)
                .in(HubDept::getDeptId, deptIds)
                .select(HubDept::getDeptName)
                .list();
        return Collections.copyToList(list, HubDept::getDeptName);
    }

    @Override
    public List<DeptListPto> queryDeptList(String tenantId, DeptQuery query) {
        return baseMapper.queryDeptListDpo(tenantId, query);
    }

    @Override
    public DeptInfoPto queryDeptInfo(String tenantId, Integer deptId) {
        return baseMapper.queryDeptInfoDpo(tenantId, deptId);
    }

    @Cacheable(value = DEPT_DIAGRAM, key = "#tenantId")
    @Override
    public Tree<Integer> queryDeptDiagram(String tenantId) {
        List<DeptDiagramPto> list = deptDiagramMapper.listDiagramDpo(tenantId);
        // 根节点
        HubTenant hubTenant = tenantMapper.selectById(tenantId);
        list.add(DeptDiagramPto.newRootNode(hubTenant.getTenantName()));
        // 构造Tree
        return TreeUtil.build(list, -1, DeptDiagramPto.DIAGRAM_CONFIG, (u, node) -> {
            node.setId(u.getId());
            node.setParentId(u.getPid());
            node.setName(u.getLabel());
        }).get(0);
    }

    @Override
    public List<Integer> queryChildDeptIds(Integer deptId) {
        return deptDiagramMapper.childIds(deptId);
    }

    @Override
    public long countChildDepts(List<Integer> deptIds) {
        return deptDiagramMapper.selectCount(new LambdaQueryWrapper<HubDeptDiagram>()
                .in(HubDeptDiagram::getParentId, deptIds));
    }

    @Override
    public Page<DeptPostPto> queryConfiguredPosts(String tenantId, DeptPostQuery query) {
        return deptPostMapper.getConfiguredPosts(tenantId, query, Access.page());
    }

    @Override
    public Page<DeptPostPto> queryUnConfiguredPosts(String tenantId, DeptPostQuery query) {
        return deptPostMapper.getUnConfiguredPosts(tenantId, query, Access.page());
    }

    @Override
    public List<TreeNode> listDeptPostDiagramNode(String tenantId) {
        return deptPostMapper.listDeptPostDiagramNode(tenantId);
    }

    @Override
    public List<HubDeptPost> queryDeptPosts(List<HubDeptPost> list) {
        return deptPostMapper.queryDeptPosts(list);
    }

    @Override
    public List<Integer> queryDeptsWithMultiDefaultPost() {
        return deptPostMapper.deptWithMultiDefaultPost();
    }

    @Override
    public Page<DeptUserPto> queryJoinedMembers(String tenantId, DeptUserQuery query) {
        return userDeptMapper.getJoinedMembers(tenantId, query, Access.page());
    }

    @Override
    public Page<DeptUserPto> queryUnJoinedMembers(String tenantId, DeptUserQuery query) {
        return userDeptMapper.getUnJoinedMembers(tenantId, query, Access.page());
    }

    @Override
    public List<TreeNode> listDeptUserDiagramNode(String tenantId) {
        return userDeptMapper.listDeptUserDiagramNode(tenantId);
    }

    @Override
    public List<UserNamePto> queryCandidatesByCode(String tenantId, String deptCode) {
        return userDeptMapper.getCandidatesByCode(tenantId, deptCode);
    }

    @Override
    public void updateDept(DeptCreate dept) {
        lambdaUpdate()
                .eq(HubDept::getDeptId, dept.getDeptId())
                .set(HubDept::getUpdateBy, Access.userCode())
                .set(HubDept::getUpdateTime, new Date())
                .set(HubDept::getDeptName, dept.getDeptName())
                .set(HubDept::getDeptShort, dept.getDeptShort())
                .set(HubDept::getDeptAddr, dept.getDeptAddr())
                .set(HubDept::getDeptPhone, dept.getDeptPhone())
                .set(HubDept::getRemark, dept.getRemark())
                .update();
    }

    @Override
    public void saveDiagramBatch(List<HubDeptDiagram> list) {
        for (HubDeptDiagram diagram : list) {
            deptDiagramMapper.insert(diagram);
        }
    }

    @Override
    public void deleteDiagramParentsByDeptId(Integer deptId) {
        deptDiagramMapper.delete(new LambdaUpdateWrapper<HubDeptDiagram>()
                .eq(HubDeptDiagram::getDeptId, deptId));
    }

    @Override
    public void deleteDiagramParentsByDeptIds(List<Integer> deptIds) {
        deptDiagramMapper.delete(new LambdaUpdateWrapper<HubDeptDiagram>()
                .in(HubDeptDiagram::getDeptId, deptIds));
    }

    @Override
    public void insertDeptPosts(String tenantId, List<HubDeptPost> list) {
        deptPostMapper.insertDeptPosts(tenantId, list);
    }

    @Override
    public void removePostOfDept(Integer deptId, List<Integer> postIds) {
        deptPostMapper.delete(new LambdaUpdateWrapper<HubDeptPost>()
                .eq(HubDeptPost::getDeptId, deptId)
                .in(HubDeptPost::getPostId, postIds));
    }

    @Override
    public void removePostsByDeptIds(List<Integer> deptIds) {
        deptPostMapper.delete(new LambdaUpdateWrapper<HubDeptPost>()
                .in(HubDeptPost::getDeptId, deptIds));
    }

    @Override
    public void removeUserDeptByDeptPosts(Integer deptId, List<Integer> postIds) {
        userDeptMapper.delete(new LambdaUpdateWrapper<HubUserDept>()
                .eq(HubUserDept::getDeptId, deptId)
                .in(HubUserDept::getPostId, postIds));
    }

    @Override
    public void insertDeptUsers(String tenantId, List<HubUserDept> list) {
        userDeptMapper.insertDeptUsers(tenantId, list);
    }

    @Override
    public void removeUserOfDept(Integer deptId, List<Integer> userIds) {
        userDeptMapper.delete(new LambdaUpdateWrapper<HubUserDept>()
                .eq(HubUserDept::getDeptId, deptId)
                .in(HubUserDept::getUserId, userIds));
    }

    @Override
    public void removeUserDeptByDeptIds(List<Integer> deptIds) {
        userDeptMapper.delete(new LambdaUpdateWrapper<HubUserDept>()
                .in(HubUserDept::getDeptId, deptIds));
    }

    @Override
    public void clearUserDeptByDeptIds(List<Integer> deptIds) {
        userDeptMapper.delete(new LambdaUpdateWrapper<HubUserDept>()
                .in(HubUserDept::getDeptId, deptIds));
    }
}
