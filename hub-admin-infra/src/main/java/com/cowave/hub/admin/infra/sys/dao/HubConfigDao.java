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
package com.cowave.hub.admin.infra.sys.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.sys.entity.HubConfig;
import com.cowave.hub.admin.domain.sys.entity.query.ConfigQuery;
import com.cowave.hub.admin.domain.sys.repository.HubConfigRepository;
import com.cowave.hub.admin.infra.sys.mapper.HubConfigMapper;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.helper.redis.dict.CustomValueParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static com.cowave.hub.admin.domain.AdminRedisKeys.CONFIG_KEY;

/**
 * @author shanhuiming
 */
@Repository
public class HubConfigDao extends ServiceImpl<HubConfigMapper, HubConfig> implements HubConfigRepository {

    @Override
    public Page<HubConfig> queryPage(String tenantId, ConfigQuery query) {
        return lambdaQuery()
                .eq(HubConfig::getTenantId, tenantId)
                .ge(query.getBeginTime() != null, HubConfig::getCreateTime, query.getBeginTime())
                .le(query.getEndTime() != null, HubConfig::getCreateTime, query.getEndTime())
                .like(StringUtils.isNotBlank(query.getConfigName()), HubConfig::getConfigName, query.getConfigName())
                .orderByAsc(HubConfig::getConfigId)
                .page(Access.page());
    }

    @Override
    public List<HubConfig> queryList(String tenantId, ConfigQuery query) {
        return lambdaQuery()
                .eq(HubConfig::getTenantId, tenantId)
                .ge(query.getBeginTime() != null, HubConfig::getCreateTime, query.getBeginTime())
                .le(query.getEndTime() != null, HubConfig::getCreateTime, query.getEndTime())
                .like(StringUtils.isNotBlank(query.getConfigName()), HubConfig::getConfigName, query.getConfigName())
                .orderByAsc(HubConfig::getConfigId)
                .list();
    }

    @Override
    public HubConfig queryById(String tenantId, Integer configId) {
        return lambdaQuery()
                .eq(HubConfig::getTenantId, tenantId)
                .eq(HubConfig::getConfigId, configId)
                .one();
    }

    @Override
    @Cacheable(value = CONFIG_KEY, key = "#tenantId + ':' + #configKey")
    public <T> T queryConfigValue(String tenantId, String configKey) {
        HubConfig config = lambdaQuery()
                .eq(HubConfig::getTenantId, tenantId)
                .eq(HubConfig::getConfigKey, configKey)
                .one();
        if (config == null) {
            return null;
        }
        return (T) CustomValueParser.getValue(config.getConfigValue(), config.getValueType(), config.getValueParser());
    }

    @Override
    public void resetTenantConfig(String tenantId) {
        baseMapper.resetTenantConfig(tenantId);
    }

    @Override
    public void updateConfig(HubConfig config) {
        lambdaUpdate()
                .eq(HubConfig::getConfigId, config.getConfigId())
                .set(HubConfig::getConfigName, config.getConfigName())
                .set(HubConfig::getConfigKey, config.getConfigKey())
                .set(HubConfig::getConfigValue, config.getConfigValue())
                .set(HubConfig::getValueParser, config.getValueParser())
                .set(HubConfig::getValueType, config.getValueType())
                .set(HubConfig::getIsDefault, config.getIsDefault())
                .set(HubConfig::getRemark, config.getRemark())
                .set(HubConfig::getUpdateBy, Access.userCode())
                .set(HubConfig::getUpdateTime, new Date())
                .update();
    }
}
