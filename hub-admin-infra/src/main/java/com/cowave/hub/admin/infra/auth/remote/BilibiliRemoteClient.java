package com.cowave.hub.admin.infra.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.bo.BilibiliResponse;
import com.cowave.hub.admin.domain.auth.entity.bo.BilibiliToken;
import com.cowave.hub.admin.domain.auth.entity.bo.BilibiliUser;
import com.cowave.zoo.http.client.annotation.HttpClient;
import com.cowave.zoo.http.client.annotation.HttpHeaders;
import com.cowave.zoo.http.client.annotation.HttpLine;
import com.cowave.zoo.http.client.annotation.HttpParam;
import com.cowave.zoo.http.client.response.HttpResponse;

/**
 * @author shanhuiming
 */
@HttpClient
public interface BilibiliRemoteClient {

    /**
     * 授权码兑换令牌
     */
    @HttpHeaders("Content-Type: application/x-www-form-urlencoded")
    @HttpLine("POST https://api.bilibili.com/x/account-oauth2/v1/token?client_id={clientId}&client_secret={clientSecret}&grant_type=authorization_code&code={code}")
    HttpResponse<BilibiliResponse<BilibiliToken>> getToken(@HttpParam("clientId") String clientId,
            @HttpParam("clientSecret") String clientSecret, @HttpParam("code") String code);

    /**
     * 查询授权用户信息
     */
    @HttpHeaders({"Accept: application/json", "Content-Type: application/json",
            "Access-Token: {accessToken}", "Authorization: {signature}",
            "X-Bili-Accesskeyid: {clientId}", "X-Bili-Content-Md5: d41d8cd98f00b204e9800998ecf8427e",
            "X-Bili-Signature-Method: HMAC-SHA256", "X-Bili-Signature-Nonce: {nonce}",
            "X-Bili-Signature-Version: 2.0", "X-Bili-Timestamp: {timestamp}"})
    @HttpLine("GET https://member.bilibili.com/arcopen/fn/user/account/info")
    HttpResponse<BilibiliResponse<BilibiliUser>> getUser(@HttpParam("accessToken") String accessToken,
            @HttpParam("clientId") String clientId, @HttpParam("signature") String signature,
            @HttpParam("nonce") String nonce, @HttpParam("timestamp") String timestamp);
}
