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
 * 通知实际接收人表
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysNoticeReceiver {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long receiverId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 通知id
     */
    private Long noticeId;

    /**
     * 接收用户id
     */
    private Integer userId;

    /**
     * 接收用户名称快照
     */
    private String userName;

    /**
     * 接收状态(0生成失败 1已生成 2可送达)
     */
    private Integer receiveStatus;

    /**
     * 阅读状态(0未读 1已读)
     */
    private Integer readStatus;

    /**
     * 阅读时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date readTime;

    /**
     * 点击状态(0未点击 1已点击)
     */
    private Integer clickStatus;

    /**
     * 首次点击时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date clickTime;

    /**
     * 归档状态(0未归档 1已归档)
     */
    private Integer archiveStatus;

    /**
     * 归档时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date archiveTime;

    /**
     * 用户侧删除状态(0未删除 1已删除)
     */
    private Integer deleteStatus;

    /**
     * 用户侧删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deleteTime;

    /**
     * 业务处理状态(0未处理 1已处理)
     */
    private Integer actionStatus;

    /**
     * 业务处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actionTime;

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
