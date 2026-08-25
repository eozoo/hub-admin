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
package com.cowave.hub.admin.client.dto;

import lombok.Data;

/**
 * 三方授权入口
 *
 * @author shanhuiming
 */
@Data
public class OAuthEntryDto {

    private String oauthProvider;

    private String oauthType;

    private String oauthName;

    private String oauthIcon;

    private String oauthTip;

    private String linkUrl;

    private String authorizeUrl;

    private String clientId;
}
