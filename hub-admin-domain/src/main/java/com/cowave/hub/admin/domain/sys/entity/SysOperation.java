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
import com.cowave.zoo.framework.access.operation.OperationInfo;
import com.cowave.zoo.framework.access.security.AccessInfo;
import com.cowave.zoo.framework.helper.es.HitEntity;
import com.cowave.zoo.framework.support.excel.DateConverter;
import com.cowave.hub.admin.domain.rbac.enums.SuccessStatus;
import com.cowave.hub.admin.domain.rbac.enums.converter.SuccessStatusConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.cowave.hub.admin.domain.rbac.enums.SuccessStatus.FAILED;
import static com.cowave.hub.admin.domain.rbac.enums.SuccessStatus.SUCCESS;

/**
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@ColumnWidth(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@NoArgsConstructor
@Getter
@Setter
public class SysOperation implements HitEntity {

    public static final String INDEX_NAME = "sys_operation_log";

    /**
     * 日志id
     */
    private String id;

    /**
     * 操作模块
     */
    @ExcelProperty("操作模块")
    private String opModule;

    /**
     * 操作类型
     */
    @ExcelProperty("操作类型")
    private String opType;

    /**
     * 操作动作
     */
    @ExcelProperty("操作动作")
    private String opAction;

    /**
     * 操作时间
     */
    @ExcelProperty(value = "操作时间", converter = DateConverter.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date opTime;

    /**
     * 操作状态
     */
    @ExcelProperty(value = "操作状态", converter = SuccessStatusConverter.class)
    private SuccessStatus opStatus;

    /**
     * 访问ip
     */
    @ExcelProperty("访问ip")
    private String ip;

    /**
     * 访问url
     */
    @ExcelProperty("访问url")
    private String url;

    /**
     * 请求内容
     */
    private Object request;

    /**
     * 操作描述
     */
    @ExcelProperty("操作描述")
    private String opDesc;

    /**
     * 请求内容
     */
    private Object opContent;

    /**
     * 响应内容
     */
    private Object response;

    /**
     * 访问信息
     */
    private AccessInfo access;

    public SysOperation(OperationInfo opInfo) {
        this.opModule = opInfo.getOpModule();
        this.opType = opInfo.getOpType();
        this.opAction = opInfo.getOpAction();
        this.opDesc = opInfo.getDesc();
        this.opContent = opInfo.getOpContent();
        this.opStatus = opInfo.isSuccess() ? SUCCESS : FAILED;
    }
}
