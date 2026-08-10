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
package com.cowave.hub.admin.controller.flow;

import com.cowave.hub.admin.domain.flow.entity.command.PurchaseCreate;
import com.cowave.hub.admin.domain.flow.entity.pto.PurchaseInfoPto;
import com.cowave.hub.admin.domain.flow.entity.query.PurchaseQuery;
import com.cowave.hub.admin.service.flow.PurchaseFlowService;
import com.cowave.zoo.http.client.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 采购申请
 * @order 26
 * @author shanhuiming
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/flow/purchase")
public class PurchaseFlowController {

    private final PurchaseFlowService purchaseFlowService;

    /**
     * 列表
     */
    @PostMapping("/list")
    public Response<Response.Page<PurchaseInfoPto>> list(@RequestBody PurchaseQuery query) {
        return Response.page(purchaseFlowService.list(query));
    }

    /**
     * 详情
     */
    @GetMapping( "/info/{id}")
    public Response<PurchaseInfoPto> info(@PathVariable String id) {
        return Response.success(purchaseFlowService.info(id));
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Response<Void> add(@RequestBody PurchaseCreate cmd) {
        purchaseFlowService.add(cmd);
        return Response.success();
    }

    /**
     * 修改
     */
    @PostMapping("/edit")
    public Response<Void> edit(@RequestBody PurchaseCreate cmd) {
        purchaseFlowService.edit(cmd);
        return Response.success();
    }

    /**
     * 删除
     */
    @GetMapping( "/delete/{ids}")
    public Response<Void> delete(@PathVariable String[] ids) {
        purchaseFlowService.delete(ids);
        return Response.success();
    }
}
