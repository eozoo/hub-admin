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
package com.cowave.hub.admin.controller.rbac;

import com.alibaba.excel.EasyExcel;
import com.cowave.hub.admin.domain.rbac.entity.command.RoleMenuUpdate;
import com.cowave.hub.admin.domain.rbac.entity.query.RoleQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.RoleUserQuery;
import com.cowave.hub.admin.domain.rbac.entity.command.RoleUserUpdate;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.operation.Operation;
import com.cowave.zoo.framework.support.excel.write.ExcelIgnoreStyle;
import com.cowave.hub.admin.domain.rbac.entity.SysRole;
import com.cowave.hub.admin.domain.rbac.entity.pto.RoleInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.RoleUserPto;
import com.cowave.hub.admin.service.rbac.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.cowave.hub.admin.domain.sys.enums.OpAction.*;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_ROLE;

/**
 * 角色
 * @order 5
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/role")
public class SysRoleController {

    private final SysRoleService roleService;

    /**
     * 列表
     */
    @PreAuthorize("@permits.hasPermit('hub:role:query')")
    @GetMapping
    public Response<Response.Page<SysRole>> list(RoleQuery query) {
        return Response.page(roleService.list(Access.tenantId(), query));
    }

    /**
     * 详情
     *
     * @param roleId 角色id
     */
    @PreAuthorize("@permits.hasPermit('hub:role:query')")
    @GetMapping("/{roleId}")
    public Response<RoleInfoPto> info(@PathVariable Integer roleId) {
        return Response.success(roleService.info(Access.tenantId(), roleId));
    }

    /**
     * 新增
     */
    @Operation(module = SYSTEM, type = SYSTEM_ROLE, action = CREATE, desc = "新增角色：#{#role.roleName}")
    @PreAuthorize("@permits.hasPermit('hub:role:create')")
    @PostMapping
    public Response<Void> add(@Validated @RequestBody SysRole role) {
        roleService.add(Access.tenantId(), role);
        return Response.success();
    }

    /**
     * 删除
     *
     * @param roleIds 角色id列表
     */
    @Operation(module = SYSTEM, type = SYSTEM_ROLE, action = DELETE, desc = "删除角色")
    @PreAuthorize("@permits.hasPermit('hub:role:delete')")
    @DeleteMapping("/{roleIds}")
    public Response<Void> delete(@PathVariable List<Integer> roleIds) {
        roleService.delete(Access.tenantId(), roleIds);
        return Response.success();
    }

    /**
     * 修改
     */
    @Operation(module = SYSTEM, type = SYSTEM_ROLE, action = EDIT, desc = "修改角色：#{#role.roleName}")
    @PreAuthorize("@permits.hasPermit('hub:role:edit')")
    @PatchMapping
    public Response<Void> edit(@Validated @RequestBody SysRole role) {
        roleService.edit(Access.tenantId(), role);
        return Response.success();
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("@permits.hasPermit('hub:role:menus')")
    @PatchMapping("/menus")
    public Response<Void> updateMenus(@RequestBody RoleMenuUpdate roleUpdate) {
        roleService.updateMenus(Access.tenantId(), roleUpdate);
        return Response.success();
    }

    /**
     * 导出角色
     */
    @PreAuthorize("@permits.hasPermit('hub:role:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, RoleQuery query) throws IOException {
    	String fileName = URLEncoder.encode("角色数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        List<SysRole> roleList = roleService.list(Access.tenantId(), query).getRecords();
		EasyExcel.write(response.getOutputStream(), SysRole.class)
		.sheet("角色").registerWriteHandler(new ExcelIgnoreStyle()).doWrite(roleList);
    }

    /**
     * 授权用户
     */
    @PreAuthorize("@permits.hasPermit('hub:role:members:grant')")
    @PostMapping("/user/grant")
    public Response<Void> grantUser(@Validated @RequestBody RoleUserUpdate roleUpdate) {
    	roleService.grantUser(Access.tenantId(), roleUpdate);
    	return Response.success();
    }

    /**
     * 取消用户
     */
    @PreAuthorize("@permits.hasPermit('hub:role:members:cancel')")
    @PostMapping("/user/cancel")
    public Response<Void> cancelUser(@Validated @RequestBody RoleUserUpdate roleUpdate) {
    	roleService.cancelUser(Access.tenantId(), roleUpdate);
    	return Response.success();
    }

    /**
     * 用户列表（已授权）
     */
    @PreAuthorize("@permits.hasPermit('hub:role:members:query')")
    @GetMapping("/users/authed")
    public Response<Response.Page<RoleUserPto>> getAuthedUser(@Valid RoleUserQuery query) {
    	return Response.page(roleService.queryAuthedUser(Access.tenantId(), query));
    }

    /**
     * 用户列表（未授权）
     */
    @PreAuthorize("@permits.hasPermit('hub:role:members:query')")
    @GetMapping("/users/unAuthed")
    public Response<Response.Page<RoleUserPto>> getUnAuthedUser(@Valid RoleUserQuery query) {
    	return Response.page(roleService.queryUnAuthedUser(Access.tenantId(), query));
    }

    /**
     * 角色名称查询
     */
    @GetMapping("/name/{roleIds}")
    public Response<List<String>> getNames(@PathVariable List<Integer> roleIds) {
        return Response.success(roleService.queryNames(Access.tenantId(), roleIds));
    }
}
