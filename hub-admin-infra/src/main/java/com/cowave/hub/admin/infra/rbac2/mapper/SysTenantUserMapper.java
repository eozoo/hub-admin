/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.infra.rbac2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.hub.admin.domain.rbac2.entity.SysTenantUser;
import com.cowave.hub.admin.domain.rbac2.entity.pto.PermitScopePto;
import com.cowave.hub.admin.domain.rbac2.entity.pto.TenantAccessPto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 租户用户访问关系数据访问
 *
 * @author shanhuiming
 */
@Mapper
public interface SysTenantUserMapper extends BaseMapper<SysTenantUser> {

    /**
     * 查询用户登录时使用的租户及主部门
     */
    @Select("""
            with active_tenant as (
                select t.tenant_id,
                       t.tenant_code,
                       t.tenant_type,
                       tu.user_type,
                       tu.user_code,
                       coalesce(tu.display_name, u.user_name) as display_name,
                       d.dept_id,
                       d.dept_code,
                       d.dept_name,
                       tu.is_default,
                       count(*) filter (where t.tenant_type <> 'public') over () as formal_count
                from sys_tenant_user tu
                join sys_tenant t on t.tenant_id = tu.tenant_id
                join sys_user u on u.user_id = tu.user_id
                left join sys_user_dept ud
                  on ud.tenant_id = tu.tenant_id
                 and ud.user_id = tu.user_id
                 and ud.is_primary = 1
                left join sys_dept d
                  on d.tenant_id = ud.tenant_id
                 and d.dept_id = ud.dept_id
                 and d.dept_status = 1
                where tu.user_id = #{userId}
                  and tu.status = 1
                  and tu.leave_time is null
                  and t.status = 1
                  and t.is_delete = 0
                  and (t.expire_time is null or t.expire_time > current_timestamp)
            )
            select tenant_id,
                   tenant_code,
                   user_type,
                   user_code,
                   display_name,
                   dept_id,
                   dept_code,
                   dept_name
            from active_tenant
            order by is_default desc,
                     case
                         when tenant_type <> 'public' and formal_count = 1 then 1
                         when tenant_type = 'public' then 2
                         else 3
                     end,
                     tenant_id
            limit 1
            """)
    TenantAccessPto selectLoginTenant(@Param("userId") Integer userId);

    /**
     * 查询用户在当前租户的有效成员关系和主部门
     */
    @Select("""
            select t.tenant_id,
                   t.tenant_code,
                   tu.user_type,
                   tu.user_code,
                   coalesce(tu.display_name, u.user_name) as display_name,
                   d.dept_id,
                   d.dept_code,
                   d.dept_name
            from sys_tenant_user tu
            join sys_tenant t on t.tenant_id = tu.tenant_id
            join sys_user u on u.user_id = tu.user_id
            left join sys_user_dept ud
              on ud.tenant_id = tu.tenant_id
             and ud.user_id = tu.user_id
             and ud.is_primary = 1
            left join sys_dept d
              on d.tenant_id = ud.tenant_id
             and d.dept_id = ud.dept_id
             and d.dept_status = 1
            where tu.user_id = #{userId}
              and tu.tenant_id = #{tenantId}
              and tu.status = 1
              and tu.leave_time is null
              and u.is_delete = 0
              and t.status = 1
              and t.is_delete = 0
              and (t.expire_time is null or t.expire_time > current_timestamp)
            """)
    TenantAccessPto selectTenantAccess(@Param("userId") Integer userId,
                                       @Param("tenantId") Integer tenantId);

    /**
     * 查询用户角色编码
     */
    @Select("""
            select distinct r.role_code
            from sys_user_role ur
            join sys_role r
              on r.tenant_id = ur.tenant_id
             and r.role_id = ur.role_id
            where ur.tenant_id = #{tenantId}
              and ur.user_id = #{userId}
              and r.role_status = 1
            order by r.role_code
            """)
    List<String> selectRoleCodes(@Param("tenantId") Integer tenantId,
                                 @Param("userId") Integer userId);

    /**
     * 查询当前租户角色名称
     */
    @Select("""
            select distinct r.role_name
            from sys_user_role ur
            join sys_role r on r.tenant_id = ur.tenant_id and r.role_id = ur.role_id
            where ur.tenant_id = #{tenantId} and ur.user_id = #{userId}
              and r.role_status = 1
            order by r.role_name
            """)
    List<String> selectRoleNames(@Param("tenantId") Integer tenantId,
                                 @Param("userId") Integer userId);

    /**
     * 查询当前租户部门和岗位名称
     */
    @Select("""
            select distinct case when p.post_name is null then d.dept_name
                                 else concat(d.dept_name, '/', p.post_name) end
            from sys_user_dept ud
            join sys_dept d on d.tenant_id = ud.tenant_id and d.dept_id = ud.dept_id
            left join sys_post p on p.tenant_id = ud.tenant_id and p.post_id = ud.post_id
            where ud.tenant_id = #{tenantId} and ud.user_id = #{userId}
            order by 1
            """)
    List<String> selectDeptPostNames(@Param("tenantId") Integer tenantId,
                                     @Param("userId") Integer userId);

    /**
     * 查询当前租户汇报对象名称
     */
    @Select("""
            select distinct u.user_name
            from sys_user_diagram diagram
            join sys_user u on u.user_id = diagram.parent_id and u.is_delete = 0
            where diagram.tenant_id = #{tenantId} and diagram.user_id = #{userId}
            order by u.user_name
            """)
    List<String> selectParentNames(@Param("tenantId") Integer tenantId,
                                   @Param("userId") Integer userId);

    /**
     * 查询用户权限及数据范围
     */
    @Select("""
            select distinct m.menu_permit as permit,
                            rm.scope_id as scope_id
            from sys_user_role ur
            join sys_role r
              on r.tenant_id = ur.tenant_id
             and r.role_id = ur.role_id
             and r.role_status = 1
            join sys_role_menu rm
              on rm.tenant_id = ur.tenant_id
             and rm.role_id = ur.role_id
            join sys_menu m
              on m.tenant_id = ur.tenant_id
             and m.menu_id = rm.menu_id
            where ur.tenant_id = #{tenantId}
              and ur.user_id = #{userId}
              and m.menu_status = 1
              and m.menu_type in ('B', 'C')
              and m.menu_permit is not null
            order by permit, scope_id
            """)
    List<PermitScopePto> selectPermitScopes(@Param("tenantId") Integer tenantId,
                                            @Param("userId") Integer userId);
}
