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
package com.cowave.hub.admin.controller.member;

import com.cowave.zoo.framework.access.annotation.AnonymousGetMapping;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.hub.admin.domain.member.entity.vo.HubOAuthVo;
import com.cowave.hub.admin.service.member.MemberOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 门户 三方认证
 * @order 20
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/member")
public class MemberOAuthController {

    private final MemberOAuthService memberOauthService;

    /**
     * 三方授权方式列表
     */
    @AnonymousGetMapping("/oauth/list")
    public Response<List<HubOAuthVo>> memberOauthList(@RequestParam("tenantId") String tenantId) {
        return Response.success(memberOauthService.memberOauthList(tenantId));
    }

    /**
     * Gitlab回调认证
     */
    @AnonymousGetMapping("/oauth/gitlab")
    public Response<AccessUserDetails> memberGitlabCallback(
            @RequestParam("tenantId") String tenantId, @RequestParam("code") String code) {
        return Response.success(memberOauthService.memberGitlabCallback(tenantId, code));
    }

}
