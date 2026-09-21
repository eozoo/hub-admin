package com.cowave.hub.admin.infra.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.bo.QqToken;
import com.cowave.hub.admin.domain.auth.entity.bo.QqUser;
import com.cowave.hub.admin.domain.auth.remote.QqRemote;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.http.client.response.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.zoo.http.client.constants.HttpCode.INTERNAL_SERVER_ERROR;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class QqRemoteImpl implements QqRemote {

    private final QqRemoteClient qqRemoteClient;

    @Override
    public QqUser getUser(SysAuthProvider provider, String code) {
        // 授权码兑换令牌
        HttpResponse<QqToken> tokenResponse = qqRemoteClient.getToken(
                provider.getClientId(), provider.getClientSecret(), code, provider.getRedirectUrl());
        HttpAsserts.isTrue(tokenResponse.isSuccess(), INTERNAL_SERVER_ERROR, "{admin.auth.qq.remote.failed}");
        // 令牌兑换用户信息
        QqToken token = tokenResponse.getBody();
        HttpAsserts.isTrue(token != null && token.getError() == null
                        && StringUtils.isNoneBlank(token.getAccessToken(), token.getOpenid()),
                BAD_REQUEST, "{admin.auth.qq.token.invalid}");
        HttpResponse<QqUser> userResponse = qqRemoteClient.getUser(
                token.getAccessToken(), provider.getClientId(), token.getOpenid());
        HttpAsserts.isTrue(userResponse.isSuccess(), INTERNAL_SERVER_ERROR, "{admin.auth.qq.remote.failed}");
        QqUser user = userResponse.getBody();
        HttpAsserts.isTrue(user != null && Integer.valueOf(0).equals(user.getRet()),
                BAD_REQUEST, "{admin.auth.qq.user.invalid}");
        // 用户标识头像
        user.setOpenid(token.getOpenid());
        user.setAvatarUrl(StringUtils.defaultIfBlank(user.getAvatarUrl(), user.getSmallAvatarUrl()));
        return user;
    }
}
