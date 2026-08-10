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

import cn.hutool.core.lang.tree.Tree;
import com.cowave.zoo.http.client.asserts.I18Messages;
import com.cowave.zoo.http.client.response.HttpResponse;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.Access;
import com.cowave.hub.admin.domain.auth.entity.pto.UserProfile;
import com.cowave.hub.admin.domain.auth.entity.command.ApiTokenCreate;
import com.cowave.hub.admin.domain.auth.entity.command.MfaBind;
import com.cowave.hub.admin.domain.auth.entity.command.PasswdReset;
import com.cowave.hub.admin.domain.auth.entity.command.ProfileUpdate;
import com.cowave.hub.admin.domain.auth.entity.vo.TokenVo;
import com.cowave.hub.admin.domain.auth.entity.vo.MfaVo;
import com.cowave.hub.admin.service.auth.ApiTokenService;
import com.cowave.hub.admin.service.auth.ProfileService;
import com.cowave.hub.admin.service.rbac.HubMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cowave.zoo.http.client.constants.HttpCode.UNAUTHORIZED;

/**
 * 个人信息
 * @order 13
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private final ProfileService profileService;
    private final ApiTokenService apiTokenService;
    private final HubMenuService menuService;

    /**
     * 详情
     */
    @GetMapping
    public Response<UserProfile> info() throws Exception {
        return Response.success(profileService.info());
    }

    /**
     * 修改
     */
    @PatchMapping
    public Response<Void> edit(@Validated @RequestBody ProfileUpdate profile) throws Exception {
        profileService.edit(profile);
        return Response.success();
    }

    /**
     * 重置密码
     */
    @PatchMapping(value = {"/passwd"})
    public HttpResponse<Response<Void>> resetPasswd(@RequestBody PasswdReset passwdReset) {
        profileService.resetPasswd(passwdReset);
        return HttpResponse.body(UNAUTHORIZED, Response.msg(UNAUTHORIZED,
                I18Messages.translateIfNeed("{admin.auth.passwd.reset}")));
    }

	/**
     * MFA获取
     */
    @GetMapping("/mfa")
    public Response<MfaVo> generateMfa() {
        return Response.success(profileService.generateMfa());
    }

    /**
     * MFA绑定
     */
    @PatchMapping("/mfa/enable")
    public Response<Void> enableMfa(@Validated @RequestBody MfaBind mfaBind) {
        profileService.enableMfa(mfaBind);
        return Response.success();
    }

    /**
     * MFA解除
     */
    @PatchMapping("/mfa/disable")
    public Response<Void> disableMfa(@Validated @RequestBody MfaBind mfaBind) {
        profileService.disableMfa(mfaBind);
        return Response.success();
    }

    /**
	 * Api令牌权限集
	 */
	@GetMapping("/api/permits")
	public Response<List<Tree<Integer>>> getApiTree(){
		return Response.success(menuService.queryApiPermitsByUser(Access.tenantId()));
	}

    /**
	 * Api令牌列表
	 */
	@GetMapping("/api/token")
	public Response<List<TokenVo>> listApiToken() {
		return Response.success(apiTokenService.listApiToken());
	}

    /**
	 * 创建Api令牌
	 */
	@PostMapping("/api/token")
	public Response<String> creatApiToken(@RequestBody ApiTokenCreate tokenCreate) {
		return Response.success(apiTokenService.creatApiToken(tokenCreate));
	}

    /**
	 * 删除Api令牌
	 */
	@DeleteMapping("/api/token/{tokenId}")
	public Response<Void> deleteApiToken(@PathVariable Integer tokenId) {
		apiTokenService.deleteApiToken(tokenId);
		return Response.success();
	}
}
