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
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import org.apache.commons.lang3.StringUtils;

import static com.cowave.hub.admin.domain.rbac.enums.EnableStatus.DISABLE;
import static com.cowave.hub.admin.domain.rbac.enums.EnableStatus.ENABLE;

/**
 * @author shanhuiming
 */
public class EnableStatusConverter implements Converter<EnableStatus> {

    @Override
    public WriteCellData<String> convertToExcelData(WriteConverterContext<EnableStatus> context) {
        EnableStatus enableStatus = context.getValue();
        if (enableStatus == null) {
            return new WriteCellData<>("");
        }
        if (ENABLE == enableStatus) {
            return new WriteCellData<>("启用");
        } else {
            return new WriteCellData<>("停用");
        }
    }

    @Override
    public EnableStatus convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String enableStatus = cellData.getStringValue();
        if (StringUtils.isBlank(enableStatus)) {
            return null;
        }
        if ("启用".equals(enableStatus)) {
            return ENABLE;
        } else {
            return DISABLE;
        }
    }
}
