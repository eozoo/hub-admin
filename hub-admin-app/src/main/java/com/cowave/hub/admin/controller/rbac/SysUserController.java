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

import cn.hutool.core.lang.tree.Tree;
import com.alibaba.excel.EasyExcel;
import com.cowave.hub.admin.domain.rbac.entity.query.UserExportQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.UserMemberQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.UserQuery;
import com.cowave.hub.admin.domain.rbac.entity.command.*;
import com.cowave.zoo.http.client.asserts.I18Messages;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.operation.Operation;
import com.cowave.zoo.framework.support.excel.write.DropdownWriteHandler;
import com.cowave.zoo.framework.support.excel.write.ExcelIgnoreStyle;
import com.cowave.hub.admin.domain.rbac.entity.SysUser;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserInfoPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserListPto;
import com.cowave.hub.admin.domain.rbac.entity.pto.UserNamePto;
import com.cowave.hub.admin.domain.rbac.entity.vo.UserMemberOption;
import com.cowave.hub.admin.service.rbac.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.cowave.hub.admin.domain.sys.enums.OpAction.*;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_USER;

/**
 * 用户
 * @order 4
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/user")
public class SysUserController {

    private final SysUserService userService;

    /**
     * 列表
     */
    @PreAuthorize("@permits.hasPermit('hub:user:query')")
    @GetMapping
    public Response<Response.Page<UserListPto>> list(UserQuery query) {
        return Response.success(userService.list(Access.tenantId(), query));
    }

    /**
     * 详情
     *
     * @param userId 用户id
     */
    @PreAuthorize("@permits.hasPermit('hub:user:query')")
    @GetMapping("/{userId}")
    public Response<UserInfoPto> info(@PathVariable Integer userId) {
        return Response.success(userService.info(Access.tenantId(), userId));
    }

    /**
     * 新增
     */
    @Operation(module = SYSTEM, type = SYSTEM_USER, action = CREATE, desc = "新增用户：#{#user.userName}")
    @PreAuthorize("@permits.hasPermit('hub:user:create')")
    @PostMapping
    public Response<Void> create(@Validated @RequestBody UserCreate user) {
        userService.create(Access.tenantId(), user);
        return Response.success();
    }

    /**
     * 删除
     *
     * @param userIds id列表
     */
    @Operation(module = SYSTEM, type = SYSTEM_USER, action = DELETE, desc = "删除用户")
    @PreAuthorize("@permits.hasPermit('hub:user:delete')")
    @DeleteMapping("/{userIds}")
    public Response<Void> delete(@PathVariable List<Integer> userIds) {
        userService.delete(Access.tenantId(), userIds);
        return Response.success();
    }

    /**
     * 修改
     */
    @Operation(module = SYSTEM, type = SYSTEM_USER, action = EDIT, desc = "修改用户：#{#user.userName}")
    @PreAuthorize("@permits.hasPermit('hub:user:edit')")
    @PatchMapping
    public Response<Void> edit(@Validated @RequestBody UserCreate user) {
        userService.edit(Access.tenantId(), user);
        return Response.success();
    }

    /**
     * 修改角色
     */
    @Operation(module = SYSTEM, type = SYSTEM_USER, action = GRANT, desc = "修改用户角色：#{#user.userName}")
    @PreAuthorize("@permits.hasPermit('hub:user:grant')")
    @PatchMapping("/roles")
    public Response<Void> changeRoles(@Validated @RequestBody UserRoleUpdate user) {
        userService.changeRoles(Access.tenantId(), user);
        return Response.success();
    }

    /**
     * 修改状态
     */
    @Operation(module = SYSTEM, type = SYSTEM_USER, action = STATUS, desc = "修改用户状态：#{#user.userName}")
    @PreAuthorize("@permits.hasPermit('hub:user:status')")
    @PatchMapping("/status")
    public Response<Void> changeStatus(@Validated @RequestBody UserStatusUpdate user) {
        userService.changeStatus(Access.tenantId(), user);
        return Response.success();
    }

    /**
     * 修改密码
     */
    @Operation(module = SYSTEM, type = SYSTEM_USER, action = PASSWD, desc = "修改用户密码：#{#user.userName}")
    @PreAuthorize("@permits.hasPermit('hub:user:passwd')")
    @PatchMapping("/passwd")
    public Response<Void> changePasswd(@Validated @RequestBody UserPasswdUpdate user) {
        userService.changePasswd(Access.tenantId(), user);
        return Response.success();
    }

    /**
     * 导入用户
     */
    @PreAuthorize("@permits.hasPermit('hub:user:import')")
    @PostMapping("/import")
    public Response<Void> importUser(MultipartFile file, boolean updateSupport) throws Exception {
        try (InputStream inputStream = file.getInputStream()) {
            List<SysUser> list = EasyExcel.read(inputStream).head(SysUser.class).sheet().doReadSync();
            userService.importUsers(Access.tenantId(), list, updateSupport);
        }
        return Response.success(null, I18Messages.msg("admin.import.success"));
    }

    /**
     * 导出用户
     */
    @PreAuthorize("@permits.hasPermit('hub:user:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserExportQuery query) throws Exception {
        String fileName = URLEncoder.encode("用户数据", StandardCharsets.UTF_8).replace("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        List<SysUser> userList = userService.queryListForExport(Access.tenantId(), query);
        EasyExcel.write(response.getOutputStream(), SysUser.class).sheet("用户")
                .registerWriteHandler(new ExcelIgnoreStyle()).doWrite(userList);
    }

    /**
     * 导出模板
     */
    @PreAuthorize("@permits.hasPermit('hub:user:export')")
    @PostMapping("/export/template")
    public void exportTemplate(HttpServletResponse response) throws Exception {
        String fileName = URLEncoder.encode("test", StandardCharsets.UTF_8).replace("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        DropdownWriteHandler sexHandler =
                new DropdownWriteHandler(List.of("男", "女", "未知"), 3, 1000);
        DropdownWriteHandler statusHandler =
                new DropdownWriteHandler(List.of("启用", "停用"), 6, 1000);
        EasyExcel.write(response.getOutputStream(), SysUser.class)
                .sheet("用户")
                .registerWriteHandler(sexHandler)
                .registerWriteHandler(statusHandler)
                .doWrite(new ArrayList<>());
    }

    /**
     * 用户组织架构
     */
    @PreAuthorize("@permits.hasPermit('hub:user:diagram')")
    @GetMapping("/diagram")
    public Response<Tree<Integer>> getDiagram() {
        return Response.success(userService.queryDiagram(Access.tenantId()));
    }

    /**
     * 用户流程候选人
     *
     * @param userId 用户id
     */
    @GetMapping("/candidates")
    public Response<List<UserNamePto>> getUserCandidates(Integer userId) {
        return Response.success(userService.queryUserCandidates(Access.tenantId(), userId));
    }

    /**
     * 用户名称查询
     */
    @GetMapping("/name/{userIds}")
    public Response<List<String>> getNamesById(@PathVariable List<Integer> userIds) {
        return Response.success(userService.queryNamesById(Access.tenantId(), userIds));
    }

    /**
     * 用户成员选项
     */
    @PostMapping("/options")
    public Response<Response.Page<UserMemberOption>> getUserOptions(@RequestBody UserMemberQuery query) {
        return Response.page(userService.queryUserOptions(Access.tenantId(), query), UserMemberOption.class);
    }
}
