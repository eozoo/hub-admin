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
import com.cowave.hub.admin.domain.rbac.entity.HubDept;
import com.cowave.hub.admin.domain.rbac.entity.pto.DeptInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.DeptListPto;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface HubDeptMapper extends BaseMapper<HubDept> {

    /**
     * 获取用户默认部门
     */
    HubDept getPrimaryDeptByUserId(Integer userId);

    /**
     * 部门列表视图
     */
    List<DeptListPto> queryDeptListDpo(@Param("tenantId") String tenantId, @Param("query") DeptQuery query);

    /**
     * 部门详情视图
     */
    DeptInfoPto queryDeptInfoDpo(@Param("tenantId") String tenantId, @Param("deptId") Integer deptId);
}
