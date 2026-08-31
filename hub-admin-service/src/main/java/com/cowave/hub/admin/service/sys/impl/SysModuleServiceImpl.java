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

import com.cowave.hub.admin.domain.sys.entity.SysModule;
import com.cowave.hub.admin.domain.sys.entity.vo.SelectOptionVo;
import com.cowave.hub.admin.domain.sys.repository.facade.SysModuleRepositoryFacade;
import com.cowave.hub.admin.service.sys.SysModuleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
public class SysModuleServiceImpl implements SysModuleService {

    private final SysModuleRepositoryFacade moduleRepositoryFacade;

    @Override
    public Collection<SelectOptionVo> queryModuleTree(String tenantId) {
        List<SysModule> list = moduleRepositoryFacade.listByTenant(tenantId);
        Map<String, SelectOptionVo> map = new LinkedHashMap<>();
        for (SysModule module : list) {
            if (StringUtils.isEmpty(module.getParentCode())) {
                map.computeIfAbsent(module.getModuleCode(),
                        k -> new SelectOptionVo(module.getModuleCode(), module.getModuleName()));
            }
        }
        for (SysModule module : list) {
            if (StringUtils.isNotEmpty(module.getParentCode())) {
                SelectOptionVo parent = map.get(module.getParentCode());
                if (parent != null) {
                    List<SelectOptionVo> children = parent.getChildren();
                    if (children == null) {
                        children = new ArrayList<>();
                        parent.setChildren(children);
                    }
                    children.add(new SelectOptionVo(module.getModuleCode(), module.getModuleName()));
                }
            }
        }
        return map.values();
    }
}
