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

import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.framework.access.security.TenantUserDetailsService;
import com.cowave.hub.admin.domain.rbac.entity.HubTenant;
import com.cowave.hub.admin.domain.auth.repository.facade.UserDetailsRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubUserRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.entity.HubUser;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.cowave.hub.admin.domain.rbac.enums.UserType.SYS;
import static com.cowave.zoo.http.client.constants.HttpCode.FORBIDDEN;
import static com.cowave.hub.admin.domain.rbac.enums.EnableStatus.ENABLE;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class HubUserDetailsServiceImpl implements TenantUserDetailsService {
    private final MfaConfiguration mfaConfiguration;
    private final HubUserRepositoryFacade userRepositoryFacade;
    private final HubTenantRepositoryFacade tenantRepositoryFacade;
    private final UserDetailsRepositoryFacade userDetailsRepositoryFacade;

    @Override
	public UserDetails loadTenantUserByUsername(String tenantId, String userAccount) {
        HubUser hubUser = userRepositoryFacade.queryByAccount(tenantId, SYS, userAccount);
        if(hubUser == null){
            return null;
        }
        HttpAsserts.equals(ENABLE, hubUser.getUserStatus(), FORBIDDEN, "{admin.user.account.disable}", userAccount);

        // 租户
        HubTenant hubTenant = tenantRepositoryFacade.queryById(hubUser.getTenantId());
        HttpAsserts.equals(ENABLE, hubTenant.getStatus(),
                FORBIDDEN, "{admin.user.tenant.disable}", hubTenant.getTenantName());
        if(hubTenant.getExpireTime() != null){
            HttpAsserts.isTrue(hubTenant.getExpireTime().after(new Date()),
                    FORBIDDEN, "{admin.user.tenant.expired}", hubTenant.getTenantName());
        }

        String mfaKey = hubUser.getMfa();
        if(StringUtils.isBlank(mfaKey)){
            return userDetailsRepositoryFacade.queryUserDetails(SYS, hubTenant, hubUser, true);
        }else{
            String mfaToken = mfaConfiguration.buildMfaToken(tenantId, userAccount);
            AccessUserDetails userDetails = new AccessUserDetails();
            userDetails.setUsername(userAccount);
            userDetails.setUserPasswd(hubUser.getUserPasswd());
            userDetails.setMfaRequired(true);
            userDetails.setAccessToken(mfaToken);
            return userDetails;
        }
	}
}
