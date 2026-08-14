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
import com.cowave.hub.admin.domain.auth.entity.SysLdap;
import com.cowave.hub.admin.domain.auth.entity.SysLdapUser;
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
    public Response<SysLdap> getLdap() {
        return Response.success(ldapService.getLdap(Access.tenantId()));
    }

    /**
     * 修改配置
     */
    @PreAuthorize("@permits.hasPermit('sys:ldap:edit')")
    @PatchMapping
    public Response<Void> editLdap(@Validated @RequestBody SysLdap sysLdap) {
        ldapService.editLdap(Access.tenantId(), sysLdap);
        return Response.success();
    }

    /**
     * 测试配置
     */
    @PreAuthorize("@permits.hasPermit('sys:ldap:edit')")
    @PostMapping("/valid")
    public Response<Void> validConfig(@Validated @RequestBody SysLdap sysLdap) {
        ldapService.validConfig(sysLdap);
        return Response.success();
    }

    /**
     * 用户列表
     * @param ldapAccount ladp账号
     */
    @PreAuthorize("@permits.hasPermit('sys:ldap:query')")
    @GetMapping(value = {"/user"})
    public Response<Response.Page<SysLdapUser>> listUser(String ldapAccount) {
        return Response.page(ldapService.listUser(Access.tenantId(), ldapAccount));
    }
}
