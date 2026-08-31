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
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cowave.zoo.framework.support.mybatis.handler.JsonListHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 系统评分留言
 *
 * @author shanhuiming
 */
@Getter
@Setter
@TableName(autoResultMap = true)
public class SysFeedback {

    /**
     * 留言id
     */
    @TableId(type = IdType.AUTO)
    private Long feedbackId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 留言用户id
     */
    private Integer userId;

    /**
     * 留言用户名称快照
     */
    private String userName;

    /**
     * 反馈类型，如rating评分、suggestion建议、complaint投诉
     */
    private String feedbackType;

    /**
     * 评分 1-5，非评分类型可由应用忽略
     */
    private Integer score;

    /**
     * 留言内容
     */
    private String content;

    /**
     * 图片信息JSON数组
     */
    @TableField(typeHandler = JsonListHandler.class)
    private List<String> images;

    /**
     * 是否匿名展示 1是 0否
     */
    private Integer isAnonymous;

    /**
     * 状态：1正常 2隐藏 3关闭
     */
    private Integer status;

    /**
     * 点赞数冗余统计
     */
    private Integer likeCount;

    /**
     * 评论数冗余统计
     */
    private Integer replyCount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
