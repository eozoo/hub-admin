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
import com.cowave.hub.admin.domain.auth.entity.SysAuthLdap;
import com.cowave.hub.admin.domain.auth.entity.command.IdentityStatusUpdate;
import com.cowave.hub.admin.domain.auth.entity.vo.LdapUserVo;
import com.cowave.hub.admin.service.auth.LdapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Ldap鉴权
 * @order 10
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ldap")
public class LdapController {

    private final LdapService ldapService;

    /**
     * 获取配置
     */
    @PreAuthorize("@permits.hasPermit('sys:ldap:query')")
    @GetMapping
    public Response<SysAuthLdap> getLdap() {
        return Response.success(ldapService.getLdap());
    }

    /**
     * 修改配置
     */
    @PreAuthorize("@permits.hasPermit('sys:ldap:edit')")
    @PatchMapping
    public Response<Void> editLdap(@Validated @RequestBody SysAuthLdap config) {
        ldapService.editLdap(config);
        return Response.success();
    }

    /**
     * 测试配置
     */
    @PreAuthorize("@permits.hasPermit('sys:ldap:edit')")
    @PostMapping("/valid")
    public Response<Void> validConfig(@Validated @RequestBody SysAuthLdap config) {
        ldapService.validConfig(config);
        return Response.success();
    }

    /**
     * 用户列表
     * @param ldapAccount ladp账号
     */
    @PreAuthorize("@permits.hasPermit('sys:ldap:query')")
    @GetMapping(value = {"/user"})
    public Response<Response.Page<LdapUserVo>> listUser(
            @RequestParam(value = "ldapAccount", required = false) String ldapAccount) {
        return Response.page(ldapService.listUser(ldapAccount));
    }

    /**
     * 修改LDAP身份状态
     */
    @PreAuthorize("@permits.hasPermit('sys:ldap:edit')")
    @PatchMapping("/user/{identityId}/status")
    public Response<Void> updateIdentityStatus(@PathVariable("identityId") Long identityId,
                                               @Validated @RequestBody IdentityStatusUpdate command) {
        ldapService.updateIdentityStatus(identityId, command.getAuthStatus());
        return Response.success();
    }

    /**
     * 删除LDAP身份绑定
     */
    @PreAuthorize("@permits.hasPermit('sys:ldap:delete')")
    @DeleteMapping("/user/{identityId}")
    public Response<Void> deleteIdentity(@PathVariable("identityId") Long identityId) {
        ldapService.deleteIdentity(identityId);
        return Response.success();
    }
}
