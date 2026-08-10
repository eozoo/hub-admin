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
import com.cowave.hub.admin.domain.auth.entity.HubOAuth;
import com.cowave.hub.admin.domain.auth.entity.HubOAuthUser;
import com.cowave.hub.admin.domain.auth.repository.HubOAuthRepository;
import com.cowave.hub.admin.infra.auth.mapper.HubOAuthMapper;
import com.cowave.hub.admin.infra.auth.mapper.HubOAuthUserMapper;
import com.cowave.zoo.framework.access.Access;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class HubOAuthDao extends ServiceImpl<HubOAuthMapper, HubOAuth> implements HubOAuthRepository {

    private final HubOAuthUserMapper oauthUserMapper;

    @Override
    public HubOAuth queryByServerType(String tenantId, String serverType) {
        return lambdaQuery().eq(HubOAuth::getTenantId, tenantId).eq(HubOAuth::getServerType, serverType).one();
    }

    @Override
    public Page<HubOAuthUser> queryUserPage(String tenantId, String serverType, String userAccount) {
        return oauthUserMapper.selectPage(Access.page(),
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HubOAuthUser>()
                        .eq(HubOAuthUser::getTenantId, tenantId)
                        .eq(HubOAuthUser::getServerType, serverType)
                        .like(StringUtils.isNotBlank(userAccount), HubOAuthUser::getUserAccount, userAccount));
    }

    @Override
    public HubOAuthUser queryUserByAccount(String tenantId, String serverType, String userAccount) {
        return oauthUserMapper.selectOne(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HubOAuthUser>()
                .eq(HubOAuthUser::getTenantId, tenantId)
                .eq(HubOAuthUser::getServerType, serverType)
                .eq(HubOAuthUser::getUserAccount, userAccount));
    }

    @Override
    public void removeByServerType(String tenantId, String serverType) {
        lambdaUpdate().eq(HubOAuth::getTenantId, tenantId).eq(HubOAuth::getServerType, serverType).remove();
    }

    @Override
    public void saveOauthUser(HubOAuthUser oauthUser) {
        oauthUserMapper.insert(oauthUser);
    }

    @Override
    public void updateOauthUserById(HubOAuthUser oauthUser) {
        oauthUserMapper.updateById(oauthUser);
    }
}
