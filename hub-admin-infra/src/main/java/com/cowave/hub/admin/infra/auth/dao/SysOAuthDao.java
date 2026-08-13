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
import com.cowave.hub.admin.domain.auth.entity.SysOAuth;
import com.cowave.hub.admin.domain.auth.entity.SysOAuthUser;
import com.cowave.hub.admin.domain.auth.repository.SysOAuthRepository;
import com.cowave.hub.admin.infra.auth.mapper.SysOAuthMapper;
import com.cowave.hub.admin.infra.auth.mapper.SysOAuthUserMapper;
import com.cowave.zoo.framework.access.Access;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class SysOAuthDao extends ServiceImpl<SysOAuthMapper, SysOAuth> implements SysOAuthRepository {

    private final SysOAuthUserMapper oauthUserMapper;

    @Override
    public SysOAuth queryByServerType(String tenantId, String serverType) {
        return lambdaQuery().eq(SysOAuth::getTenantId, tenantId).eq(SysOAuth::getServerType, serverType).one();
    }

    @Override
    public Page<SysOAuthUser> queryUserPage(String tenantId, String serverType, String userAccount) {
        return oauthUserMapper.selectPage(Access.page(),
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysOAuthUser>()
                        .eq(SysOAuthUser::getTenantId, tenantId)
                        .eq(SysOAuthUser::getServerType, serverType)
                        .like(StringUtils.isNotBlank(userAccount), SysOAuthUser::getUserAccount, userAccount));
    }

    @Override
    public SysOAuthUser queryUserByAccount(String tenantId, String serverType, String userAccount) {
        return oauthUserMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysOAuthUser>()
                .eq(SysOAuthUser::getTenantId, tenantId)
                .eq(SysOAuthUser::getServerType, serverType)
                .eq(SysOAuthUser::getUserAccount, userAccount));
    }

    @Override
    public void removeByServerType(String tenantId, String serverType) {
        lambdaUpdate().eq(SysOAuth::getTenantId, tenantId).eq(SysOAuth::getServerType, serverType).remove();
    }

    @Override
    public void saveOauthUser(SysOAuthUser oauthUser) {
        oauthUserMapper.insert(oauthUser);
    }

    @Override
    public void updateOauthUserById(SysOAuthUser oauthUser) {
        oauthUserMapper.updateById(oauthUser);
    }
}
