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

import com.cowave.hub.admin.domain.sys.entity.vo.SelectOptionVo;
import com.cowave.hub.admin.service.sys.SysModuleService;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.http.client.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * 领域模块
 * @order 8
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/module")
public class SysModuleController {

	private final SysModuleService moduleService;

	/**
	 * 领域模块树
	 */
	@GetMapping("/tree")
	public Response<Collection<SelectOptionVo>> tree() {
		return Response.success(moduleService.queryModuleTree(Access.tenantId()));
	}
}
