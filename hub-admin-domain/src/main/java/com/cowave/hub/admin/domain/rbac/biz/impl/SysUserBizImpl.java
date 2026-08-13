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
package com.cowave.hub.admin.domain.rbac.biz.impl;

import com.cowave.hub.admin.domain.rbac.biz.SysUserBiz;
import com.cowave.hub.admin.domain.rbac.entity.SysUser;
import com.cowave.hub.admin.domain.rbac.entity.SysUserDiagram;
import com.cowave.hub.admin.domain.rbac.entity.SysUserRole;
import com.cowave.hub.admin.domain.auth.entity.command.ProfileUpdate;
import com.cowave.hub.admin.domain.rbac.entity.command.TenantManagerRemove;
import com.cowave.hub.admin.domain.rbac.entity.command.UserCreate;
import com.cowave.hub.admin.domain.rbac.entity.command.UserRoleUpdate;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserInfoPto;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.domain.rbac.repository.SysUserRepository;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.hub.admin.domain.AdminRedisKeys.DEPT_USER_DIAGRAM;
import static com.cowave.hub.admin.domain.AdminRedisKeys.USER_DIAGRAM;
import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.zoo.http.client.constants.HttpCode.NOT_FOUND;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysUserBizImpl implements SysUserBiz {

    private final SysUserRepository userRepository;

    @CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#user.tenantId")
    @Override
    public void createUser(UserCreate user) {
        userRepository.save(user);
        if (CollectionUtils.isNotEmpty(user.getUserRoles())) {
            userRepository.saveUserRolesBatch(user.getUserRoles());
        }
        if (CollectionUtils.isNotEmpty(user.getUserParents())) {
            userRepository.saveUserDiagramBatch(user.getUserParents());
        }
        if (CollectionUtils.isNotEmpty(user.getUserDeptPosts())) {
            userRepository.saveUserDeptsBatch(user.getUserDeptPosts());
        }
    }

    @CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#tenantId")
    @Override
    public UserInfoPto deleteUser(String tenantId, Integer userId) {
        UserInfoPto preUser = userRepository.queryInfo(tenantId, userId);
        if (preUser == null) {
            return null;
        }
        userRepository.removeById(userId);
        userRepository.removeUserRolesByUserId(userId);
        userRepository.removeUserDeptsByUserId(userId);
        userRepository.removeUserDiagramParentsByUserId(userId);
        userRepository.removeUserDiagramChildrenByUserId(userId);
        return preUser;
    }

    @CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#tenantId")
    @Override
    public void editUser(String tenantId, UserCreate user) {
        Integer userId = user.getUserId();
        HttpAsserts.notNull(userId, BAD_REQUEST, "{admin.user.id.null}");

        UserInfoPto preUser = userRepository.queryInfo(tenantId, userId);
        HttpAsserts.notNull(preUser, NOT_FOUND, "{admin.user.not.exist}", userId);

        userRepository.updateUser(user);

        userRepository.removeUserRolesByUserId(userId);
        if (CollectionUtils.isNotEmpty(user.getUserRoles())) {
            userRepository.saveUserRolesBatch(user.getUserRoles());
        }

        userRepository.removeUserDiagramParentsByUserId(userId);
        List<Integer> parentIds = user.getParentIds();
        if (CollectionUtils.isNotEmpty(parentIds)) {
            List<Integer> childIds = userRepository.queryChildUserIds(userId);
            childIds.add(userId);
            HttpAsserts.isTrue(java.util.Collections.disjoint(childIds, parentIds), BAD_REQUEST, "{admin.user.tree.cycle}");
            userRepository.saveUserDiagramBatch(user.getUserParents());
        }

        userRepository.removeUserDeptsByUserId(userId);
        if (CollectionUtils.isNotEmpty(user.getUserDeptPosts())) {
            userRepository.saveUserDeptsBatch(user.getUserDeptPosts());
        }
    }

    @Override
    public void changeRoles(String tenantId, UserRoleUpdate user) {
        UserInfoPto preUser = userRepository.queryInfo(tenantId, user.getUserId());
        HttpAsserts.notNull(preUser, NOT_FOUND, "{admin.user.not.exist}", user.getUserId());
        userRepository.removeUserRolesByUserId(user.getUserId());
        if (CollectionUtils.isNotEmpty(user.getUserRoles())) {
            userRepository.saveUserRolesBatch(user.getUserRoles());
        }
    }

    @Override
    public void changeStatus(String tenantId, Integer userId, EnableStatus status) {
        UserInfoPto preUser = userRepository.queryInfo(tenantId, userId);
        HttpAsserts.notNull(preUser, NOT_FOUND, "{admin.user.not.exist}", userId);
        userRepository.updateStatusById(userId, status);
    }

    @Override
    public void changePasswd(Integer userId, String encodedPasswd) {
        userRepository.updatePasswdById(userId, encodedPasswd);
    }

    @Override
    public void batchInsert(List<SysUser> list, boolean overwrite) {
        userRepository.batchInsert(list, overwrite);
    }

    @CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#tenantId")
    @Override
    public void saveUserDiagram(Integer userId, Integer parentId, String tenantId) {
        userRepository.saveUserDiagramBatch(List.of(new SysUserDiagram(userId, parentId, tenantId)));
    }

    @Override
    public void saveUserRole(Integer userId, Integer roleId) {
        userRepository.saveUserRolesBatch(List.of(new SysUserRole(userId, roleId)));
    }

    @Override
    public void createTenantManager(SysUser sysUser) {
        userRepository.save(sysUser);
    }

    @Override
    public void removeTenantManager(TenantManagerRemove managerRemove) {
        userRepository.removeTenantManager(managerRemove);
    }

    @Override
    public void updateProfile(Integer userId, ProfileUpdate profile) {
        userRepository.updateProfileById(userId, profile);
    }

    @Override
    public void enableMfa(Integer userId, String mfaKey) {
        userRepository.setMfa(userId, mfaKey);
    }

    @Override
    public void disableMfa(Integer userId) {
        userRepository.deleteMfa(userId);
    }

    @Override
    public void updateLdapUser(SysUser sysUser) {
        userRepository.updateLdapByCode(sysUser);
    }

    @Override
    public void updateGitlabUser(SysUser sysUser) {
        userRepository.updateGitlabByCode(sysUser);
    }
}
