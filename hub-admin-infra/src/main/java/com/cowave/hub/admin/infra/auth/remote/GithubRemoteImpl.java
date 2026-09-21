package com.cowave.hub.admin.infra.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.bo.GithubToken;
import com.cowave.hub.admin.domain.auth.entity.bo.GithubUser;
import com.cowave.hub.admin.domain.auth.remote.GithubRemote;
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
public class GithubRemoteImpl implements GithubRemote {

    private final GithubRemoteClient githubRemoteClient;

    @Override
    public GithubUser getUser(SysAuthProvider provider, String code) {
        // 授权码兑换令牌
        HttpResponse<GithubToken> tokenResponse = githubRemoteClient.getToken(
                provider.getClientId(), provider.getClientSecret(), provider.getRedirectUrl(), code);
        HttpAsserts.isTrue(tokenResponse.isSuccess(), INTERNAL_SERVER_ERROR, tokenResponse.getMessage());
        // 令牌兑换用户信息
        GithubToken token = tokenResponse.getBody();
        HttpAsserts.notNull(token, INTERNAL_SERVER_ERROR, "GitHub token response is empty");
        HttpAsserts.isTrue(token.getAccessToken() != null, INTERNAL_SERVER_ERROR, "GitHub access token is empty");
        HttpResponse<GithubUser> userResponse = githubRemoteClient.getUser(token.getAccessToken());
        HttpAsserts.isTrue(userResponse.isSuccess(), INTERNAL_SERVER_ERROR, userResponse.getMessage());
        return userResponse.getBody();
    }
}
