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
package com.cowave.hub.admin.domain.rbac.biz.impl;

import com.cowave.hub.admin.domain.rbac.biz.SysScopeBiz;
import com.cowave.hub.admin.domain.rbac.entity.SysScope;
import com.cowave.hub.admin.domain.rbac.entity.command.ScopeInfoUpdate;
import com.cowave.hub.admin.domain.rbac.entity.command.ScopeNameUpdate;
import com.cowave.hub.admin.domain.rbac.entity.command.ScopeStatusUpdate;
import com.cowave.hub.admin.domain.rbac.repository.SysScopeRepository;
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
public class SysScopeBizImpl implements SysScopeBiz {

    private final SysScopeRepository scopeRepository;

    @Override
    public void createScope(String tenantId, SysScope sysScope) {
        sysScope.setTenantId(tenantId);
        scopeRepository.save(sysScope);
    }

    @Override
    public void deleteScopes(String tenantId, List<Integer> scopeIds) {
        scopeRepository.deleteByIds(tenantId, scopeIds);
    }

    @Override
    public void editName(String tenantId, ScopeNameUpdate nameUpdate) {
        scopeRepository.updateNameById(tenantId, nameUpdate.getScopeId(), nameUpdate.getScopeName());
    }

    @Override
    public void switchStatus(String tenantId, ScopeStatusUpdate statusUpdate) {
        scopeRepository.updateStatusById(tenantId, statusUpdate.getScopeId(), statusUpdate.getScopeStatus());
    }

    @Override
    public void editContent(String tenantId, ScopeInfoUpdate infoUpdate) {
        scopeRepository.updateContentById(tenantId, infoUpdate.getScopeId(), infoUpdate.getScopeContent());
    }
}
