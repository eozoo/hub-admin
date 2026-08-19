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

import com.cowave.zoo.framework.access.annotation.AnonymousGetMapping;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.hub.admin.domain.home.entity.vo.HubOAuthVo;
import com.cowave.hub.admin.service.home.HomeOAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Home门户 三方授权服务
 * @order 20
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/api/v1/home/oauth")
public class HomeOAuthController {

    private final HomeOAuthService homeOauthService;

    /**
     * 三方授权方式列表
     */
    @AnonymousGetMapping("/list")
    public Response<List<HubOAuthVo>> oauthList(@RequestParam("tenantId") String tenantId) {
        return Response.success(homeOauthService.oauthList(tenantId));
    }

    /**
     * Gitlab回调认证
     */
    @AnonymousGetMapping("/gitlab")
    public Response<AccessUserDetails> gitlabCallback(
            @RequestParam("tenantId") String tenantId, @RequestParam("code") String code) {
        return Response.success(homeOauthService.gitlabCallback(tenantId, code));
    }

}
