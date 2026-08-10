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
package com.cowave.hub.admin.domain.sys.entity.command;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class AlarmHandles {

    /**
     * 告警id
     */
    @NotNull(message = "{admin.alarm.id.null}")
    private Long id;

    /**
     * 告警状态
     */
    @NotNull(message = "{admin.alarm.status.null}")
    private Integer alarmStatus;

    /**
     * 处理方式
     */
    private int handleType = 1;

    /**
     * 处理意见
     */
    private String handleMsg;

    /**
     * 处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date handleTime = new Date();
}
