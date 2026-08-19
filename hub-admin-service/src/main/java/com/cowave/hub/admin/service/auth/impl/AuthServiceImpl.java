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

import com.cowave.hub.admin.domain.sys.biz.SysAttachBiz;
import com.cowave.hub.admin.service.auth.AuthService;

import com.cowave.hub.admin.domain.rbac.entity.*;
import com.cowave.hub.admin.domain.sys.repository.facade.SysConfigRepositoryFacade;
import com.cowave.hub.admin.domain.sys.biz.SysNoticeBiz;
import com.cowave.hub.admin.service.auth.support.MfaAuthVerifier;
import com.cowave.hub.admin.service.auth.support.MfaConfiguration;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.http.client.asserts.HttpHintException;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.operation.OperationInfo;
import com.cowave.zoo.framework.access.security.*;
import com.cowave.zoo.framework.helper.redis.RedisHelper;
import com.cowave.hub.admin.domain.auth.entity.SysOAuthUser;
import com.cowave.hub.admin.domain.auth.entity.command.UserRegister;
import com.cowave.hub.admin.domain.auth.entity.vo.AuthVo;
import com.cowave.hub.admin.domain.auth.entity.vo.OnlineAccess;
import com.cowave.hub.admin.domain.auth.entity.vo.OnlineVo;
import com.cowave.hub.admin.domain.sys.entity.SysAttach;
import com.cowave.hub.admin.domain.rbac.entity.vo.Route;
import com.cowave.hub.admin.domain.rbac.entity.vo.RouteMeta;
import com.cowave.hub.admin.domain.auth.repository.facade.UserDetailsRepositoryFacade;
import com.cowave.hub.admin.domain.sys.biz.SysOperationBiz;
import com.cowave.hub.admin.domain.auth.enums.AuthType;
import com.cowave.hub.admin.domain.auth.repository.facade.SysOAuthRepositoryFacade;
import com.cowave.hub.admin.domain.home.entity.HubMember;
import com.cowave.hub.admin.domain.home.repository.facade.HubMemberRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysRoleRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.biz.SysUserBiz;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysMenuRepositoryFacade;
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
    private final SysOperationBiz operationBiz;
    private final RedisHelper redisHelper;
    private final MfaConfiguration mfaConfiguration;
    private final SysUserBiz userBiz;
    private final SysNoticeBiz noticeBiz;
    private final SysAttachBiz attachBiz;
    private final SysUserRepositoryFacade userRepositoryFacade;
    private final SysRoleRepositoryFacade roleRepositoryFacade;
    private final SysMenuRepositoryFacade menuRepositoryFacade;
    private final SysConfigRepositoryFacade configRepositoryFacade;
    private final SysOAuthRepositoryFacade oauthRepositoryFacade;
    private final SysTenantRepositoryFacade tenantRepositoryFacade;
    private final UserDetailsRepositoryFacade userDetailsRepositoryFacade;
    private final HubMemberRepositoryFacade memberRepositoryFacade;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String register(UserRegister userRegister) {
        String tenantId = userRegister.getTenantId();

        boolean registerOnOff = configRepositoryFacade.queryConfigValue(tenantId, "hub.registerOnOff");
        HttpAsserts.isTrue(registerOnOff, FORBIDDEN, "{admin.register.disable}");

        String userCode = SYS.newCode(tenantId, userRegister.getUserAccount());
        String initPasswd = configRepositoryFacade.queryConfigValue(tenantId, "hub.initPassword");
        SysUser sysUser = new SysUser();
        sysUser.setTenantId(tenantId);
        sysUser.setUserType(SYS);
        sysUser.setUserStatus(ENABLE);
        sysUser.setUserCode(userCode);
        sysUser.setUserEmail(userRegister.getUserEmail());
        sysUser.setUserName(userRegister.getUserName());
        sysUser.setUserAccount(userRegister.getUserAccount());
        sysUser.setUserPasswd(passwordEncoder.encode(initPasswd));
        userBiz.saveUser(sysUser);

        SysRole sysRole = roleRepositoryFacade.queryByCode(tenantId, "role-readonly");
        if(sysRole != null) {
            userBiz.saveUserRole(sysUser.getUserId(), sysRole.getRoleId());
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
        SysUser sysUser = userRepositoryFacade.queryByAccount(tenantId, SYS, userAccount);

        String mfaKey = sysUser.getMfa();
        HttpAsserts.isTrue(MfaAuthVerifier.validateCode(mfaKey, mfaCode), BAD_REQUEST, "{admin.mfa.code.invalid}");

        SysTenant sysTenant = tenantRepositoryFacade.queryById(tenantId);
        AccessUserDetails userDetails = userDetailsRepositoryFacade.queryUserDetails(sysTenant, sysUser, true);
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
                    .userType(refresh.getUserType())
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
        SysTenant sysTenant = tenantRepositoryFacade.queryById(tenantId);
        authVo.setTenantId(tenantId);
        authVo.setTenantTitle(sysTenant.getTitle());
        authVo.setTenantLogo(sysTenant.getLogo());

        // Avatar
        if (AuthType.MEMBER.getVal().equals(userDetails.getAuthType())) {
            String memberCode = userDetails.getUserCode();
            HubMember hubMember = memberRepositoryFacade.queryByCode(memberCode);
            if (hubMember != null) {
                authVo.setAvatar(hubMember.getMemberAvatar());
            }
        } else if (GITLAB.equalsVal(userDetails.getUserType())) {
            SysOAuthUser oauthUser =
                    oauthRepositoryFacade.queryUserByAccount(tenantId, GITLAB.getVal(), userDetails.getUsername());
            authVo.setAvatar(oauthUser.getUserAvatar());
        } else if (SYS.equalsVal(userDetails.getUserType())) {
            SysAttach avatar = attachBiz.previewLatestByOwner(String.valueOf(userId), SYSTEM_USER, AVATAR);
            if (avatar != null) {
                authVo.setAvatar(avatar.getViewUrl());
            }
        }
        return authVo;
    }

    @Override
    public List<Route> menus(){
        List<SysMenu> menuList;
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

        List<SysMenu> rootMenus = new ArrayList<>();
        for(SysMenu menu : menuList){
            if (menu.getParentId() == 0) {
                recursionFn(menuList, menu);
                rootMenus.add(menu);
            }
        }
        return buildRoutes(rootMenus);
    }

    private void recursionFn(List<SysMenu> list, SysMenu menu) {
        List<SysMenu> childList = getChildList(list, menu);
        menu.setChildren(childList);
        for (SysMenu child : childList) {
            if (hasChild(list, child)) {
                recursionFn(list, child);
            }
        }
    }

    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return !getChildList(list, t).isEmpty();
    }

    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu parent) {
        List<SysMenu> children = new ArrayList<>();
        for (SysMenu child : list) {
            if (child.getParentId().equals(parent.getMenuId())) {
                children.add(child);
            }
        }
        return children;
    }

    private List<Route> buildRoutes(List<SysMenu> menus){
        List<Route> routes = new LinkedList<>();
        for (SysMenu menu : menus) {
            Route route = new Route();
            route.setHidden("L".equals(menu.getMenuType())); // 链接不展示在菜单
            route.setName(menu.routeName());
            route.setPath(menu.routePath());
            route.setComponent(menu.routeComponent());
            route.setQuery(menu.getMenuParam());
            route.setMeta(new RouteMeta(menu.getMenuName(), menu.getMenuIcon(), false, menu.getMenuPath()));

            List<SysMenu> cMenus = menu.getChildren();
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
