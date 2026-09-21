/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cowave.hub.admin.domain.notice.repository.facade;

/**
 * @author shanhuiming
 */
public interface SysNoticeReceiverRepositoryFacade {

    /**
     * 查询未读通知数
     */
    long countUnread(Integer tenantId, Integer userId);
}
