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
import com.cowave.hub.admin.domain.rbac.entity.SysPost;
import com.cowave.hub.admin.domain.rbac.entity.pto.PostInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserNamePto;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptPostQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysPostMapper extends BaseMapper<SysPost> {

    /**
     * 分页列表
     */
    Page<SysPost> pageList(@Param("tenantId") String tenantId, @Param("query") DeptPostQuery query, Page<SysPost> page);

    /**
	 * 列表
	 */
	List<SysPost> list(@Param("tenantId") String tenantId, @Param("query") DeptPostQuery query);

    /**
     * 详情
     */
    PostInfoPto info(@Param("tenantId") String tenantId, @Param("postId") Integer postId);

    /**
     * 岗位流程候选人
     */
    List<UserNamePto> getCandidatesByCode(@Param("tenantId") String tenantId, @Param("postCode") String postCode);

    /**
     * 查询部门岗位名称
     */
    String getNameOfDeptPost(@Param("tenantId") String tenantId, @Param("deptId") Integer deptId, @Param("postId") Integer postId);
}
