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
package com.cowave.hub.admin.controller.auth;

import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.Access;
import com.cowave.hub.admin.domain.auth.entity.HubOAuthApp;
import com.cowave.hub.admin.domain.auth.entity.command.RoleAppGrant;
import com.cowave.hub.admin.domain.auth.entity.HubOAuthAppMenu;
import com.cowave.hub.admin.domain.auth.entity.vo.OAuthAppCard;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.service.auth.OAuthAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OAuth应用
 * @order 12
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oauth/app")
public class OAuthAppController {

    private final OAuthAppService OAuthAppService;

    /**
     * 授权应用列表
     */
    @PreAuthorize("@permits.hasPermit('oauth:app:query')")
    @GetMapping
    public Response<Response.Page<HubOAuthApp>> listOauthApp(String clientName) {
        return Response.page(OAuthAppService.listOauthApp(Access.tenantId(), clientName));
    }

    /**
     * 新增授权应用
     */
    @PreAuthorize("@permits.hasPermit('oauth:app:create')")
    @PostMapping
    public Response<HubOAuthApp> createOauthApp(@RequestBody HubOAuthApp oauthApp) {
        return Response.success(OAuthAppService.createOauthApp(Access.tenantId(), oauthApp));
    }

    /**
     * 删除授权应用
     */
    @PreAuthorize("@permits.hasPermit('oauth:app:delete')")
    @DeleteMapping("/{ids}")
    public Response<Void> deleteOauthApp(@PathVariable List<Integer> ids) {
        OAuthAppService.deleteOauthApp(Access.tenantId(), ids);
        return Response.success();
    }

    /**
     * 获取授权应用选项
     */
    @GetMapping("/options")
    public Response<List<OAuthAppCard>> getOauthAppOptions() {
        return Response.success(OAuthAppService.queryOauthAppOptions(Access.tenantId()));
    }

    /**
     * 获取人员授权应用
     */
    @GetMapping("/card")
    public Response<List<OAuthAppCard>> getOauthAppCards() {
        return Response.success(OAuthAppService.queryOauthAppCards());
    }

    /**
     * 给角色授权应用
     */
    @PostMapping("/role")
    public Response<Void> grantRoleOauthApp(@RequestBody RoleAppGrant appGrant) {
        OAuthAppService.grantRoleOauthApp(appGrant);
        return Response.success();
    }

    /**
     * 获取角色授权应用
     */
    @GetMapping("/role/{roleId}")
    public Response<List<Integer>> getRoleOauthApp(@PathVariable Integer roleId) {
        return Response.success(OAuthAppService.queryRoleOauthApp(roleId));
    }

    /**
	 * 获取授权应用的菜单列表
     * @param appId 应用id
	 * @param menuName 菜单名称
	 * @param menuStatus 菜单状态
	 */
	@GetMapping("/menu/{appId}")
	public Response<Response.Page<HubOAuthAppMenu>> listAppMenus(
            @PathVariable Integer appId, String menuName, EnableStatus menuStatus){
		return Response.page(OAuthAppService.listAppMenus(appId, menuName, menuStatus));
	}
}
