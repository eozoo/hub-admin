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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.sys.entity.SysDict;
import com.cowave.hub.admin.domain.sys.entity.SysDictType;
import com.cowave.hub.admin.domain.sys.entity.pto.DictPto;
import com.cowave.hub.admin.domain.sys.repository.SysDictRepository;
import com.cowave.hub.admin.infra.sys.mapper.SysDictMapper;
import com.cowave.hub.admin.infra.sys.mapper.SysDictTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
@RequiredArgsConstructor
public class SysDictDao extends ServiceImpl<SysDictMapper, SysDict> implements SysDictRepository {

    private final SysDictTypeMapper typeMapper;

    @Override
    public SysDict queryByCode(String tenantId, String dictCode) {
        return lambdaQuery()
                .eq(SysDict::getTenantId, tenantId)
                .eq(SysDict::getDictCode, dictCode)
                .one();
    }

    @Override
    public List<SysDict> listByType(String tenantId, String typeCode) {
        return lambdaQuery()
                .eq(SysDict::getTenantId, tenantId)
                .eq(SysDict::getTypeCode, typeCode)
                .orderByAsc(SysDict::getDictOrder)
                .list();
    }

    @Override
    public Page<SysDictType> queryTypePageByModule(String tenantId, String moduleCode, int pageNum, int pageSize) {
        Page<SysDictType> page = new Page<>(pageNum, pageSize);
        return typeMapper.selectPage(page, new LambdaQueryWrapper<SysDictType>()
                .eq(moduleCode != null && !moduleCode.isEmpty(), SysDictType::getModuleCode, moduleCode)
                .eq(SysDictType::getTenantId, tenantId)
                .orderByAsc(SysDictType::getModuleCode));
    }

    @Override
    public List<DictPto> queryDictList(String tenantId, String typeCode, String moduleCode) {
        return baseMapper.queryDictList(tenantId, typeCode, moduleCode);
    }

    @Override
    public DictPto queryDictById(String tenantId, Long id) {
        return baseMapper.queryDictById(tenantId, id);
    }

    @Override
    public void saveType(SysDictType type) {
        typeMapper.insert(type);
    }

    @Override
    public void updateType(SysDictType type) {
        typeMapper.updateById(type);
    }

    @Override
    public void updateTypeStatus(Integer typeId, Integer status) {
        typeMapper.update(null, new LambdaUpdateWrapper<SysDictType>()
                .eq(SysDictType::getTypeId, typeId)
                .set(SysDictType::getStatus, status));
    }

    @Override
    public void deleteTypes(List<Integer> typeIds) {
        typeMapper.deleteBatchIds(typeIds);
    }

    @Override
    public List<SysDictType> listTypesByIds(List<Integer> typeIds) {
        return typeMapper.selectBatchIds(typeIds);
    }

    @Override
    public void removeDictsByType(String tenantId, String typeCode) {
        lambdaUpdate()
                .eq(SysDict::getTenantId, tenantId)
                .eq(SysDict::getTypeCode, typeCode)
                .remove();
    }

    @Override
    public void updateDictStatus(Long dictId, Integer status) {
        lambdaUpdate()
                .eq(SysDict::getId, dictId)
                .set(SysDict::getStatus, status)
                .update();
    }
}
