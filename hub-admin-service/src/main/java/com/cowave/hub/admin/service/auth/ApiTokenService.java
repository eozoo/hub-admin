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
package com.cowave.hub.admin.service.auth;

import com.cowave.hub.admin.domain.auth.entity.command.ApiTokenCreate;
import com.cowave.hub.admin.domain.auth.entity.vo.TokenVo;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface ApiTokenService {

    /**
     * 列表
     */
    List<TokenVo> listApiToken();

    /**
     * 创建用户令牌
     */
    String creatApiToken(ApiTokenCreate tokenCreate);

    /**
     * 删除用户令牌
     */
    void deleteApiToken(Integer tokenId);
}
