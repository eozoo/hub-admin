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
package com.cowave.hub.admin.domain.sys.entity.pto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cowave.hub.admin.domain.sys.entity.SysDict;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class DictPto extends SysDict {

    /**
     * 分组编码
     */
	@ExcelProperty("分组码")
    private String groupCode;

	/**
	 * 分组名称
	 */
    @ExcelProperty("分组名称")
	private String groupName;

    /**
     * 类型编码
     */
	@ExcelProperty("类型码")
    private String typeCode;

	/**
	 * 类型名称
	 */
    @ExcelProperty("类型名称")
	private String typeName;
}
