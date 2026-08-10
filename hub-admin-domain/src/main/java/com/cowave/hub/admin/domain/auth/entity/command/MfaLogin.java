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
package com.cowave.hub.admin.domain.auth.entity.command;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class MfaLogin {

    /**
     * MFA令牌
     */
    @NotBlank(message = "{admin.mfa.token.null}")
    private String mfaToken;

    /**
     * MFA口令
     */
    @NotBlank(message = "{admin.mfa.code.null}")
    private String mfaCode;
}
