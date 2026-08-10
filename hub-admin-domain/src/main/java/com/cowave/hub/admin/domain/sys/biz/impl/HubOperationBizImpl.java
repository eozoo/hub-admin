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
package com.cowave.hub.admin.domain.sys.biz.impl;

import com.cowave.hub.admin.domain.sys.biz.HubOperationBiz;
import com.cowave.hub.admin.domain.sys.entity.HubOperation;
import com.cowave.hub.admin.domain.sys.repository.HubOperationRepository;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.operation.OperationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubOperationBizImpl implements HubOperationBiz {

    private final HubOperationRepository operationRepository;

    @Override
    public void createOperation(OperationInfo opInfo, Object resp) {
        HubOperation hubOperation = new HubOperation(opInfo);
        if(hubOperation.getAccess() == null){
            hubOperation.setAccess(Access.accessInfo());
        }
        hubOperation.setIp(Access.accessIp());
        hubOperation.setUrl(Access.accessMethod() + " " + Access.accessUrl());
        hubOperation.setOpTime(Access.accessTime());
        // 请求内容
        hubOperation.setRequest(Access.getAccessLogParams());
        // 响应内容
        hubOperation.setResponse(resp);
        save(hubOperation);
    }

    @Override
    public void save(HubOperation hubOperation) {
        operationRepository.save(hubOperation);
    }

    @Override
    public void delete(List<String> ids) {
        operationRepository.delete(ids);
    }

    @Override
    public void clean(String tenantId) {
        operationRepository.clean(tenantId);
    }
}
