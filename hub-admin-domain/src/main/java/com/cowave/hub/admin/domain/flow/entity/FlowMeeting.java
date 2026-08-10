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
package com.cowave.hub.admin.domain.flow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class FlowMeeting {

    /**
     * 待签到
     */
    public static final Integer PROCESS_STATUS_UNSIGN = 1;

    /**
     * 会议中
     */
    public static final Integer PROCESS_STATUS_SIGN = 2;

    /**
     * 已结束
     */
    public static final Integer PROCESS_STATUS_FINISHED = 3;

    /**
     * id
     */
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 会议主题
     */
    private String meetingTopic;

    /**
     * 会议室
     */
    private String meetingRoom;

    /**
     * 参与人员
     */
    private List<String> members;

    /**
     * 会议纪要
     */
    private String content;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 发起人
     */
    private String applyUser;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;

    /**
     * 流程id
     */
    private String processId;

    /**
     * 流程状态
     */
    private Integer processStatus;
}
