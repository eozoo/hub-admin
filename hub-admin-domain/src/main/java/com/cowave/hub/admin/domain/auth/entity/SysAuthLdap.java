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
package com.cowave.hub.admin.domain.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cowave.zoo.framework.access.security.AccessInfoSetter;
import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;
import com.cowave.zoo.framework.support.mybatis.handler.JsonObjectHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

/**
 * LDAP认证配置
 *
 * @author shanhuiming
 */
@Getter
@Setter
@TableName(autoResultMap = true)
public class SysAuthLdap implements AccessInfoSetter {

    /**
     * LDAP配置id
     */
    @TableId(type = IdType.AUTO)
    private Integer ldapId;

    /**
     * LDAP状态
     */
    private EnableStatus ldapStatus;

    /**
     * LDAP地址
     */
    private String ldapUrl;

    /**
     * LDAP绑定用户
     */
    private String ldapUser;

    /**
     * LDAP绑定密码
     */
    private String ldapPasswd;

    /**
     * 基础搜索DN
     */
    private String baseDn;

    /**
     * 是否只读连接 0否 1是
     */
    private Integer readonly;

    /**
     * 用户搜索DN
     */
    private String userDn;

    /**
     * 用户对象类
     */
    private String userClass;

    /**
     * 用户名属性
     */
    private String accountProperty;

    /**
     * LDAP稳定身份属性，如AD的objectGUID 或 OpenLDAP的entryUUID
     */
    private String subjectProperty;

    /**
     * 姓名属性
     */
    private String nameProperty;

    /**
     * 邮箱属性
     */
    private String emailProperty;

    /**
     * 电话属性
     */
    private String phoneProperty;

    /**
     * 岗位属性
     */
    private String postProperty;

    /**
     * 部门属性
     */
    private String deptProperty;

    /**
     * 上级用户属性
     */
    private String leaderProperty;

    /**
     * 用户信息属性
     */
    private String infoProperty;

    /**
     * LDAP环境属性
     */
    @TableField(typeHandler = JsonObjectHandler.class)
    private Map<String, Object> environment;

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
