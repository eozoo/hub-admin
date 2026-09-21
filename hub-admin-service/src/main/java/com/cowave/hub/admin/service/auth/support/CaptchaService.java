/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.service.auth.support;

import cn.hutool.core.util.IdUtil;
import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.repository.facade.SysAuthRepositoryFacade;
import com.cowave.hub.admin.domain.sys2.repository.facade.SysConfigRepositoryFacade;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.http.client.asserts.I18Messages;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.helper.redis.RedisHelper;
import com.cowave.hub.admin.domain.auth.entity.vo.CaptchaVo;
import com.google.code.kaptcha.Producer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import jakarta.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_CAPTCHA;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_EMAIL_CAPTCHA;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_EMAIL_COOLDOWN;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_EMAIL_SENDS;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_OAUTH_STATE;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class CaptchaService {
    private static final int     LOOKUPLENGTH         = 64;
    private static final int     TWENTYFOURBITGROUP   = 24;
    private static final int     EIGHTBIT             = 8;
    private static final int     SIXTEENBIT           = 16;
    private static final int     SIGN                 = -128;
    private static final char    PAD                  = '=';
    private static final char[] LOOKUP_BASE64_ALPHABET = new char[LOOKUPLENGTH];
    private static final Integer CAPTCHA_EXPIRATION = 3;

    static {
        for (int i = 0; i <= 25; i++) {
            LOOKUP_BASE64_ALPHABET[i] = (char) ('A' + i);
        }
        for (int i = 26, j = 0; i <= 51; i++, j++) {
            LOOKUP_BASE64_ALPHABET[i] = (char) ('a' + j);
        }
        for (int i = 52, j = 0; i <= 61; i++, j++) {
            LOOKUP_BASE64_ALPHABET[i] = (char) ('0' + j);
        }
        LOOKUP_BASE64_ALPHABET[62] = '+';
        LOOKUP_BASE64_ALPHABET[63] = '/';
    }

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;

    private final RedisHelper redisHelper;
    private final JavaMailSender mailSender;
    private final SecureRandom random = new SecureRandom();
    private final SysAuthRepositoryFacade authRepositoryFacade;
    private final SysConfigRepositoryFacade configRepositoryFacade;

    public CaptchaVo captcha() throws IOException {
        Map<String, String> oauthUrls = queryOauthUrls();
        boolean registerOnOff = configRepositoryFacade.queryConfigValue("hub.registerOnOff");
        boolean captchaOnOff = configRepositoryFacade.queryConfigValue("hub.captchaOnOff");
        if (!captchaOnOff) {
            return new CaptchaVo(registerOnOff, oauthUrls);
        }

        String uuid = IdUtil.randomUUID();
        String capStr, code = null;
        BufferedImage image = null;
        // 生成验证码
        String captchaType = configRepositoryFacade.queryConfigValue("hub.captchaType");
        if ("math".equals(captchaType)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            image = captchaProducerMath.createImage(capStr);
        } else if ("char".equals(captchaType)) {
            capStr = code = captchaProducer.createText();
            image = captchaProducer.createImage(capStr);
        }

        redisHelper.putExpire(AUTH_CAPTCHA.formatted(uuid), code, CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        assert image != null;
        ImageIO.write(image, "jpg", os);
        return new CaptchaVo(uuid, encode(os.toByteArray()), true, registerOnOff, oauthUrls);
    }

    public void validCaptcha(String captchaId, String captcha){
        boolean captchaOnOff = configRepositoryFacade.queryConfigValue("hub.captchaOnOff");
        if(captchaOnOff){
            String stub = redisHelper.getValueAndDelete(AUTH_CAPTCHA.formatted(captchaId));
            HttpAsserts.notNull(stub, BAD_REQUEST, "{admin.captcha.expired}");
            HttpAsserts.equals(stub, captcha, BAD_REQUEST, "{admin.captcha.failed}");
        }
    }

    private Map<String, String> queryOauthUrls() {
        Map<String, String> oauthUrls = new LinkedHashMap<>();
        for (SysAuthProvider provider : authRepositoryFacade.queryEnabledOauthProviders()) {
            if (StringUtils.isAnyBlank(provider.getAuthUrl(), provider.getClientId(),
                    provider.getRedirectUrl(), provider.getResponseType())) {
                continue;
            }
            String state = UUID.randomUUID().toString();
            String authUrl = provider.buildAuthorizeUrl(state);
            redisHelper.putExpire(AUTH_OAUTH_STATE.formatted(state), provider.getProviderId(), 10, TimeUnit.MINUTES);
            oauthUrls.put(provider.getProviderCode().getVal(), authUrl);
        }
        return oauthUrls;
    }

    public void captchaEmail(String email) {
        // 邮箱验证码IP限流
        String sendsKey = AUTH_EMAIL_SENDS.formatted(Access.accessIp());
        Long sends = redisHelper.incrementValue(sendsKey, 1);
        if (sends == 1) {
            redisHelper.expire(sendsKey, 10, TimeUnit.MINUTES);
        }
        HttpAsserts.isTrue(sends <= 10, BAD_REQUEST, "{admin.captcha.email.rate.limit}");
        // 邮箱验证码频率限制
        String cooldownKey = AUTH_EMAIL_COOLDOWN.formatted(email);
        Boolean putted = redisHelper.putExpireIfAbsent(cooldownKey, "-", 60, TimeUnit.SECONDS);
        HttpAsserts.isTrue(putted, BAD_REQUEST, "{admin.captcha.email.cooldown}");
        // 发送验证码
        int code = (random.nextInt(9) + 1) * 100000 + random.nextInt(100000);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("hubadmin@163.com");
        mailMessage.setTo(email);
        mailMessage.setSubject(I18Messages.msg("admin.captcha.title"));
        mailMessage.setText(I18Messages.msg("admin.captcha.msg", String.valueOf(code), CAPTCHA_EXPIRATION));
        try {
            mailSender.send(mailMessage);
            redisHelper.putExpire(AUTH_EMAIL_CAPTCHA.formatted(email), String.valueOf(code), CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        } catch (RuntimeException e) {
            // mail失败就不冷却了
            redisHelper.delete(cooldownKey);
            throw e;
        }
    }

    public void validEmail(String email, String captcha){
        String stub = redisHelper.getValueAndDelete(AUTH_EMAIL_CAPTCHA.formatted(email));
        HttpAsserts.notNull(stub, BAD_REQUEST, "{admin.captcha.expired}");
        HttpAsserts.equals(stub, captcha, BAD_REQUEST, "{admin.captcha.failed}");
    }

    private static String encode(byte[] binaryData) {
        if (binaryData == null) {
            return null;
        }

        int lengthDataBits = binaryData.length * EIGHTBIT;
        if (lengthDataBits == 0) {
            return "";
        }

        int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
        int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
        char[] encodedData = new char[numberQuartet * 4];

        byte k, l, b1, b2, b3;
        int encodedIndex = 0;
        int dataIndex = 0;

        for (int i = 0; i < numberTriplets; i++) {
            b1 = binaryData[dataIndex++];
            b2 = binaryData[dataIndex++];
            b3 = binaryData[dataIndex++];

            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
            byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);

            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[val1];
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[(val2 & 0xFF) | (k << 4)];
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[(l << 2) | (val3 & 0xFF)];
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[b3 & 0x3f];
        }

        // form integral number of 6-bit groups
        if (fewerThan24bits == EIGHTBIT) {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[val1];
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[k << 4];
            encodedData[encodedIndex++] = PAD;
            encodedData[encodedIndex++] = PAD;
        } else if (fewerThan24bits == SIXTEENBIT) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[val1];
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[(val2 & 0xFF) | (k << 4)];
            encodedData[encodedIndex++] = LOOKUP_BASE64_ALPHABET[l << 2];
            encodedData[encodedIndex++] = PAD;
        }
        return new String(encodedData);
    }
}
