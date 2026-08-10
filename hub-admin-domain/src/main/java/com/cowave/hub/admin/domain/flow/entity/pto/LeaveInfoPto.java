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
package com.cowave.hub.admin.domain.flow.entity.pto;

import com.cowave.hub.admin.domain.flow.entity.FlowLeave;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class LeaveInfoPto extends FlowLeave {

    /**
     * 请假人姓名
     */
    private String applyUserName;

    /**
     * 流程变量
     */
    private Map<String, Object> processVariables;

    /**
     * 流程任务节点
     */
    private String processTask;

    /**
     * 流程任务id
     */
    private String taskId;

    /**
     * 节点办理人
     */
    private String processTaskUser;
}
