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
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cowave.zoo.framework.support.mybatis.handler.JsonObjectHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

/**
 * 通知模板表
 *
 * @author shanhuiming
 */
@Getter
@Setter
@TableName(autoResultMap = true)
public class SysNoticeTemplate {

    /**
     * 模板id
     */
    @TableId(type = IdType.AUTO)
    private Long templateId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 模板编码
     */
    private String templateCode;

    /**
     * 模板标题
     */
    private String title;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 适用渠道类型(1邮箱 2钉钉 3企微,空为通用)
     */
    private Integer channelType;

    /**
     * 变量定义(JSON)
     */
    @TableField(typeHandler = JsonObjectHandler.class)
    private Map<String, Object> variables;

    /**
     * 语言
     */
    private String lang;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 状态(0停用 1启用)
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

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
