package com.cowave.hub.admin.infra.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.bo.GitlabToken;
import com.cowave.hub.admin.domain.auth.entity.bo.GitlabUser;
import com.cowave.hub.admin.domain.auth.remote.GitlabRemote;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.http.client.response.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.cowave.zoo.http.client.constants.HttpCode.INTERNAL_SERVER_ERROR;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class GitlabRemoteImpl implements GitlabRemote {

    private final GitlabRemoteClient gitlabRemoteClient;

    @Override
    public GitlabUser getGitlabUser(String gitlabUrl, String clientId, String clientSecret,
                                    String redirectUri, String grantType, String scope, String code) {
        // 授权码兑换令牌
        HttpResponse<GitlabToken> tokenResponse = gitlabRemoteClient.getGitlabToken(gitlabUrl,
                clientId, clientSecret, redirectUri, grantType, scope, code);
        HttpAsserts.isTrue(tokenResponse.isSuccess(), INTERNAL_SERVER_ERROR, tokenResponse.getMessage());

        // 令牌兑换用户信息
        GitlabToken gitlabToken = tokenResponse.getBody();
        HttpResponse<GitlabUser> userResponse = gitlabRemoteClient.getGitlabUser(gitlabUrl, gitlabToken.getAccessToken());
        HttpAsserts.isTrue(userResponse.isSuccess(), INTERNAL_SERVER_ERROR, userResponse.getMessage());

        return userResponse.getBody();
    }
}
