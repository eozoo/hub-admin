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
package com.cowave.hub.admin.service.flow;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.flow.entity.command.MeetingCreate;
import com.cowave.hub.admin.domain.flow.entity.pto.MeetingInfoPto;
import com.cowave.hub.admin.domain.flow.entity.query.MeetingQuery;

/**
 * @author shanhuiming
 */
public interface MeetingFlowService {

    /**
     * 列表
     */
    Page<MeetingInfoPto> list(MeetingQuery query);

    /**
     * 详情
     */
    MeetingInfoPto info(String id);

    /**
     * 新增
     */
    void add(MeetingCreate cmd);

    /**
     * 修改
     */
    void edit(MeetingCreate cmd);

    /**
     * 删除
     */
    void delete(String[] ids);
}
