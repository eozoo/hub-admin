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
package com.cowave.hub.admin.domain.auth.entity.pto;

import com.cowave.hub.admin.domain.rbac.enums.UserSex;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class UserProfile {

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户编码
     */
    private String userCode;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户性别
     */
    private UserSex userSex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 二次认证密钥
     */
    private String mfa;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 部门/岗位
     */
    private List<String> depts = new ArrayList<>();

    /**
     * 用户角色
     */
    private List<String> roles = new ArrayList<>();

    /**
     * 汇报对象
     */
    private List<String> parents = new ArrayList<>();

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
