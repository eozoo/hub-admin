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
package com.cowave.hub.admin.service.auth.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.auth.biz.SysLdapBiz;
import com.cowave.hub.admin.domain.auth.entity.SysLdap;
import com.cowave.hub.admin.domain.auth.entity.SysLdapUser;
import com.cowave.hub.admin.domain.auth.repository.facade.SysLdapRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.biz.SysUserBiz;
import com.cowave.hub.admin.domain.rbac.entity.*;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysRoleRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.domain.sys.biz.SysOperationBiz;
import com.cowave.hub.admin.domain.auth.repository.facade.UserDetailsRepositoryFacade;
import com.cowave.hub.admin.service.auth.LdapService;
import com.cowave.hub.admin.service.auth.remote.LdapRemoteService;
import com.cowave.zoo.framework.access.operation.OperationInfo;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.access.security.BearerTokenService;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.http.client.asserts.HttpException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.cowave.hub.admin.domain.rbac.enums.UserType.LDAP;
import static com.cowave.hub.admin.domain.sys.enums.OpAction.LOGIN;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_AUTH;
import static com.cowave.zoo.http.client.constants.HttpCode.*;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class LdapServiceImpl implements LdapService {
    private final LdapRemoteService ldapRemoteService;
    private final BearerTokenService bearerTokenService;
    private final SysOperationBiz operationBiz;
    private final SysUserBiz userBiz;
    private final SysLdapBiz ldapBiz;
    private final SysLdapRepositoryFacade ldapRepositoryFacade;
    private final SysTenantRepositoryFacade tenantRepositoryFacade;
    private final SysUserRepositoryFacade userRepositoryFacade;
    private final SysRoleRepositoryFacade roleRepositoryFacade;
    private final UserDetailsRepositoryFacade userDetailsRepositoryFacade;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AccessUserDetails authenticate(String tenantId, String userAccount, String passWord) {
        SysLdap sysLdap = ldapRepositoryFacade.queryById(tenantId);
        if (sysLdap == null || sysLdap.getLdapStatus() == 0) {
            throw new HttpException(FORBIDDEN, "ldap认证不支持");
        }

        String filter = "(&(objectClass=" + sysLdap.getUserClass() + ")(" + sysLdap.getAccountProperty() + "=" + userAccount + "))";
        boolean isAuthenticated = ldapRemoteService.authenticate(sysLdap, filter, passWord);
        HttpAsserts.isTrue(isAuthenticated, UNAUTHORIZED, "{frame.auth.pass.invalid}");

        List<SysLdapUser> list = ldapRemoteService.searchUser(sysLdap, filter);
        HttpAsserts.isTrue(list.size() == 1, FORBIDDEN, "{admin.ldap.failed.user}");
        SysLdapUser newUser = list.get(0);

        // Ldap用户信息
        SysLdapUser ldapUser = ldapRepositoryFacade.queryUserByAccount(tenantId, newUser.getUserAccount());
        if (ldapUser != null) {
            ldapUser.setUserName(newUser.getUserName());
            ldapUser.setUserPhone(newUser.getUserPhone());
            ldapUser.setUserEmail(newUser.getUserEmail());
            ldapUser.setUserPost(newUser.getUserPost());
            ldapUser.setUserDept(newUser.getUserDept());
            ldapUser.setUserLeader(newUser.getUserLeader());
            ldapUser.setUpdateTime(new Date());
            ldapBiz.updateLdapUserById(ldapUser);
        } else {
            ldapUser = newUser;
            ldapUser.setUserPasswd(passWord);
            ldapUser.setTenantId(sysLdap.getTenantId());
            ldapBiz.saveLdapUser(ldapUser);
        }

        // 对应的SysUser信息
        String userCode = LDAP.newCode(tenantId, newUser.getUserAccount());
        SysUser sysUser = userRepositoryFacade.queryByCode(userCode);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setUserCode(userCode);
            sysUser.setTenantId(sysLdap.getTenantId());
            sysUser.setUserType(LDAP);
            sysUser.setUserAccount(newUser.getUserAccount());
            sysUser.setUserName(newUser.getUserName());
            sysUser.setUserPhone(newUser.getUserPhone());
            sysUser.setUserEmail(newUser.getUserEmail());
            userBiz.saveUser(sysUser);
            SysRole sysRole = roleRepositoryFacade.queryByCode(tenantId, sysLdap.getRoleCode());
            if (sysRole != null) {
                userBiz.saveUserRole(sysUser.getUserId(), sysRole.getRoleId());
            }
            userBiz.saveUserDiagram(sysUser.getUserId(), 0, sysLdap.getTenantId());
        } else {
            sysUser.setUserName(newUser.getUserName());
            sysUser.setUserPhone(newUser.getUserPhone());
            sysUser.setUserEmail(newUser.getUserEmail());
            userBiz.updateLdapUser(sysUser);
        }

        SysTenant sysTenant = tenantRepositoryFacade.queryById(tenantId);
        AccessUserDetails userDetails = userDetailsRepositoryFacade.queryUserDetails(sysTenant, sysUser, true);
        bearerTokenService.assignAccessRefreshToken(userDetails);

        OperationInfo operationInfo = OperationInfo.builder()
                .success(true)
                .opModule(SYSTEM)
                .opType(SYSTEM_AUTH)
                .opAction(LOGIN)
                .desc("LDAP登录：" + userAccount)
                .build();
        operationBiz.createOperation(operationInfo, null);
        return userDetails;
    }

    @Override
    public void validConfig(SysLdap sysLdap) {
        ldapRemoteService.authenticate(sysLdap, "(objectClass=*)", sysLdap.getLdapPasswd());
    }

    @Override
    public SysLdap getLdap(String tenantId) {
        return ldapRepositoryFacade.queryById(tenantId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editLdap(String tenantId, SysLdap sysLdap) {
        sysLdap.setTenantId(tenantId);
        ldapBiz.editLdap(sysLdap);
    }

    @Override
    public Page<SysLdapUser> listUser(String tenantId, String ldapAccount) {
        return ldapRepositoryFacade.queryUserPage(tenantId, ldapAccount);
    }
}
