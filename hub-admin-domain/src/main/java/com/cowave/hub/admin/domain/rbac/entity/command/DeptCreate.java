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

import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.security.AccessInfoSetter;
import com.cowave.zoo.tools.Collections;
import com.cowave.hub.admin.domain.rbac.entity.HubDept;
import com.cowave.hub.admin.domain.rbac.entity.HubDeptDiagram;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class DeptCreate extends HubDept implements AccessInfoSetter {

    /**
	 * 上级部门Id列表
	 */
	@NotEmpty(message = "{admin.dept.parentIds.null}")
	private List<Integer> parentIds;

    public List<HubDeptDiagram> getDeptParents(){
        if(CollectionUtils.isNotEmpty(parentIds)){
            return Collections.copyToList(parentIds, parentId -> new HubDeptDiagram(getDeptId(), parentId, Access.tenantId()));
        }else{
            return List.of(new HubDeptDiagram(getDeptId(), 0, Access.tenantId()));
        }
    }
}
