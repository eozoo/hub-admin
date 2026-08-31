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
package com.cowave.hub.admin.domain.rbac2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 部门信息
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysDept {

    /**
     * 部门id
     */
    @TableId(type = IdType.AUTO)
    private Integer deptId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 部门类型
     */
    private String deptType;

    /**
     * 部门状态 1启用 2停用
     */
    private Integer deptStatus;

    /**
     * 部门排序
     */
    private Integer deptOrder;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门简称
     */
    private String deptShort;

    /**
     * 部门地址
     */
    private String deptAddr;

    /**
     * 部门电话
     */
    private String deptPhone;

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
