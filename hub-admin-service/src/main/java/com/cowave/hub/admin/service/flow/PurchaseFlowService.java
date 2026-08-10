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
package com.cowave.hub.admin.service.flow;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.flow.entity.command.PurchaseCreate;
import com.cowave.hub.admin.domain.flow.entity.pto.PurchaseInfoPto;
import com.cowave.hub.admin.domain.flow.entity.query.PurchaseQuery;

/**
 * @author shanhuiming
 */
public interface PurchaseFlowService {

    /**
     * 列表
     */
    Page<PurchaseInfoPto> list(PurchaseQuery query);

    /**
     * 详情
     */
    PurchaseInfoPto info(String id);

    /**
     * 新增
     */
    void add(PurchaseCreate cmd);

    /**
     * 修改
     */
    void edit(PurchaseCreate cmd);

    /**
     * 删除
     */
    void delete(String[] ids);
}
