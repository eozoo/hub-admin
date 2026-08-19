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
package com.cowave.hub.admin.domain.home.repository.facade;

import com.cowave.hub.admin.domain.home.entity.HubMember;

import java.util.List;

/**
 * HubMember聚合根Query操作
 *
 * @see HubMember
 *
 * @author shanhuiming
 */
public interface HubMemberRepositoryFacade {

    /**
     * 按编码查询
     */
    HubMember queryByCode(String memberCode);

    /**
     * 查询角色id列表
     */
    List<Integer> queryMemberRoleIdsByMemberId(Integer memberId);
}
