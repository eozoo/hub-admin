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
package com.cowave.hub.admin.domain.sys.entity.pto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.cowave.zoo.framework.support.excel.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 告警
 *
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Getter
@Setter
public class AlarmPto {

	/**
	 * 告警id
	 */
	@ExcelProperty("告警id")
    private Long id;

    /**
     * 告警校验码
     */
    private String alarmCode;

    /**
     * 告警源id
     */
    private Long sourceId;

    /**
     * 告警源名称
     */
    private String sourceName;

    /**
     * 告警源类型
     */
    private Long sourceType;

    /**
     * 告警类型
     */
    private Long alarmType;

    /**
     * 类型名称
     */
    @ExcelProperty("告警类型")
    private String typeName;

    /**
	 * 详情路径
	 */
	private String viewPath;

    /**
     * 告警等级
     */
    private Integer alarmLevel;

	/**
     * 告警次数
     */
	@ExcelProperty("告警次数")
    private Integer alarmTimes;

    /**
     * 告警描述
     */
	@ExcelProperty("告警描述")
    private String alarmDesc;

    /**
     * 详细内容
     */
	@ExcelProperty(value = "告警详情", converter = JsonConverter.class)
    private Object alarmContent;

    /**
     * 首次告警时间
     */
	@ExcelProperty(value = "首次告警时间", converter = DateConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date firstTime;

    /**
     * 最近告警时间
     */
	@ExcelProperty(value = "最近告警时间", converter = DateConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastTime;

	/**
     * 告警状态
     */
    private Integer alarmStatus;

    /**
     * 处理人
     */
    private Long resolveUser;

    /**
     * 处理时间
     */
	@ExcelProperty(value = "处理时间", converter = DateConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date resolveTime;

    /**
     * 处理方式
     */
    private Integer resolveType;

	/**
	 * 处理意见
	 */
	@ExcelProperty(value = "处理意见")
	private String resolveMsg;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    public void recordAlarm(String alarmCode, String sourceName, String alarmType, String desc, Object content){
        this.firstTime = new Date();
        this.lastTime = new Date();
        this.setSourceName(sourceName);
        this.setAlarmType(1L);
        this.setAlarmCode(alarmCode);
        this.setAlarmDesc(desc);
        this.setAlarmContent(content);
    }
}
