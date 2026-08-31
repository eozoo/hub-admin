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
 * 字典项
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysDict {

    /**
     * 字典项id
     */
    @TableId(type = IdType.AUTO)
    private Long dictId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 所属类型编码
     */
    private String typeCode;

    /**
     * 类型内唯一字典项编码
     */
    private String dictCode;

    /**
     * 字典项名称
     */
    private String dictName;

    /**
     * 字典项值
     */
    private String dictValue;

    /**
     * 值类型，默认string
     */
    private String valueType;

    /**
     * 值转换器
     */
    private String valueParser;

    /**
     * 字典项排序
     */
    private Integer dictOrder;

    /**
     * 是否类型内默认项 1是 0否
     */
    private Integer isDefault;

    /**
     * 展示样式
     */
    private String css;

    /**
     * 状态 1启用 2停用
     */
    private Integer status;

    /**
     * 是否新租户模板数据 1是 0否，仅system租户有效，复制后置0
     */
    private Integer isTemplate;

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
