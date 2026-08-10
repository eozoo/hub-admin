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

import com.cowave.zoo.framework.access.security.AccessTokenInfo;
import com.cowave.zoo.framework.access.security.RefreshTokenInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author shanhuiming
 */
@NoArgsConstructor
@Getter
@Setter
public class OnlineAccess {

    /**
     * 授权id
     */
    private String grantId;

    /**
     * 刷新id
     */
    private String refreshId;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 是否注销
     */
    private int revoked;

    /**
     * 授权类型
     */
    private String grantType;

    /**
     * 授权ip
     */
    private String grantIp;

    /**
     * 授权App
     */
    private String grantApp;

    /**
     * 授权时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date grantTime;

    public OnlineAccess(AccessTokenInfo accessToken) {
        this.grantId = accessToken.getAccessId();
        this.refreshId = accessToken.getRefreshId();
        this.userAccount = accessToken.getUserAccount();
        this.grantType = accessToken.getAccessType();
        this.grantIp = accessToken.getAccessIp();
        this.grantTime = accessToken.getAccessTime();
        this.revoked = accessToken.getRevoked();
    }

    public OnlineAccess(RefreshTokenInfo oauthToken) {
        this.grantId = oauthToken.getRefreshId();
        this.userAccount = oauthToken.getUserAccount();
        this.grantType = oauthToken.getAuthType();
        this.grantIp = oauthToken.getLoginIp();
        this.grantTime = oauthToken.getLoginTime();
        this.grantApp = oauthToken.getOauthName();
        this.appId = oauthToken.getOauthId();
    }
}
