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
import com.cowave.zoo.framework.support.mybatis.handler.ArrayListHandler;
import com.cowave.zoo.framework.support.mybatis.handler.JsonObjectHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统通知主表
 *
 * @author shanhuiming
 */
@Getter
@Setter
@TableName(autoResultMap = true)
public class SysNotice {

    /**
     * 通知id
     */
    @TableId(type = IdType.AUTO)
    private Long noticeId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 通知业务编号，用于幂等和外部追踪
     */
    private String noticeNo;

    /**
     * 通知标题
     */
    private String title;

    /**
     * 通知内容
     */
    private String content;

    /**
     * 内容格式(1纯文本 2HTML 3Markdown 4富文本)
     */
    private Integer contentFormat;

    /**
     * 通知封面
     */
    private String cover;

    /**
     * 业务模块标识
     */
    private String noticeModule;

    /**
     * 优先级(0普通 1重要 2紧急)
     */
    private Integer noticePriority;

    /**
     * 分类id
     */
    private Long typeId;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 发布时使用的模板版本
     */
    private Integer templateVersion;

    /**
     * 模板渲染变量(JSON)
     */
    @TableField(typeHandler = JsonObjectHandler.class)
    private Map<String, Object> variables;

    /**
     * 发送渠道id数组
     */
    @TableField(typeHandler = ArrayListHandler.class)
    private List<Long> channelIds;

    /**
     * 发送目标(JSON)：all、userIds、deptIds、roleIds、groupIds，可组合选择
     */
    @TableField(typeHandler = JsonObjectHandler.class)
    private Map<String, Object> targetScope;

    /**
     * 是否补发给发布后新进入目标范围的成员(0否 1是)
     */
    private Integer toFutureMembers;

    /**
     * 发送人id
     */
    private Integer senderId;

    /**
     * 发送人名称
     */
    private String senderName;

    /**
     * 业务状态(0草稿 1待发布 2发布中 3已发布 4已撤回 5已过期 6发布失败)
     */
    private Integer noticeStatus;

    /**
     * 原始发送目标数量
     */
    private Integer targetCount;

    /**
     * 展开去重后的实际接收人数
     */
    private Integer receiverCount;

    /**
     * 已读人数统计
     */
    private Integer readCount;

    /**
     * 评论数统计
     */
    private Integer commentCount;

    /**
     * 点赞数统计
     */
    private Integer likeCount;

    /**
     * 差评数统计
     */
    private Integer dislikeCount;

    /**
     * 跳转类型(0无 1内部路由 2外部URL 3业务详情)
     */
    private Integer linkType;

    /**
     * 跳转地址
     */
    private String linkUrl;

    /**
     * 定时发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date scheduleTime;

    /**
     * 实际发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;

    /**
     * 撤回时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date revokeTime;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

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
