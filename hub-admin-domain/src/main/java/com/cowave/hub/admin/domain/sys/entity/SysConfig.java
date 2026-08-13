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
package com.cowave.hub.admin.domain.sys.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cowave.zoo.framework.access.security.AccessInfoSetter;
import com.cowave.hub.admin.domain.rbac.enums.YesNo;
import com.cowave.hub.admin.domain.rbac.enums.converter.YesNoConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@ColumnWidth(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Getter
@Setter
public class SysConfig implements AccessInfoSetter {

    /**
     * 配置id
     */
    @TableId(type = IdType.AUTO)
    @ExcelProperty(value = "配置id")
    private Integer configId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 配置名称
     */
    @ExcelProperty(value = "配置名称")
    @NotBlank(message = "{admin.config.name.null}")
    private String configName;

    /**
     * 配置key
     */
    @ColumnWidth(40)
    @ExcelProperty(value = "配置key")
    @NotBlank(message = "{admin.config.key.null}")
    private String configKey;

    /**
     * 配置值
     */
    @ExcelProperty(value = "配置值")
    @NotBlank(message = "{admin.config.value.null}")
    private String configValue;

    /**
     * 值类型
     */
    @ExcelProperty(value = "值类型")
    private String valueType;

    /**
     * 值转换器
     */
    @ColumnWidth(60)
    @ExcelProperty(value = "值转换器")
    private String valueParser;

    /**
     * 是否默认
     */
    @ExcelProperty(value = "系统内置", converter = YesNoConverter.class)
    private YesNo isDefault;

    /**
     * 备注
     */
    @ColumnWidth(80)
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
