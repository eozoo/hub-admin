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
import com.cowave.hub.admin.domain.auth.entity.pto.UserProfile;
import com.cowave.hub.admin.domain.rbac.entity.HubUser;
import com.cowave.hub.admin.domain.rbac.entity.command.TenantManagerRemove;
import com.cowave.hub.admin.domain.rbac.entity.pto.TenantManagerPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserListPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserNamePto;
import com.cowave.hub.admin.domain.rbac.entity.query.UserQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface HubUserMapper extends BaseMapper<HubUser> {

    /**
     * 用户个人信息
     */
    UserProfile getUserProfile(Integer userId);

    /**
     * 列表
     */
    List<UserListPto> list(@Param("tenantId") String tenantId, @Param("query") UserQuery query);

    /**
     * 计数
     */
    int count(@Param("tenantId") String tenantId, @Param("query") UserQuery query);

    /**
     * 单位用户列表
     */
    List<UserListPto> listOfDept(@Param("tenantId") String tenantId, @Param("query") UserQuery query);

    /**
     * 单位用户计数
     */
    int countOfDept(@Param("tenantId") String tenantId, @Param("query") UserQuery query);

    /**
     * 详情
     */
    UserInfoPto getById(@Param("tenantId") String tenantId, @Param("userId") Integer userId);

    /**
     * 批量插入
     */
    void batchInsert(@Param("list") List<HubUser> list, @Param("overwrite") boolean overwrite);

    /**
     * 用户流程候选人
     */
    List<UserNamePto> getUserCandidates(@Param("tenantId") String tenantId, @Param("userId") Integer userId);

    /**
     * 租户管理员列表
     */
    Page<TenantManagerPto> listTenantManager(@Param("tenantId") String tenantId, Page<TenantManagerPto> page);

    /**
     * 移除租户管理员
     */
    void removeTenantManager(TenantManagerRemove managerRemove);
}
