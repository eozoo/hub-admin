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

import com.cowave.hub.admin.domain.auth.biz.HubTokenBiz;
import com.cowave.hub.admin.domain.auth.entity.HubToken;
import com.cowave.hub.admin.domain.auth.entity.HubTokenMenu;
import com.cowave.hub.admin.domain.auth.entity.command.ApiTokenCreate;
import com.cowave.hub.admin.domain.auth.entity.vo.TokenVo;
import com.cowave.hub.admin.domain.auth.repository.facade.HubTokenRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.entity.pto.MenuScopePto;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysMenuRepositoryFacade;
import com.cowave.hub.admin.service.auth.ApiTokenService;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.AccessProperties;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.access.security.Permission;
import com.cowave.zoo.framework.configuration.ApplicationProperties;
import com.cowave.zoo.framework.helper.redis.RedisHelper;
import com.cowave.zoo.tools.Collections;
import com.cowave.zoo.tools.NetUtils;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_API;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_API_CURRENT;
import static com.cowave.hub.admin.domain.auth.enums.AuthType.API;
import static com.cowave.zoo.framework.access.security.BearerTokenDelegate.*;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ApiTokenServiceImpl implements ApiTokenService {
    private final ApplicationProperties applicationProperties;
    private final AccessProperties accessProperties;
    private final RedisHelper redisHelper;
    private final HubTokenBiz tokenBiz;
    private final HubTokenRepositoryFacade tokenRepositoryFacade;
    private final SysMenuRepositoryFacade menuRepositoryFacade;

    @PostConstruct
    public void indexApiToken() {
        List<HubToken> list = tokenRepositoryFacade.queryList();
        for (HubToken hubToken : list) {
            setApiTokenToUse(hubToken);
        }
    }

    private void setApiTokenToUse(HubToken hubToken) {
        List<NetUtils.IpMask> ipRules = parseIpMask(hubToken.getIpRule());
        if (hubToken.getExpire() != null) {
            long expire = hubToken.getExpire().getTime() - System.currentTimeMillis();
            if (expire > 0) {
                redisHelper.putExpire(AUTH_API.formatted(hubToken.getTokenId()), ipRules, expire, TimeUnit.MILLISECONDS);
            }
        } else {
            redisHelper.putValue(AUTH_API.formatted(hubToken.getTokenId()), ipRules);
        }
    }

    private List<NetUtils.IpMask> parseIpMask(String ipRule) {
        if (StringUtils.isBlank(ipRule)) {
            return new ArrayList<>();
        }
        List<NetUtils.IpMask> list = new ArrayList<>();
        String[] array = ipRule.split(",");
        for (String ip : array) {
            list.add(new NetUtils.IpMask(ip));
        }
        return list;
    }

    @Override
    public List<TokenVo> listApiToken() {
        List<HubToken> tokenList = tokenRepositoryFacade.queryListByUserCode(Access.userCode());
        List<TokenVo> list = Collections.convertToList(tokenList, TokenVo.class);
        for (TokenVo tokenVo : list) {
            Map<String, Object> accessInfo = redisHelper.getValue(AUTH_API_CURRENT.formatted(tokenVo.getTokenId()));
            if (accessInfo != null) {
                tokenVo.setAccessIp((String) accessInfo.get("ip"));
                tokenVo.setAccessUrl((String) accessInfo.get("url"));
                tokenVo.setAccessTime((Date) accessInfo.get("time"));
            }
            tokenVo.setMenuIds(tokenRepositoryFacade.queryMenuIdsByTokenId(tokenVo.getTokenId()));
        }
        return list;
    }

    @Override
    public String creatApiToken(ApiTokenCreate tokenCreate) {
        tokenCreate.setUserCode(Access.userCode());
        tokenBiz.saveToken(tokenCreate);

        List<String> permits = new ArrayList<>();
        List<MenuScopePto> menuScopes = tokenCreate.getMenuScopes();
        if (CollectionUtils.isNotEmpty(menuScopes)) {
            permits = menuRepositoryFacade.queryPermitsByIds(
                    Collections.copyToList(menuScopes, MenuScopePto::getMenuId));
        }

        AccessUserDetails userDetails = Access.userDetails();
        List<String> roles = userDetails.getRoles();
        if (CollectionUtils.isNotEmpty(roles)) {
            roles.remove(Permission.ROLE_ADMIN);
        }

        JwtBuilder jwtBuilder = Jwts.builder()
                .claim(CLAIM_ACCESS_ID, String.valueOf(tokenCreate.getTokenId()))
                .claim(CLAIM_TYPE, API.getVal())
                .claim(CLAIM_USER_ID, userDetails.getUserId())
                .claim(CLAIM_USER_CODE, userDetails.getUserCode())
                .claim(CLAIM_USER_PROPERTIES, userDetails.getUserProperties())
                .claim(CLAIM_USER_NAME, userDetails.getUserNick())
                .claim(CLAIM_USER_ACCOUNT, userDetails.getUsername())
                .claim(CLAIM_DEPT_ID, userDetails.getDeptId())
                .claim(CLAIM_DEPT_CODE, userDetails.getDeptCode())
                .claim(CLAIM_DEPT_NAME, userDetails.getDeptName())
                .claim(CLAIM_CLUSTER_ID, applicationProperties.getClusterId())
                .claim(CLAIM_CLUSTER_LEVEL, applicationProperties.getClusterLevel())
                .claim(CLAIM_CLUSTER_NAME, applicationProperties.getClusterName())
                .claim(CLAIM_USER_ROLE, roles)
                .claim(CLAIM_USER_PERM, permits)
                .claim(CLAIM_ACCESS_UNIQUE, 0)
                .setIssuedAt(new Date());

        byte[] keyBytes = accessProperties.accessSecret().getBytes(StandardCharsets.UTF_8);
        Key key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
        jwtBuilder.signWith(SignatureAlgorithm.HS512, key);

        if (tokenCreate.getExpire() != null) {
            jwtBuilder.setExpiration(tokenCreate.getExpire());
        }
        String tokenValue = jwtBuilder.compact();

        tokenCreate.setTokenValue(tokenValue);
        tokenBiz.updateToken(tokenCreate);

        if (CollectionUtils.isNotEmpty(menuScopes)) {
            List<HubTokenMenu> tokenMenus = Collections.copyToList(menuScopes,
                    v -> new HubTokenMenu(tokenCreate.getTokenId(), v.getMenuId(), v.getScopeId()));
            tokenBiz.saveTokenMenus(tokenMenus);
        }

        setApiTokenToUse(tokenCreate);
        return tokenValue;
    }

    @Override
    public void deleteApiToken(Integer tokenId) {
        tokenBiz.deleteApiToken(tokenId);
        redisHelper.delete(AUTH_API.formatted(tokenId));
    }
}
