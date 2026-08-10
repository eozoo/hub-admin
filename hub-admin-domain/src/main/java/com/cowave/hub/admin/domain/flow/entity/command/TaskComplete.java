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
package com.cowave.hub.admin.domain.flow.entity.command;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 完成任务
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class TaskComplete {

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务变量
     */
    Map<String, Object> variables;

    /**
     * 任务备注
     */
    private String comment;
}
