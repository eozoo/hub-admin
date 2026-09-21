package com.cowave.hub.admin.infra.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.bo.BilibiliResponse;
import com.cowave.hub.admin.domain.auth.entity.bo.BilibiliToken;
import com.cowave.hub.admin.domain.auth.entity.bo.BilibiliUser;
import com.cowave.hub.admin.domain.auth.remote.BilibiliRemote;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.http.client.response.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.zoo.http.client.constants.HttpCode.INTERNAL_SERVER_ERROR;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class BilibiliRemoteImpl implements BilibiliRemote {

    private final BilibiliRemoteClient bilibiliRemoteClient;

    @Override
    public BilibiliUser getUser(SysAuthProvider provider, String code) {
        // 授权码兑换令牌
        HttpResponse<BilibiliResponse<BilibiliToken>> tokenResponse = bilibiliRemoteClient.getToken(
                provider.getClientId(), provider.getClientSecret(), code);
        HttpAsserts.isTrue(tokenResponse.isSuccess(), INTERNAL_SERVER_ERROR, "{admin.auth.bilibili.remote.failed}");
        BilibiliResponse<BilibiliToken> tokenResult = tokenResponse.getBody();
        HttpAsserts.isTrue(tokenResult != null && Integer.valueOf(0).equals(tokenResult.getCode())
                        && tokenResult.getData() != null && StringUtils.isNotBlank(tokenResult.getData().getAccessToken()),
                BAD_REQUEST, "{admin.auth.bilibili.token.invalid}");
        // 令牌兑换用户信息
        String timestamp = Long.toString(Instant.now().getEpochSecond());
        String nonce = UUID.randomUUID().toString();
        String signature = sign(provider.getClientId(), provider.getClientSecret(), nonce, timestamp);
        HttpResponse<BilibiliResponse<BilibiliUser>> userResponse = bilibiliRemoteClient.getUser(
                tokenResult.getData().getAccessToken(), provider.getClientId(), signature, nonce, timestamp);
        HttpAsserts.isTrue(userResponse.isSuccess(), INTERNAL_SERVER_ERROR, "{admin.auth.bilibili.remote.failed}");
        BilibiliResponse<BilibiliUser> userResult = userResponse.getBody();
        HttpAsserts.isTrue(userResult != null && Integer.valueOf(0).equals(userResult.getCode())
                        && userResult.getData() != null && StringUtils.isNotBlank(userResult.getData().getOpenid()),
                BAD_REQUEST, "{admin.auth.bilibili.user.invalid}");
        return userResult.getData();
    }

    static String sign(String clientId, String secret, String nonce, String timestamp) {
        String content = "x-bili-accesskeyid:" + clientId
                + "\nx-bili-content-md5:d41d8cd98f00b204e9800998ecf8427e"
                + "\nx-bili-signature-method:HMAC-SHA256"
                + "\nx-bili-signature-nonce:" + nonce
                + "\nx-bili-signature-version:2.0"
                + "\nx-bili-timestamp:" + timestamp;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal(content.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Unable to calculate Bilibili signature", exception);
        }
    }
}
