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
package com.cowave.hub.admin.service.rbac;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rbac.entity.query.UserExportQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.UserMemberQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.UserQuery;
import com.cowave.hub.admin.domain.rbac.entity.command.*;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.hub.admin.domain.rbac.entity.SysUser;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserListPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserNamePto;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysUserService {

    /**
     * 列表
     */
    Response.Page<UserListPto> list(String tenantId, UserQuery query);

    /**
     * 详情
     */
    UserInfoPto info(String tenantId, Integer userId);

    /**
     * 新增
     */
    void create(String tenantId, UserCreate user);

    /**
     * 删除
     */
    void delete(String tenantId, List<Integer> userIds);

    /**
     * 修改
     */
    void edit(String tenantId, UserCreate user);

    /**
     * 修改角色
     */
    void changeRoles(String tenantId, UserRoleUpdate user);

    /**
     * 修改状态
     */
    void changeStatus(String tenantId, UserStatusUpdate user);

    /**
     * 修改密码
     */
    void changePasswd(String tenantId, UserPasswdUpdate user);

    /**
     * 导入用户
     */
    void importUsers(String tenantId, List<SysUser> list, boolean overwrite);

    /**
     * 导出用户
     */
    List<SysUser> queryListForExport(String tenantId, UserExportQuery userExport);

    /**
     * 用户组织架构
     */
    Tree<Integer> queryDiagram(String tenantId);

    /**
     * 用户流程候选人
     */
    List<UserNamePto> queryUserCandidates(String tenantId, Integer userId);

    /**
     * 用户名称查询
     */
    List<String> queryNamesById(String tenantId, List<Integer> userIds);

    /**
     * 成员列表选项
     */
    Page<SysUser> queryUserOptions(String tenantId, UserMemberQuery query);
}
