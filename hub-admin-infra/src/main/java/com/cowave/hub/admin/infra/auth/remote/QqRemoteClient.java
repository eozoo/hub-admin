package com.cowave.hub.admin.infra.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.bo.QqToken;
import com.cowave.hub.admin.domain.auth.entity.bo.QqUser;
import com.cowave.zoo.http.client.annotation.HttpClient;
import com.cowave.zoo.http.client.annotation.HttpLine;
import com.cowave.zoo.http.client.annotation.HttpParam;
import com.cowave.zoo.http.client.response.HttpResponse;

/**
 * @author shanhuiming
 */
@HttpClient
public interface QqRemoteClient {

    /**
     * 获取QQ令牌和OpenID，以JSON格式返回
     */
    @HttpLine("GET https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id={clientId}&client_secret={clientSecret}&code={code}&redirect_uri={redirectUri}&fmt=json&need_openid=1")
    HttpResponse<QqToken> getToken(@HttpParam("clientId") String clientId,
                                  @HttpParam("clientSecret") String clientSecret,
                                  @HttpParam("code") String code,
                                  @HttpParam("redirectUri") String redirectUri);

    /**
     * 获取QQ用户
     */
    @HttpLine("GET https://graph.qq.com/user/get_user_info?access_token={accessToken}&oauth_consumer_key={clientId}&openid={openid}")
    HttpResponse<QqUser> getUser(@HttpParam("accessToken") String accessToken,
                                @HttpParam("clientId") String clientId,
                                @HttpParam("openid") String openid);
}
