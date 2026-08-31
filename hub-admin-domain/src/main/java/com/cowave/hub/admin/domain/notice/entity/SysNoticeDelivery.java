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
package com.cowave.hub.admin.domain.notice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cowave.zoo.framework.support.mybatis.handler.JsonObjectHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

/**
 * 通知渠道投递表
 *
 * @author shanhuiming
 */
@Getter
@Setter
@TableName(autoResultMap = true)
public class SysNoticeDelivery {

    /**
     * 投递记录id
     */
    @TableId(type = IdType.AUTO)
    private Long deliveryId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 通知id
     */
    private Long noticeId;

    /**
     * 实际接收记录id
     */
    private Long receiverId;

    /**
     * 渠道id
     */
    private Long channelId;

    /**
     * 渠道类型快照
     */
    private Integer channelType;

    /**
     * 投递状态(0待发送 1发送中 2成功 3失败 4重试中 5放弃)
     */
    private Integer deliveryStatus;

    /**
     * 已重试次数
     */
    private Integer retryCount;

    /**
     * 下次重试时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date nextRetryTime;

    /**
     * 最近一次发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date sendTime;

    /**
     * 发送成功时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date successTime;

    /**
     * 失败错误码
     */
    private String errorCode;

    /**
     * 失败原因
     */
    private String errorMessage;

    /**
     * 渠道响应(JSON)
     */
    @TableField(typeHandler = JsonObjectHandler.class)
    private Map<String, Object> response;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
