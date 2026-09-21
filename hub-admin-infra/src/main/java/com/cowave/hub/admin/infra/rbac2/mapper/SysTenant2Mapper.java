/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.infra.rbac2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.hub.admin.domain.rbac2.entity.SysTenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysTenant2Mapper extends BaseMapper<SysTenant> {

    /**
     * 查询租户启用的访客角色
     */
    @Select("select role_id from sys_role where tenant_id = #{tenantId} and role_code = 'visitor' and role_status = 1")
    Integer selectVisitorRoleId(@Param("tenantId") Integer tenantId);
}
