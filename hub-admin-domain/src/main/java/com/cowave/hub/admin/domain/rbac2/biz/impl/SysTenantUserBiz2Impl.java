package com.cowave.hub.admin.domain.rbac2.biz.impl;

import com.cowave.hub.admin.domain.rbac2.biz.SysTenantUserBiz;
import com.cowave.hub.admin.domain.rbac2.entity.SysTenantUser;
import com.cowave.hub.admin.domain.rbac2.entity.SysUserRole;
import com.cowave.hub.admin.domain.rbac2.repository.SysTenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class SysTenantUserBiz2Impl implements SysTenantUserBiz {
    private final SysTenantRepository tenantRepository;

    @Override
    public void createMember(SysTenantUser member) {
        tenantRepository.createMember(member);
    }

    @Override
    public void grantRole(SysUserRole userRole) {
        tenantRepository.grantRole(userRole);
    }
}
