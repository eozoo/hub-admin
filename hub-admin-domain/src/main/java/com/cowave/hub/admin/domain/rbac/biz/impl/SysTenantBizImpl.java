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

import com.cowave.hub.admin.domain.rbac.biz.SysTenantBiz;
import com.cowave.hub.admin.domain.rbac.entity.command.TenantCreate;
import com.cowave.hub.admin.domain.rbac.entity.command.TenantStatusUpdate;
import com.cowave.hub.admin.domain.rbac.repository.SysTenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysTenantBizImpl implements SysTenantBiz {

    private final SysTenantRepository tenantRepository;

    @Override
    public void createTenant(TenantCreate tenantCreate) {
        tenantRepository.save(tenantCreate);
    }

    @Override
    public void editTenant(TenantCreate tenantCreate) {
        tenantRepository.updateTenant(tenantCreate);
    }

    @Override
    public void updateStatus(TenantStatusUpdate statusUpdate) {
        tenantRepository.updateStatus(statusUpdate);
    }
}
