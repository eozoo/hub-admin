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
package com.cowave.hub.admin.domain.sys.biz;

import com.cowave.hub.admin.domain.sys.entity.SysOperation;
import com.cowave.zoo.framework.access.operation.OperationInfo;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysOperationBiz {

    /**
     * 新增
     */
    void createOperation(OperationInfo opInfo, Object resp);

    /**
     * 保存
     */
    void save(SysOperation sysOperation);

    /**
     * 删除
     */
    void delete(List<String> ids);

    /**
     * 清空
     */
    void clean(String tenantId);
}
