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
package com.cowave.hub.admin.infra.auth.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.auth.entity.HubToken;
import com.cowave.hub.admin.domain.auth.entity.HubTokenMenu;
import com.cowave.hub.admin.domain.auth.repository.HubTokenRepository;
import com.cowave.hub.admin.infra.auth.mapper.HubTokenMapper;
import com.cowave.hub.admin.infra.auth.mapper.HubTokenMenuMapper;
import com.cowave.zoo.tools.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class HubTokenDao extends ServiceImpl<HubTokenMapper, HubToken> implements HubTokenRepository {

    private final HubTokenMenuMapper tokenMenuMapper;

    @Override
    public List<HubToken> queryList() {
        return list();
    }

    @Override
    public List<HubToken> queryListByUserCode(String userCode) {
        return lambdaQuery().eq(HubToken::getUserCode, userCode).list();
    }

    @Override
    public List<String> queryPermitsByTokenId(Integer tokenId) {
        List<HubTokenMenu> list = tokenMenuMapper.selectList(new LambdaQueryWrapper<HubTokenMenu>()
                .eq(HubTokenMenu::getTokenId, tokenId));
        return Collections.copyToList(list, HubTokenMenu::getPermit);
    }

    @Override
    public void saveTokenMenusBatch(List<HubTokenMenu> list) {
        for (HubTokenMenu tm : list) {
            tokenMenuMapper.insert(tm);
        }
    }

    @Override
    public void removeTokenMenusByTokenId(Integer tokenId) {
        tokenMenuMapper.delete(new LambdaUpdateWrapper<HubTokenMenu>()
                .eq(HubTokenMenu::getTokenId, tokenId));
    }
}
