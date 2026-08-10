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
package com.cowave.hub.admin.domain.flow.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cowave.hub.admin.domain.flow.entity.FlowPurchase;
import com.cowave.hub.admin.domain.flow.repository.facade.FlowPurchaseRepositoryFacade;

/**
 * @author shanhuiming
 */
public interface FlowPurchaseRepository extends FlowPurchaseRepositoryFacade, IService<FlowPurchase> {

    void insert(FlowPurchase flowPurchase);

    void update(FlowPurchase flowPurchase);

    void changeProcessStatus(String id, Integer processStatus);

    void delete(String[] ids);
}
