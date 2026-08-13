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
package com.cowave.hub.admin.service.sys;

import com.cowave.zoo.framework.access.operation.OperationHandler;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.hub.admin.domain.sys.entity.SysOperation;
import com.cowave.hub.admin.domain.sys.entity.query.OperationQuery;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysOperationService extends OperationHandler {

	/**
	 * 列表
	 */
	Response.Page<SysOperation> queryPage(String tenantId, OperationQuery query, boolean isPage);

	/**
	 * 删除
	 */
	void delete(List<String> ids);

	/**
	 * 清空
	 */
	void clean(String tenantId);

	void saveLog(SysOperation sysOperation);
}
