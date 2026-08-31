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
 * 留言评论
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysFeedbackComment {

    /**
     * 评论id
     */
    @TableId(type = IdType.AUTO)
    private Long commentId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 留言id
     */
    private Long feedbackId;

    /**
     * 父评论id，0表示顶级评论
     */
    private Long parentId;

    /**
     * 被回复用户id
     */
    private Integer replyToUserId;

    /**
     * 被回复用户名称快照
     */
    private String replyToName;

    /**
     * 评论用户id
     */
    private Integer userId;

    /**
     * 评论用户名称快照
     */
    private String userName;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 状态：1正常 2隐藏 3关闭
     */
    private Integer status;

    /**
     * 点赞数冗余统计
     */
    private Integer likeCount;

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
