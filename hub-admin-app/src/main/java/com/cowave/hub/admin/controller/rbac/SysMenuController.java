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
package com.cowave.hub.admin.controller.rbac;

import cn.hutool.core.lang.tree.Tree;
import com.alibaba.excel.EasyExcel;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.support.excel.write.ExcelIgnoreStyle;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.domain.rbac.entity.SysMenu;
import com.cowave.hub.admin.service.rbac.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 菜单
 * @order 6
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/menu")
public class SysMenuController {

	private final SysMenuService menuService;

	/**
	 * 菜单树
	 */
	@GetMapping("/tree")
	public Response<List<Tree<Integer>>> tree(){
		return Response.success(menuService.queryTree(Access.tenantId()));
	}

	/**
	 * 列表
	 * @param menuName 菜单名称
	 * @param menuStatus 菜单状态
	 */
	@PreAuthorize("@permits.hasPermit('hub:menu:query')")
	@GetMapping
	public Response<Response.Page<SysMenu>> list(String menuName, EnableStatus menuStatus){
		return Response.page(menuService.list(menuName, menuStatus));
	}

	/**
     * 详情
     *
     * @param menuId 菜单id
     */
	@PreAuthorize("@permits.hasPermit('hub:menu:query')")
    @GetMapping("/{menuId}")
    public Response<SysMenu> info(@PathVariable Integer menuId) {
        return Response.success(menuService.info(menuId));
    }

    /**
     * 新增
     */
	@PreAuthorize("@permits.hasPermit('hub:menu:create')")
    @PostMapping
    public Response<Void> add(@Validated @RequestBody SysMenu sysMenu) {
		menuService.add(sysMenu);
        return Response.success();
    }

	/**
     * 删除
     *
     * @param menuId 菜单id
     */
	@PreAuthorize("@permits.hasPermit('hub:menu:delete')")
    @DeleteMapping("/{menuId}")
    public Response<Void> delete(@PathVariable Integer menuId) {
        menuService.delete(menuId);
        return Response.success();
    }

    /**
     * 修改
     */
	@PreAuthorize("@permits.hasPermit('hub:menu:edit')")
    @PatchMapping
    public Response<Void> edit(@Validated @RequestBody SysMenu sysMenu) {
        menuService.edit(sysMenu);
        return Response.success();
    }

	/**
	 * 导出菜单
	 */
	@PreAuthorize("@permits.hasPermit('hub:menu:export')")
	@PostMapping("/export")
	public void export(HttpServletResponse response) throws IOException {
		String fileName = URLEncoder.encode("菜单数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		List<SysMenu> menuList = menuService.list(null, null);
		EasyExcel.write(response.getOutputStream(), SysMenu.class)
		.sheet("菜单").registerWriteHandler(new ExcelIgnoreStyle()).doWrite(menuList);
	}
}
