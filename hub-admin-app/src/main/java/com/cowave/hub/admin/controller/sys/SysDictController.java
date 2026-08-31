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
package com.cowave.hub.admin.controller.sys;

import com.alibaba.excel.EasyExcel;
import com.cowave.hub.admin.domain.sys.entity.SysDict;
import com.cowave.hub.admin.domain.sys.entity.SysDictType;
import com.cowave.hub.admin.domain.sys.entity.command.DictCreate;
import com.cowave.hub.admin.domain.sys.entity.command.DictTypeCreate;
import com.cowave.hub.admin.domain.sys.entity.pto.DictPto;
import com.cowave.hub.admin.service.sys.SysDictService;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.http.client.response.Response;
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
 * 字典
 * @order 7
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dict")
public class SysDictController {

	private final SysDictService dictService;

	/**
	 * 获取类型字典
	 */
	@GetMapping("/type/{typeCode}")
	public Response<List<SysDict>> listByType(@PathVariable String typeCode) {
		return Response.success(dictService.queryListByType(Access.tenantId(), typeCode));
	}

	/**
	 * 类型列表
	 */
	@PreAuthorize("@permits.hasPermit('sys:dict:query')")
	@GetMapping("/type")
	public Response<Response.Page<SysDictType>> listType(String moduleCode,
														 @RequestParam(defaultValue = "1") Integer pageNum,
														 @RequestParam(defaultValue = "10") Integer pageSize) {
		return Response.page(dictService.queryTypePageByModule(Access.tenantId(), moduleCode, pageNum, pageSize));
	}

	/**
	 * 新增类型
	 */
	@PreAuthorize("@permits.hasPermit('sys:dict:create')")
	@PostMapping("/type")
	public Response<Void> createType(@RequestBody DictTypeCreate typeCreate) {
		dictService.addType(Access.tenantId(), typeCreate);
		return Response.success();
	}

	/**
	 * 修改类型
	 */
	@PreAuthorize("@permits.hasPermit('sys:dict:edit')")
	@PatchMapping("/type")
	public Response<Void> editType(@RequestBody DictTypeCreate typeCreate) {
		dictService.editType(Access.tenantId(), typeCreate);
		return Response.success();
	}

	/**
	 * 删除类型
	 */
	@PreAuthorize("@permits.hasPermit('sys:dict:delete')")
	@DeleteMapping("/type/{typeIds}")
	public Response<Void> deleteType(@PathVariable List<Integer> typeIds) {
		dictService.deleteType(Access.tenantId(), typeIds);
		return Response.success();
	}

	/**
	 * 修改类型状态
	 */
	@PreAuthorize("@permits.hasPermit('sys:dict:edit')")
	@PatchMapping("/type/status")
	public Response<Void> updateTypeStatus(@RequestParam Integer typeId, @RequestParam Integer status) {
		dictService.updateTypeStatus(Access.tenantId(), typeId, status);
		return Response.success();
	}

	/**
	 * 导出字典
	 */
	@PreAuthorize("@permits.hasPermit('sys:dict:export')")
	@PostMapping("/export")
	public void export(HttpServletResponse response, String typeCode, String moduleCode) throws IOException {
		String fileName = URLEncoder.encode("字典数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), DictPto.class)
				.sheet("字典数据").doWrite(dictService.queryList(Access.tenantId(), typeCode, moduleCode));
	}

	/**
	 * 获取字典
	 */
	@GetMapping("/code/{dictCode}")
	public Response<SysDict> getByCode(@PathVariable String dictCode) {
		return Response.success(dictService.queryByCode(Access.tenantId(), dictCode));
	}

	/**
	 * 字典列表
	 */
	@PreAuthorize("@permits.hasPermit('sys:dict:query')")
	@GetMapping("/list")
	public Response<List<DictPto>> list(String typeCode, String moduleCode) {
		return Response.success(dictService.queryList(Access.tenantId(), typeCode, moduleCode));
	}

	/**
	 * 字典详情
	 */
	@PreAuthorize("@permits.hasPermit('sys:dict:query')")
	@GetMapping("/{dictId}")
	public Response<DictPto> info(@PathVariable Long dictId) {
		return Response.success(dictService.info(Access.tenantId(), dictId));
	}

	/**
	 * 新增字典
	 */
	@PreAuthorize("@permits.hasPermit('sys:dict:create')")
	@PostMapping
	public Response<Void> create(@RequestBody DictCreate dictCreate) {
		dictService.add(Access.tenantId(), dictCreate);
		return Response.success();
	}

	/**
	 * 修改字典
	 */
	@PreAuthorize("@permits.hasPermit('sys:dict:edit')")
	@PatchMapping
	public Response<Void> edit(@RequestBody DictCreate dictCreate) {
		dictService.edit(Access.tenantId(), dictCreate);
		return Response.success();
	}

	/**
	 * 删除字典
	 */
	@PreAuthorize("@permits.hasPermit('sys:dict:delete')")
	@DeleteMapping("/{dictIds}")
	public Response<Void> delete(@PathVariable List<Long> dictIds) {
		dictService.delete(Access.tenantId(), dictIds);
		return Response.success();
	}

	/**
	 * 修改字典状态
	 */
	@PreAuthorize("@permits.hasPermit('sys:dict:edit')")
	@PatchMapping("/status")
	public Response<Void> updateStatus(@RequestParam Long dictId, @RequestParam Integer status) {
		dictService.updateDictStatus(Access.tenantId(), dictId, status);
		return Response.success();
	}
}
