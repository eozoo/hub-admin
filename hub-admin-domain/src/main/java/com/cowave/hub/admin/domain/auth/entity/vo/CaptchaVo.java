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
package com.cowave.hub.admin.domain.auth.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author shanhuiming
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CaptchaVo {

    /**
     * 验证码标识
     */
    private String uuid;

    /**
     * 验证码图片
     */
    private String img;

    /**
     * 是否开启验证码
     */
    private boolean captchaOnOff;

    /**
     * 是否开放注册
     */
    private boolean registerOnOff;

    /**
     * OAuth 提供方授权地址
     */
    private Map<String, String> oauthUrls;

    public CaptchaVo(boolean registerOnOff, Map<String, String> oauthUrls){
        this.registerOnOff = registerOnOff;
        this.oauthUrls = oauthUrls;
    }
}
