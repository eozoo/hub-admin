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
 * 通知/评论评价表
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysNoticeReaction {

    /**
     * 评价id
     */
    @TableId(type = IdType.AUTO)
    private Long reactionId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 目标类型(1通知 2评论)
     */
    private Integer targetType;

    /**
     * 目标id
     */
    private Long targetId;

    /**
     * 通知id(冗余,便于按通知统计)
     */
    private Long noticeId;

    /**
     * 评价类型(1点赞 2差评)
     */
    private Integer reactionType;

    /**
     * 评价用户id
     */
    private Integer userId;

    /**
     * 评价用户昵称
     */
    private String userName;

    /**
     * 首次评价时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 评价更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
