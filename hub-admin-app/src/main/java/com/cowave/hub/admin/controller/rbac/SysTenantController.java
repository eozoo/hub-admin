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

import com.cowave.hub.admin.domain.rbac.entity.query.TenantQuery;
import com.cowave.hub.admin.domain.rbac.entity.command.*;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.hub.admin.domain.sys.entity.vo.SelectOptionVo;
import com.cowave.hub.admin.domain.rbac.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac.entity.pto.TenantManagerPto;
import com.cowave.hub.admin.service.rbac.SysTenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 租户
 * @order 1
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tenant")
public class SysTenantController {

    private final SysTenantService tenantService;

    /**
     * 列表
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:query')")
    @GetMapping
    public Response<Response.Page<SysTenant>> list(TenantQuery query) {
        return Response.page(tenantService.page(query));
    }

    /**
     * 详情
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:query')")
    @GetMapping("/{tenantId}")
    public Response<SysTenant> info(@PathVariable String tenantId) {
        return Response.success(tenantService.info(tenantId));
    }

    /**
     * 新增
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:creat')")
    @PostMapping
    public Response<Void> create(@Validated @RequestBody TenantCreate tenantCreate) {
        tenantService.create(tenantCreate);
        return Response.success();
    }

    /**
     * 修改
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:edit')")
    @PatchMapping
    public Response<Void> edit(@Validated @RequestBody TenantCreate tenantCreate) {
        tenantService.edit(tenantCreate);
        return Response.success();
    }

    /**
     * 修改状态
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:status')")
    @PatchMapping("/status")
    public Response<Void> updateStatus(@RequestBody TenantStatusUpdate statusUpdate) {
        tenantService.updateStatus(statusUpdate);
        return Response.success();
    }

    /**
     * 管理员列表
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:manager:query')")
    @GetMapping("/manager/{tenantId}")
    public Response<Response.Page<TenantManagerPto>> listManager(@PathVariable String tenantId) {
        return Response.page(tenantService.listManager(tenantId));
    }

    /**
     * 创建管理员
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:manager:create')")
    @PostMapping("/manager")
    public Response<Void> createManager(@Validated @RequestBody TenantManagerCreate managerCreate) {
        tenantService.createManager(managerCreate);
        return Response.success();
    }

    /**
     * 移除管理员
     */
    @PreAuthorize("@permits.hasPermit('sys:tenant:manager:remove')")
    @PatchMapping("/manager/remove")
    public Response<Void> removeManager(@Validated @RequestBody TenantManagerRemove managerRemove) {
        tenantService.removeManager(managerRemove);
        return Response.success();
    }

    /**
     * 租户选项
     */
    @GetMapping("/options")
    public Response<List<SelectOptionVo>> tenantOptions() {
        return Response.success(tenantService.queryTenantOptions());
    }
}
