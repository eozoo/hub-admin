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
package com.cowave.hub.admin.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.biz.SysConfigBiz;
import com.cowave.hub.admin.domain.sys.entity.SysConfig;
import com.cowave.hub.admin.domain.sys.entity.query.ConfigQuery;
import com.cowave.hub.admin.domain.sys.repository.facade.SysConfigRepositoryFacade;
import com.cowave.hub.admin.service.sys.SysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.hub.admin.domain.AdminRedisKeys.CONFIG_KEY;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysConfigServiceImpl implements SysConfigService {

    private final SysConfigBiz configBiz;

    private final SysConfigRepositoryFacade configRepositoryFacade;

    @Override
    public Page<SysConfig> page(String tenantId, ConfigQuery query) {
        return configRepositoryFacade.queryPage(tenantId, query);
    }

    @Override
    public List<SysConfig> list(String tenantId, ConfigQuery query) {
        return configRepositoryFacade.queryList(tenantId, query);
    }

    @Override
    public SysConfig info(String tenantId, Integer configId) {
        return configRepositoryFacade.queryById(tenantId, configId);
    }

    @Override
    public void add(SysConfig sysConfig) {
        configBiz.saveConfig(sysConfig);
    }

    @CacheEvict(value = CONFIG_KEY, key = "#sysConfig.tenantId + ':' + #sysConfig.configKey")
    @Override
    public void edit(SysConfig sysConfig) {
        configBiz.editConfig(sysConfig);
    }

    @Override
    public void delete(String tenantId, List<Integer> configIds) {
        configBiz.deleteConfigs(tenantId, configIds);
    }

    @Override
    public void resetConfig(String tenantId) {
        configBiz.resetTenantConfig(tenantId);
    }

    @Override
    public <T> T queryConfigValue(String tenantId, String configKey) {
        return configRepositoryFacade.queryConfigValue(tenantId, configKey);
    }
}
