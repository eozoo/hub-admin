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
package com.cowave.hub.admin.infra.flow.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.flow.entity.FlowPurchase;
import com.cowave.hub.admin.domain.flow.entity.pto.PurchaseInfoPto;
import com.cowave.hub.admin.domain.flow.repository.FlowPurchaseRepository;
import com.cowave.hub.admin.infra.flow.mapper.FlowPurchaseMapper;
import com.cowave.zoo.framework.access.Access;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class FlowPurchaseDao extends ServiceImpl<FlowPurchaseMapper, FlowPurchase> implements FlowPurchaseRepository {

    private final FlowPurchaseMapper purchaseMapper;

    @Override
    public Page<PurchaseInfoPto> queryPage(PurchaseInfoPto purchaseInfoPto) {
        return purchaseMapper.list(Access.page(), purchaseInfoPto);
    }

    @Override
    public PurchaseInfoPto queryById(String id) {
        return purchaseMapper.info(id);
    }

    @Override
    public void insert(FlowPurchase flowPurchase) {
        save(flowPurchase);
    }

    @Override
    public void update(FlowPurchase flowPurchase) {
        lambdaUpdate()
                .eq(FlowPurchase::getId, flowPurchase.getId())
                .set(FlowPurchase::getContent, flowPurchase.getContent())
                .set(FlowPurchase::getMoney, flowPurchase.getMoney())
                .update();
    }

    @Override
    public void changeProcessStatus(String id, Integer processStatus) {
        lambdaUpdate()
                .eq(FlowPurchase::getId, id)
                .set(FlowPurchase::getProcessStatus, processStatus)
                .update();
    }

    @Override
    public void delete(String[] ids) {
        baseMapper.deleteBatchIds(Arrays.asList(ids));
    }
}
