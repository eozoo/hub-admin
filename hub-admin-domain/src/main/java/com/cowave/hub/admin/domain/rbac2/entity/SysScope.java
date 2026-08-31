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
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cowave.zoo.framework.support.mybatis.handler.JsonObjectHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

/**
 * 数据权限
 *
 * @author shanhuiming
 */
@Getter
@Setter
@TableName(autoResultMap = true)
public class SysScope {

    /**
     * 权限id
     */
    @TableId(type = IdType.AUTO)
    private Integer scopeId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 权限名称
     */
    private String scopeName;

    /**
     * 权限模块
     */
    private String scopeModule;

    /**
     * 权限状态
     */
    private Integer scopeStatus;

    /**
     * 权限规则
     */
    @TableField(typeHandler = JsonObjectHandler.class)
    private Map<String, Object> scopeContent;

    /**
     * 备注
     */
    private String remark;

    /**
     * create_by
     */
    private String createBy;

    /**
     * create_time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * update_by
     */
    private String updateBy;

    /**
     * update_time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
