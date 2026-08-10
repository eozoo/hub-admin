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
package com.cowave.hub.admin.domain.rbac.enums.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.cowave.hub.admin.domain.rbac.enums.SuccessStatus;

import static com.cowave.hub.admin.domain.rbac.enums.SuccessStatus.SUCCESS;

/**
 * @author shanhuiming
 */
public class SuccessStatusConverter implements Converter<SuccessStatus> {

    @Override
    public WriteCellData<String> convertToExcelData(WriteConverterContext<SuccessStatus> context) {
        SuccessStatus successStatus = context.getValue();
        if (successStatus == null) {
            return new WriteCellData<>("");
        }
        if (SUCCESS == successStatus) {
            return new WriteCellData<>("成功");
        } else {
            return new WriteCellData<>("失败");
        }
    }
}
