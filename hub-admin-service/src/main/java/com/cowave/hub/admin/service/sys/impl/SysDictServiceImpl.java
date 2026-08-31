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
import com.cowave.hub.admin.domain.sys.entity.SysDictType;
import com.cowave.hub.admin.domain.sys.entity.command.DictCreate;
import com.cowave.hub.admin.domain.sys.entity.command.DictTypeCreate;
import com.cowave.hub.admin.domain.sys.entity.pto.DictPto;
import com.cowave.hub.admin.domain.sys.repository.facade.SysDictRepositoryFacade;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.service.sys.SysDictService;
import com.cowave.zoo.framework.helper.redis.dict.CustomValueParser;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.hub.admin.domain.AdminRedisKeys.DICT_CODE;
import static com.cowave.hub.admin.domain.AdminRedisKeys.DICT_TYPE;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysDictServiceImpl implements SysDictService {

    private final SysDictRepositoryFacade dictRepositoryFacade;

    private final SysDictBiz dictBiz;

    @Cacheable(value = DICT_TYPE, key = "#tenantId + ':' + #typeCode")
    @Override
    public List<SysDict> queryListByType(String tenantId, String typeCode) {
        List<SysDict> list = dictRepositoryFacade.listByType(tenantId, typeCode);
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
    public Page<SysDictType> queryTypePageByModule(String tenantId, String moduleCode, int pageNum, int pageSize) {
        return dictRepositoryFacade.queryTypePageByModule(tenantId, moduleCode, pageNum, pageSize);
    }

    @Override
    public void addType(String tenantId, DictTypeCreate typeCreate) {
        dictBiz.saveType(tenantId, typeCreate);
    }

    @CacheEvict(value = DICT_TYPE, key = "#tenantId + ':' + #typeCreate.typeCode")
    @Override
    public void editType(String tenantId, DictTypeCreate typeCreate) {
        dictBiz.editType(tenantId, typeCreate);
    }

    @Override
    public void deleteType(String tenantId, List<Integer> typeIds) {
        dictBiz.deleteTypes(tenantId, typeIds);
    }

    @Override
    public void updateTypeStatus(String tenantId, Integer typeId, Integer status) {
        dictBiz.updateTypeStatus(tenantId, typeId, status);
    }

    @Cacheable(value = DICT_CODE, key = "#tenantId + ':' + #dictCode")
    @Override
    public SysDict queryByCode(String tenantId, String dictCode) {
        SysDict dict = dictRepositoryFacade.queryByCode(tenantId, dictCode);
        if (dict == null) {
            return null;
        }

        Object dictValue = CustomValueParser.getValue(
                dict.getDictValue(), dict.getValueType(), dict.getValueParser());
        dict.setDictValue(dictValue);
        return dict;
    }

    @Override
    public List<DictPto> queryList(String tenantId, String typeCode, String moduleCode) {
        return dictRepositoryFacade.queryDictList(tenantId, typeCode, moduleCode);
    }

    @Override
    public DictPto info(String tenantId, Long id) {
        return dictRepositoryFacade.queryDictById(tenantId, id);
    }

    @Override
    public void add(String tenantId, DictCreate dictCreate) {
        dictBiz.saveDict(tenantId, dictCreate);
    }

    @Override
    public void edit(String tenantId, DictCreate dictCreate) {
        dictBiz.editDict(tenantId, dictCreate);
    }

    @Override
    public void delete(String tenantId, List<Long> dictIds) {
        dictBiz.deleteDicts(tenantId, dictIds);
    }

    @Override
    public void updateDictStatus(String tenantId, Long dictId, Integer status) {
        dictBiz.updateDictStatus(tenantId, dictId, status);
    }
}
