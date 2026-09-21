package com.cowave.hub.admin.infra.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.bo.GitlabToken;
import com.cowave.hub.admin.domain.auth.entity.bo.GitlabUser;
import com.cowave.zoo.http.client.annotation.HttpClient;
import com.cowave.zoo.http.client.annotation.HttpHeaders;
import com.cowave.zoo.http.client.annotation.HttpHost;
import com.cowave.zoo.http.client.annotation.HttpLine;
import com.cowave.zoo.http.client.annotation.HttpParam;
import com.cowave.zoo.http.client.response.HttpResponse;

import static com.cowave.zoo.http.client.constants.HttpHeader.Authorization;

/**
 * @author shanhuiming
 */
@HttpClient
public interface GitlabRemoteClient {

    /**
     * 获取Gitlab令牌
     */
    @HttpLine("POST /oauth/token?client_id={clientId}&client_secret={clientSecret}&redirect_uri={redirectUri}&grant_type={grantType}&scope={scope}&code={code}")
    HttpResponse<GitlabToken> getToken(@HttpHost String gitlabUrl,
                                        @HttpParam("clientId") String clientId,
                                        @HttpParam("clientSecret") String clientSecret,
                                        @HttpParam("redirectUri") String redirectUri,
                                        @HttpParam("grantType") String grantType,
                                        @HttpParam("scope") String scope,
                                        @HttpParam("code") String code);

    /**
     * 获取Gitlab用户
     */
    @HttpHeaders({Authorization + ": Bearer {accessToken}"})
    @HttpLine("GET /api/v4/user")
    HttpResponse<GitlabUser> getUser(@HttpHost String gitlabUrl,
                                      @HttpParam("accessToken") String accessToken);
}
