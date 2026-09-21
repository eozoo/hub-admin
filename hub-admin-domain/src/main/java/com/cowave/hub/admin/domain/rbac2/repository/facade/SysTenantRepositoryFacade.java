/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.domain.rbac2.repository.facade;

import com.cowave.hub.admin.domain.rbac2.entity.pto.PermitScopePto;
import com.cowave.hub.admin.domain.rbac2.entity.pto.TenantAccessPto;
import com.cowave.hub.admin.domain.rbac2.entity.SysTenant;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysTenantRepositoryFacade {

    /**
     * 查询登录租户上下文
     */
    TenantAccessPto queryLoginTenant(Integer userId);

    /**
     * 查询用户在指定租户的有效成员关系
     */
    TenantAccessPto queryTenantAccess(Integer userId, Integer tenantId);

    /**
     * 查询租户信息
     */
    SysTenant queryTenantById(Integer tenantId);

    /**
     * 查询公共访客租户
     */
    SysTenant queryPublicTenant();

    /**
     * 查询租户内启用的访客角色
     */
    Integer queryVisitorRoleId(Integer tenantId);

    /**
     * 查询用户角色编码
     */
    List<String> queryRoleCodes(Integer tenantId, Integer userId);

    /**
     * 查询用户权限及数据范围
     */
    List<PermitScopePto> queryPermitScopes(Integer tenantId, Integer userId);

    /**
     * 查询用户在当前租户的角色名称
     */
    List<String> queryRoleNames(Integer tenantId, Integer userId);

    /**
     * 查询用户在当前租户的部门和岗位名称
     */
    List<String> queryDeptPostNames(Integer tenantId, Integer userId);

    /**
     * 查询用户在当前租户的汇报对象名称
     */
    List<String> queryParentNames(Integer tenantId, Integer userId);
}
