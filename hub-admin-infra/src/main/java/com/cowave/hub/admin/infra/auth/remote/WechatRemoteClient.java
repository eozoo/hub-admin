package com.cowave.hub.admin.infra.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.bo.WechatToken;
import com.cowave.hub.admin.domain.auth.entity.bo.WechatUser;
import com.cowave.zoo.http.client.annotation.HttpClient;
import com.cowave.zoo.http.client.annotation.HttpLine;
import com.cowave.zoo.http.client.annotation.HttpParam;
import com.cowave.zoo.http.client.response.HttpResponse;

/**
 * @author shanhuiming
 */
@HttpClient
public interface WechatRemoteClient {

    /**
     * 获取微信令牌
     */
    @HttpLine("GET https://api.weixin.qq.com/sns/oauth2/access_token?appid={appId}&secret={secret}&code={code}&grant_type=authorization_code")
    HttpResponse<WechatToken> getToken(@HttpParam("appId") String appId,
                                       @HttpParam("secret") String secret,
                                       @HttpParam("code") String code);

    /**
     * 获取微信用户
     */
    @HttpLine("GET https://api.weixin.qq.com/sns/userinfo?access_token={accessToken}&openid={openid}&lang=zh_CN")
    HttpResponse<WechatUser> getUser(@HttpParam("accessToken") String accessToken,
                                     @HttpParam("openid") String openid);
}
