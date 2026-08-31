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
package com.cowave.hub.admin.domain.auth2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户本地密码凭据及历史记录
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysUserPasswd {

    /**
     * 密码记录id
     */
    @TableId(type = IdType.AUTO)
    private Long passwdId;

    /**
     * 全局用户id
     */
    private Integer userId;

    /**
     * 密码哈希值，不保存明文密码
     */
    private String passwdHash;

    /**
     * 密码哈希算法，如bcrypt、argon2
     */
    private String passwdAlgo;

    /**
     * 是否当前有效密码 1是 0否
     */
    private Integer isCurrent;

    /**
     * 是否需要修改密码 1是 0否，首次创建或管理员重置时置1
     */
    private Integer needChange;

    /**
     * 密码生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date effectiveTime;

    /**
     * 密码到期时间，空表示不过期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

    /**
     * 密码失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date invalidTime;

    /**
     * 密码来源：initial首次创建、self用户修改、admin管理员重置、recovery找回密码
     */
    private String changeSource;

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
