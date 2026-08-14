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
import com.cowave.hub.admin.domain.rbac.entity.SysTenant;
import com.cowave.hub.admin.domain.auth.repository.facade.UserDetailsRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.entity.SysUser;
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
public class SysUserDetailsServiceImpl implements TenantUserDetailsService {
    private final MfaConfiguration mfaConfiguration;
    private final SysUserRepositoryFacade userRepositoryFacade;
    private final SysTenantRepositoryFacade tenantRepositoryFacade;
    private final UserDetailsRepositoryFacade userDetailsRepositoryFacade;

    @Override
	public UserDetails loadTenantUserByUsername(String tenantId, String userAccount) {
        SysUser sysUser = userRepositoryFacade.queryByAccount(tenantId, SYS, userAccount);
        if(sysUser == null){
            return null;
        }
        HttpAsserts.equals(ENABLE, sysUser.getUserStatus(), FORBIDDEN, "{admin.user.account.disable}", userAccount);

        // 租户
        SysTenant sysTenant = tenantRepositoryFacade.queryById(sysUser.getTenantId());
        HttpAsserts.equals(ENABLE, sysTenant.getStatus(),
                FORBIDDEN, "{admin.user.tenant.disable}", sysTenant.getTenantName());
        if(sysTenant.getExpireTime() != null){
            HttpAsserts.isTrue(sysTenant.getExpireTime().after(new Date()),
                    FORBIDDEN, "{admin.user.tenant.expired}", sysTenant.getTenantName());
        }

        String mfaKey = sysUser.getMfa();
        if(StringUtils.isBlank(mfaKey)){
            return userDetailsRepositoryFacade.queryUserDetails(sysTenant, sysUser, true);
        }else{
            String mfaToken = mfaConfiguration.buildMfaToken(tenantId, userAccount);
            AccessUserDetails userDetails = new AccessUserDetails();
            userDetails.setUsername(userAccount);
            userDetails.setUserPasswd(sysUser.getUserPasswd());
            userDetails.setMfaRequired(true);
            userDetails.setAccessToken(mfaToken);
            return userDetails;
        }
	}
}
