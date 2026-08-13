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
package com.cowave.hub.admin.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class SysAlarm {

    /**
	 * 告警id
	 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 告警校验码
     */
    private String alarmCode;

    /**
     * 告警类型
     */
    private Long alarmType;

    /**
     * 告警等级
     */
    private Integer alarmLevel;

    /**
     * 告警源id
     */
    private Long sourceId;

    /**
     * 告警源类型
     */
    private Long sourceType;

    /**
     * 告警源名称
     */
    private String sourceName;

    /**
     * 告警状态
     */
    private Integer alarmStatus;

    /**
     * 告警次数
     */
    private Integer alarmTimes;

    /**
     * 首次告警时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date firstTime;

    /**
     * 最近告警时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastTime;

    /**
     * 告警描述
     */
    private String alarmDesc;

    /**
     * 告警详情
     */
    private Object alarmContent;

    /**
     * 处理人
     */
    private Long resolveUser;

    /**
	 * 处理意见
	 */
	private String resolveMsg;

    /**
     * 处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date resolveTime;

    /**
     * 处理方式
     */
    private Integer resolveType;
}
