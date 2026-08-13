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
import com.cowave.hub.admin.domain.sys.entity.command.DictCreate;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.support.excel.write.ExcelIgnoreStyle;
import com.cowave.hub.admin.domain.sys.entity.SysDict;
import com.cowave.hub.admin.domain.sys.entity.pto.DictPto;
import com.cowave.hub.admin.domain.sys.entity.query.DictQuery;
import com.cowave.hub.admin.domain.sys.entity.vo.SelectOptionVo;
import com.cowave.hub.admin.service.sys.SysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
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
	 * 列表
	 */
	@PreAuthorize("@permits.hasPermit('hub:dict:query')")
	@GetMapping
	public Response<List<DictPto>> list(DictQuery query) {
		return Response.success(dictService.queryList(query));
	}

	/**
	 * 详情
	 */
	@PreAuthorize("@permits.hasPermit('hub:dict:query')")
	@GetMapping("/{dictId}")
	public Response<DictPto> info(@PathVariable Long dictId) {
		return Response.success(dictService.info(dictId));
	}

	/**
	 * 新增
	 */
	@PreAuthorize("@permits.hasPermit('hub:dict:create')")
	@PostMapping
	public Response<Void> create(@RequestBody DictCreate dictCreate) {
		dictService.add(dictCreate);
		return Response.success();
	}

	/**
	 * 删除
	 */
	@PreAuthorize("@permits.hasPermit('hub:dict:delete')")
	@DeleteMapping("/{dictIds}")
	public Response<Void> delete(@PathVariable List<Integer> dictIds) {
		dictService.delete(dictIds);
		return Response.success();
	}

	/**
	 * 修改
	 */
	@PreAuthorize("@permits.hasPermit('hub:dict:edit')")
	@PatchMapping
	public Response<Void> edit(@RequestBody DictCreate dictCreate) {
		dictService.edit(dictCreate);
		return Response.success();
	}

	/**
	 * 导出字典
	 */
	@PreAuthorize("@permits.hasPermit('hub:dict:export')")
	@PostMapping ("/export")
	public void export(HttpServletResponse response) throws IOException {
		String fileName = URLEncoder.encode("字典数据", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), DictPto.class)
		.sheet("字典数据").registerWriteHandler(new ExcelIgnoreStyle()).doWrite(dictService.queryList(new DictQuery()));
	}

	/**
	 * 获取字典
	 */
	@GetMapping("/code/{dictCode}")
	public Response<SysDict> getByCode(@PathVariable String dictCode) {
		return Response.success(dictService.queryByCode(dictCode));
	}

	/**
	 * 获取类型字典
	 */
	@GetMapping("/type/{typeCode}")
	public Response<List<SysDict>> listByType(@PathVariable String typeCode) {
		return Response.success(dictService.queryListByType(typeCode));
	}

	/**
	 * 获取分组字典
	 */
	@GetMapping("/group/{groupCode}")
	public Response<List<SysDict>> listByGroup(@PathVariable String groupCode) {
		return Response.success(dictService.queryListByGroup(groupCode));
	}

	/**
	 * 获取分组类型
	 */
	@GetMapping("/group/types/{groupCode}")
	public Response<Collection<SelectOptionVo>> listTypeByGroup(@PathVariable String groupCode) {
		return Response.success(dictService.queryListTypeByGroup(groupCode));
	}
}
