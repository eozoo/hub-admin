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
import com.cowave.hub.admin.domain.sys.entity.HubDict;
import com.cowave.hub.admin.domain.sys.entity.command.DictCreate;
import com.cowave.hub.admin.domain.sys.entity.pto.DictPto;
import com.cowave.hub.admin.domain.sys.repository.HubDictRepository;
import com.cowave.hub.admin.infra.sys.mapper.HubDictMapper;
import com.cowave.zoo.framework.access.Access;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class HubDictDao extends ServiceImpl<HubDictMapper, HubDict> implements HubDictRepository {

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
    public HubDict queryByCode(String dictCode) {
        return lambdaQuery().eq(HubDict::getDictCode, dictCode).one();
    }

    @Override
    public List<HubDict> listByType(String typeCode) {
        return lambdaQuery().eq(HubDict::getParentCode, typeCode).list();
    }

    @Override
    public List<HubDict> listByGroup(String groupCode) {
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
        lambdaUpdate().eq(HubDict::getParentCode, parentCode).remove();
    }

    @Override
    public void updateDict(DictCreate dictCreate) {
        lambdaUpdate().eq(HubDict::getId, dictCreate.getId())
                .set(HubDict::getUpdateBy, Access.userCode())
                .set(HubDict::getUpdateTime, new Date())
                .set(HubDict::getDictCode, dictCreate.getDictCode())
                .set(HubDict::getDictName, dictCreate.getDictName())
                .set(HubDict::getDictValue, dictCreate.getDictValue())
                .set(HubDict::getDictOrder, dictCreate.getDictOrder())
                .set(HubDict::getValueParser, dictCreate.getValueParser())
                .set(HubDict::getValueType, dictCreate.getValueType())
                .set(HubDict::getIsDefault, dictCreate.getIsDefault())
                .set(HubDict::getCss, dictCreate.getCss())
                .set(HubDict::getStatus, dictCreate.getStatus())
                .set(HubDict::getRemark, dictCreate.getRemark())
                .update();
    }

    @Override
    public void updateParentCode(String newParent, String oldParent) {
        lambdaUpdate()
                .eq(HubDict::getParentCode, oldParent)
                .set(HubDict::getParentCode, newParent)
                .update();
    }
}
