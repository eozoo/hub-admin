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

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.repository.ProcessDefinitionQuery;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class FlowDeploy {

	/**
	 * 流程id
	 */
	private String id;

	/**
	 * 流程key
	 */
	private String key;

	/**
	 * 流程名称
	 */
	private String name;

	/**
	 * 版本
	 */
	private Integer version;

	/**
	 * 部署id
	 */
	private String deploymentId;

	/**
	 * 资源名称
	 */
	private String resourceName;

	/**
	 * 是否最新版本
	 */
	private boolean latest;

	private String diagramresourceName;

	public void fillProcessQuery(ProcessDefinitionQuery queryCondition){
		if (StringUtils.isNotEmpty(key)) {
			queryCondition.processDefinitionKey(key);
		}
		if (StringUtils.isNotEmpty(name)) {
			queryCondition.processDefinitionName(name);
		}
		if (latest) {
			queryCondition.latestVersion();
		}
	}
}
