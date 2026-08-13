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
package com.cowave.hub.admin.service.rbac.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rbac.biz.SysUserBiz;
import com.cowave.hub.admin.domain.rbac.entity.SysUser;
import com.cowave.hub.admin.domain.rbac.entity.command.*;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserListPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserNamePto;
import com.cowave.hub.admin.domain.rbac.entity.query.UserExportQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.UserMemberQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.UserQuery;
import com.cowave.hub.admin.domain.rbac.enums.UserType;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.domain.sys.repository.facade.SysConfigRepositoryFacade;
import com.cowave.hub.admin.service.rbac.SysUserService;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.operation.OperationContext;
import com.cowave.zoo.framework.access.security.BearerTokenService;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.http.client.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.cowave.hub.admin.domain.AdminRedisKeys.DEPT_USER_DIAGRAM;
import static com.cowave.hub.admin.domain.AdminRedisKeys.USER_DIAGRAM;
import static com.cowave.zoo.http.client.constants.HttpCode.*;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {
    private final SysUserBiz userBiz;
    private final SysUserRepositoryFacade userRepositoryFacade;
	private final SysConfigRepositoryFacade configRepositoryFacade;
	private final PasswordEncoder passwordEncoder;
    private final BearerTokenService bearerTokenService;

    @Override
    public Response.Page<UserListPto> list(String tenantId, UserQuery query) {
        if (query.getDeptId() == 0) {
            return new Response.Page<>(userRepositoryFacade.queryList(tenantId, query),
                    userRepositoryFacade.countList(tenantId, query));
        } else {
            return new Response.Page<>(userRepositoryFacade.queryListOfDept(tenantId, query),
                    userRepositoryFacade.countListOfDept(tenantId, query));
        }
    }

    @Override
    public UserInfoPto info(String tenantId, Integer userId) {
        return userRepositoryFacade.queryInfo(tenantId, userId);
    }

    @CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#tenantId")
    @Override
    public void create(String tenantId, UserCreate user) {
        String userAccount = user.getUserAccount();
        String userPasswd = user.getUserPasswd();
        HttpAsserts.notNull(userPasswd, BAD_REQUEST, "{admin.user.passwd.null}");

        long accountCount = userRepositoryFacade.countByAccount(tenantId, UserType.SYS, userAccount, null);
        HttpAsserts.isTrue(accountCount == 0, BAD_REQUEST, "{admin.user.account.conflict}", userAccount);

        user.setUserType(UserType.SYS);
        user.setUserCode(UserType.SYS.newCode(tenantId, userAccount));
        user.setUserPasswd(passwordEncoder.encode(userPasswd));
        userBiz.createUser(user);
    }

    @CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#tenantId")
    @Override
    public void delete(String tenantId, List<Integer> userIds) {
        List<UserInfoPto> list = new ArrayList<>();
        for (Integer userId : userIds) {
            UserInfoPto deleteUser = deleteUser(tenantId, userId);
            if (deleteUser != null) {
                list.add(deleteUser);
            }
        }
        OperationContext.prepareContent(list);
    }

    private UserInfoPto deleteUser(String tenantId, Integer userId) {
        UserInfoPto preUser = userRepositoryFacade.queryInfo(tenantId, userId);
        if (preUser == null) {
            return null;
        }
        HttpAsserts.notEquals(Access.userAccount(), preUser.getUserAccount(), FORBIDDEN, "{admin.user.forbid.self.delete}");
        userBiz.deleteUser(tenantId, userId);
        bearerTokenService.revokeRefreshToken(tenantId, preUser.getUserType().getVal(), preUser.getUserAccount());
        return preUser;
    }

    @CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#tenantId")
    @Override
    public void edit(String tenantId, UserCreate user) {
        Integer userId = user.getUserId();
        HttpAsserts.notNull(userId, BAD_REQUEST, "{admin.user.id.null}");

        String userAccount = user.getUserAccount();
        long accountCount = userRepositoryFacade.countByAccount(tenantId, UserType.SYS, userAccount, userId);
        HttpAsserts.isTrue(accountCount == 0, BAD_REQUEST, "{admin.user.account.conflict}", userAccount);

        UserInfoPto preUser = userRepositoryFacade.queryInfo(tenantId, userId);
        HttpAsserts.notNull(preUser, NOT_FOUND, "{admin.user.not.exist}", userId);
        OperationContext.prepareContent(preUser);

        userBiz.editUser(tenantId, user);
    }

    @Override
    public void changeRoles(String tenantId, UserRoleUpdate user) {
        userBiz.changeRoles(tenantId, user);
    }

    @Override
    public void changeStatus(String tenantId, UserStatusUpdate user) {
        userBiz.changeStatus(tenantId, user.getUserId(), user.getUserStatus());
    }

    @Override
    public void changePasswd(String tenantId, UserPasswdUpdate user) {
        String newPasswd = passwordEncoder.encode(user.getUserPasswd());
        userBiz.changePasswd(user.getUserId(), newPasswd);
    }

    @CacheEvict(value = {USER_DIAGRAM, DEPT_USER_DIAGRAM}, key = "#tenantId")
    @Override
    public void importUsers(String tenantId, List<SysUser> list, boolean overwrite) {
        String passwd = configRepositoryFacade.queryConfigValue(tenantId, "hub.initPassword");
        for (SysUser sysUser : list) {
            sysUser.setTenantId(tenantId);
            sysUser.setUserType(UserType.SYS);
            sysUser.setUserCode(UserType.SYS.newCode(tenantId, sysUser.getUserAccount()));
            sysUser.setUserPasswd(passwordEncoder.encode(passwd));
            sysUser.setCreateBy(Access.userCode());
            sysUser.setCreateTime(Access.accessTime());
            sysUser.setUpdateBy(Access.userCode());
            sysUser.setUpdateTime(Access.accessTime());
        }
        userBiz.batchInsert(list, overwrite);
    }

    @Override
    public List<SysUser> queryListForExport(String tenantId, UserExportQuery userExport) {
        return userRepositoryFacade.queryListForExport(tenantId, userExport);
    }

    @Override
    public Tree<Integer> queryDiagram(String tenantId) {
        return userRepositoryFacade.queryDiagram(tenantId);
    }

    @Override
    public List<UserNamePto> queryUserCandidates(String tenantId, Integer userId) {
        if (userId == null) {
            userId = Access.userId();
        }
        return userRepositoryFacade.queryCandidates(tenantId, userId);
    }

    @Override
    public List<String> queryNamesById(String tenantId, List<Integer> userIds) {
        return userRepositoryFacade.queryNamesById(tenantId, userIds);
    }

    @Override
    public Page<SysUser> queryUserOptions(String tenantId, UserMemberQuery query) {
        return userRepositoryFacade.queryUserOptions(tenantId, query);
    }
}
