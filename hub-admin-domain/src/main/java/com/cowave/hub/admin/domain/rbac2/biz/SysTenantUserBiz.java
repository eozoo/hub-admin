package com.cowave.hub.admin.domain.rbac2.biz;

import com.cowave.hub.admin.domain.rbac2.entity.SysTenantUser;
import com.cowave.hub.admin.domain.rbac2.entity.SysUserRole;

/**
 * @author shanhuiming
 */
public interface SysTenantUserBiz {

    /**
     * 创建租户成员关系
     */
    void createMember(SysTenantUser member);

    /**
     * 授予租户角色
     */
    void grantRole(SysUserRole userRole);
}
