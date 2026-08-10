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
package com.cowave.hub.admin.domain.sys.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cowave.hub.admin.domain.sys.entity.HubConfig;
import com.cowave.hub.admin.domain.sys.repository.facade.HubConfigRepositoryFacade;

/**
 * @author shanhuiming
 */
public interface HubConfigRepository extends HubConfigRepositoryFacade, IService<HubConfig> {

    /**
     * 重置租户配置
     */
    void resetTenantConfig(String tenantId);

    /**
     * 更新配置
     */
    void updateConfig(HubConfig hubConfig);
}
