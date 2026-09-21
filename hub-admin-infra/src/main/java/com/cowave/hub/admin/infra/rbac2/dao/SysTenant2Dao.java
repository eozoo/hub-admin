/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.infra.rbac2.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;
import com.cowave.hub.admin.domain.rbac2.enums.TenantType;
import com.cowave.hub.admin.domain.rbac2.entity.pto.PermitScopePto;
import com.cowave.hub.admin.domain.rbac2.entity.pto.TenantAccessPto;
import com.cowave.hub.admin.domain.rbac2.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac2.entity.SysTenantUser;
import com.cowave.hub.admin.domain.rbac2.entity.SysUserRole;
import com.cowave.hub.admin.domain.rbac2.repository.SysTenantRepository;
import com.cowave.hub.admin.infra.rbac2.mapper.SysTenantUserMapper;
import com.cowave.hub.admin.infra.rbac2.mapper.SysTenant2Mapper;
import com.cowave.hub.admin.infra.rbac2.mapper.SysUserRole2Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class SysTenant2Dao implements SysTenantRepository {
    private final SysTenantUserMapper tenantUserMapper;
    private final SysTenant2Mapper tenantMapper;
    private final SysUserRole2Mapper userRoleMapper;

    @Override
    public SysTenant queryPublicTenant() {
        return tenantMapper.selectOne(new LambdaQueryWrapper<SysTenant>()
                .eq(SysTenant::getTenantType, TenantType.PUBLIC)
                .eq(SysTenant::getStatus, EnableStatus.ENABLE)
                .eq(SysTenant::getIsDelete, 0));
    }

    @Override
    public Integer queryVisitorRoleId(Integer tenantId) {
        return tenantMapper.selectVisitorRoleId(tenantId);
    }

    @Override
    public void createMember(SysTenantUser member) {
        tenantUserMapper.insert(member);
    }

    @Override
    public void grantRole(SysUserRole userRole) {
        userRoleMapper.insert(userRole);
    }

    @Override
    public TenantAccessPto queryLoginTenant(Integer userId) {
        return tenantUserMapper.selectLoginTenant(userId);
    }

    @Override
    public TenantAccessPto queryTenantAccess(Integer userId, Integer tenantId) {
        return tenantUserMapper.selectTenantAccess(userId, tenantId);
    }

    @Override
    public SysTenant queryTenantById(Integer tenantId) {
        return tenantMapper.selectById(tenantId);
    }

    @Override
    public List<String> queryRoleCodes(Integer tenantId, Integer userId) {
        return tenantUserMapper.selectRoleCodes(tenantId, userId);
    }

    @Override
    public List<PermitScopePto> queryPermitScopes(Integer tenantId, Integer userId) {
        return tenantUserMapper.selectPermitScopes(tenantId, userId);
    }

    @Override
    public List<String> queryRoleNames(Integer tenantId, Integer userId) {
        return tenantUserMapper.selectRoleNames(tenantId, userId);
    }

    @Override
    public List<String> queryDeptPostNames(Integer tenantId, Integer userId) {
        return tenantUserMapper.selectDeptPostNames(tenantId, userId);
    }

    @Override
    public List<String> queryParentNames(Integer tenantId, Integer userId) {
        return tenantUserMapper.selectParentNames(tenantId, userId);
    }
}
