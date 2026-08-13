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

import com.cowave.hub.admin.domain.sys.biz.SysDictBiz;
import com.cowave.hub.admin.domain.sys.entity.SysDict;
import com.cowave.hub.admin.domain.sys.entity.command.DictCreate;
import com.cowave.hub.admin.domain.sys.entity.pto.DictPto;
import com.cowave.hub.admin.domain.sys.entity.query.DictQuery;
import com.cowave.hub.admin.domain.sys.entity.vo.SelectOptionVo;
import com.cowave.hub.admin.domain.sys.repository.facade.SysDictRepositoryFacade;
import com.cowave.hub.admin.service.sys.SysDictService;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.helper.redis.dict.CustomValueParser;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.cowave.hub.admin.domain.AdminRedisKeys.*;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.*;
import static com.cowave.zoo.framework.access.security.Permission.TENANT_SYSTEM;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysDictServiceImpl implements SysDictService {

    private final SysDictBiz dictBiz;

    private final SysDictRepositoryFacade dictRepositoryFacade;

    @Override
    public List<DictPto> queryList(DictQuery query) {
        return dictRepositoryFacade.queryList(query.getDictCode(), query.getDictName());
    }

    @Override
    public DictPto info(Long dictId) {
        return dictRepositoryFacade.queryById(dictId);
    }

    @Override
    public void add(DictCreate dictCreate) {
        dictBiz.saveDict(dictCreate);
    }

    @Override
    public void delete(List<Integer> dictIds) {
        dictBiz.deleteDicts(dictIds);
    }

    @Override
    public void edit(DictCreate dictCreate) {
        dictBiz.editDict(dictCreate);
    }

    @Cacheable(value = DICT_CODE, key = "#dictCode")
    @Override
    public SysDict queryByCode(String dictCode) {
        SysDict dict = dictRepositoryFacade.queryByCode(dictCode);
        if (dict == null) {
            return null;
        }
        Object dictValue = CustomValueParser.getValue(
                dict.getDictValue(), dict.getValueType(), dict.getValueParser());
        dict.setDictValue(dictValue);
        return dict;
    }

    @Cacheable(value = DICT_TYPE, key = "#typeCode")
    @Override
    public List<SysDict> queryListByType(String typeCode) {
        List<SysDict> list = dictRepositoryFacade.listByType(typeCode);
        if (list.isEmpty()) {
            return list;
        }
        for (SysDict dict : list) {
            Object dictValue = CustomValueParser.getValue(
                    dict.getDictValue(), dict.getValueType(), dict.getValueParser());
            dict.setDictValue(dictValue);
        }
        return list;
    }

    @Cacheable(value = DICT_GROUP, key = "#groupCode")
    @Override
    public List<SysDict> queryListByGroup(String groupCode) {
        List<SysDict> list = dictRepositoryFacade.listByGroup(groupCode);
        if (list.isEmpty()) {
            return list;
        }
        for (SysDict dict : list) {
            Object dictValue = CustomValueParser.getValue(
                    dict.getDictValue(), dict.getValueType(), dict.getValueParser());
            dict.setDictValue(dictValue);
        }
        return list;
    }

    @Override
    public Collection<SelectOptionVo> queryListTypeByGroup(String groupCode) {
        List<DictPto> list = dictRepositoryFacade.listTypeByGroup(groupCode);
        Map<String, SelectOptionVo> map = new LinkedHashMap<>();
        for (DictPto infoDto : list) {
            if ("domain_module".equals(groupCode) && !TENANT_SYSTEM.equals(Access.tenantId())
                    && (SYSTEM_TENANT.equals(infoDto.getTypeCode())
                            || SYSTEM_MENU.equals(infoDto.getTypeCode())
                            || SYSTEM_DICT.equals(infoDto.getTypeCode())
                            || SYSTEM_ATTACH.equals(infoDto.getTypeCode()))
            ) {
                continue;
            }

            if (groupCode.equals(infoDto.getGroupCode())) {
                map.computeIfAbsent(infoDto.getTypeCode(),
                        k -> new SelectOptionVo(infoDto.getTypeCode(), infoDto.getTypeName()));
                continue;
            }

            SelectOptionVo option = map.computeIfAbsent(infoDto.getGroupCode(),
                    k -> new SelectOptionVo(infoDto.getGroupCode(), infoDto.getGroupName()));

            List<SelectOptionVo> children = option.getChildren();
            if (children == null) {
                children = new ArrayList<>();
                option.setChildren(children);
            }
            children.add(new SelectOptionVo(infoDto.getTypeCode(), infoDto.getTypeName()));
        }
        return map.values();
    }
}
