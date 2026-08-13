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

import com.cowave.hub.admin.domain.sys.biz.SysOperationBiz;
import com.cowave.hub.admin.domain.sys.entity.SysOperation;
import com.cowave.hub.admin.domain.sys.repository.SysOperationRepository;
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
public class SysOperationBizImpl implements SysOperationBiz {

    private final SysOperationRepository operationRepository;

    @Override
    public void createOperation(OperationInfo opInfo, Object resp) {
        SysOperation sysOperation = new SysOperation(opInfo);
        if(sysOperation.getAccess() == null){
            sysOperation.setAccess(Access.accessInfo());
        }
        sysOperation.setIp(Access.accessIp());
        sysOperation.setUrl(Access.accessMethod() + " " + Access.accessUrl());
        sysOperation.setOpTime(Access.accessTime());
        // 请求内容
        sysOperation.setRequest(Access.getAccessLogParams());
        // 响应内容
        sysOperation.setResponse(resp);
        save(sysOperation);
    }

    @Override
    public void save(SysOperation sysOperation) {
        operationRepository.save(sysOperation);
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
