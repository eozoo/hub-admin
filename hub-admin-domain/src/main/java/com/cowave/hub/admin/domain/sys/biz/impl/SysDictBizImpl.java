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
import com.cowave.hub.admin.domain.sys.entity.command.DictCreate;
import com.cowave.hub.admin.domain.sys.entity.pto.DictPto;
import com.cowave.hub.admin.domain.sys.repository.SysDictRepository;
import com.cowave.zoo.framework.helper.redis.StringRedisHelper;
import com.cowave.zoo.framework.helper.redis.dict.CustomValueParser;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.hub.admin.domain.AdminRedisKeys.DICT_CODE;
import static com.cowave.hub.admin.domain.AdminRedisKeys.DICT_GROUP;
import static com.cowave.hub.admin.domain.AdminRedisKeys.DICT_TYPE;
import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.zoo.http.client.constants.HttpCode.NOT_FOUND;

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
    public void saveDict(DictCreate dictCreate) {
        CustomValueParser.getValue(dictCreate.getDictValue(), dictCreate.getValueType(), dictCreate.getValueParser());
        dictCreate.setParentCode(dictCreate.getTypeCode());
        dictRepository.save(dictCreate);

        DictPto dictPto = dictRepository.queryById(dictCreate.getId());
        redisHelper.delete(DICT_TYPE + ":" + dictPto.getTypeCode());
        redisHelper.delete(DICT_GROUP + ":" + dictPto.getGroupCode());
    }

    @Override
    public void editDict(DictCreate dictCreate) {
        HttpAsserts.notNull(dictCreate.getId(), BAD_REQUEST, "{admin.dict.id.null}");

        CustomValueParser.getValue(dictCreate.getDictValue(), dictCreate.getValueType(), dictCreate.getValueParser());

        DictPto preDict = dictRepository.queryById(dictCreate.getId());
        HttpAsserts.notNull(preDict, NOT_FOUND, "{admin.dict.not.exist}", dictCreate.getId());

        dictRepository.updateDict(dictCreate);
        DictPto newDict = dictRepository.queryById(dictCreate.getId());

        // 更新下级字典码
        if ("root".equals(preDict.getGroupCode()) || "group".equals(preDict.getGroupCode())) {
            dictRepository.updateParentCode(newDict.getDictCode(), preDict.getDictCode());
        }
        // 清除缓存
        redisHelper.delete(DICT_CODE + ":" + preDict.getDictCode());
        redisHelper.delete(DICT_TYPE + ":" + preDict.getTypeCode());
        redisHelper.delete(DICT_GROUP + ":" + preDict.getGroupCode());
    }

    @Override
    public void deleteDicts(List<Integer> dictIds) {
        List<DictPto> list = dictRepository.queryByIds(dictIds);
        dictRepository.removeByIds(dictIds);
        for (DictPto dictPto : list) {
            if ("root".equals(dictPto.getGroupCode())) {
                dictRepository.removeByGroup(dictPto.getDictCode());
            } else if ("group".equals(dictPto.getGroupCode())) {
                dictRepository.removeByType(dictPto.getDictCode());
            }
        }
        redisHelper.luaClean("hub-admin:dict:*");
    }
}
