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

import com.cowave.zoo.http.client.asserts.HttpHintException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

import static com.cowave.zoo.framework.access.security.BearerTokenDelegate.CLAIM_TENANT_ID;
import static com.cowave.zoo.framework.access.security.BearerTokenDelegate.CLAIM_USER_ACCOUNT;
import static com.cowave.zoo.http.client.constants.HttpCode.UNAUTHORIZED;

/**
 * @author shanhuiming
 */
@Data
@Configuration
public class MfaConfiguration {

    @Value("${mfa.expire:300}")
    private int mfaExpire;

    @Value("${mfa.secret:mfa@cowave.com}")
    private String mfaSecret;

    public String buildMfaToken(String tenantId, String userAccount) {
        return Jwts.builder()
                .claim(CLAIM_TENANT_ID, tenantId)
                .claim(CLAIM_USER_ACCOUNT, userAccount)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, mfaSecret)
                .setExpiration(new Date(System.currentTimeMillis() + mfaExpire * 1000L))
                .compact();
    }

    public Claims parseMfaToken(String mfaToken) {
        try {
            return Jwts.parser().setSigningKey(mfaSecret).parseClaimsJws(mfaToken).getBody();
        } catch (ExpiredJwtException e) {
            throw new HttpHintException(UNAUTHORIZED, "{frame.auth.access.expire}");
        } catch (Exception e) {
            throw new HttpHintException(UNAUTHORIZED, "{frame.auth.access.invalid}");
        }
    }
}
