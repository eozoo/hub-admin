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
package com.cowave.hub.admin.domain.rbac.repository.facade;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rbac.entity.HubUser;
import com.cowave.hub.admin.domain.rbac.entity.HubUserDept;
import com.cowave.hub.admin.domain.rbac.entity.HubUserDiagram;
import com.cowave.hub.admin.domain.auth.entity.pto.UserProfile;
import com.cowave.hub.admin.domain.rbac.entity.HubUserRole;
import com.cowave.hub.admin.domain.rbac.entity.pto.TenantManagerPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserListPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserNamePto;
import com.cowave.hub.admin.domain.rbac.entity.query.UserExportQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.UserMemberQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.UserQuery;
import com.cowave.hub.admin.domain.rbac.enums.UserType;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * HubUser聚合根Query操作
 *
 * @see HubUser
 * @see HubUserRole
 * @see HubUserDept
 * @see HubUserDiagram
 *
 * @author shanhuiming
 */
public interface HubUserRepositoryFacade {

    /**
     * 用户列表
     */
    List<UserListPto> queryList(String tenantId, UserQuery query);

    /**
     * 用户列表计数
     */
    int countList(String tenantId, UserQuery query);

    /**
     * 部门用户列表
     */
    List<UserListPto> queryListOfDept(String tenantId, UserQuery query);

    /**
     * 部门用户列表计数
     */
    int countListOfDept(String tenantId, UserQuery query);

    /**
     * 详情
     */
    UserInfoPto queryInfo(String tenantId, Integer userId);

    /**
     * 导出列表
     */
    List<HubUser> queryListForExport(String tenantId, UserExportQuery query);

    /**
     * 用户组织架构树
     */
    Tree<Integer> queryDiagram(String tenantId);

    /**
     * 下级用户id（递归）
     */
    List<Integer> queryChildUserIds(Integer userId);

    /**
     * 流程候选人
     */
    List<UserNamePto> queryCandidates(String tenantId, Integer userId);

    /**
     * 用户名称（按id）
     */
    String queryNameById(Integer userId);

    /**
     * 用户名称（按code）
     */
    String queryNameByCode(String userCode);

    /**
     * 批量用户名称
     */
    List<String> queryNamesById(String tenantId, List<Integer> userIds);

    /**
     * 用户编码-名称映射
     */
    Map<String, String> queryCodeNameMap(Collection<String> userCodes);

    /**
     * 用户选项（成员选择器）
     */
    Page<HubUser> queryUserOptions(String tenantId, UserMemberQuery query);

    /**
     * 按账号查询
     */
    HubUser queryByAccount(String tenantId, UserType userType, String userAccount);

    /**
     * 按编码查询
     */
    HubUser queryByCode(String userCode);

    /**
     * 账号冲突检测
     */
    long countByAccount(String tenantId, UserType userType, String userAccount, Integer userId);

    /**
     * 查询用户角色id列表
     */
    List<Integer> queryUserRoleIdsByUserId(Integer userId);

    /**
     * 租户管理员列表
     */
    Page<TenantManagerPto> queryTenantManager(String tenantId);

    /**
     * 个人信息
     */
    UserProfile queryUserProfile(Integer userId);
}
