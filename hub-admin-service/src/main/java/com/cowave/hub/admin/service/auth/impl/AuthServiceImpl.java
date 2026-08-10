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

import com.cowave.hub.admin.domain.sys.biz.HubAttachBiz;
import com.cowave.hub.admin.service.auth.AuthService;

import com.cowave.hub.admin.domain.rbac.entity.*;
import com.cowave.hub.admin.domain.sys.repository.facade.HubConfigRepositoryFacade;
import com.cowave.hub.admin.domain.sys.biz.HubNoticeBiz;
import com.cowave.hub.admin.service.auth.support.MfaAuthVerifier;
import com.cowave.hub.admin.service.auth.support.MfaConfiguration;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.http.client.asserts.HttpHintException;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.operation.OperationInfo;
import com.cowave.zoo.framework.access.security.*;
import com.cowave.zoo.framework.helper.redis.RedisHelper;
import com.cowave.hub.admin.domain.auth.entity.HubOAuthUser;
import com.cowave.hub.admin.domain.auth.entity.command.UserRegister;
import com.cowave.hub.admin.domain.auth.entity.vo.AuthVo;
import com.cowave.hub.admin.domain.auth.entity.vo.OnlineAccess;
import com.cowave.hub.admin.domain.auth.entity.vo.OnlineVo;
import com.cowave.hub.admin.domain.sys.entity.HubAttach;
import com.cowave.hub.admin.domain.rbac.entity.vo.Route;
import com.cowave.hub.admin.domain.rbac.entity.vo.RouteMeta;
import com.cowave.hub.admin.domain.auth.repository.facade.UserDetailsRepositoryFacade;
import com.cowave.hub.admin.domain.sys.biz.HubOperationBiz;
import com.cowave.hub.admin.domain.auth.repository.facade.HubOAuthRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubRoleRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.biz.HubUserBiz;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubUserRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubMenuRepositoryFacade;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.cowave.hub.admin.domain.rbac.enums.UserType.GITLAB;
import static com.cowave.hub.admin.domain.rbac.enums.UserType.SYS;
import static com.cowave.zoo.framework.access.security.BearerTokenDelegate.CLAIM_TENANT_ID;
import static com.cowave.zoo.framework.access.security.BearerTokenDelegate.CLAIM_USER_ACCOUNT;
import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.zoo.http.client.constants.HttpCode.FORBIDDEN;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_FAILS;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_LOCK;
import static com.cowave.hub.admin.domain.sys.enums.AttachType.AVATAR;
import static com.cowave.hub.admin.domain.rbac.enums.EnableStatus.ENABLE;
import static com.cowave.hub.admin.domain.sys.enums.OpAction.LOGIN;
import static com.cowave.hub.admin.domain.sys.enums.OpAction.LOGOUT_FORCE;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.*;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final BearerTokenService bearerTokenService;
    private final HubOperationBiz operationBiz;
    private final RedisHelper redisHelper;
    private final MfaConfiguration mfaConfiguration;
    private final HubUserBiz userBiz;
    private final HubNoticeBiz noticeBiz;
    private final HubAttachBiz attachBiz;
    private final HubUserRepositoryFacade userRepositoryFacade;
    private final HubRoleRepositoryFacade roleRepositoryFacade;
    private final HubMenuRepositoryFacade menuRepositoryFacade;
    private final HubConfigRepositoryFacade configRepositoryFacade;
    private final HubOAuthRepositoryFacade oauthRepositoryFacade;
    private final HubTenantRepositoryFacade tenantRepositoryFacade;
    private final UserDetailsRepositoryFacade userDetailsRepositoryFacade;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String register(UserRegister userRegister) {
        String tenantId = userRegister.getTenantId();

        boolean registerOnOff = configRepositoryFacade.queryConfigValue(tenantId, "hub.registerOnOff");
        HttpAsserts.isTrue(registerOnOff, FORBIDDEN, "{admin.register.disable}");

        String userCode = SYS.newCode(tenantId, userRegister.getUserAccount());
        String initPasswd = configRepositoryFacade.queryConfigValue(tenantId, "hub.initPassword");
        HubUser hubUser = new HubUser();
        hubUser.setTenantId(tenantId);
        hubUser.setUserType(SYS);
        hubUser.setUserStatus(ENABLE);
        hubUser.setUserCode(userCode);
        hubUser.setUserEmail(userRegister.getUserEmail());
        hubUser.setUserName(userRegister.getUserName());
        hubUser.setUserAccount(userRegister.getUserAccount());
        hubUser.setUserPasswd(passwordEncoder.encode(initPasswd));
        userBiz.createTenantManager(hubUser);

        HubRole hubRole = roleRepositoryFacade.queryByCode(tenantId, "role-readonly");
        if(hubRole != null) {
            userBiz.saveUserRole(hubUser.getUserId(), hubRole.getRoleId());
        }

        // 注册用户的通知消息
        noticeBiz.initNoticeMsgForNewUser(userCode);
        noticeBiz.updateNoticeStatForNewUser();
        return initPasswd;
    }

    @Override
    public AccessUserDetails login(String tenantId, String userAccount, String passwd) {
        Long lockTime = redisHelper.getExpire(AUTH_LOCK.formatted(tenantId, userAccount));
        if (lockTime != null && lockTime > 0) {
            long minutes = (lockTime + 59) / 60;
            throw new HttpHintException(BAD_REQUEST, "{admin.auth.locked}", minutes);
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new TenantUsernamePasswordAuthenticationToken(tenantId, userAccount, passwd));
            AccessUserDetails userDetails = (AccessUserDetails) authentication.getPrincipal();
            // 如果有MFA，需要二次认证
            if (!userDetails.isMfaRequired()) {
                bearerTokenService.assignAccessRefreshToken(userDetails);
                // 登录日志
                OperationInfo operationInfo = OperationInfo.builder()
                        .success(true)
                        .opModule(SYSTEM)
                        .opType(SYSTEM_AUTH)
                        .opAction(LOGIN)
                        .desc("用户登录：" + userAccount)
                        .build();
                operationBiz.createOperation(operationInfo, null);
            }
            return userDetails;
        } catch (BadCredentialsException e) {
            // 5min内最多允许尝试5次密码，否则锁定30min
            Long failCount = redisHelper.incrementValue(AUTH_FAILS.formatted(tenantId, userAccount), 1);
            if (failCount == 1) {
                redisHelper.expire(AUTH_FAILS.formatted(tenantId, userAccount), 300, TimeUnit.SECONDS);
            }
            if (failCount >= 5) {
                redisHelper.putExpire(AUTH_LOCK.formatted(tenantId, userAccount), "-", 1800, TimeUnit.SECONDS);
                throw new HttpHintException(BAD_REQUEST, "{admin.auth.locked}", 30);
            }
            throw new HttpHintException(BAD_REQUEST, "{admin.auth.failed}", 5 - failCount);
        }
    }

    @Override
    public AccessUserDetails mfa(String mfaToken, String mfaCode) {
        Claims claims = mfaConfiguration.parseMfaToken(mfaToken);
        String tenantId = (String) claims.get(CLAIM_TENANT_ID);
        String userAccount = (String) claims.get(CLAIM_USER_ACCOUNT);
        HubUser hubUser = userRepositoryFacade.queryByAccount(tenantId, SYS, userAccount);

        String mfaKey = hubUser.getMfa();
        HttpAsserts.isTrue(MfaAuthVerifier.validateCode(mfaKey, mfaCode), BAD_REQUEST, "{admin.mfa.code.invalid}");

        HubTenant hubTenant = tenantRepositoryFacade.queryById(tenantId);
        AccessUserDetails userDetails = userDetailsRepositoryFacade.queryUserDetails(SYS, hubTenant, hubUser, true);
        bearerTokenService.assignAccessRefreshToken(userDetails);
        return userDetails;
    }

    @Override
    public void logout() throws IOException {
        bearerTokenService.revoke();
    }

    @Override
    public List<OnlineVo> onlineList() {
        List<AccessTokenInfo> accesslist = bearerTokenService.listAccessToken(Access.tenantId());
        List<OnlineAccess> accessGrantList = com.cowave.zoo.tools.Collections.copyToList(accesslist, OnlineAccess::new);
        Map<String, List<OnlineAccess>> accessMap = com.cowave.zoo.tools.Collections.groupToMap(accessGrantList,
                grant -> grant.getGrantType() + grant.getUserAccount());

        List<RefreshTokenInfo> oauthList = bearerTokenService.listOauthToken(Access.tenantId());
        List<OnlineAccess> oauthGrantList = com.cowave.zoo.tools.Collections.copyToList(oauthList, OnlineAccess::new);
        Map<String, List<OnlineAccess>> oauthMap = com.cowave.zoo.tools.Collections.groupToMap(oauthGrantList,
                grant -> grant.getGrantType() + grant.getUserAccount());

        List<OnlineVo> onlineList = new ArrayList<>();
        List<RefreshTokenInfo> refreshList = bearerTokenService.listRefreshToken(Access.tenantId());
        refreshList.sort(Comparator.comparing(RefreshTokenInfo::getLoginTime).reversed());
        for (RefreshTokenInfo refresh : refreshList) {
            List<OnlineAccess> accessGrants = accessMap.get(refresh.getAuthType() + refresh.getUserAccount());
            List<OnlineAccess> oauthGrants = oauthMap.get(refresh.getAuthType() + refresh.getUserAccount());
            List<OnlineAccess> grantList = new ArrayList<>();
            grantList.addAll(Optional.ofNullable(accessGrants).orElse(Collections.emptyList()));
            grantList.addAll(Optional.ofNullable(oauthGrants).orElse(Collections.emptyList()));
            onlineList.add(OnlineVo.builder()
                    .refreshId(refresh.getRefreshId())
                    .authType(refresh.getAuthType())
                    .userAccount(refresh.getUserAccount())
                    .userName(refresh.getUserName())
                    .cluster(refresh.getClusterName())
                    .loginIp(refresh.getLoginIp())
                    .loginTime(refresh.getLoginTime())
                    .accessList(grantList)
                    .build());
        }
        return onlineList;
    }

    @Override
    public void revokeAccess(String tenantId, String authType, String userAccount, String accessId) {
        bearerTokenService.revokeAccessToken(tenantId, authType, userAccount, accessId);
    }

    @Override
    public void revokeRefresh(String tenantId, String authType, String userAccount) {
        bearerTokenService.revokeRefreshToken(tenantId, authType, userAccount);
        // 强退日志
        OperationInfo operationInfo = OperationInfo.builder()
                .success(true)
                .opModule(SYSTEM)
                .opType(SYSTEM_AUTH)
                .opAction(LOGOUT_FORCE)
                .desc("强制退出：" + userAccount)
                .build();
        operationBiz.createOperation(operationInfo, null);
    }

    @Override
    public AccessUserDetails refresh(String refreshToken) throws Exception{
        return bearerTokenService.refreshAccessRefreshToken(refreshToken);
    }

    @Override
    public AuthVo getAuth() throws Exception {
        AccessUserDetails userDetails = Access.userDetails();
        Integer userId = userDetails.getUserId();

        AuthVo authVo = new AuthVo();
        authVo.setUserId(userId);
        authVo.setUserName(userDetails.getUserNick());
        authVo.setRoles(userDetails.getRoles());
        authVo.setPermissions(userDetails.getPermissions());

        String tenantId = userDetails.getTenantId();
        HubTenant hubTenant = tenantRepositoryFacade.queryById(tenantId);
        authVo.setTenantId(tenantId);
        authVo.setTenantTitle(hubTenant.getTitle());
        authVo.setTenantLogo(hubTenant.getLogo());

        // Avatar
        if (GITLAB.equalsVal(userDetails.getAuthType())) {
            HubOAuthUser oauthUser =
                    oauthRepositoryFacade.queryUserByAccount(tenantId, GITLAB.getVal(), userDetails.getUsername());
            authVo.setAvatar(oauthUser.getUserAvatar());
        } else if (SYS.equalsVal(userDetails.getAuthType())) {
            HubAttach avatar = attachBiz.previewLatestByOwner(String.valueOf(userId), SYSTEM_USER, AVATAR);
            if (avatar != null) {
                authVo.setAvatar(avatar.getViewUrl());
            }
        }
        return authVo;
    }

    @Override
    public List<Route> menus(){
        List<HubMenu> menuList;
        if(Access.isAdminUser()){
            menuList = menuRepositoryFacade.queryMenusByAdmin(Access.tenantId());
        }else{
            List<String> userRoles = Access.userRoles();
            if(CollectionUtils.isEmpty(userRoles)){
                menuList = menuRepositoryFacade.queryMenusInPublic(Access.tenantId());
            } else {
                menuList = menuRepositoryFacade.queryMenusByRoles(Access.tenantId(), userRoles);
            }
        }

        if(menuList.isEmpty()){
            return Collections.emptyList();
        }

        List<HubMenu> rootMenus = new ArrayList<>();
        for(HubMenu menu : menuList){
            if (menu.getParentId() == 0) {
                recursionFn(menuList, menu);
                rootMenus.add(menu);
            }
        }
        return buildRoutes(rootMenus);
    }

    private void recursionFn(List<HubMenu> list, HubMenu menu) {
        List<HubMenu> childList = getChildList(list, menu);
        menu.setChildren(childList);
        for (HubMenu child : childList) {
            if (hasChild(list, child)) {
                recursionFn(list, child);
            }
        }
    }

    private boolean hasChild(List<HubMenu> list, HubMenu t) {
        return !getChildList(list, t).isEmpty();
    }

    private List<HubMenu> getChildList(List<HubMenu> list, HubMenu parent) {
        List<HubMenu> children = new ArrayList<>();
        for (HubMenu child : list) {
            if (child.getParentId().equals(parent.getMenuId())) {
                children.add(child);
            }
        }
        return children;
    }

    private List<Route> buildRoutes(List<HubMenu> menus){
        List<Route> routes = new LinkedList<>();
        for (HubMenu menu : menus) {
            Route route = new Route();
            route.setHidden("L".equals(menu.getMenuType())); // 链接不展示在菜单
            route.setName(menu.routeName());
            route.setPath(menu.routePath());
            route.setComponent(menu.routeComponent());
            route.setQuery(menu.getMenuParam());
            route.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), false, menu.getMenuPath()));

            List<HubMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && "M".equals(menu.getMenuType())) {
                route.setAlwaysShow(true);
                route.setRedirect("noRedirect");
                route.setChildren(buildRoutes(cMenus));
            } else if (menu.ifMenuFrame()) {
                route.setMeta(null);
                List<Route> childrenList = new ArrayList<>();
                Route children = new Route();
                children.setPath(menu.getMenuPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getMenuPath()));
                children.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), false, menu.getMenuPath()));
                children.setQuery(menu.getMenuParam());
                childrenList.add(children);
                route.setChildren(childrenList);
            } else if (menu.getParentId() == 0L && menu.ifInnerLink()) {
                route.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon()));
                route.setPath("/");
                List<Route> childrenList = new ArrayList<>();
                Route children = new Route();
                String routerPath = menu.getMenuPath();
                routerPath = routerPath.replace("http://", "");
                routerPath = routerPath.replace("https://", "");
                children.setPath(routerPath);
                children.setComponent("InnerLink");
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), menu.getMenuPath()));
                childrenList.add(children);
                route.setChildren(childrenList);
            }
            routes.add(route);
        }
        return routes;
    }
}
