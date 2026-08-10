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
package com.cowave.hub.admin.service.rbac;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rbac.entity.HubScope;
import com.cowave.hub.admin.domain.rbac.entity.command.ScopeInfoUpdate;
import com.cowave.hub.admin.domain.rbac.entity.command.ScopeNameUpdate;
import com.cowave.hub.admin.domain.rbac.entity.query.ScopeQuery;
import com.cowave.hub.admin.domain.rbac.entity.command.ScopeStatusUpdate;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubScopeService {

    /**
     * 列表
     */
    Page<HubScope> page(String tenantId, ScopeQuery query);

    /**
     * 详情
     */
    HubScope info(String tenantId, Integer scopeId);

    /**
     * 新增
     */
    void create(String tenantId, HubScope hubScope);

    /**
     * 删除
     */
    void delete(String tenantId, List<Integer> scopeIds);

    /**
     * 修改
     */
    void edit(String tenantId, ScopeNameUpdate nameUpdate);

    /**
     * 修改状态
     */
    void switchStatus(String tenantId, ScopeStatusUpdate statusUpdate);

    /**
     * 修改权限
     */
    void editContent(String tenantId, ScopeInfoUpdate infoUpdate);
}
