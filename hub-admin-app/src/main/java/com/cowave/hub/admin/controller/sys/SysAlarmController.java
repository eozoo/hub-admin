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
import com.cowave.zoo.framework.support.excel.write.ExcelIgnoreStyle;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmPto;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmTypePto;
import com.cowave.hub.admin.domain.sys.entity.command.AlarmHandles;
import com.cowave.hub.admin.service.sys.SysAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 系统告警
 * @order 17
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/alarm")
public class SysAlarmController {

	private final SysAlarmService alarmService;

	/**
     * 类型列表
     */
	@PostMapping(value = "/type/list")
    public Response<Response.Page<AlarmTypePto>> typeList(@RequestBody AlarmTypePto alarmTypePto) {
        return Response.page(alarmService.typeList(alarmTypePto));
    }

	/**
     * 类型新增
     */
	@PostMapping(value = "/type/add")
    public Response<Void> typeAdd(@Validated @RequestBody AlarmTypePto alarmTypePto) {
		alarmService.typeAdd(alarmTypePto);
        return Response.success();
    }

    /**
     * 类型修改
     */
	@PostMapping(value = "/type/edit")
    public Response<Void> typeEdit(@Validated @RequestBody AlarmTypePto alarmTypePto) {
		alarmService.typeEdit(alarmTypePto);
        return Response.success();
    }

    /**
     * 类型删除
     */
	@GetMapping(value = "/type/delete")
    public Response<Void> typeDelete(Long id) {
		alarmService.typeDelete(id);
        return Response.success();
    }

	/**
	 * 列表
	 */
	@PostMapping("/list")
    public Response<Response.Page<AlarmPto>> list(@RequestBody AlarmPto alarmPto) {
        return Response.page(alarmService.list(alarmPto));
    }

	/**
	 * 详情
	 *
	 * @param id 告警id
	 */
	@GetMapping(value = "/info")
    public Response<AlarmPto> info(@NotNull(message = "{admin.alarm.id.null}") Long id) {
        return Response.success(alarmService.info(id));
    }

	/**
	 * 删除
	 *
	 * @param id 告警id
	 */
	@GetMapping("/delete")
	public Response<Void> delete(@NotNull(message = "{admin.alarm.id.null}") Long id) {
		alarmService.delete(id);
		return Response.success();
	}

	/**
	 * 导出
	 */
	@PostMapping("/export")
	public void export(HttpServletResponse response, AlarmPto alarmPto) throws IOException {
		String fileName = URLEncoder.encode("系统告警", StandardCharsets.UTF_8).replace("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		EasyExcel.write(response.getOutputStream(), AlarmPto.class)
		.sheet("系统告警").registerWriteHandler(new ExcelIgnoreStyle()).doWrite(alarmService.list(alarmPto).getRecords());
	}

	/**
     * 告警处理
     */
    @PostMapping("/handle")
    public Response<Void> handle(@Validated @RequestBody AlarmHandles alarmHandles) {
        alarmService.handle(alarmHandles);
        return Response.success();
    }
}
