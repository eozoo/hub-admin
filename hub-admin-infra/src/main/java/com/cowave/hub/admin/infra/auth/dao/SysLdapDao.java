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
import com.cowave.hub.admin.domain.auth.entity.SysLdap;
import com.cowave.hub.admin.domain.auth.entity.SysLdapUser;
import com.cowave.hub.admin.domain.auth.repository.SysLdapRepository;
import com.cowave.hub.admin.infra.auth.mapper.SysLdapMapper;
import com.cowave.hub.admin.infra.auth.mapper.SysLdapUserMapper;
import com.cowave.zoo.framework.access.Access;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class SysLdapDao extends ServiceImpl<SysLdapMapper, SysLdap> implements SysLdapRepository {

    private final SysLdapUserMapper ldapUserMapper;

    @Override
    public SysLdap queryById(String tenantId) {
        return getById(tenantId);
    }

    @Override
    public Page<SysLdapUser> queryUserPage(String tenantId, String userAccount) {
        return ldapUserMapper.selectPage(Access.page(),
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysLdapUser>()
                        .eq(SysLdapUser::getTenantId, tenantId)
                        .like(StringUtils.isNotBlank(userAccount), SysLdapUser::getUserAccount, userAccount));
    }

    @Override
    public SysLdapUser queryUserByAccount(String tenantId, String userAccount) {
        return ldapUserMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysLdapUser>()
                .eq(SysLdapUser::getTenantId, tenantId)
                .eq(SysLdapUser::getUserAccount, userAccount));
    }

    @Override
    public void saveLdapUser(SysLdapUser ldapUser) {
        ldapUserMapper.insert(ldapUser);
    }

    @Override
    public void updateLdapUserById(SysLdapUser ldapUser) {
        ldapUserMapper.updateById(ldapUser);
    }
}
