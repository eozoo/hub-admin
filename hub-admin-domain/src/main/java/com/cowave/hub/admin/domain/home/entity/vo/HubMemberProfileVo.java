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
package com.cowave.hub.admin.domain.home.entity.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class HubMemberProfileVo {

    /**
     * 会员id
     */
    private Integer memberId;

    /**
     * 会员编码
     */
    private String memberCode;

    /**
     * 会员类型
     */
    private String memberType;

    /**
     * 会员名称
     */
    private String memberName;

    /**
     * 会员账号
     */
    private String memberAccount;

    /**
     * 会员邮箱
     */
    private String memberEmail;

    /**
     * 会员头像
     */
    private String memberAvatar;

    /**
     * 个性签名
     */
    private String memberSign;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 租户名称
     */
    private String tenantName;
}
