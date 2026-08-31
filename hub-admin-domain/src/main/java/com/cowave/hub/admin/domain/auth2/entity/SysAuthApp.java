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
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cowave.zoo.framework.support.mybatis.handler.ArrayListHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 认证应用
 *
 * @author shanhuiming
 */
@Getter
@Setter
@TableName(value = "sys_auth_app", autoResultMap = true)
public class SysAuthApp {

    /**
     * 应用id
     */
    @TableId(type = IdType.AUTO)
    private Integer appId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用类型 oauth/link
     */
    private String appType;

    /**
     * 可见性 public/all/sys
     */
    private String appVisible;

    /**
     * 状态 1启用 2停用
     */
    private Integer appStatus;

    /**
     * 排序
     */
    private Integer appSort;

    /**
     * 卡片名称
     */
    private String cardName;

    /**
     * 卡片图标
     */
    private String cardIcon;

    /**
     * 跳转地址
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
     * 授权类型
     */
    @TableField(typeHandler = ArrayListHandler.class)
    private List<String> grantType;

    /**
     * 授权范围
     */
    @TableField(typeHandler = ArrayListHandler.class)
    private List<String> authScope;

    /**
     * 重定向地址
     */
    private String redirectUrl;

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
