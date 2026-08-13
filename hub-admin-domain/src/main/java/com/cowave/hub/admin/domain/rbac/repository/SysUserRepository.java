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
package com.cowave.hub.admin.domain.rbac.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cowave.hub.admin.domain.auth.entity.command.ProfileUpdate;
import com.cowave.hub.admin.domain.rbac.entity.*;
import com.cowave.hub.admin.domain.rbac.entity.command.TenantManagerRemove;
import com.cowave.hub.admin.domain.rbac.entity.command.UserCreate;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysUserRepositoryFacade;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysUserRepository extends SysUserRepositoryFacade, IService<SysUser> {

    /**
     * 更新用户
     */
    void updateUser(UserCreate user);

    /**
     * 更新用户状态
     */
    void updateStatusById(Integer userId, EnableStatus status);

    /**
     * 更新密码
     */
    void updatePasswdById(Integer userId, String passwd);

    /**
     * 删除用户角色
     */
    void removeUserRolesByUserId(Integer userId);

    /**
     * 删除用户部门岗位
     */
    void removeUserDeptsByUserId(Integer userId);

    /**
     * 删除上级用户关系
     */
    void removeUserDiagramParentsByUserId(Integer userId);

    /**
     * 删除下级用户关系
     */
    void removeUserDiagramChildrenByUserId(Integer userId);

    /**
     * 批量保存用户角色
     */
    void saveUserRolesBatch(List<SysUserRole> list);

    /**
     * 批量保存用户部门岗位
     */
    void saveUserDeptsBatch(List<SysUserDept> list);

    /**
     * 批量保存用户上下级关系
     */
    void saveUserDiagramBatch(List<SysUserDiagram> list);

    /**
     * 批量导入用户
     */
    void batchInsert(List<SysUser> list, boolean overwrite);

    /**
     * 移除租户管理员
     */
    void removeTenantManager(TenantManagerRemove managerRemove);

    /**
     * 修改个人信息
     */
    void updateProfileById(Integer userId, ProfileUpdate profile);

    /**
     * 绑定MFA
     */
    void setMfa(Integer userId, String mfaKey);

    /**
     * 解绑MFA
     */
    void deleteMfa(Integer userId);

    /**
     * 更新LDAP用户信息（按userCode）
     */
    void updateLdapByCode(SysUser sysUser);

    /**
     * 更新Gitlab用户信息（按userCode）
     */
    void updateGitlabByCode(SysUser sysUser);
}
