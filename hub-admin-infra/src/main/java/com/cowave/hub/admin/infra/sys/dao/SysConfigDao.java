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
import com.cowave.hub.admin.domain.sys.entity.SysConfig;
import com.cowave.hub.admin.domain.sys.entity.query.ConfigQuery;
import com.cowave.hub.admin.domain.sys.repository.SysConfigRepository;
import com.cowave.hub.admin.infra.sys.mapper.SysConfigMapper;
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
public class SysConfigDao extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigRepository {

    @Override
    public Page<SysConfig> queryPage(String tenantId, ConfigQuery query) {
        return lambdaQuery()
                .eq(SysConfig::getTenantId, tenantId)
                .ge(query.getBeginTime() != null, SysConfig::getCreateTime, query.getBeginTime())
                .le(query.getEndTime() != null, SysConfig::getCreateTime, query.getEndTime())
                .like(StringUtils.isNotBlank(query.getConfigName()), SysConfig::getConfigName, query.getConfigName())
                .orderByAsc(SysConfig::getConfigId)
                .page(Access.page());
    }

    @Override
    public List<SysConfig> queryList(String tenantId, ConfigQuery query) {
        return lambdaQuery()
                .eq(SysConfig::getTenantId, tenantId)
                .ge(query.getBeginTime() != null, SysConfig::getCreateTime, query.getBeginTime())
                .le(query.getEndTime() != null, SysConfig::getCreateTime, query.getEndTime())
                .like(StringUtils.isNotBlank(query.getConfigName()), SysConfig::getConfigName, query.getConfigName())
                .orderByAsc(SysConfig::getConfigId)
                .list();
    }

    @Override
    public SysConfig queryById(String tenantId, Integer configId) {
        return lambdaQuery()
                .eq(SysConfig::getTenantId, tenantId)
                .eq(SysConfig::getConfigId, configId)
                .one();
    }

    @Override
    @Cacheable(value = CONFIG_KEY, key = "#tenantId + ':' + #configKey")
    public <T> T queryConfigValue(String tenantId, String configKey) {
        SysConfig config = lambdaQuery()
                .eq(SysConfig::getTenantId, tenantId)
                .eq(SysConfig::getConfigKey, configKey)
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
    public void updateConfig(SysConfig config) {
        lambdaUpdate()
                .eq(SysConfig::getConfigId, config.getConfigId())
                .set(SysConfig::getConfigName, config.getConfigName())
                .set(SysConfig::getConfigKey, config.getConfigKey())
                .set(SysConfig::getConfigValue, config.getConfigValue())
                .set(SysConfig::getValueParser, config.getValueParser())
                .set(SysConfig::getValueType, config.getValueType())
                .set(SysConfig::getIsDefault, config.getIsDefault())
                .set(SysConfig::getRemark, config.getRemark())
                .set(SysConfig::getUpdateBy, Access.userCode())
                .set(SysConfig::getUpdateTime, new Date())
                .update();
    }
}
