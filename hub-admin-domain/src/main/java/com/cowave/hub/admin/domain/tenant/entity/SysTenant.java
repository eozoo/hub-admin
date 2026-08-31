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
package com.cowave.hub.admin.domain.tenant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 租户信息
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysTenant {

    /**
     * 租户id
     */
    @TableId(type = IdType.AUTO)
    private Integer tenantId;

    /**
     * 租户稳定业务编码，系统租户固定使用system
     */
    private String tenantCode;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 租户类型：system系统租户、normal普通租户、trial试用租户
     */
    private String tenantType;

    /**
     * 租户独立域名或访问入口
     */
    private String tenantDomain;

    /**
     * 用户上限
     */
    private Integer userLimit;

    /**
     * 用户统计
     */
    private Integer userCount;

    /**
     * 租户状态
     */
    private Integer status;

    /**
     * 到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

    /**
     * 租户标题
     */
    private String title;

    /**
     * view_index
     */
    private String viewIndex;

    /**
     * 租户联系人
     */
    private String tenantUser;

    /**
     * 租户地址
     */
    private String tenantAddr;

    /**
     * 租户电话
     */
    private String tenantPhone;

    /**
     * 租户邮箱
     */
    private String tenantEmail;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

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
