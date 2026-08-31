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
package com.cowave.hub.admin.domain.notice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 通知分类表
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysNoticeType {

    /**
     * 分类id
     */
    @TableId(type = IdType.AUTO)
    private Long typeId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 分类编码
     */
    private String typeCode;

    /**
     * 分类名称
     */
    private String typeName;

    /**
     * 父级id,0为顶级
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态(0停用 1启用)
     */
    private Integer status;

    /**
     * 是否删除(0否 1是)
     */
    private Integer isDeleted;

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
