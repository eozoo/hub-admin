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
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cowave.zoo.framework.support.mybatis.handler.JsonObjectHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

/**
 * 用户外部身份
 *
 * @author shanhuiming
 */
@Getter
@Setter
@TableName(autoResultMap = true)
public class SysAuthUser {

    /**
     * 外部身份id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 绑定的全局用户id
     */
    private Integer userId;

    /**
     * 身份类型：ldap、oauth
     */
    private String identityType;

    /**
     * LDAP配置id，LDAP身份使用
     */
    private Integer ldapId;

    /**
     * 认证提供方id，外部认证身份使用
     */
    private Integer providerId;

    /**
     * 外部系统稳定用户标识，如entryUUID或OAuth subject
     */
    private String externalSubject;

    /**
     * 外部系统用户账号
     */
    private String userAccount;

    /**
     * 外部身份密码，LDAP同步场景可正常保存
     */
    private String userPasswd;

    /**
     * 外部系统用户名称
     */
    private String userName;

    /**
     * 外部系统头像地址
     */
    private String userAvatar;

    /**
     * 外部系统用户电话
     */
    private String userPhone;

    /**
     * 外部系统用户邮箱
     */
    private String userEmail;

    /**
     * 外部系统岗位信息
     */
    private String userPost;

    /**
     * 外部系统部门信息
     */
    private String userDept;

    /**
     * 外部系统上级用户标识
     */
    private String userLeader;

    /**
     * 外部身份扩展原始信息
     */
    @TableField(typeHandler = JsonObjectHandler.class)
    private Map<String, Object> identityInfo;

    /**
     * 身份状态 1启用 2停用
     */
    private Integer authStatus;

    /**
     * 最近登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    /**
     * 最近同步时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastSyncTime;

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
