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
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 请假申请
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class FlowLeave {

    /**
     * 请假申请业务id，同时作为流程业务键
     */
    @TableId(type = IdType.INPUT)
    private String leaveId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 请假类型
     */
    private Integer leaveType;

    /**
     * 请假原因
     */
    private String reason;

    /**
     * 请假开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    /**
     * 请假结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 请假时长，由业务日历计算
     */
    private BigDecimal leaveDuration;

    /**
     * 时长单位，如day、hour
     */
    private String durationUnit;

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
     * 审批结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;

    /**
     * 撤销或终止原因
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
