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
package com.cowave.hub.admin.domain.rbac.biz;

import com.cowave.hub.admin.domain.rbac.entity.command.TenantCreate;
import com.cowave.hub.admin.domain.rbac.entity.command.TenantStatusUpdate;

/**
 * @author shanhuiming
 */
public interface HubTenantBiz {

    /**
     * 新增租户（含配置重置和附件关联）
     */
    void createTenant(TenantCreate tenantCreate);

    /**
     * 修改租户（含附件更新）
     */
    void editTenant(TenantCreate tenantCreate);

    /**
     * 修改状态
     */
    void updateStatus(TenantStatusUpdate statusUpdate);
}
