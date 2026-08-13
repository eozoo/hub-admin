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
package com.cowave.hub.admin.domain.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cowave.hub.admin.domain.sys.enums.NoticeLevel;
import com.cowave.hub.admin.domain.sys.enums.NoticeStatus;
import com.cowave.hub.admin.domain.sys.enums.NoticeType;
import com.cowave.hub.admin.domain.rbac.enums.YesNo;
import com.cowave.zoo.framework.support.mybatis.handler.ArrayListHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

import static com.cowave.hub.admin.domain.sys.enums.NoticeStatus.DRAFT;

/**
 * @author shanhuiming
 */
@Getter
@Setter
@TableName(autoResultMap = true)
public class SysNotice {

    /**
     * 公告id
     */
    @TableId(type = IdType.AUTO)
    private Long noticeId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 公告标题
     */
    @NotBlank(message = "{admin.notice.title.null}")
    private String noticeTitle;

    /**
     * 公告状态
     */
    private NoticeStatus noticeStatus = DRAFT;

    /**
     * 公告类型
     */
    private NoticeType noticeType;

    /**
     * 公告等级
     */
    private NoticeLevel noticeLevel;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 是否系统公告
     */
    private YesNo isSystem;

    /**
     * 总人数
     */
    private Integer statTotal;

    /**
     * 已读人数
     */
    private Integer statRead;

    /**
     * 是否全员
     */
    private YesNo goalsAll;

    /**
     * 目标单位
     */
    @TableField(typeHandler = ArrayListHandler.class)
    private List<Integer> goalsDept;

    /**
     * 目标角色
     */
    @TableField(typeHandler = ArrayListHandler.class)
    private List<Integer> goalsRole;

    /**
     * 目标用户
     */
    @TableField(typeHandler = ArrayListHandler.class)
    private List<Integer> goalsUser;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date publishTime;

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
