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
package com.cowave.hub.admin.service.rbac.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rbac.biz.HubScopeBiz;
import com.cowave.hub.admin.domain.rbac.entity.HubScope;
import com.cowave.hub.admin.domain.rbac.entity.command.ScopeInfoUpdate;
import com.cowave.hub.admin.domain.rbac.entity.command.ScopeNameUpdate;
import com.cowave.hub.admin.domain.rbac.entity.command.ScopeStatusUpdate;
import com.cowave.hub.admin.domain.rbac.entity.query.ScopeQuery;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubScopeRepositoryFacade;
import com.cowave.hub.admin.service.rbac.HubScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
public class HubScopeServiceImpl implements HubScopeService {

    private final HubScopeBiz scopeBiz;

    private final HubScopeRepositoryFacade scopeRepositoryFacade;

    @Override
    public Page<HubScope> page(String tenantId, ScopeQuery query) {
        return scopeRepositoryFacade.queryPage(tenantId, query);
    }

    @Override
    public HubScope info(String tenantId, Integer scopeId) {
        return scopeRepositoryFacade.queryById(tenantId, scopeId);
    }

    @Override
    public void create(String tenantId, HubScope hubScope) {
        scopeBiz.createScope(tenantId, hubScope);
    }

    @Override
    public void delete(String tenantId, List<Integer> scopeIds) {
        scopeBiz.deleteScopes(tenantId, scopeIds);
    }

    @Override
    public void edit(String tenantId, ScopeNameUpdate nameUpdate) {
        scopeBiz.editName(tenantId, nameUpdate);
    }

    @Override
    public void switchStatus(String tenantId, ScopeStatusUpdate statusUpdate) {
        scopeBiz.switchStatus(tenantId, statusUpdate);
    }

    @Override
    public void editContent(String tenantId, ScopeInfoUpdate infoUpdate) {
        scopeBiz.editContent(tenantId, infoUpdate);
    }
}
