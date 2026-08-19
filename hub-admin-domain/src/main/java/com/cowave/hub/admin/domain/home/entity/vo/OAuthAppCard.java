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
package com.cowave.hub.admin.domain.home.entity.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class OAuthAppCard {

    /**
     * id
     */
    private Integer id;

    /**
     * 应用卡片名称
     */
    private String cardName;

    /**
     * 应用卡片图标
     */
    private String cardIcon;

    /**
     * 应用名称
     */
    private String clientName;

    /**
     * 应用id
     */
    private String clientId;

    /**
     * 回调地址
     */
    private String redirectUrl;
}
