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

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 租户用户关系
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysTenantUser {

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 租户内用户类型：employee员工、external外部成员、service服务账号
     */
    private String userType;

    /**
     * 租户内用户编码
     */
    private String userCode;

    /**
     * 租户内显示名称
     */
    private String displayName;

    /**
     * 租户内职级
     */
    private String userRank;

    /**
     * 租户内成员状态 1启用 2停用
     */
    private Integer status;

    /**
     * 是否默认租户 1是 0否
     */
    private Integer isDefault;

    /**
     * 加入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date joinTime;

    /**
     * 离开租户时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date leaveTime;

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
