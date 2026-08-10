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
package com.cowave.hub.admin.service.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.entity.HubConfig;
import com.cowave.hub.admin.domain.sys.entity.query.ConfigQuery;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubConfigService {

    /**
     * 分页
     */
    Page<HubConfig> page(String tenantId, ConfigQuery query);

    /**
     * 列表
     */
    List<HubConfig> list(String tenantId, ConfigQuery query);

    /**
     * 详情
     */
    HubConfig info(String tenantId, Integer configId);

    /**
     * 新增
     */
    void add(HubConfig hubConfig);

    /**
     * 编辑
     */
    void edit(HubConfig hubConfig);

    /**
     * 删除
     */
    void delete(String tenantId, List<Integer> configIds);

    /**
     * 重置恢复
     */
    void resetConfig(String tenantId);

    /**
     * 获取配置
     */
    <T> T queryConfigValue(String tenantId, String configKey);
}
