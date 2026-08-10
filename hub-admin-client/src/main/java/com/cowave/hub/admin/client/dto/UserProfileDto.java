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
package com.cowave.hub.admin.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Data
public class UserProfileDto {

    private String tenantId;

    private String tenantName;

    private Integer userId;

    private String userCode;

    private String userType;

    private String userName;

    private String userAccount;

    private String userSex;

    private String avatar;

    private String mfa;

    private String userPhone;

    private String userEmail;

    private List<String> depts = new ArrayList<>();

    private List<String> roles = new ArrayList<>();

    private List<String> parents = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
