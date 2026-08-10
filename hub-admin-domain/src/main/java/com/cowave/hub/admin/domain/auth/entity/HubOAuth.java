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
package com.cowave.hub.admin.domain.auth.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cowave.zoo.framework.access.security.AccessInfoSetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Getter
@Setter
@TableName("hub_oauth")
public class HubOAuth implements AccessInfoSetter {

    /**
     * id
     */
    @TableId
    private String tenantId;

    /**
     * 状态
     */
    private int status;

    /**
     * 用户角色
     */
    private String roleCode;

    /**
     * 服务类型
     */
    private String serverType;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 应用secret
     */
    private String appSecret;

    /**
     * 授权服务url
     */
    private String authUrl;

    /**
     * 应用回调地址
     */
    private String redirectUrl;

    /**
     * 授权方式
     */
    private String grantType = "authorization_code";

    /**
     * 授权范围
     */
    private String authScope = "read_user";

    /**
     * Token响应类型
     */
    private String responseType = "code";

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public String gitlabAuthorizeUrl() {
        return authUrl + "/oauth/authorize?client_id=" + appId
                + "&redirect_uri=" + redirectUrl + "&response_type=" + responseType;
    }
}
