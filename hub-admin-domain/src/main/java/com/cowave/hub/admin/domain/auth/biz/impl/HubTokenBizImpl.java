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
package com.cowave.hub.admin.domain.auth.biz.impl;

import com.cowave.hub.admin.domain.auth.biz.HubTokenBiz;
import com.cowave.hub.admin.domain.auth.entity.HubToken;
import com.cowave.hub.admin.domain.auth.entity.HubTokenMenu;
import com.cowave.hub.admin.domain.auth.repository.HubTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubTokenBizImpl implements HubTokenBiz {

    private final HubTokenRepository tokenRepository;

    @Override
    public void saveToken(HubToken token) {
        tokenRepository.save(token);
    }

    @Override
    public void updateToken(HubToken token) {
        tokenRepository.updateById(token);
    }

    @Override
    public void saveTokenMenus(List<HubTokenMenu> list) {
        tokenRepository.saveTokenMenusBatch(list);
    }

    @Override
    public void deleteApiToken(Integer tokenId) {
        tokenRepository.removeTokenMenusByTokenId(tokenId);
        tokenRepository.removeById(tokenId);
    }
}
