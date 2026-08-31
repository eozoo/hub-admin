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
 * 通知评论表
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysNoticeComment {

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
     * 通知id
     */
    private Long noticeId;

    /**
     * 上级评论id,0为顶级
     */
    private Long parentId;

    /**
     * 根评论id
     */
    private Long rootId;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 评论人id
     */
    private Integer commentUserId;

    /**
     * 评论人昵称
     */
    private String commentUserName;

    /**
     * 回复人id
     */
    private Integer replyUserId;

    /**
     * 回复人昵称
     */
    private String replyUserName;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数统计
     */
    private Integer likeCount;

    /**
     * 差评数统计
     */
    private Integer dislikeCount;

    /**
     * 状态(0屏蔽 1正常)
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
