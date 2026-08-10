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
package com.cowave.hub.admin.domain.flow.biz.impl;

import com.cowave.hub.admin.domain.flow.entity.FlowPurchase;
import com.cowave.hub.admin.domain.flow.biz.FlowPurchaseBiz;
import com.cowave.hub.admin.domain.flow.repository.FlowPurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class FlowPurchaseBizImpl implements FlowPurchaseBiz {

    private final FlowPurchaseRepository flowPurchaseRepository;

    @Override
    public void create(FlowPurchase flowPurchase) {
        flowPurchaseRepository.insert(flowPurchase);
    }

    @Override
    public void edit(FlowPurchase flowPurchase) {
        flowPurchaseRepository.update(flowPurchase);
    }

    @Override
    public void changeProcessStatus(String id, Integer processStatus) {
        flowPurchaseRepository.changeProcessStatus(id, processStatus);
    }

    @Override
    public void delete(String[] ids) {
        flowPurchaseRepository.delete(ids);
    }
}
