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
package com.cowave.hub.admin.domain.flow2.entity;

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
 * 会议预约
 *
 * @author shanhuiming
 */
@Getter
@Setter
@TableName(autoResultMap = true)
public class FlowMeeting {

    /**
     * 会议预约业务id，同时作为流程业务键
     */
    @TableId(type = IdType.INPUT)
    private String meetingId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 会议主题
     */
    private String meetingTopic;

    /**
     * 会议室id
     */
    private Integer meetingRoomId;

    /**
     * 会议室名称快照
     */
    private String meetingRoomName;

    /**
     * 参会用户id数组
     */
    @TableField(typeHandler = ArrayListHandler.class)
    private List<Integer> memberUserIds;

    /**
     * 会议议程
     */
    private String meetingAgenda;

    /**
     * 会议纪要
     */
    private String meetingMinutes;

    /**
     * 线上会议地址
     */
    private String onlineUrl;

    /**
     * 会议开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    /**
     * 会议结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 申请人用户id
     */
    private Integer applyUserId;

    /**
     * 申请人名称快照
     */
    private String applyUserName;

    /**
     * 申请时所属部门id
     */
    private Integer applyDeptId;

    /**
     * 申请时所属部门名称快照
     */
    private String applyDeptName;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 审批状态：0草稿 1审批中 2已通过 3已驳回 4已撤销 5已终止
     */
    private Integer processStatus;

    /**
     * 会议状态：0待审批 1待开始 2进行中 3已结束 4已取消
     */
    private Integer meetingStatus;

    /**
     * 审批结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;

    /**
     * 撤销、终止或取消原因
     */
    private String cancelReason;

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
