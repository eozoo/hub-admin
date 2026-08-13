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
package com.cowave.hub.admin.domain.rbac.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cowave.zoo.framework.access.security.AccessInfoSetter;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 租户
 */
@Getter
@Setter
public class SysTenant implements AccessInfoSetter {

    /**
     * 租户id
     */
    @NotBlank(message = "{admin.tenant.id.null}")
    @TableId
    private String tenantId;

    /**
     * 租户名称
     */
    @NotBlank(message = "{admin.tenant.name.null}")
    private String tenantName;

    /**
     * 租户首页
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
     * 用户序号
     */
    private Integer userIndex;

    /**
     * 用户统计
     */
    private Integer userCount;

    /**
     * 用户上限
     */
    private Integer userLimit;

    /**
     * 租户标题
     */
    @NotBlank(message = "{admin.tenant.title.null}")
    private String title;

    /**
     * 租户图标
     */
    private String logo;

    /**
     * 租户状态
     */
    private EnableStatus status;

    /**
     * 租户到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expireTime;

    /**
     * 备注
     */
    @ExcelProperty("备注")
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
