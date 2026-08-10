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
package com.cowave.hub.admin.domain.sys.entity.command;

import com.cowave.hub.admin.domain.rbac.enums.YesNo;
import com.cowave.hub.admin.domain.sys.enums.AttachType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * @author shanhuiming
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AttachUpload {

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
     * 是否私有的
     */
    private YesNo isPrivate;
}
