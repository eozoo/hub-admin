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
package com.cowave.hub.admin.service.rbac;

import cn.hutool.core.lang.tree.Tree;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.domain.rbac.entity.SysMenu;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysMenuService {

	/**
	 * 菜单权限（管理员）
	 */
	List<SysMenu> queryMenusByAdmin(String tenantId);

	/**
	 * 菜单权限（管理员）
	 */
	List<SysMenu> queryMenusInPublic(String tenantId);

	/**
	 * 菜单权限（指定角色）
	 */
	List<SysMenu> queryMenusByRoles(String tenantId, List<String> roleList);

	/**
	 * 菜单树
	 */
	List<Tree<Integer>> queryTree(String tenantId);

	/**
	 * 列表
	 */
	List<SysMenu> list(String menuName, EnableStatus menuStatus);

	/**
	 * Api令牌权限树
	 */
	List<Tree<Integer>> queryApiPermitsByUser(String tenantId);

	/**
	 * 详情
	 */
	SysMenu info(Integer menuId);

	/**
	 * 新增
	 */
	void add(SysMenu sysMenu);

	/**
	 * 删除
	 */
	void delete(Integer menuId);

	/**
	 * 修改
	 */
	void edit(SysMenu sysMenu);

}
