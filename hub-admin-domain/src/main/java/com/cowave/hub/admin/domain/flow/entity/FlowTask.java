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
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;

import java.util.Date;

/**
 * 流程任务
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class FlowTask {

	/**
	 * 任务id
	 */
	private String taskId;

	/**
	 * 任务名称
	 */
	private String taskName;

	/**
	 * 任务类型
	 */
	private String taskType;

	/**
	 * 执行id
	 */
	private String executionId;

	/**
	 * 业务标识
	 */
	private String businessKey;

	/**
	 * 流程id
	 */
	private String processInstanceId;

	/**
	 * 流程key
	 */
	private String processKey;

	/**
	 * 流程名称
	 */
	private String processName;

	/**
	 * 发起人
	 */
	private String starter;

	/**
	 * 发起人
	 */
	private String starterName;

	/**
	 * 委托人
	 */
	private String assignee;

	/**
	 * 委托人
	 */
	private String assigneeName;

	/**
	 * 流程开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date startTime;

	/**
	 * 任务开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date beginTime;

	/**
	 * 任务截止时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date dueTime;

	/**
	 * 任务结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date endTime;

	/**
	 * 表单标识
	 */
	private String formKey;

	/**
	 * 备注
	 */
	private String comment;

	public void fillTaskQuery(TaskQuery taskQuery){
        if (StringUtils.isNotEmpty(taskName)) {
			taskQuery.taskName(taskName);
        }
        if (StringUtils.isNotEmpty(processName)) {
			taskQuery.processDefinitionName(processName);
        }
		if (StringUtils.isNotEmpty(processKey)) {
			taskQuery.processDefinitionKey(processKey);
		}
		if(beginTime != null){
			taskQuery.taskCreatedAfter(beginTime);
		}
		if(endTime != null){
			taskQuery.taskCreatedBefore(endTime);
		}
	}

	public void fillHistoryTaskQuery(HistoricTaskInstanceQuery historyTaskQuery){
		if (StringUtils.isNotEmpty(taskName)) {
			historyTaskQuery.taskName(taskName);
		}
		if (StringUtils.isNotEmpty(processName)) {
			historyTaskQuery.processDefinitionName(processName);
		}
		if (StringUtils.isNotEmpty(processKey)) {
			historyTaskQuery.processDefinitionKey(processKey);
		}
		if(beginTime != null){
			historyTaskQuery.taskCompletedAfter(beginTime);
		}
		if(endTime != null){
			historyTaskQuery.taskCreatedBefore(endTime);
		}
	}
}
