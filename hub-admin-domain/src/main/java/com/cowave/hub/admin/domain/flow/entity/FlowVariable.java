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
package com.cowave.hub.admin.domain.flow.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class FlowVariable {

    /**
     * 流程id
     */
    private String processId;

    /**
     * 执行
     */
    private String executionId;

    /**
     * 变量id
     */
    private String variableId;

    /**
     * 变量名称
     */
    private String variableName;

    /**
     * 变量类型
     */
    private String variableTypeName;

    /**
     * 变量值
     */
    private Object value;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdatedTime;

    /**
     * 修改按钮控制
     */
    private boolean edit;
}
