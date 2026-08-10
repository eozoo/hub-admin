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
package com.cowave.hub.admin.domain.rbac.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.domain.rbac.enums.converter.EnableStatusConverter;
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
public class HubPost {

    /**
     * 岗位id
     */
    @TableId(type = IdType.AUTO)
    private Integer postId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 岗位编码
     */
    @ColumnWidth(50)
	@ExcelProperty(value = "岗位编码")
    private String postCode;

    /**
     * 岗位类型
     */
    @ExcelProperty(value = "岗位类型")
    private String postType;

    /**
     * 岗位名称
     */
    @NotBlank(message = "{admin.post.name.null}")
	@ExcelProperty(value = "岗位名称")
    private String postName;

    /**
     * 岗位级别
     */
    private Integer postLevel;

    /**
     * 岗位状态
     */
    @ExcelProperty(value = "岗位状态", converter = EnableStatusConverter.class)
    private EnableStatus postStatus;

    /**
     * 备注
     */
    @ExcelProperty("备注")
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
