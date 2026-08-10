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
import com.cowave.hub.admin.domain.rbac.entity.HubMenu;
import com.cowave.hub.admin.service.rbac.HubMenuService;
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
public class HubMenuController {

	private final HubMenuService hubMenuService;

	/**
	 * 菜单树
	 */
	@GetMapping("/tree")
	public Response<List<Tree<Integer>>> tree(){
		return Response.success(hubMenuService.queryTree(Access.tenantId()));
	}

	/**
	 * 列表
	 * @param menuName 菜单名称
	 * @param menuStatus 菜单状态
	 */
	@PreAuthorize("@permits.hasPermit('hub:menu:query')")
	@GetMapping
	public Response<Response.Page<HubMenu>> list(String menuName, EnableStatus menuStatus){
		return Response.page(hubMenuService.list(menuName, menuStatus));
	}

	/**
     * 详情
     *
     * @param menuId 菜单id
     */
	@PreAuthorize("@permits.hasPermit('hub:menu:query')")
    @GetMapping("/{menuId}")
    public Response<HubMenu> info(@PathVariable Integer menuId) {
        return Response.success(hubMenuService.info(menuId));
    }

    /**
     * 新增
     */
	@PreAuthorize("@permits.hasPermit('hub:menu:create')")
    @PostMapping
    public Response<Void> add(@Validated @RequestBody HubMenu hubMenu) {
		hubMenuService.add(hubMenu);
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
        hubMenuService.delete(menuId);
        return Response.success();
    }

    /**
     * 修改
     */
	@PreAuthorize("@permits.hasPermit('hub:menu:edit')")
    @PatchMapping
    public Response<Void> edit(@Validated @RequestBody HubMenu hubMenu) {
        hubMenuService.edit(hubMenu);
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
		List<HubMenu> menuList = hubMenuService.list(null, null);
		EasyExcel.write(response.getOutputStream(), HubMenu.class)
		.sheet("菜单").registerWriteHandler(new ExcelIgnoreStyle()).doWrite(menuList);
	}
}
