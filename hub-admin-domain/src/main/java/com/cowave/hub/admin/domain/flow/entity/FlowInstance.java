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

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.runtime.ProcessInstanceQuery;

import java.util.Date;
import java.util.List;

/**
 * 流程实例
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class FlowInstance {

    /**
     * 流程标识
     */
    private String processKey;

    /**
     * 流程实例id
     */
    private String instanceId;

    /**
     * 流程名称
     */
    private String instanceName;

    /**
     * 当前任务
     */
    private String taskName;

    /**
     * 任务办理人
     */
    private String assignee;

    /**
     * 任务办理人
     */
    private String assigneeName;

    /**
     * 发起人
     */
    private String startUser;

    /**
     * 发起人
     */
    private String startUserName;

    /**
     * 业务标识
     */
    private String businessKey;

    /**
     * 执行id
     */
    private String executionId;

    /**
     * 父执行id
     */
    private String parentExecutionId;

    /**
     * 是否挂起
     */
    private boolean suspended;

    /**
     * 是否结束
     */
    private boolean ended;

    /**
     * 是否存活
     */
    private boolean active;

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
     * 当前任务列表
     */
    private List<FlowTask> taskList;

    public void fillInstanceQuery(ProcessInstanceQuery instanceQuery){
        if (StringUtils.isNotEmpty(processKey)) {
            instanceQuery.processDefinitionKey(processKey);
        }
        if(beginTime != null){
            instanceQuery.startedAfter(beginTime);
        }
        if(endTime != null){
            instanceQuery.startedBefore(endTime);
        }
    }

    public void fillHistoryQuery(HistoricProcessInstanceQuery historyQuery){
        if (StringUtils.isNotEmpty(processKey)) {
            historyQuery.processDefinitionKey(processKey);
        }
        if(beginTime != null){
            historyQuery.startedAfter(beginTime);
        }
        if(endTime != null){
            historyQuery.startedBefore(endTime);
        }
    }
}
