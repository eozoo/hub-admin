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

import com.cowave.hub.admin.domain.sys.biz.SysDictBiz;
import com.cowave.hub.admin.domain.sys.entity.SysDict;
import com.cowave.hub.admin.domain.sys.entity.SysDictType;
import com.cowave.hub.admin.domain.sys.entity.command.DictCreate;
import com.cowave.hub.admin.domain.sys.entity.command.DictTypeCreate;
import com.cowave.hub.admin.domain.sys.repository.SysDictRepository;
import com.cowave.zoo.framework.helper.redis.StringRedisHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.cowave.hub.admin.domain.AdminRedisKeys.DICT_CODE;
import static com.cowave.hub.admin.domain.AdminRedisKeys.DICT_TYPE;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysDictBizImpl implements SysDictBiz {

    private final SysDictRepository dictRepository;

    private final StringRedisHelper redisHelper;

    @Override
    public void saveType(String tenantId, DictTypeCreate typeCreate) {
        typeCreate.setTenantId(tenantId);
        dictRepository.saveType(typeCreate);
    }

    @Override
    public void editType(String tenantId, DictTypeCreate typeCreate) {
        typeCreate.setTenantId(tenantId);
        dictRepository.updateType(typeCreate);
    }

    @Override
    public void deleteTypes(String tenantId, List<Integer> typeIds) {
        List<SysDictType> types = dictRepository.listTypesByIds(typeIds);
        for (SysDictType type : types) {
            List<SysDict> dicts = dictRepository.listByType(tenantId, type.getTypeCode());
            dictRepository.removeDictsByType(tenantId, type.getTypeCode());
            redisHelper.delete(DICT_TYPE + ":" + tenantId + ":" + type.getTypeCode());
            for (SysDict dict : dicts) {
                redisHelper.delete(DICT_CODE + ":" + tenantId + ":" + dict.getDictCode());
            }
        }
        dictRepository.deleteTypes(typeIds);
    }

    @Override
    public void updateTypeStatus(String tenantId, Integer typeId, Integer status) {
        List<SysDictType> types = dictRepository.listTypesByIds(Collections.singletonList(typeId));
        dictRepository.updateTypeStatus(typeId, status);
        if (!types.isEmpty()) {
            redisHelper.delete(DICT_TYPE + ":" + tenantId + ":" + types.get(0).getTypeCode());
        }
    }

    @Override
    public void saveDict(String tenantId, DictCreate dictCreate) {
        dictCreate.setTenantId(tenantId);
        dictRepository.save(dictCreate);
        redisHelper.delete(DICT_TYPE + ":" + tenantId + ":" + dictCreate.getTypeCode());
    }

    @Override
    public void editDict(String tenantId, DictCreate dictCreate) {
        dictCreate.setTenantId(tenantId);
        dictRepository.updateById(dictCreate);
        redisHelper.delete(DICT_TYPE + ":" + tenantId + ":" + dictCreate.getTypeCode());
        redisHelper.delete(DICT_CODE + ":" + tenantId + ":" + dictCreate.getDictCode());
    }

    @Override
    public void deleteDicts(String tenantId, List<Long> dictIds) {
        List<SysDict> dicts = dictRepository.listByIds(dictIds);
        dictRepository.removeByIds(dictIds);
        for (SysDict dict : dicts) {
            redisHelper.delete(DICT_TYPE + ":" + tenantId + ":" + dict.getTypeCode());
            redisHelper.delete(DICT_CODE + ":" + tenantId + ":" + dict.getDictCode());
        }
    }

    @Override
    public void updateDictStatus(String tenantId, Long dictId, Integer status) {
        SysDict dict = dictRepository.getById(dictId);
        dictRepository.updateDictStatus(dictId, status);
        if (dict != null) {
            redisHelper.delete(DICT_TYPE + ":" + tenantId + ":" + dict.getTypeCode());
            redisHelper.delete(DICT_CODE + ":" + tenantId + ":" + dict.getDictCode());
        }
    }
}
