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
import com.cowave.hub.admin.domain.auth.biz.HubLdapBiz;
import com.cowave.hub.admin.domain.auth.entity.HubLdap;
import com.cowave.hub.admin.domain.auth.entity.HubLdapUser;
import com.cowave.hub.admin.domain.auth.repository.facade.HubLdapRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.biz.HubUserBiz;
import com.cowave.hub.admin.domain.rbac.entity.*;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubRoleRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubUserRepositoryFacade;
import com.cowave.hub.admin.domain.sys.biz.HubOperationBiz;
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
    private final HubOperationBiz operationBiz;
    private final HubUserBiz userBiz;
    private final HubLdapBiz ldapBiz;
    private final HubLdapRepositoryFacade ldapRepositoryFacade;
    private final HubTenantRepositoryFacade tenantRepositoryFacade;
    private final HubUserRepositoryFacade userRepositoryFacade;
    private final HubRoleRepositoryFacade roleRepositoryFacade;
    private final UserDetailsRepositoryFacade userDetailsRepositoryFacade;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AccessUserDetails authenticate(String tenantId, String userAccount, String passWord) {
        HubLdap hubLdap = ldapRepositoryFacade.queryById(tenantId);
        if (hubLdap == null || hubLdap.getLdapStatus() == 0) {
            throw new HttpException(FORBIDDEN, "ldap认证不支持");
        }

        String filter = "(&(objectClass=" + hubLdap.getUserClass() + ")(" + hubLdap.getAccountProperty() + "=" + userAccount + "))";
        boolean isAuthenticated = ldapRemoteService.authenticate(hubLdap, filter, passWord);
        HttpAsserts.isTrue(isAuthenticated, UNAUTHORIZED, "{frame.auth.pass.invalid}");

        List<HubLdapUser> list = ldapRemoteService.searchUser(hubLdap, filter);
        HttpAsserts.isTrue(list.size() == 1, FORBIDDEN, "{admin.ldap.failed.user}");
        HubLdapUser newUser = list.get(0);

        HubLdapUser hubLdapUser = ldapRepositoryFacade.queryUserByAccount(tenantId, newUser.getUserAccount());
        if (hubLdapUser != null) {
            hubLdapUser.setUserName(newUser.getUserName());
            hubLdapUser.setUserPhone(newUser.getUserPhone());
            hubLdapUser.setUserEmail(newUser.getUserEmail());
            hubLdapUser.setUserPost(newUser.getUserPost());
            hubLdapUser.setUserDept(newUser.getUserDept());
            hubLdapUser.setUserLeader(newUser.getUserLeader());
            hubLdapUser.setUpdateTime(new Date());
            ldapBiz.updateLdapUserById(hubLdapUser);
        } else {
            hubLdapUser = newUser;
            hubLdapUser.setUserPasswd(passWord);
            hubLdapUser.setTenantId(hubLdap.getTenantId());
            ldapBiz.saveLdapUser(hubLdapUser);
        }

        String userCode = LDAP.newCode(tenantId, newUser.getUserAccount());
        HubUser hubUser = userRepositoryFacade.queryByCode(userCode);
        if (hubUser == null) {
            hubUser = new HubUser();
            hubUser.setUserCode(userCode);
            hubUser.setTenantId(hubLdap.getTenantId());
            hubUser.setUserType(LDAP);
            hubUser.setUserAccount(newUser.getUserAccount());
            hubUser.setUserName(newUser.getUserName());
            hubUser.setUserPhone(newUser.getUserPhone());
            hubUser.setUserEmail(newUser.getUserEmail());
            userBiz.createTenantManager(hubUser);
            HubRole hubRole = roleRepositoryFacade.queryByCode(tenantId, hubLdap.getRoleCode());
            if (hubRole != null) {
                userBiz.saveUserRole(hubUser.getUserId(), hubRole.getRoleId());
            }
            userBiz.saveUserDiagram(hubUser.getUserId(), 0, hubLdap.getTenantId());
        } else {
            hubUser.setUserName(newUser.getUserName());
            hubUser.setUserPhone(newUser.getUserPhone());
            hubUser.setUserEmail(newUser.getUserEmail());
            userBiz.updateLdapUser(hubUser);
        }

        HubTenant hubTenant = tenantRepositoryFacade.queryById(tenantId);
        AccessUserDetails userDetails = userDetailsRepositoryFacade.queryUserDetails(LDAP, hubTenant, hubUser, true);
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
    public void validConfig(HubLdap hubLdap) {
        ldapRemoteService.authenticate(hubLdap, "(objectClass=*)", hubLdap.getLdapPasswd());
    }

    @Override
    public HubLdap getLdap(String tenantId) {
        return ldapRepositoryFacade.queryById(tenantId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editLdap(String tenantId, HubLdap hubLdap) {
        hubLdap.setTenantId(tenantId);
        ldapBiz.editLdap(hubLdap);
    }

    @Override
    public Page<HubLdapUser> listUser(String tenantId, String ldapAccount) {
        return ldapRepositoryFacade.queryUserPage(tenantId, ldapAccount);
    }
}
