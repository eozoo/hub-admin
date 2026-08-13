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
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cowave.zoo.framework.access.security.AccessInfoSetter;
import com.cowave.zoo.framework.support.mybatis.handler.JsonObjectHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

/**
 * @author shanhuiming
 */
@Getter
@Setter
@TableName(autoResultMap = true)
public class SysScope implements AccessInfoSetter {

    /**
     * 权限id
     */
    private Integer scopeId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 权限模块
     */
    private String scopeModule;

    /**
     * 权限名称
     */
    private String scopeName;

    /**
     * 权限状态
     */
    private Integer scopeStatus;

    /**
     * 权限内容
     */
    @TableField(typeHandler = JsonObjectHandler.class)
    private Map<String, Object> scopeContent;

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
