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
package com.cowave.hub.admin.domain.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.bo.GitlabUser;

/**
 * @author shanhuiming
 */
public interface GitlabRemote {

    /**
     * 回调获取gitlab用户信息
     * @param gitlabUrl 授权服务url
     * @param clientId 应用id
     * @param clientSecret 应用secret
     * @param redirectUri 应用回调地址
     * @param grantType 授权方式
     * @param scope 授权范围
     * @param code 授权码
     */
    GitlabUser getGitlabUser(String gitlabUrl, String clientId, String clientSecret,
                             String redirectUri, String grantType, String scope, String code);
}
