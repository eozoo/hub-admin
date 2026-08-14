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
package com.cowave.hub.admin.service.auth.support;

import com.cowave.hub.admin.domain.AdminRedisKeys;
import com.cowave.hub.admin.domain.auth.enums.AuthType;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.AccessProperties;
import com.cowave.zoo.framework.access.filter.AccessIdGenerator;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.access.security.BearerTokenDelegate;
import com.cowave.zoo.framework.access.security.BearerTokenServiceImpl;
import com.cowave.zoo.framework.helper.redis.RedisHelper;
import com.cowave.zoo.tools.NetUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.cowave.zoo.http.client.constants.HttpCode.UNAUTHORIZED;

/**
 * @author shanhuiming
 */
@Component
public class SysBearerTokenServiceImpl extends BearerTokenServiceImpl {

    private final RedisHelper redisHelper;
    private final AccessProperties accessProperties;

    public SysBearerTokenServiceImpl(RedisHelper redisHelper, ObjectMapper objectMapper,
                                     AccessIdGenerator accessIdGenerator, BearerTokenDelegate bearerTokenDelegate,
                                     AccessProperties accessProperties) {
        super(redisHelper, objectMapper, accessIdGenerator, bearerTokenDelegate);
        this.redisHelper = redisHelper;
        this.accessProperties = accessProperties;
    }

    @Override
    protected boolean validateUserDetails(AccessUserDetails userDetails,
                                          HttpServletResponse response, boolean useRefreshToken) throws IOException {
        // API Token：注销检查和IP白名单
        if (AuthType.API.getVal().equals(userDetails.getAuthType())) {
            String accessId = userDetails.getAccessId();
            if (!validateApiToken(response, accessId)) {
                return false;
            }
            recordApiTokenAccess(userDetails);
            return true;
        }
        // OAuth令牌：授权应用列表校验
        if (AuthType.OAUTH.getVal().equals(userDetails.getAuthType())) {
            List<String> oauthApps = userDetails.getApps();
            String selfAppId = accessProperties.oauthAppId();
            if (StringUtils.isNotBlank(selfAppId) && !oauthApps.contains(selfAppId)) {
                writeResponse(response, UNAUTHORIZED, "frame.oauth.invalid");
                return false;
            }
        }
        return super.validateUserDetails(userDetails, response, useRefreshToken);
    }

    private boolean validateApiToken(HttpServletResponse response, String accessId) throws IOException {
        // 是否已注销
        List<NetUtils.IpMask> ipRules = redisHelper.getValue(AdminRedisKeys.AUTH_API.formatted(accessId));
        if (ipRules == null) {
            writeResponse(response, UNAUTHORIZED, "frame.auth.access.denied");
            return false;
        }
        // 校验IP白名单
        if (CollectionUtils.isNotEmpty(ipRules)) {
            String accessIp = Access.accessIp();
            boolean allowed = ipRules.stream().anyMatch(mask -> mask.contains(accessIp));
            if (!allowed) {
                writeResponse(response, UNAUTHORIZED, "frame.auth.access.denied.ip");
                return false;
            }
        }
        return true;
    }

    private void recordApiTokenAccess(AccessUserDetails userDetails) {
        Map<String, Object> accessInfo = Map.of(
                "ip", Access.accessIp(),
                "url", Access.accessUrl(),
                "time", new Date());
        redisHelper.putExpire(AdminRedisKeys.AUTH_API_CURRENT.formatted(userDetails.getAccessId()),
                accessInfo, 15, TimeUnit.MINUTES);
    }
}
