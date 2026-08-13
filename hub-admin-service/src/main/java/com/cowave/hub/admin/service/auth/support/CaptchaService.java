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
import com.cowave.hub.admin.domain.sys.repository.facade.SysConfigRepositoryFacade;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.http.client.asserts.I18Messages;
import com.cowave.zoo.framework.helper.redis.RedisHelper;
import com.cowave.hub.admin.domain.auth.entity.vo.CaptchaVo;
import com.cowave.hub.admin.domain.auth.entity.SysOAuth;
import com.cowave.hub.admin.domain.auth.repository.facade.SysOAuthRepositoryFacade;
import com.google.code.kaptcha.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.FastByteArrayOutputStream;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.hub.admin.domain.AdminRedisKeys.AUTH_CAPTCHA;

/**
 *
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
    private final SysOAuthRepositoryFacade oauthRepositoryFacade;
    private final SysConfigRepositoryFacade configRepositoryFacade;

    public CaptchaVo captcha(String tenantId) throws IOException {
        SysOAuth gitlabServer = oauthRepositoryFacade.queryByServerType(tenantId, "gitlab");
        String oauthUrl = gitlabServer.gitlabAuthorizeUrl();
        boolean registerOnOff = configRepositoryFacade.queryConfigValue(tenantId, "hub.registerOnOff");
        boolean captchaOnOff = configRepositoryFacade.queryConfigValue(tenantId, "hub.captchaOnOff");
        if (!captchaOnOff) {
            return new CaptchaVo(registerOnOff, oauthUrl);
        }

        String uuid = IdUtil.randomUUID();
        String capStr, code = null;
        BufferedImage image = null;
        // 生成验证码
        String captchaType = configRepositoryFacade.queryConfigValue(tenantId, "hub.captchaType");
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
        return new CaptchaVo(uuid, encode(os.toByteArray()), true, registerOnOff, oauthUrl);
    }

    public void validCaptcha(String tenantId, String captchaId, String captcha){
        boolean captchaOnOff = configRepositoryFacade.queryConfigValue(tenantId, "hub.captchaOnOff");
        if(captchaOnOff){
            String stub = redisHelper.getValue(AUTH_CAPTCHA.formatted(captchaId));
            HttpAsserts.notNull(stub, BAD_REQUEST, "{admin.captcha.expired}");
            HttpAsserts.equals(stub, captcha, BAD_REQUEST, "{admin.captcha.failed}");
        }
    }

    public void captchaEmail(String email) {
        int code = (random.nextInt(9) + 1) * 100000 + random.nextInt(100000);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("cowaveAdmin@163.com");
        mailMessage.setTo(email);
        mailMessage.setSubject(I18Messages.msg("admin.captcha.title"));
        mailMessage.setText(I18Messages.msg("admin.captcha.msg", String.valueOf(code), CAPTCHA_EXPIRATION));
        mailSender.send(mailMessage);
        redisHelper.putExpire(AUTH_CAPTCHA.formatted(code), email, CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
    }

    public void validEmail(String email, String captcha){
        String stub = redisHelper.getValue(AUTH_CAPTCHA.formatted(captcha));
        HttpAsserts.notNull(stub, BAD_REQUEST, "{admin.captcha.expired}");
        HttpAsserts.equals(stub, email, BAD_REQUEST, "{admin.register.failed}");
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
