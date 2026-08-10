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
import com.cowave.hub.admin.domain.rbac.enums.UserSex;
import org.apache.commons.lang3.StringUtils;

import static com.cowave.hub.admin.domain.rbac.enums.UserSex.*;

/**
 * @author shanhuiming
 */
public class UserSexConverter implements Converter<UserSex> {

    @Override
    public WriteCellData<String> convertToExcelData(WriteConverterContext<UserSex> context) {
        UserSex userSex = context.getValue();
        if(userSex == null){
            return new WriteCellData<>("");
        }
        return switch(userSex) {
            case MALE -> new WriteCellData<>("男");
            case FEMALE -> new WriteCellData<>("女");
            default -> new WriteCellData<>("未知");
        };
    }

    @Override
    public UserSex convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        String userSex = cellData.getStringValue();
        if(StringUtils.isBlank(userSex)){
            return null;
        }
        return switch (userSex) {
            case "男" -> MALE;
            case "女" -> FEMALE;
            default -> UNKNOWN;
        };
    }
}
