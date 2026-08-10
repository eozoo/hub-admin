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
package com.cowave.hub.admin.domain.sys.biz.impl;

import com.cowave.hub.admin.domain.sys.biz.HubConfigBiz;
import com.cowave.hub.admin.domain.sys.entity.HubConfig;
import com.cowave.hub.admin.domain.sys.repository.HubConfigRepository;
import com.cowave.zoo.framework.helper.redis.RedisHelper;
import com.cowave.zoo.framework.helper.redis.StringRedisHelper;
import com.cowave.zoo.framework.helper.redis.dict.CustomValueParser;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.hub.admin.domain.AdminRedisKeys.CONFIG_KEY;
import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.zoo.http.client.constants.HttpCode.NOT_FOUND;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubConfigBizImpl implements HubConfigBiz {

    private final HubConfigRepository configRepository;

    private final RedisHelper redisHelper;

    private final StringRedisHelper stringRedisHelper;

    @Override
    public void saveConfig(HubConfig config) {
        CustomValueParser.getValue(config.getConfigValue(), config.getValueType(), config.getValueParser());
        configRepository.save(config);
    }

    @Override
    public void editConfig(HubConfig config) {
        HttpAsserts.notNull(config.getConfigId(), BAD_REQUEST, "{admin.config.id.null}");

        HubConfig pre = configRepository.queryById(config.getTenantId(), config.getConfigId());
        HttpAsserts.notNull(pre, NOT_FOUND, "{admin.config.not.exist}", config.getConfigId());

        CustomValueParser.getValue(config.getConfigValue(), config.getValueType(), config.getValueParser());
        configRepository.updateConfig(config);
    }

    @Override
    public void deleteConfigs(String tenantId, List<Integer> configIds) {
        List<HubConfig> list = configRepository.listByIds(configIds);
        configRepository.removeByIds(configIds);
        for (HubConfig conf : list) {
            redisHelper.delete(CONFIG_KEY + ":" + tenantId + ":" + conf.getConfigKey());
        }
    }

    @Override
    public void resetTenantConfig(String tenantId) {
        configRepository.resetTenantConfig(tenantId);
        stringRedisHelper.luaClean(CONFIG_KEY + ":" + tenantId + ":");
    }
}
