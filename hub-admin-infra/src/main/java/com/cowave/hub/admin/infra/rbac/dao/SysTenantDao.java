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
import com.cowave.hub.admin.domain.rbac.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac.entity.command.TenantStatusUpdate;
import com.cowave.hub.admin.domain.rbac.entity.query.TenantQuery;
import com.cowave.hub.admin.domain.rbac.repository.SysTenantRepository;
import com.cowave.hub.admin.infra.rbac.mapper.SysTenantMapper;
import com.cowave.zoo.framework.access.Access;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysTenantDao extends ServiceImpl<SysTenantMapper, SysTenant> implements SysTenantRepository {

    @Override
    public Page<SysTenant> queryPage(TenantQuery query) {
        return lambdaQuery()
                .like(StringUtils.isNotBlank(query.getTenantId()), SysTenant::getTenantId, query.getTenantId())
                .orderByAsc(SysTenant::getCreateTime)
                .page(Access.page());
    }

    @Override
    public SysTenant queryById(String tenantId) {
        return getById(tenantId);
    }

    @Override
    public String queryNameById(String tenantId) {
        return lambdaQuery()
                .eq(SysTenant::getTenantId, tenantId)
                .select(SysTenant::getTenantName)
                .oneOpt().map(SysTenant::getTenantName).orElse(null);
    }

    @Override
    public List<SysTenant> queryList() {
        return list();
    }

    @Override
    public void updateTenant(SysTenant sysTenant) {
        lambdaUpdate()
                .eq(SysTenant::getTenantId, sysTenant.getTenantId())
                .set(SysTenant::getTenantName, sysTenant.getTenantName())
                .set(SysTenant::getTenantUser, sysTenant.getTenantUser())
                .set(SysTenant::getTenantEmail, sysTenant.getTenantEmail())
                .set(SysTenant::getTenantPhone, sysTenant.getTenantPhone())
                .set(SysTenant::getTenantAddr, sysTenant.getTenantAddr())
                .set(SysTenant::getUserLimit, sysTenant.getUserLimit())
                .set(SysTenant::getTitle, sysTenant.getTitle())
                .set(SysTenant::getLogo, sysTenant.getLogo())
                .set(SysTenant::getUpdateBy, sysTenant.getUpdateBy())
                .set(SysTenant::getUpdateTime, sysTenant.getUpdateTime())
                .update();
    }

    @Override
    public void updateStatus(TenantStatusUpdate statusUpdate) {
        lambdaUpdate().eq(SysTenant::getTenantId, statusUpdate.getTenantId())
                .set(SysTenant::getStatus, statusUpdate.getStatus())
                .set(SysTenant::getUpdateBy, Access.userCode())
                .set(SysTenant::getUpdateTime, new Date())
                .update();
    }
}
