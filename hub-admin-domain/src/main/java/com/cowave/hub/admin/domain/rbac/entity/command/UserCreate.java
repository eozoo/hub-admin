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
import com.cowave.hub.admin.domain.rbac.entity.SysUser;
import com.cowave.hub.admin.domain.rbac.entity.SysUserDept;
import com.cowave.hub.admin.domain.rbac.entity.SysUserRole;
import com.cowave.hub.admin.domain.rbac.entity.SysUserDiagram;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class UserCreate extends SysUser implements AccessInfoSetter {

    /**
	 * 角色id列表
	 */
	private List<Integer> roleIds;

    /**
	 * 上级用户id列表
	 */
	private List<Integer> parentIds;

    /**
	 * 部门-岗位id列表
	 */
	private List<String> deptPostIds;

    public List<SysUserRole> getUserRoles(){
        return Collections.copyToList(roleIds, roleId -> new SysUserRole(getUserId(), roleId));
    }

    public List<SysUserDiagram> getUserParents(){
        if(CollectionUtils.isNotEmpty(parentIds)){
            return Collections.copyToList(parentIds, parentId -> new SysUserDiagram(getUserId(), parentId, Access.tenantId()));
        }else{
            return List.of(new SysUserDiagram(getUserId(), 0, Access.tenantId()));
        }
    }

    public List<SysUserDept> getUserDeptPosts(){
        return Collections.copyToList(deptPostIds, v -> {
				String[] arr = v.split("-");
				return new SysUserDept(getUserId(), Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
			});
    }
}
