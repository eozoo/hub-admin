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
import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.support.excel.write.ExcelIgnoreStyle;
import com.cowave.hub.admin.domain.sys.entity.HubOperation;
import com.cowave.hub.admin.domain.sys.entity.query.OperationQuery;
import com.cowave.hub.admin.service.sys.HubOperationService;
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
 * 操作日志
 * @order 16
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/oplog")
public class HubOperationController {

	private final HubOperationService hubOperationService;

	/**
	 * 列表
	 */
	@PreAuthorize("@permits.hasPermit('monitor:log:query')")
	@GetMapping
    public Response<Response.Page<HubOperation>> list(OperationQuery query) {
        return Response.success(hubOperationService.queryPage(Access.tenantId(), query, true));
    }

	/**
	 * 删除
	 *
	 * @param ids 日志id列表
	 */
	@PreAuthorize("@permits.hasPermit('monitor:log:delete')")
	@DeleteMapping("/{ids}")
	public Response<Void> delete(@PathVariable List<String> ids) {
		hubOperationService.delete(ids);
		return Response.success();
	}

	/**
	 * 清空
	 */
	@DeleteMapping("/clean")
	public Response<Void> clean() {
		hubOperationService.clean(Access.tenantId());
		return Response.success();
	}

	/**
	 * 导出
	 */
	@PreAuthorize("@permits.hasPermit('monitor:log:export')")
	@RequestMapping("/export")
	public void export(HttpServletResponse response, OperationQuery query) throws IOException {
		String fileName = URLEncoder.encode("操作日志", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		Collection<HubOperation> operationList =
				hubOperationService.queryPage(Access.tenantId(), query, false).getList();
		EasyExcel.write(response.getOutputStream(), HubOperation.class)
		.sheet("操作日志").registerWriteHandler(new ExcelIgnoreStyle()).doWrite(operationList);
	}
}
