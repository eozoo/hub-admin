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
package com.cowave.hub.admin.infra.rbac.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.rbac.entity.SysScope;
import com.cowave.hub.admin.domain.rbac.entity.query.ScopeQuery;
import com.cowave.hub.admin.domain.rbac.repository.SysScopeRepository;
import com.cowave.hub.admin.infra.rbac.mapper.SysScopeMapper;
import com.cowave.zoo.framework.access.Access;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
@Repository
public class SysScopeDao extends ServiceImpl<SysScopeMapper, SysScope> implements SysScopeRepository {

    @Override
    public Page<SysScope> queryPage(String tenantId, ScopeQuery query) {
        return lambdaQuery()
                .eq(SysScope::getTenantId, tenantId)
                .eq(StringUtils.isNotBlank(query.getScopeModule()), SysScope::getScopeModule, query.getScopeModule())
                .page(Access.page());
    }

    @Override
    public SysScope queryById(String tenantId, Integer scopeId) {
        return lambdaQuery()
                .eq(SysScope::getTenantId, tenantId)
                .eq(SysScope::getScopeId, scopeId)
                .one();
    }

    @Override
    public List<SysScope> queryListByPermit(String permit, List<String> roleCodes) {
        return baseMapper.listScopeByPermit(permit, roleCodes);
    }

    @Override
    public void deleteByIds(String tenantId, List<Integer> scopeIds) {
        lambdaUpdate()
                .eq(SysScope::getTenantId, tenantId)
                .in(SysScope::getScopeId, scopeIds)
                .remove();
    }

    @Override
    public void updateNameById(String tenantId, Integer scopeId, String scopeName) {
        lambdaUpdate()
                .eq(SysScope::getTenantId, tenantId)
                .eq(SysScope::getScopeId, scopeId)
                .set(SysScope::getScopeName, scopeName)
                .update();
    }

    @Override
    public void updateStatusById(String tenantId, Integer scopeId, Integer status) {
        lambdaUpdate()
                .eq(SysScope::getTenantId, tenantId)
                .eq(SysScope::getScopeId, scopeId)
                .set(SysScope::getScopeStatus, status)
                .update();
    }

    @Override
    public void updateContentById(String tenantId, Integer scopeId, Map<String, Object> content) {
        SysScope sysScope = new SysScope();
        sysScope.setScopeContent(content);
        lambdaUpdate()
                .eq(SysScope::getTenantId, tenantId)
                .eq(SysScope::getScopeId, scopeId)
                .update(sysScope);
    }
}
