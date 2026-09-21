package com.cowave.hub.admin.infra.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.bo.WechatToken;
import com.cowave.hub.admin.domain.auth.entity.bo.WechatUser;
import com.cowave.hub.admin.domain.auth.remote.WechatRemote;
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
public class WechatRemoteImpl implements WechatRemote {

    private final WechatRemoteClient wechatRemoteClient;

    @Override
    public WechatUser getUser(SysAuthProvider provider, String code) {
        // 授权码兑换令牌
        HttpResponse<WechatToken> tokenResponse = wechatRemoteClient.getToken(
                provider.getClientId(), provider.getClientSecret(), code);
        HttpAsserts.isTrue(tokenResponse.isSuccess(), INTERNAL_SERVER_ERROR, tokenResponse.getMessage());
        // 令牌兑换用户信息
        WechatToken token = tokenResponse.getBody();
        HttpAsserts.notNull(token, INTERNAL_SERVER_ERROR, "WeChat token response is empty");
        HttpAsserts.notNull(token.getAccessToken(), INTERNAL_SERVER_ERROR, "WeChat access token is empty");
        HttpResponse<WechatUser> userResponse = wechatRemoteClient.getUser(token.getAccessToken(), token.getOpenid());
        HttpAsserts.isTrue(userResponse.isSuccess(), INTERNAL_SERVER_ERROR, userResponse.getMessage());
        return userResponse.getBody();
    }
}
