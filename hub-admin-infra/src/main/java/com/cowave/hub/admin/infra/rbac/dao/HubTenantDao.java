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
import com.cowave.hub.admin.domain.rbac.entity.HubTenant;
import com.cowave.hub.admin.domain.rbac.entity.command.TenantStatusUpdate;
import com.cowave.hub.admin.domain.rbac.entity.query.TenantQuery;
import com.cowave.hub.admin.domain.rbac.repository.HubTenantRepository;
import com.cowave.hub.admin.infra.rbac.mapper.HubTenantMapper;
import com.cowave.zoo.framework.access.Access;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class HubTenantDao extends ServiceImpl<HubTenantMapper, HubTenant> implements HubTenantRepository {

    @Override
    public Page<HubTenant> queryPage(TenantQuery query) {
        return lambdaQuery()
                .like(StringUtils.isNotBlank(query.getTenantId()), HubTenant::getTenantId, query.getTenantId())
                .orderByAsc(HubTenant::getCreateTime)
                .page(Access.page());
    }

    @Override
    public HubTenant queryById(String tenantId) {
        return getById(tenantId);
    }

    @Override
    public String queryNameById(String tenantId) {
        return lambdaQuery()
                .eq(HubTenant::getTenantId, tenantId)
                .select(HubTenant::getTenantName)
                .oneOpt().map(HubTenant::getTenantName).orElse(null);
    }

    @Override
    public List<HubTenant> queryList() {
        return list();
    }

    @Override
    public void updateTenant(HubTenant hubTenant) {
        lambdaUpdate()
                .eq(HubTenant::getTenantId, hubTenant.getTenantId())
                .set(HubTenant::getTenantName, hubTenant.getTenantName())
                .set(HubTenant::getTenantUser, hubTenant.getTenantUser())
                .set(HubTenant::getTenantEmail, hubTenant.getTenantEmail())
                .set(HubTenant::getTenantPhone, hubTenant.getTenantPhone())
                .set(HubTenant::getTenantAddr, hubTenant.getTenantAddr())
                .set(HubTenant::getUserLimit, hubTenant.getUserLimit())
                .set(HubTenant::getTitle, hubTenant.getTitle())
                .set(HubTenant::getLogo, hubTenant.getLogo())
                .set(HubTenant::getUpdateBy, hubTenant.getUpdateBy())
                .set(HubTenant::getUpdateTime, hubTenant.getUpdateTime())
                .update();
    }

    @Override
    public void updateStatus(TenantStatusUpdate statusUpdate) {
        lambdaUpdate().eq(HubTenant::getTenantId, statusUpdate.getTenantId())
                .set(HubTenant::getStatus, statusUpdate.getStatus())
                .set(HubTenant::getUpdateBy, Access.userCode())
                .set(HubTenant::getUpdateTime, new Date())
                .update();
    }
}
