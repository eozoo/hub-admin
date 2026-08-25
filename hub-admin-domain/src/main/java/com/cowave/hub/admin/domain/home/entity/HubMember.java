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
package com.cowave.hub.admin.domain.home.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.domain.rbac.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class HubMember {

    /**
     * 会员id
     */
    @TableId(type = IdType.AUTO)
    private Integer memberId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 会员类型（授权服务类型 gitlab/qq/wechat）
     */
    private UserType memberType;

    /**
     * 会员编码
     */
    private String memberCode;

    /**
     * 会员账号
     */
    private String memberAccount;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 会员头像
     */
    private String memberAvatar;

    /**
     * 会员邮箱
     */
    private String memberEmail;

    /**
     * 个性签名
     */
    private String memberSign;

    /**
     * 会员状态
     */
    private EnableStatus memberStatus;

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
