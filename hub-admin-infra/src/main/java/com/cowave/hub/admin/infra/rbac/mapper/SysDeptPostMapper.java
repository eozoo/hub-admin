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
package com.cowave.hub.admin.infra.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rbac.entity.SysDeptPost;
import com.cowave.hub.admin.domain.rbac.entity.pto.DeptPostPto;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptPostQuery;
import com.cowave.hub.admin.domain.rbac.entity.pto.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysDeptPostMapper extends BaseMapper<SysDeptPost> {

    /**
     * 部门岗位列表（已设置）
     */
    Page<DeptPostPto> getConfiguredPosts(@Param("tenantId") String tenantId, @Param("query") DeptPostQuery query, Page<DeptPostPto> page);

    /**
     * 获取部门岗位（未设置）
     */
    Page<DeptPostPto> getUnConfiguredPosts(@Param("tenantId") String tenantId, @Param("query") DeptPostQuery query, Page<DeptPostPto> page);

    /**
     * 插入部门岗位
     */
    void insertDeptPosts(@Param("tenantId") String tenantId, @Param("list") List<SysDeptPost> list);

    /**
     * 查询插入的部门岗位
     */
    List<SysDeptPost> queryDeptPosts(List<SysDeptPost> list);

    /**
     * 默认岗位只允许一个
     */
    List<Integer> deptWithMultiDefaultPost();

    /**
     * 部门岗位选项
     */
    List<TreeNode> listDeptPostDiagramNode(String tenantId);
}
