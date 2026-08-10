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
package com.cowave.hub.admin.domain.flow.entity.command;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class PurchaseCreate {

    /**
     * id（修改时传入）
     */
    private String id;

    /**
     * 采购内容
     */
    private String content;

    /**
     * 总金额
     */
    private BigDecimal money;

    /**
     * 采购审批人
     */
    private String manager;

    /**
     * 财务审批人
     */
    private String finance;

    /**
     * 出纳核实人
     */
    private String cashier;

    /**
     * 总经理审批人
     */
    private String general;
}
