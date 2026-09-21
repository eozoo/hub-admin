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
package com.cowave.hub.admin.infra.rbac2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.hub.admin.domain.rbac2.entity.SysMenu;
import com.cowave.hub.admin.domain.rbac2.entity.SysScope;
import com.cowave.hub.admin.domain.rbac2.entity.pto.ApiPermitMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysMenu2Mapper extends BaseMapper<SysMenu> {

    /**
     * 查询管理员菜单
     */
    @Select("""
            select m.*
            from sys_menu m
            where m.tenant_id = #{tenantId}
              and m.menu_status = 1
              and m.is_visible = 1
              and m.menu_type in ('C', 'M')
            order by m.parent_id, m.menu_order
            """)
    List<SysMenu> selectMenusByAdmin(@Param("tenantId") Integer tenantId);

    /**
     * 查询用户授权菜单
     */
    @Select("""
            select distinct m.*
            from sys_menu m
            where m.tenant_id = #{tenantId}
              and m.menu_status = 1
              and m.is_visible = 1
              and m.menu_type in ('C', 'M')
              and (
                    m.is_protected = 0
                    or exists (
                        select 1
                        from sys_user_role ur
                        join sys_role r
                          on r.tenant_id = ur.tenant_id
                         and r.role_id = ur.role_id
                         and r.role_status = 1
                        join sys_role_menu rm
                          on rm.tenant_id = ur.tenant_id
                         and rm.role_id = ur.role_id
                         and rm.menu_id = m.menu_id
                        where ur.tenant_id = #{tenantId}
                          and ur.user_id = #{userId}
                    )
              )
            order by m.parent_id, m.menu_order
            """)
    List<SysMenu> selectMenusByUser(@Param("tenantId") Integer tenantId,
                                    @Param("userId") Integer userId);

    /**
     * 管理员可授权的 API 菜单
     */
    @Select("""
            select m.menu_id, m.parent_id, m.menu_name, m.menu_type, m.menu_permit
            from sys_menu m
            where m.tenant_id = #{tenantId} and m.menu_status = 1 and m.is_protected = 1
            order by m.parent_id, m.menu_order
            """)
    List<ApiPermitMenu> selectApiPermitsByAdmin(@Param("tenantId") Integer tenantId);

    /**
     * 用户已获授权的 API 菜单
     */
    @Select("""
            select distinct m.menu_id, m.parent_id, m.menu_name, m.menu_type, m.menu_permit, m.menu_order
            from sys_menu m
            join sys_role_menu rm on rm.tenant_id = m.tenant_id and rm.menu_id = m.menu_id
            join sys_user_role ur on ur.tenant_id = rm.tenant_id and ur.role_id = rm.role_id
            join sys_role r on r.tenant_id = rm.tenant_id and r.role_id = rm.role_id
            where m.tenant_id = #{tenantId} and ur.user_id = #{userId}
              and m.menu_status = 1 and m.is_protected = 1 and r.role_status = 1
            order by m.parent_id, m.menu_order
            """)
    List<ApiPermitMenu> selectApiPermitsByUser(@Param("tenantId") Integer tenantId,
                                               @Param("userId") Integer userId);

    /**
     * 菜单模块可选数据范围
     */
    @Select("""
            select s.* from sys_scope s join sys_menu m
              on m.tenant_id = s.tenant_id and m.menu_module = s.scope_module
            where m.tenant_id = #{tenantId} and m.menu_id = #{menuId} and s.scope_status = 1
            order by s.scope_id
            """)
    List<SysScope> selectApiScopes(@Param("tenantId") Integer tenantId,
                                   @Param("menuId") Integer menuId);

    /**
     * 用户在菜单上已获授权的数据范围
     */
    @Select("""
            select distinct s.* from sys_scope s
            join sys_role_menu rm on rm.tenant_id = s.tenant_id and rm.scope_id = s.scope_id
            join sys_menu m on m.tenant_id = rm.tenant_id and m.menu_id = rm.menu_id and m.menu_module = s.scope_module
            join sys_user_role ur on ur.tenant_id = rm.tenant_id and ur.role_id = rm.role_id
            join sys_role r on r.tenant_id = rm.tenant_id and r.role_id = rm.role_id
            where s.tenant_id = #{tenantId} and ur.user_id = #{userId}
              and rm.menu_id = #{menuId} and s.scope_status = 1 and r.role_status = 1
            order by s.scope_id
            """)
    List<SysScope> selectApiScopesByUser(@Param("tenantId") Integer tenantId,
                                          @Param("userId") Integer userId,
                                          @Param("menuId") Integer menuId);
}
