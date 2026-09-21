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
package com.cowave.hub.admin.infra.sys2.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.sys2.entity.SysConfig;
import com.cowave.hub.admin.domain.sys2.repository.SysConfigRepository;
import com.cowave.hub.admin.infra.sys2.mapper.SysConfig2Mapper;
import com.cowave.zoo.framework.helper.redis.dict.CustomValueParser;
import org.springframework.stereotype.Repository;

/**
 * @author shanhuiming
 */
@Repository
public class SysConfig2Dao extends ServiceImpl<SysConfig2Mapper, SysConfig> implements SysConfigRepository {

    @Override
    public <T> T queryConfigValue(String configKey) {
        SysConfig config = lambdaQuery()
                .eq(SysConfig::getConfigKey, configKey)
                .eq(SysConfig::getStatus, 1)
                .one();
        if (config == null) {
            return null;
        }
        return (T) CustomValueParser.getValue(config.getConfigValue(), config.getValueType(), config.getValueParser());
    }
}
