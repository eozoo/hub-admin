package com.cowave.hub.admin.infra.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.bo.GitlabToken;
import com.cowave.hub.admin.domain.auth.entity.bo.GitlabUser;
import com.cowave.hub.admin.domain.auth.remote.GitlabRemote;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.http.client.response.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.net.URI;
import org.apache.commons.lang3.StringUtils;

import static com.cowave.zoo.http.client.constants.HttpCode.INTERNAL_SERVER_ERROR;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class GitlabRemoteImpl implements GitlabRemote {

    private final GitlabRemoteClient gitlabRemoteClient;

    @Override
    public GitlabUser getUser(SysAuthProvider provider, String code) {
        String gitlabUrl = StringUtils.removeEnd(URI.create(provider.getAuthUrl()).resolve("../").toString(), "/");
        // 授权码兑换令牌
        HttpResponse<GitlabToken> tokenResponse = gitlabRemoteClient.getToken(gitlabUrl,
                provider.getClientId(), provider.getClientSecret(), provider.getRedirectUrl(),
                provider.getGrantType(), provider.getAuthScope(), code);
        HttpAsserts.isTrue(tokenResponse.isSuccess(), INTERNAL_SERVER_ERROR, tokenResponse.getMessage());
        // 令牌兑换用户信息
        GitlabToken token = tokenResponse.getBody();
        HttpAsserts.notNull(token, INTERNAL_SERVER_ERROR, "GitLab token response is empty");
        HttpResponse<GitlabUser> userResponse = gitlabRemoteClient.getUser(gitlabUrl, token.getAccessToken());
        HttpAsserts.isTrue(userResponse.isSuccess(), INTERNAL_SERVER_ERROR, userResponse.getMessage());
        return userResponse.getBody();
    }
}
