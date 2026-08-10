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
package com.cowave.hub.admin.controller.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.bo.GitlabToken;
import com.cowave.hub.admin.domain.auth.entity.bo.GitlabUser;
import com.cowave.hub.admin.service.auth.remote.GitlabRemoteService;
import com.cowave.zoo.http.client.response.HttpResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Primary
@Component
public class GitlabRemoteServiceTestImpl implements GitlabRemoteService {

    @Override
    public HttpResponse<GitlabToken> getGitlabToken(String gitlabUrl, String clientId, String clientSecret,
                                                     String redirectUri, String grantType, String scope, String code) {
        GitlabToken token = new GitlabToken();
        token.setScope(scope);
        token.setTokenType("Bearer");
        token.setExpiresIn(7200);
        token.setAccessToken("test-gitlab-access-token");
        token.setRefreshToken("test-gitlab-refresh-token");
        token.setCreatedAt(new Date());
        return HttpResponse.success(token);
    }

    @Override
    public HttpResponse<GitlabUser> getGitlabUser(String gitlabUrl, String accessToken) {
        GitlabUser user = new GitlabUser();
        user.setName("Gitlab测试用户");
        user.setUsername("gitlabtest");
        user.setAvatarUrl("https://gitlab.cowave.com/uploads/-/system/user/avatar/1/avatar.png");
        user.setEmail("gitlabtest@cowave.com");
        return HttpResponse.success(user);
    }
}
