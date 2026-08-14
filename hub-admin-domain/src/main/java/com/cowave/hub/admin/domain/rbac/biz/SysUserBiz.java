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
package com.cowave.hub.admin.domain.rbac.biz;

import com.cowave.hub.admin.domain.rbac.entity.SysUser;
import com.cowave.hub.admin.domain.rbac.entity.SysUserDept;
import com.cowave.hub.admin.domain.rbac.entity.SysUserDiagram;
import com.cowave.hub.admin.domain.rbac.entity.SysUserRole;
import com.cowave.hub.admin.domain.rbac.entity.command.TenantManagerRemove;
import com.cowave.hub.admin.domain.rbac.entity.command.UserCreate;
import com.cowave.hub.admin.domain.auth.entity.command.ProfileUpdate;
import com.cowave.hub.admin.domain.rbac.entity.command.UserRoleUpdate;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;

import java.util.List;

/**
 * HubUser聚合根Command操作
 *
 * @see SysUser
 * @see SysUserRole
 * @see SysUserDept
 * @see SysUserDiagram
 *
 * @author shanhuiming
 */
public interface SysUserBiz {

    /**
     * 新增用户（含角色、部门岗位、上下级）
     */
    void createUser(UserCreate user);

    /**
     * 删除用户（含角色、部门岗位、上下级），返回被删除的用户信息
     */
    void deleteUser(String tenantId, Integer userId);

    /**
     * 修改用户（含角色、部门岗位、上下级）
     */
    void editUser(String tenantId, UserCreate user);

    /**
     * 修改用户角色
     */
    void changeRoles(String tenantId, UserRoleUpdate user);

    /**
     * 修改用户状态
     */
    void changeStatus(String tenantId, Integer userId, EnableStatus status);

    /**
     * 修改用户密码
     */
    void changePasswd(Integer userId, String encodedPasswd);

    /**
     * 批量导入用户
     */
    void batchInsert(List<SysUser> list, boolean overwrite);

    /**
     * 保存用户上下级关系（auth域创建用户时调用）
     */
    void saveUserDiagram(Integer userId, Integer parentId, String tenantId);

    /**
     * 保存用户角色关联（auth域创建用户时调用）
     */
    void saveUserRole(Integer userId, Integer roleId);

    /**
     * 保存用户（auth域创建用户时调用，仅基本保存无关联数据）
     */
    void saveUser(SysUser sysUser);

    /**
     * 移除租户管理员
     */
    void removeTenantManager(TenantManagerRemove managerRemove);

    /**
     * 修改个人信息
     */
    void updateProfile(Integer userId, ProfileUpdate profile);

    /**
     * 绑定MFA
     */
    void enableMfa(Integer userId, String mfaKey);

    /**
     * 解绑MFA
     */
    void disableMfa(Integer userId);

    /**
     * 更新LDAP用户信息
     */
    void updateLdapUser(SysUser sysUser);

    /**
     * 更新Gitlab用户信息
     */
    void updateGitlabUser(SysUser sysUser);
}
