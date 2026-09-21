package com.cowave.hub.admin.infra.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.bo.GithubToken;
import com.cowave.hub.admin.domain.auth.entity.bo.GithubUser;
import com.cowave.zoo.http.client.annotation.HttpClient;
import com.cowave.zoo.http.client.annotation.HttpHeaders;
import com.cowave.zoo.http.client.annotation.HttpLine;
import com.cowave.zoo.http.client.annotation.HttpParam;
import com.cowave.zoo.http.client.response.HttpResponse;

import static com.cowave.zoo.http.client.constants.HttpHeader.Authorization;

/**
 * @author shanhuiming
 */
@HttpClient
public interface GithubRemoteClient {

    /**
     * 获取GitHub令牌
     */
    @HttpHeaders("Accept: application/json")
    @HttpLine("POST https://github.com/login/oauth/access_token?client_id={clientId}&client_secret={clientSecret}&redirect_uri={redirectUri}&code={code}")
    HttpResponse<GithubToken> getToken(@HttpParam("clientId") String clientId,
                                       @HttpParam("clientSecret") String clientSecret,
                                       @HttpParam("redirectUri") String redirectUri,
                                       @HttpParam("code") String code);

    /**
     * 获取GitHub用户
     */
    @HttpHeaders({Authorization + ": Bearer {accessToken}", "Accept: application/vnd.github+json"})
    @HttpLine("GET https://api.github.com/user")
    HttpResponse<GithubUser> getUser(@HttpParam("accessToken") String accessToken);
}
