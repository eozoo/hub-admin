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
package com.cowave.hub.admin.service.member;

import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.hub.admin.domain.member.entity.vo.HubOAuthVo;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface MemberOAuthService {

    /**
     * 三方授权方式列表
     */
    List<HubOAuthVo> memberOauthList(String tenantId);

    /**
     * Gitlab回调认证
     */
    AccessUserDetails memberGitlabCallback(String tenantId, String code);

}
