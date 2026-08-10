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
package com.cowave.hub.admin.domain.flow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class FlowPurchase {

    /**
     * 部门审批
     */
    public static final Integer STATUS_DEPT = 1;

    /**
     * 财务审批
     */
    public static final Integer STATUS_FINANCE = 2;

    /**
     * 总经理审批
     */
    public static final Integer STATUS_GENERAL = 3;

    /**
     * 审批驳回
     */
    public static final Integer STATUS_REJECT = 4;

    /**
     * 待付款
     */
    public static final Integer STATUS_CASHIER = 5;

    /**
     * 待收货
     */
    public static final Integer STATUS_RECEIVING = 6;

    /**
     * 已收货
     */
    public static final Integer STATUS_RECEIVED = 7;

    /**
     * 已撤销
     */
    public static final Integer STATUS_REVOCATED = 8;

    /**
     * id
     */
    @TableId(type = IdType.INPUT)
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
     * 申请人
     */
    private String applyUser;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;

    /**
     * 流程id
     */
    private String processId;

    /**
     * 流程状态
     */
    private Integer processStatus;
}
