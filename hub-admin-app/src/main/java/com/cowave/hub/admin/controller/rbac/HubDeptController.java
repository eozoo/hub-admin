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
import com.cowave.hub.admin.domain.rbac.entity.pto.*;
import com.cowave.hub.admin.domain.rbac.entity.command.DeptCreate;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptPostQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptQuery;
import com.cowave.hub.admin.domain.rbac.entity.query.DeptUserQuery;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.operation.Operation;
import com.cowave.zoo.framework.support.excel.write.ExcelIgnoreStyle;
import com.cowave.hub.admin.domain.rbac.entity.HubDept;
import com.cowave.hub.admin.domain.rbac.entity.HubDeptPost;
import com.cowave.hub.admin.domain.rbac.entity.HubUserDept;
import com.cowave.hub.admin.service.rbac.HubDeptService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
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
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_DEPT;

/**
 * 部门
 * @order 2
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dept")
public class HubDeptController {

    private final HubDeptService hubDeptService;

    /**
     * 列表
     */
    @PreAuthorize("@permits.hasPermit('hub:dept:query')")
    @GetMapping
    public Response<Response.Page<DeptListPto>> list(DeptQuery query) {
        return Response.page(hubDeptService.list(Access.tenantId(), query));
    }

    /**
     * 详情
     *
     * @param deptId 部门id
     */
    @PreAuthorize("@permits.hasPermit('hub:dept:query')")
    @GetMapping("/{deptId}")
    public Response<DeptInfoPto> info(@PathVariable Integer deptId) {
        return Response.success(hubDeptService.info(Access.tenantId(), deptId));
    }

    /**
     * 新增
     */
    @Operation(module = SYSTEM, type = SYSTEM_DEPT, action = CREATE, desc = "新增部门：#{#dept.deptName}")
    @PreAuthorize("@permits.hasPermit('hub:dept:create')")
    @PostMapping
    public Response<Void> create(@Validated @RequestBody DeptCreate dept) {
        hubDeptService.create(Access.tenantId(), dept);
        return Response.success();
    }

    /**
     * 删除
     *
     * @param deptIds 部门id列表
     */
    @Operation(module = SYSTEM, type = SYSTEM_DEPT, action = DELETE, desc = "删除部门")
    @PreAuthorize("@permits.hasPermit('hub:dept:delete')")
    @DeleteMapping("/{deptIds}")
    public Response<Void> delete(@PathVariable List<Integer> deptIds) {
        hubDeptService.delete(Access.tenantId(), deptIds);
        return Response.success();
    }

    /**
     * 修改
     */
    @Operation(module = SYSTEM, type = SYSTEM_DEPT, action = EDIT, desc = "修改部门：#{#dept.deptName}")
    @PreAuthorize("@permits.hasPermit('hub:dept:edit')")
    @PatchMapping
    public Response<Void> edit(@RequestBody DeptCreate dept) {
        hubDeptService.edit(Access.tenantId(), dept);
        return Response.success();
    }

    /**
     * 导出部门
     */
    @PreAuthorize("@permits.hasPermit('hub:dept:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        String sheet = "部门";
        String excel = "部门数据";
        List<HubDept> list = hubDeptService.queryListForExport(Access.tenantId());
        if (CollectionUtils.isNotEmpty(list)) {
            sheet = list.get(0).getDeptName();
            excel = "部门-" + sheet;
        }
        String fileName = URLEncoder.encode(excel, StandardCharsets.UTF_8).replace("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        EasyExcel.write(response.getOutputStream(), HubDept.class)
                .sheet(sheet).registerWriteHandler(new ExcelIgnoreStyle()).doWrite(list);
    }

    /**
     * 部门组织架构
     *
     * @param deptId 部门id
     */
    @PreAuthorize("@permits.hasPermit('hub:dept:diagram')")
    @GetMapping("/diagram")
    public Response<List<Tree<Integer>>> getDiagram(Integer deptId) {
        return Response.success(hubDeptService.queryDiagram(Access.tenantId(), deptId));
    }

    /**
	 * 部门岗位树
	 */
	@GetMapping("/diagram/post")
	public Response<List<Tree<String>>> getPostDiagram() {
		return Response.success(List.of(hubDeptService.queryPostDiagram(Access.tenantId())));
	}

    /**
     * 部门用户树
     */
    @GetMapping("/diagram/user")
    public Response<List<Tree<String>>> getUserDiagram() {
        return Response.success(List.of(hubDeptService.queryUserDiagram(Access.tenantId())));
    }

    /**
     * 添加部门岗位
     */
    @PreAuthorize("@permits.hasPermit('hub:dept:positions:add')")
    @PostMapping("/posts")
    public Response<Void> addPosts(@Valid @RequestBody List<HubDeptPost> list) {
        hubDeptService.addPosts(Access.tenantId(), list);
        return Response.success();
    }

    /**
     * 移除部门岗位
     *
     * @param deptId 部门id
     * @param postIds 岗位id列表
     */
    @PreAuthorize("@permits.hasPermit('hub:dept:positions:remove')")
    @DeleteMapping("/posts/{deptId}/{postIds}")
    public Response<Void> removePosts(@PathVariable Integer deptId, @PathVariable List<Integer> postIds) {
        hubDeptService.removePosts(Access.tenantId(), deptId, postIds);
        return Response.success();
    }

    /**
     * 获取部门岗位（已设置）
     */
    @PreAuthorize("@permits.hasPermit('hub:dept:positions:query')")
    @GetMapping("/posts/configured")
    public Response<Response.Page<DeptPostPto>> getConfiguredPosts(@Valid DeptPostQuery query) {
        return Response.page(hubDeptService.queryConfiguredPosts(Access.tenantId(), query));
    }

    /**
     * 获取部门岗位（未设置）
     */
    @PreAuthorize("@permits.hasPermit('hub:dept:positions:query')")
    @GetMapping("/posts/unConfigured")
    public Response<Response.Page<DeptPostPto>> getUnConfiguredPosts(@Valid DeptPostQuery query) {
        return Response.page(hubDeptService.queryUnConfiguredPosts(Access.tenantId(), query));
    }

    /**
     * 添加部门成员
     */
    @PreAuthorize("@permits.hasPermit('hub:dept:members:add')")
    @PostMapping("/members")
    public Response<Void> addMembers(@Valid @RequestBody List<HubUserDept> list) {
        hubDeptService.addMembers(Access.tenantId(), list);
        return Response.success();
    }

    /**
     * 移除部门成员
     *
     * @param deptId 部门id
     * @param userIds 用户id列表
     */
    @PreAuthorize("@permits.hasPermit('hub:dept:members:remove')")
    @DeleteMapping("/members/{deptId}/{userIds}")
    public Response<Void> removeMembers(@PathVariable Integer deptId, @PathVariable List<Integer> userIds) {
        hubDeptService.removeMembers(Access.tenantId(), deptId, userIds);
        return Response.success();
    }

    /**
     * 获取部门成员（已加入）
     */
    @PreAuthorize("@permits.hasPermit('hub:dept:members:query')")
    @GetMapping("/members/joined")
    public Response<Response.Page<DeptUserPto>> getJoinedMembers(@Valid DeptUserQuery query) {
        return Response.page(hubDeptService.queryJoinedMembers(Access.tenantId(), query));
    }

    /**
     * 获取部门成员（未加入）
     */
    @PreAuthorize("@permits.hasPermit('hub:dept:members:query')")
    @GetMapping("/members/unJoined")
    public Response<Response.Page<DeptUserPto>> getUnJoinedMembers(@Valid DeptUserQuery query) {
        return Response.page(hubDeptService.queryUnJoinedMembers(Access.tenantId(), query));
    }

    /**
     * 部门流程候选人
     *
     * @param deptCode 部门编码
     */
    @GetMapping("/candidates/{deptCode}")
    public Response<List<UserNamePto>> getCandidatesByCode(@PathVariable String deptCode) {
        return Response.success(hubDeptService.queryCandidatesByCode(Access.tenantId(), deptCode));
    }

    /**
     * 部门名称查询
     */
    @GetMapping("/name/{userIds}")
    public Response<List<String>> getNamesById(@PathVariable List<Integer> userIds) {
        return Response.success(hubDeptService.queryNamesById(Access.tenantId(), userIds));
    }
}
