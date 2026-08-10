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
package com.cowave.hub.admin.service.sys.impl;

import com.cowave.hub.admin.domain.sys.biz.HubOperationBiz;
import com.cowave.hub.admin.domain.sys.entity.HubOperation;
import com.cowave.hub.admin.domain.sys.entity.query.OperationQuery;
import com.cowave.hub.admin.domain.sys.repository.facade.HubOperationRepositoryFacade;
import com.cowave.hub.admin.service.sys.HubOperationService;
import com.cowave.zoo.framework.access.operation.OperationInfo;
import com.cowave.zoo.http.client.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Service
public class HubOperationServiceImpl implements HubOperationService {
    private final HubOperationBiz operationBiz;
    private final HubOperationRepositoryFacade operationRepositoryFacade;

    @Override
    public void handle(OperationInfo opInfo, Map<String, Object> args, Object resp, Exception e) {
        operationBiz.createOperation(opInfo, resp);
    }

    @Override
    public Response.Page<HubOperation> queryPage(String tenantId, OperationQuery query, boolean isPage) {
        return operationRepositoryFacade.queryPage(tenantId, query, isPage);
    }

    @Override
    public void delete(List<String> ids) {
        operationBiz.delete(ids);
    }

    @Override
    public void clean(String tenantId) {
        operationBiz.clean(tenantId);
    }

    @Override
    public void saveLog(HubOperation hubOperation) {
        operationBiz.save(hubOperation);
    }
}
