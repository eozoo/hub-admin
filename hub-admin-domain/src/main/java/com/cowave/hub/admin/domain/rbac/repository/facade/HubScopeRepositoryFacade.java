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
package com.cowave.hub.admin.domain.rbac.repository.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rbac.entity.HubScope;
import com.cowave.hub.admin.domain.rbac.entity.query.ScopeQuery;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubScopeRepositoryFacade {

    /**
     * 分页列表
     */
    Page<HubScope> queryPage(String tenantId, ScopeQuery query);

    /**
     * 详情
     */
    HubScope queryById(String tenantId, Integer scopeId);

    /**
     * 按权限标识查询数据范围
     */
    List<HubScope> queryListByPermit(String permit, List<String> roleCodes);
}
