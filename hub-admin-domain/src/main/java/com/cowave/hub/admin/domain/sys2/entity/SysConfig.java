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
package com.cowave.hub.admin.domain.sys2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 系统配置
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysConfig {

    /**
     * 配置id
     */
    @TableId(type = IdType.AUTO)
    private Integer configId;

    /**
     * 租户id，系统默认配置归属tenant_code为system的系统租户
     */
    private Integer tenantId;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 租户内唯一配置键
     */
    private String configKey;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 值类型，默认string
     */
    private String valueType;

    /**
     * 值转换器
     */
    private String valueParser;

    /**
     * 是否系统默认配置 1是 0否
     */
    private Integer isDefault;

    /**
     * 是否新租户模板数据 1是 0否，仅system租户有效，复制后置0
     */
    private Integer isTemplate;

    /**
     * 状态 1启用 2停用
     */
    private Integer status;

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
