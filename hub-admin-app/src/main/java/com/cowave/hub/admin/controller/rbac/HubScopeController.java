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

import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.Access;
import com.cowave.hub.admin.domain.rbac.entity.HubScope;
import com.cowave.hub.admin.domain.rbac.entity.command.ScopeInfoUpdate;
import com.cowave.hub.admin.domain.rbac.entity.command.ScopeNameUpdate;
import com.cowave.hub.admin.domain.rbac.entity.query.ScopeQuery;
import com.cowave.hub.admin.domain.rbac.entity.command.ScopeStatusUpdate;
import com.cowave.hub.admin.service.rbac.HubScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据权限
 * @order 14
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/scope")
public class HubScopeController {

    private final HubScopeService hubScopeService;

    /**
	 * 列表
	 */
	@PreAuthorize("@permits.hasPermit('hub:scope:query')")
	@GetMapping
	public Response<Response.Page<HubScope>> list(ScopeQuery query) {
		return Response.page(hubScopeService.page(Access.tenantId(), query));
	}

    /**
	 * 详情
	 */
	@PreAuthorize("@permits.hasPermit('hub:scope:query')")
	@GetMapping("/{scopeId}")
	public Response<HubScope> info(@PathVariable Integer scopeId) {
		return Response.success(hubScopeService.info(Access.tenantId(), scopeId));
	}

    /**
     * 新增
     */
    @PreAuthorize("@permits.hasPermit('hub:scope:create')")
    @PostMapping
    public Response<Void> create(@RequestBody HubScope hubScope) {
        hubScopeService.create(Access.tenantId(), hubScope);
        return Response.success();
    }

    /**
     * 删除
     */
    @PreAuthorize("@permits.hasPermit('hub:scope:delete')")
    @DeleteMapping("/{scopeIds}")
    public Response<Void> delete(@PathVariable List<Integer> scopeIds) {
        hubScopeService.delete(Access.tenantId(), scopeIds);
        return Response.success();
    }

    /**
     * 修改
     */
    @PreAuthorize("@permits.hasPermit('hub:scope:edit')")
    @PatchMapping
    public Response<Void> edit(@RequestBody ScopeNameUpdate nameUpdate) {
        hubScopeService.edit(Access.tenantId(), nameUpdate);
        return Response.success();
    }

    /**
     * 修改状态
     */
    @PreAuthorize("@permits.hasPermit('hub:scope:edit')")
    @PatchMapping("/status")
    public Response<Void> switchStatus(@RequestBody ScopeStatusUpdate statusUpdate) {
        hubScopeService.switchStatus(Access.tenantId(), statusUpdate);
        return Response.success();
    }

    /**
     * 修改权限内容
     */
    @PreAuthorize("@permits.hasPermit('hub:scope:edit')")
    @PatchMapping("/content")
    public Response<Void> editContent(@RequestBody ScopeInfoUpdate infoUpdate) {
        hubScopeService.editContent(Access.tenantId(), infoUpdate);
        return Response.success();
    }
}
