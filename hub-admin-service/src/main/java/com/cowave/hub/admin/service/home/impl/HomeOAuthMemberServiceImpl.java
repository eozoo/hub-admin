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
package com.cowave.hub.admin.service.home.impl;

import com.cowave.hub.admin.domain.home.biz.HubMemberBiz;
import com.cowave.hub.admin.domain.home.entity.HubMember;
import com.cowave.hub.admin.domain.home.entity.command.HubMemberProfileUpdate;
import com.cowave.hub.admin.domain.home.entity.vo.HubMemberProfileVo;
import com.cowave.hub.admin.domain.home.repository.facade.HubMemberRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysTenantRepositoryFacade;
import com.cowave.hub.admin.service.home.HomeOAuthMemberService;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class HomeOAuthMemberServiceImpl implements HomeOAuthMemberService {
    private final HubMemberRepositoryFacade hubMemberRepositoryFacade;
    private final HubMemberBiz hubMemberBiz;
    private final SysTenantRepositoryFacade tenantRepositoryFacade;

    @Override
    public HubMemberProfileVo profile() {
        AccessUserDetails userDetails = Access.userDetails();
        String memberCode = userDetails.getUserCode();
        HubMember hubMember = hubMemberRepositoryFacade.queryByCode(memberCode);

        HubMemberProfileVo vo = new HubMemberProfileVo();
        vo.setMemberId(hubMember.getMemberId());
        vo.setMemberCode(hubMember.getMemberCode());
        vo.setMemberType(hubMember.getMemberType().getVal());
        vo.setMemberName(hubMember.getMemberName());
        vo.setMemberAccount(hubMember.getMemberAccount());
        vo.setMemberEmail(hubMember.getMemberEmail());
        vo.setMemberAvatar(hubMember.getMemberAvatar());
        vo.setMemberSign(hubMember.getMemberSign());

        SysTenant sysTenant = tenantRepositoryFacade.queryById(userDetails.getTenantId());
        vo.setTenantId(sysTenant.getTenantId());
        vo.setTenantName(sysTenant.getTenantName());
        return vo;
    }

    @Override
    public void updateProfile(HubMemberProfileUpdate cmd) {
        String memberCode = Access.userCode();
        HubMember hubMember = hubMemberRepositoryFacade.queryByCode(memberCode);
        hubMemberBiz.updateMemberProfile(hubMember.getMemberId(), cmd);
    }
}
