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
package com.cowave.hub.admin.infra.flow.mapper;

import java.util.List;

import com.cowave.hub.admin.domain.flow.entity.FlowExecution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author shanhuiming
 */
@Mapper
public interface FlowExecutionMapper {

    /**
     * 详情
     */
    FlowExecution selectActRuExecutionById(String id);

    /**
     * 列表
     */
    List<FlowExecution> selectActRuExecutionList(FlowExecution actRuExecution);

    /**
     * 名称查询
     */
    List<FlowExecution> selectActRuExecutionListByProcessName(@Param("name") String name);

    /**
     * 新增
     */
    int insertActRuExecution(FlowExecution actRuExecution);

    /**
     * 修改
     */
    int updateActRuExecution(FlowExecution actRuExecution);

    /**
     * 删除
     */
    int deleteActRuExecutionById(String id);

    /**
     * 批量删除
     */
    int deleteActRuExecutionByIds(String[] ids);
}
