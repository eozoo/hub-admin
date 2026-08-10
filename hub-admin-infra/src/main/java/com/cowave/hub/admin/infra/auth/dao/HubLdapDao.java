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
package com.cowave.hub.admin.infra.auth.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.auth.entity.HubLdap;
import com.cowave.hub.admin.domain.auth.entity.HubLdapUser;
import com.cowave.hub.admin.domain.auth.repository.HubLdapRepository;
import com.cowave.hub.admin.infra.auth.mapper.HubLdapMapper;
import com.cowave.hub.admin.infra.auth.mapper.HubLdapUserMapper;
import com.cowave.zoo.framework.access.Access;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class HubLdapDao extends ServiceImpl<HubLdapMapper, HubLdap> implements HubLdapRepository {

    private final HubLdapUserMapper ldapUserMapper;

    @Override
    public HubLdap queryById(String tenantId) {
        return getById(tenantId);
    }

    @Override
    public Page<HubLdapUser> queryUserPage(String tenantId, String userAccount) {
        return ldapUserMapper.selectPage(Access.page(),
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HubLdapUser>()
                        .eq(HubLdapUser::getTenantId, tenantId)
                        .like(StringUtils.isNotBlank(userAccount), HubLdapUser::getUserAccount, userAccount));
    }

    @Override
    public HubLdapUser queryUserByAccount(String tenantId, String userAccount) {
        return ldapUserMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HubLdapUser>()
                .eq(HubLdapUser::getTenantId, tenantId)
                .eq(HubLdapUser::getUserAccount, userAccount));
    }

    @Override
    public void saveLdapUser(HubLdapUser ldapUser) {
        ldapUserMapper.insert(ldapUser);
    }

    @Override
    public void updateLdapUserById(HubLdapUser ldapUser) {
        ldapUserMapper.updateById(ldapUser);
    }
}
