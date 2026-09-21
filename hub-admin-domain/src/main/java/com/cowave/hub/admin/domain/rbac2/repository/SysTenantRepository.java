package com.cowave.hub.admin.domain.rbac2.repository;

import com.cowave.hub.admin.domain.rbac2.entity.SysTenantUser;
import com.cowave.hub.admin.domain.rbac2.entity.SysUserRole;
import com.cowave.hub.admin.domain.rbac2.repository.facade.SysTenantRepositoryFacade;

/**
 * @author shanhuiming
 */
public interface SysTenantRepository extends SysTenantRepositoryFacade {

    /**
     * 创建租户成员关系
     */
    void createMember(SysTenantUser member);

    /**
     * 授予租户角色
     */
    void grantRole(SysUserRole userRole);
}
