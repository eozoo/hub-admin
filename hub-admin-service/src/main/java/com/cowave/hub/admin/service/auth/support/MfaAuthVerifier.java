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

import org.apache.commons.codec.binary.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author shanhuiming
 */
public class MfaAuthVerifier {

    private static final int VALID_TFA_WINDOW_MILLIS = 60_000;

    public static String generateKey() {
        return TimeBasedOneTimePasswordUtil.generateBase32Secret();
    }

    public static String generateAuthUrl(String tenantId, String userAccount, final String tfaKey) {
        return TimeBasedOneTimePasswordUtil.generateOtpAuthUrl(tenantId + ":" + userAccount, tfaKey);
    }

    public static boolean validateCode(String tfaKey, String tfaCode) {
        int validCode;
        try {
            validCode = Integer.parseInt(tfaCode.trim());
        } catch (NumberFormatException e) {
            return false;
        }
        return TimeBasedOneTimePasswordUtil.validateCurrentNumber(tfaKey, validCode, VALID_TFA_WINDOW_MILLIS);
    }

    public static String generateCode(String tfaKey) {
        return TimeBasedOneTimePasswordUtil.generateCurrentNumberString(tfaKey);
    }

    private static class TimeBasedOneTimePasswordUtil {
        private static final int TIME_STEP_SECONDS = 30;
        private static final int NUM_DIGITS_OUTPUT = 6;
        private static final String HMAC_ALGORITHM = "HmacSHA1";

        public static String generateBase32Secret() {
            byte[] buffer = new byte[10]; // 80 bits is standard for TOTP
            new SecureRandom().nextBytes(buffer);
            Base32 codec = new Base32();
            return codec.encodeToString(buffer).replace("=", ""); // remove padding for compatibility
        }

        public static boolean validateCurrentNumber(String base32Secret, int authNumber, int windowMillis) {
            long currentTimeMillis = System.currentTimeMillis();
            long timeStepMillis = TimeUnit.SECONDS.toMillis(TIME_STEP_SECONDS);
            long fromTime = currentTimeMillis - windowMillis;
            long toTime = currentTimeMillis + windowMillis;

            for (long time = fromTime; time <= toTime; time += timeStepMillis) {
                int generated = generateNumber(base32Secret, time);
                if (generated == authNumber) {
                    return true;
                }
            }
            return false;
        }

        public static String generateCurrentNumberString(String base32Secret) {
            int code = generateNumber(base32Secret, System.currentTimeMillis());
            return String.format("%0" + NUM_DIGITS_OUTPUT + "d", code);
        }

        public static int generateNumber(String base32Secret, long timeMillis) {
            Base32 codec = new Base32();
            byte[] key = codec.decode(base32Secret);
            long timeWindow = timeMillis / 1000 / TIME_STEP_SECONDS;

            byte[] data = new byte[8];
            for (int i = 7; i >= 0; i--) {
                data[i] = (byte) (timeWindow & 0xFF);
                timeWindow >>= 8;
            }

            try {
                Mac mac = Mac.getInstance(HMAC_ALGORITHM);
                mac.init(new SecretKeySpec(key, HMAC_ALGORITHM));
                byte[] hash = mac.doFinal(data);

                int offset = hash[hash.length - 1] & 0xF;
                long truncatedHash = 0;
                for (int i = 0; i < 4; ++i) {
                    truncatedHash <<= 8;
                    truncatedHash |= (hash[offset + i] & 0xFF);
                }

                truncatedHash &= 0x7FFFFFFF;
                truncatedHash %= 1_000_000;

                return (int) truncatedHash;
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate TOTP", e);
            }
        }

        public static String generateOtpAuthUrl(String keyId, String secret) {
            return "otpauth://totp/" + keyId + "?secret=" + secret + "&issuer=hub.cowave.com";
        }
    }
}
