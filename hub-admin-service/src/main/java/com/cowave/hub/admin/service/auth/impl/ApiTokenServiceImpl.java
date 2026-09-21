package com.cowave.hub.admin.service.auth.impl;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import com.cowave.hub.admin.domain.auth.biz.SysUserTokenBiz;
import com.cowave.hub.admin.domain.auth.entity.SysUserToken;
import com.cowave.hub.admin.domain.auth.entity.SysUserTokenMenu;
import com.cowave.hub.admin.domain.auth.entity.command.ApiTokenCreate;
import com.cowave.hub.admin.domain.auth.entity.vo.ApiTokenVo;
import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;
import com.cowave.hub.admin.domain.auth.repository.facade.SysUserTokenRepositoryFacade;
import com.cowave.hub.admin.domain.rbac2.entity.pto.ApiPermitMenu;
import com.cowave.hub.admin.domain.rbac2.entity.pto.PermitScopePto;
import com.cowave.hub.admin.domain.rbac2.repository.facade.SysMenuRepositoryFacade;
import com.cowave.hub.admin.service.auth.ApiTokenService;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.access.security.BearerTokenDelegate;
import com.cowave.zoo.framework.configuration.ApplicationProperties;
import com.cowave.zoo.framework.helper.redis.RedisHelper;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.tools.NetUtils;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_API;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_API_CURRENT;
import static com.cowave.hub.admin.domain.auth.enums.AuthType.API;
import static com.cowave.hub.admin.domain.rbac.entity.pto.DiagramNode.DIAGRAM_CONFIG;
import static com.cowave.zoo.framework.access.security.BearerTokenDelegate.*;
import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.zoo.http.client.constants.HttpCode.FORBIDDEN;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ApiTokenServiceImpl implements ApiTokenService {
    private final ApplicationProperties applicationProperties;
    private final BearerTokenDelegate bearerTokenDelegate;
    private final RedisHelper redisHelper;
    private final SysUserTokenBiz tokenBiz;
    private final SysUserTokenRepositoryFacade userTokenRepositoryFacade;
    private final SysMenuRepositoryFacade menuRepositoryFacade;

    @Override
    public List<Tree<Integer>> getApiTree() {
        List<ApiPermitMenu> menus =
                menuRepositoryFacade.queryApiPermits(Access.tenantId(), Access.userId(), Access.isAdminUser());
        return TreeUtil.build(menus, 0, DIAGRAM_CONFIG, (menu, node) -> {
            node.setId(menu.getMenuId());
            node.setParentId(menu.getParentId());
            node.setName(menu.getMenuName());
            node.put("menuType", menu.getMenuType());
            node.put("menuPermit", menu.getMenuPermit());
            node.put("scopeId", menu.getScopeId());
            node.put("scopes", menu.getScopes());
        });
    }

    @Override
    public List<ApiTokenVo> listApiToken() {
        Integer tenantId = Access.userDetails().getTenantId();
        List<SysUserToken> tokens = userTokenRepositoryFacade.queryByUser(tenantId, Access.userId());
        List<ApiTokenVo> tokenVoList = new ArrayList<>();
        for (SysUserToken token : tokens) {
            ApiTokenVo tokenVo = new ApiTokenVo();
            tokenVo.setTokenId(token.getTokenId());
            tokenVo.setTokenName(token.getTokenName());
            tokenVo.setTenantId(token.getTenantId());
            tokenVo.setUserId(token.getUserId());
            tokenVo.setTokenStatus(token.getTokenStatus());
            tokenVo.setExpireTime(token.getExpireTime());
            tokenVo.setIpRule(token.getIpRule());
            tokenVo.setCreateTime(token.getCreateTime());
            // 令牌权限
            List<String> tokenPermits = userTokenRepositoryFacade.queryMenus(tenantId, token.getTokenId())
                    .stream().map(SysUserTokenMenu::getPermit).distinct().toList();
            tokenVo.setPermits(tokenPermits);
            // 令牌访问信息
            Map<String, Object> accessInfo = redisHelper.getValue(AUTH_API_CURRENT.formatted(token.getTokenId()));
            if (accessInfo != null) {
                tokenVo.setAccessIp((String) accessInfo.get("ip"));
                tokenVo.setAccessUrl((String) accessInfo.get("url"));
                tokenVo.setAccessTime((Date) accessInfo.get("time"));
            }
            tokenVoList.add(tokenVo);
        }
        return tokenVoList;
    }

    @Override
    public String creatApiToken(ApiTokenCreate command) {
        Date now = new Date();
        HttpAsserts.isTrue(command.getExpireTime() == null || command.getExpireTime().after(now),
                BAD_REQUEST, "{admin.auth.api.expiry.invalid}");
        // 申请的令牌权限
        List<PermitScopePto> applyPermits = command.getMenuScopes() == null ? List.of() : command.getMenuScopes();
        // 用户拥有的权限
        List<ApiPermitMenu> permitMenus =
                menuRepositoryFacade.queryApiPermits(Access.tenantId(), Access.userId(), Access.isAdminUser());
        Map<String, ApiPermitMenu> allowed = permitMenus.stream().filter(
                menu -> "B".equals(menu.getMenuType()) && StringUtils.isNotBlank(menu.getMenuPermit()))
                .collect(Collectors.toMap(ApiPermitMenu::getMenuPermit, menu -> menu, (first, next) -> first));
        // 选择的权限
        Map<String, Integer> selected = new LinkedHashMap<>();
        for (PermitScopePto apply : applyPermits) {
            HttpAsserts.notNull(apply, BAD_REQUEST, "{admin.auth.api.permission.invalid}");
            if (StringUtils.isBlank(apply.getPermit())) {
                continue;
            }
            // 权限不足
            ApiPermitMenu menu = allowed.get(apply.getPermit());
            HttpAsserts.notNull(menu, FORBIDDEN, "{admin.auth.api.permission.invalid}");
            if (apply.getScopeId() != null) {
                HttpAsserts.isTrue(menu.getScopes().stream().anyMatch(
                        scope -> Objects.equals(scope.getScopeId(), apply.getScopeId())),
                        FORBIDDEN, "{admin.auth.api.scope.invalid}");
            }
            // 重复permit
            HttpAsserts.isTrue(!selected.containsKey(apply.getPermit()), BAD_REQUEST, "{admin.auth.api.permission.duplicate}");
            selected.put(apply.getPermit(), apply.getScopeId() != null
                    ? apply.getScopeId() : Access.isAdminUser() ? null : menu.getScopeId());
        }
        // 保存令牌信息
        SysUserToken userToken = new SysUserToken();
        AccessUserDetails userDetails = Access.userDetails();
        userToken.setTenantId(userDetails.getTenantId());
        userToken.setUserId(userDetails.getUserId());
        userToken.setTokenName(command.getTokenName().trim());
        userToken.setTokenValue("");
        userToken.setTokenStatus(EnableStatus.ENABLE);
        userToken.setExpireTime(command.getExpireTime());
        userToken.setIpRule(command.getIpRule());
        userToken.setCreateBy(Access.userAccount());
        userToken.setCreateTime(now);
        List<SysUserTokenMenu> menus = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : selected.entrySet()) {
            SysUserTokenMenu menu = new SysUserTokenMenu();
            menu.setTenantId(userToken.getTenantId());
            menu.setPermit(entry.getKey());
            menu.setScopeId(entry.getValue());
            menus.add(menu);
        }
        tokenBiz.create(userToken, menus);
        // 令牌信息
        Map<String, List<Integer>> scopePermits = new HashMap<>();
        selected.forEach((permit, scopeId) -> {
            if (scopeId != null) {
                scopePermits.put(permit, List.of(scopeId));
            }
        });
        JwtBuilder builder = Jwts.builder()
                .claim(CLAIM_ACCESS_ID, String.valueOf(userToken.getTokenId()))
                .claim(CLAIM_TYPE, API.getVal())
                .claim(CLAIM_TENANT_ID, userDetails.getTenantId())
                .claim(CLAIM_TENANT_CODE, userDetails.getTenantCode())
                .claim(CLAIM_USER_ID, userDetails.getUserId())
                .claim(CLAIM_USER_CODE, userDetails.getUserCode())
                .claim(CLAIM_USER_TYPE, userDetails.getUserType())
                .claim(CLAIM_USER_PROPERTIES, userDetails.getUserProperties())
                .claim(CLAIM_USER_NAME, userDetails.getUserNick())
                .claim(CLAIM_USER_ACCOUNT, userDetails.getUsername())
                .claim(CLAIM_DEPT_ID, userDetails.getDeptId())
                .claim(CLAIM_DEPT_CODE, userDetails.getDeptCode())
                .claim(CLAIM_DEPT_NAME, userDetails.getDeptName())
                .claim(CLAIM_CLUSTER_ID, applicationProperties.getClusterId())
                .claim(CLAIM_CLUSTER_LEVEL, applicationProperties.getClusterLevel())
                .claim(CLAIM_CLUSTER_NAME, applicationProperties.getClusterName())
                .claim(CLAIM_USER_ROLE, List.of())
                .claim(CLAIM_USER_PERM, new ArrayList<>(selected.keySet()))
                .claim(CLAIM_USER_SCOPE, scopePermits)
                .claim(CLAIM_ACCESS_UNIQUE, 0)
                .claim(CLAIM_ACCESS_VALID, 0)
                .issuer(bearerTokenDelegate.getAccessIssuer())
                .issuedAt(now);
        if (userToken.getExpireTime() != null) {
            builder.expiration(userToken.getExpireTime());
        }
        // 签发令牌
        SignatureAlgorithm algorithm = bearerTokenDelegate.getAccessAlgorithm();
        Key signingKey = bearerTokenDelegate.getAccessSigningKey(bearerTokenDelegate.getAccessAlgorithm());
        SecureDigestAlgorithm<Key, Key> digest =
                (SecureDigestAlgorithm<Key, Key>) Jwts.SIG.get().forKey(algorithm.getValue());
        String tokenValue = builder.signWith(signingKey, digest).compact();
        userToken.setTokenValue(tokenValue);
        // 更新令牌值
        tokenBiz.updateValue(userToken);
        // 生效令牌访问限制
        List<NetUtils.IpMask> rules = new ArrayList<>();
        if (StringUtils.isNotBlank(userToken.getIpRule())) {
            for (String ip : userToken.getIpRule().split(",")) {
                rules.add(new NetUtils.IpMask(ip.trim()));
            }
        }
        if (userToken.getExpireTime() == null) {
            redisHelper.putValue(AUTH_API.formatted(userToken.getTokenId()), rules);
        } else {
            long duration = userToken.getExpireTime().getTime() - System.currentTimeMillis();
            HttpAsserts.isTrue(duration > 0, BAD_REQUEST, "{admin.auth.api.expiry.invalid}");
            redisHelper.putExpire(AUTH_API.formatted(userToken.getTokenId()), rules, duration, TimeUnit.MILLISECONDS);
        }
        return tokenValue;
    }

    @Override
    public void deleteApiToken(Integer tokenId) {
        Integer tenantId = Access.userDetails().getTenantId();
        HttpAsserts.notNull(userTokenRepositoryFacade.queryOwned(tenantId, Access.userId(), tokenId),
                FORBIDDEN, "{admin.auth.api.owner.invalid}");
        tokenBiz.delete(tenantId, Access.userId(), tokenId);
        redisHelper.delete(AUTH_API.formatted(tokenId));
        redisHelper.delete(AUTH_API_CURRENT.formatted(tokenId));
    }
}
