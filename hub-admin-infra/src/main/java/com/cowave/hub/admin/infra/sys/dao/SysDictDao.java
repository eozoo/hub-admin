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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.sys.entity.SysDict;
import com.cowave.hub.admin.domain.sys.entity.command.DictCreate;
import com.cowave.hub.admin.domain.sys.entity.pto.DictPto;
import com.cowave.hub.admin.domain.sys.repository.SysDictRepository;
import com.cowave.hub.admin.infra.sys.mapper.SysDictMapper;
import com.cowave.zoo.framework.access.Access;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysDictDao extends ServiceImpl<SysDictMapper, SysDict> implements SysDictRepository {

    @Override
    public List<DictPto> queryList(String dictCode, String dictName) {
        return baseMapper.queryList(dictCode, dictName);
    }

    @Override
    public List<DictPto> queryByIds(List<Integer> list) {
        return baseMapper.queryByIds(list);
    }

    @Override
    public DictPto queryById(long id) {
        return baseMapper.queryById(id);
    }

    @Override
    public SysDict queryByCode(String dictCode) {
        return lambdaQuery().eq(SysDict::getDictCode, dictCode).one();
    }

    @Override
    public List<SysDict> listByType(String typeCode) {
        return lambdaQuery().eq(SysDict::getParentCode, typeCode).list();
    }

    @Override
    public List<SysDict> listByGroup(String groupCode) {
        return baseMapper.listByGroup(groupCode);
    }

    @Override
    public List<DictPto> listTypeByGroup(String groupCode) {
        return baseMapper.listTypeByGroup(groupCode);
    }

    @Override
    public void removeByGroup(String groupCode) {
        baseMapper.removeByGroup(groupCode);
    }

    @Override
    public void removeByType(String parentCode) {
        lambdaUpdate().eq(SysDict::getParentCode, parentCode).remove();
    }

    @Override
    public void updateDict(DictCreate dictCreate) {
        lambdaUpdate().eq(SysDict::getId, dictCreate.getId())
                .set(SysDict::getUpdateBy, Access.userCode())
                .set(SysDict::getUpdateTime, new Date())
                .set(SysDict::getDictCode, dictCreate.getDictCode())
                .set(SysDict::getDictName, dictCreate.getDictName())
                .set(SysDict::getDictValue, dictCreate.getDictValue())
                .set(SysDict::getDictOrder, dictCreate.getDictOrder())
                .set(SysDict::getValueParser, dictCreate.getValueParser())
                .set(SysDict::getValueType, dictCreate.getValueType())
                .set(SysDict::getIsDefault, dictCreate.getIsDefault())
                .set(SysDict::getCss, dictCreate.getCss())
                .set(SysDict::getStatus, dictCreate.getStatus())
                .set(SysDict::getRemark, dictCreate.getRemark())
                .update();
    }

    @Override
    public void updateParentCode(String newParent, String oldParent) {
        lambdaUpdate()
                .eq(SysDict::getParentCode, oldParent)
                .set(SysDict::getParentCode, newParent)
                .update();
    }
}
