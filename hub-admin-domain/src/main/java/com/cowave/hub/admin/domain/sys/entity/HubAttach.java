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
import com.cowave.zoo.framework.access.security.AccessInfoSetter;
import com.cowave.hub.admin.domain.rbac.enums.YesNo;
import com.cowave.hub.admin.domain.sys.enums.AttachType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.cowave.hub.admin.domain.rbac.enums.YesNo.NO;

/**
 * @author shanhuiming
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HubAttach implements AccessInfoSetter {

    /**
     * 附件id
     */
    @TableId(type = IdType.AUTO)
    private Long attachId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 宿主id
     */
    private String ownerId;

    /**
     * 宿主模块
     */
    private String ownerModule;

    /**
     * 附件类型
     */
    private AttachType attachType;

    /**
     * 附件名称
     */
    private String attachName;

    /**
     * 附件大小
     */
    private Long attachSize;

    /**
     * 附件路径
     */
    private String attachPath;

    /**
     * 是否私有的
     */
    private YesNo isPrivate = NO;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

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

    /**
     * 预览地址
     */
    @TableField(exist = false)
    private String viewUrl;

    /**
     * 宿主名称
     */
    @TableField(exist = false)
    private String ownerName;
}
