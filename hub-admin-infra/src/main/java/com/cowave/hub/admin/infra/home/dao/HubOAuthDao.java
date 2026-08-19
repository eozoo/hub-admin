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
package com.cowave.hub.admin.infra.home.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.home.entity.HubOAuth;
import com.cowave.hub.admin.domain.home.repository.HubOAuthRepository;
import com.cowave.hub.admin.infra.home.mapper.HubOAuthMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class HubOAuthDao extends ServiceImpl<HubOAuthMapper, HubOAuth> implements HubOAuthRepository {

    /**
     * 三方授权方式列表
     */
    @Override
    public List<HubOAuth> queryListByTenantId(String tenantId) {
        return lambdaQuery()
                .eq(HubOAuth::getTenantId, tenantId)
                .eq(HubOAuth::getStatus, 1)
                .orderByAsc(HubOAuth::getOauthSort)
                .list();
    }

    /**
     * 查询三方授权服务配置
     */
    @Override
    public HubOAuth queryByServerType(String tenantId, String serverType) {
        return lambdaQuery()
                .eq(HubOAuth::getTenantId, tenantId)
                .eq(HubOAuth::getServerType, serverType)
                .one();
    }
}
