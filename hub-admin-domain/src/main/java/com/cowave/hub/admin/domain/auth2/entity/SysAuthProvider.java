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
package com.cowave.hub.admin.domain.auth2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cowave.zoo.framework.support.mybatis.handler.JsonObjectHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

/**
 * 租户认证提供方
 *
 * @author shanhuiming
 */
@Getter
@Setter
@TableName(autoResultMap = true)
public class SysAuthProvider {

    /**
     * 认证提供方id
     */
    @TableId(type = IdType.AUTO)
    private Integer providerId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 租户内唯一提供方编码，如cowave、gitlab、wechat
     */
    private String providerCode;

    /**
     * 提供方类型，如oauth、oidc、saml、link
     */
    private String providerType;

    /**
     * 登录入口名称
     */
    private String providerName;

    /**
     * 登录入口图标
     */
    private String providerIcon;

    /**
     * 登录入口提示
     */
    private String providerTip;

    /**
     * 登录入口排序
     */
    private Integer providerSort;

    /**
     * 直接跳转地址，link类型使用
     */
    private String linkUrl;

    /**
     * OAuth客户端id
     */
    private String clientId;

    /**
     * OAuth客户端密钥
     */
    private String clientSecret;

    /**
     * OAuth授权服务地址
     */
    private String authUrl;

    /**
     * OAuth应用回调地址
     */
    private String redirectUrl;

    /**
     * OAuth授权类型
     */
    private String grantType;

    /**
     * OAuth响应类型
     */
    private String responseType;

    /**
     * OAuth授权范围
     */
    private String authScope;

    /**
     * 不同认证类型的扩展配置
     */
    @TableField(typeHandler = JsonObjectHandler.class)
    private Map<String, Object> providerConfig;

    /**
     * 状态 0关闭 1开启
     */
    private Integer status;

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
}
