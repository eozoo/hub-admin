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
package com.cowave.hub.admin.infra.member.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.member.entity.HubMember;
import com.cowave.hub.admin.domain.member.entity.HubMemberRole;
import com.cowave.hub.admin.domain.member.repository.HubMemberRepository;
import com.cowave.hub.admin.domain.rbac.enums.UserType;
import com.cowave.hub.admin.infra.member.mapper.HubMemberMapper;
import com.cowave.hub.admin.infra.member.mapper.HubMemberRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class HubMemberDao extends ServiceImpl<HubMemberMapper, HubMember> implements HubMemberRepository {

    private final HubMemberRoleMapper memberRoleMapper;

    @Override
    public HubMember queryByCode(String memberCode) {
        return lambdaQuery().eq(HubMember::getMemberCode, memberCode).one();
    }

    @Override
    public List<Integer> queryMemberRoleIdsByMemberId(Integer memberId) {
        return memberRoleMapper.selectList(new LambdaQueryWrapper<HubMemberRole>()
                        .eq(HubMemberRole::getMemberId, memberId)).stream().map(HubMemberRole::getRoleId).toList();
    }

    @Override
    public void saveMemberRole(Integer memberId, Integer roleId) {
        memberRoleMapper.insert(new HubMemberRole(memberId, roleId));
    }
}
