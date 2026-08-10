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
package com.cowave.hub.admin.domain.rbac.entity.command;

import com.cowave.hub.admin.domain.rbac.entity.HubPost;
import com.cowave.zoo.framework.access.security.AccessInfoSetter;
import lombok.Getter;
import lombok.Setter;

/**
 * 岗位新增/编辑入参
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class PostCreate extends HubPost implements AccessInfoSetter {

    /**
     * 上级岗位id
     */
    private Integer parentId = 0;
}
