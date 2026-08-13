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
import com.cowave.hub.admin.domain.rbac.entity.SysUserDept;
import com.cowave.hub.admin.domain.rbac.entity.pto.DeptUserPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.RoleUserPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserNamePto;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptUserQuery;
import com.cowave.hub.admin.domain.rbac.entity.pto.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysUserDeptMapper extends BaseMapper<SysUserDept> {

    /**
     * 获取部门成员（已加入）
     */
    Page<DeptUserPto> getJoinedMembers(@Param("tenantId") String tenantId, @Param("query") DeptUserQuery query, Page<RoleUserPto> page);

    /**
     * 获取部门成员（未加入）
     */
    Page<DeptUserPto> getUnJoinedMembers(@Param("tenantId") String tenantId, @Param("query") DeptUserQuery query, Page<RoleUserPto> page);

    /**
     * 插入部门人员
     */
    void insertDeptUsers(@Param("tenantId") String tenantId, @Param("list") List<SysUserDept> list);

    /**
     * 部门用户树
     */
    List<TreeNode> listDeptUserDiagramNode(String tenantId);

    /**
     * 部门流程候选人
     */
    List<UserNamePto> getCandidatesByCode(@Param("tenantId") String tenantId, @Param("deptCode") String deptCode);

}
