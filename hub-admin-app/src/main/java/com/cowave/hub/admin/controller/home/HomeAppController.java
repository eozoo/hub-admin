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
package com.cowave.hub.admin.controller.home;

import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.annotation.AnonymousGetMapping;
import com.cowave.hub.admin.domain.home.entity.HubApp;
import com.cowave.hub.admin.domain.home.entity.command.RoleAppGrant;
import com.cowave.hub.admin.domain.home.entity.HubAppMenu;
import com.cowave.hub.admin.domain.home.entity.vo.OAuthAppCard;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.service.home.HomeAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Home门户 OAuth应用管理
 * @order 22
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/home/app")
public class HomeAppController {

    private final HomeAppService homeAppService;

    /**
     * 应用导航列表（匿名）
     */
    @AnonymousGetMapping("/nav")
    public Response<List<OAuthAppCard>> getAppNav(@RequestParam("tenantId") String tenantId) {
        return Response.success(homeAppService.queryAppNav(tenantId));
    }

    /**
     * 应用导航列表（登录）
     */
    @GetMapping("/nav/authorized")
    public Response<List<OAuthAppCard>> getAuthorizedAppNav() {
        return Response.success(homeAppService.queryAppNav(null));
    }

    /**
     * 应用列表
     */
    @PreAuthorize("@permits.hasPermit('oauth:app:query')")
    @GetMapping
    public Response<Response.Page<HubApp>> listOauthApp(String clientName) {
        return Response.page(homeAppService.listOauthApp(Access.tenantId(), clientName));
    }

    /**
     * 新增应用
     */
    @PreAuthorize("@permits.hasPermit('oauth:app:create')")
    @PostMapping
    public Response<HubApp> createOauthApp(@RequestBody HubApp oauthApp) {
        return Response.success(homeAppService.createOauthApp(Access.tenantId(), oauthApp));
    }

    /**
     * 删除应用
     */
    @PreAuthorize("@permits.hasPermit('oauth:app:delete')")
    @DeleteMapping("/{ids}")
    public Response<Void> deleteOauthApp(@PathVariable List<Integer> ids) {
        homeAppService.deleteOauthApp(Access.tenantId(), ids);
        return Response.success();
    }

    /**
     * 获取授权应用选项
     */
    @GetMapping("/options")
    public Response<List<OAuthAppCard>> getOauthAppOptions() {
        return Response.success(homeAppService.queryOauthAppOptions(Access.tenantId()));
    }

    /**
     * 给角色授权应用
     */
    @PostMapping("/role")
    public Response<Void> grantRoleOauthApp(@RequestBody RoleAppGrant appGrant) {
        homeAppService.grantRoleOauthApp(appGrant);
        return Response.success();
    }

    /**
     * 获取角色授权应用
     */
    @GetMapping("/role/{roleId}")
    public Response<List<Integer>> getRoleOauthApp(@PathVariable Integer roleId) {
        return Response.success(homeAppService.queryRoleOauthApp(roleId));
    }

    /**
     * 应用菜单列表
     * @param appId 应用id
     * @param menuName 菜单名称
     * @param menuStatus 菜单状态
     */
    @GetMapping("/menu/{appId}")
    public Response<Response.Page<HubAppMenu>> listAppMenus(
            @PathVariable Integer appId, String menuName, EnableStatus menuStatus){
        return Response.page(homeAppService.listAppMenus(appId, menuName, menuStatus));
    }
}
