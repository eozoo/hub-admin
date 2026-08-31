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
package com.cowave.hub.admin.domain.sys2.entity;

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
 * 全局系统告警
 *
 * @author shanhuiming
 */
@Getter
@Setter
@TableName(autoResultMap = true)
public class SysAlarm {

    /**
     * 告警id
     */
    @TableId(type = IdType.AUTO)
    private Long alarmId;

    /**
     * 告警唯一编码或聚合指纹
     */
    private String alarmCode;

    /**
     * 告警类型id
     */
    private Long alarmTypeId;

    /**
     * 告警级别：1提示 2普通 3重要 4严重 5灾难
     */
    private Integer alarmLevel;

    /**
     * 告警来源类型
     */
    private String sourceType;

    /**
     * 告警来源业务标识
     */
    private String sourceId;

    /**
     * 告警来源名称快照
     */
    private String sourceName;

    /**
     * 告警状态：0未处理 1已确认 2已解决 3已关闭
     */
    private Integer alarmStatus;

    /**
     * 同一告警累计发生次数
     */
    private Integer alarmTimes;

    /**
     * 首次告警时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date firstTime;

    /**
     * 最后告警时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastTime;

    /**
     * 告警描述
     */
    private String alarmDesc;

    /**
     * 告警扩展内容
     */
    @TableField(typeHandler = JsonObjectHandler.class)
    private Map<String, Object> alarmContent;

    /**
     * 确认人用户id
     */
    private Integer acknowledgeUserId;

    /**
     * 确认说明
     */
    private String acknowledgeMsg;

    /**
     * 确认时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date acknowledgeTime;

    /**
     * 解决人用户id
     */
    private Integer resolveUserId;

    /**
     * 解决意见
     */
    private String resolveMsg;

    /**
     * 解决时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date resolveTime;

    /**
     * 解决方式：1手动 2自动
     */
    private Integer resolveType;
}
