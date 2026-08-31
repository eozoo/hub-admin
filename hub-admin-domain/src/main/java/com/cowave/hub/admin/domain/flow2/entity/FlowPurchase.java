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
package com.cowave.hub.admin.domain.flow2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cowave.zoo.framework.support.mybatis.handler.JsonListHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 采购申请
 *
 * @author shanhuiming
 */
@Getter
@Setter
@TableName(autoResultMap = true)
public class FlowPurchase {

    /**
     * 采购申请业务id，同时作为流程业务键
     */
    @TableId(type = IdType.INPUT)
    private String purchaseId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 采购主题
     */
    private String purchaseTitle;

    /**
     * 采购说明
     */
    private String content;

    /**
     * 采购明细JSON数组
     */
    @TableField(typeHandler = JsonListHandler.class)
    private List<Map<String, Object>> purchaseItems;

    /**
     * 采购总金额
     */
    private BigDecimal totalAmount;

    /**
     * ISO 4217币种代码
     */
    private String currencyCode;

    /**
     * 申请人用户id
     */
    private Integer applyUserId;

    /**
     * 申请人名称快照
     */
    private String applyUserName;

    /**
     * 申请时所属部门id
     */
    private Integer applyDeptId;

    /**
     * 申请时所属部门名称快照
     */
    private String applyDeptName;

    /**
     * 申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date applyTime;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 审批状态：0草稿 1审批中 2已通过 3已驳回 4已撤销 5已终止
     */
    private Integer processStatus;

    /**
     * 采购状态：0待审批 1待采购 2待付款 3待收货 4已收货 5已取消
     */
    private Integer purchaseStatus;

    /**
     * 审批结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;

    /**
     * 撤销、终止或取消原因
     */
    private String cancelReason;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
