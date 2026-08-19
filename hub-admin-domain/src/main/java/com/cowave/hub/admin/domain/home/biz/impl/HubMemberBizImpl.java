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
package com.cowave.hub.admin.domain.home.biz.impl;

import com.cowave.hub.admin.domain.home.biz.HubMemberBiz;
import com.cowave.hub.admin.domain.home.entity.HubMember;
import com.cowave.hub.admin.domain.home.repository.HubMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubMemberBizImpl implements HubMemberBiz {

    private final HubMemberRepository memberRepository;

    @Override
    public void saveMember(HubMember hubMember) {
        memberRepository.save(hubMember);
    }

    @Override
    public void saveMemberRole(Integer memberId, Integer roleId) {
        memberRepository.saveMemberRole(memberId, roleId);
    }

    @Override
    public void updateMember(HubMember hubMember) {
        memberRepository.updateById(hubMember);
    }
}
