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

import com.cowave.hub.admin.domain.auth.enums.ProviderCode;
import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.command.AccountBind;
import com.cowave.hub.admin.domain.auth.entity.command.OAuthConfigUpdate;
import com.cowave.hub.admin.domain.auth.entity.query.OAuthUserQuery;
import com.cowave.hub.admin.domain.auth.entity.vo.OAuthUserVo;
import com.cowave.hub.admin.domain.auth.entity.vo.LoginVo;

/**
 * @author shanhuiming
 */
public interface OAuthService {

    /**
     * 绑定本地账号
     */
    LoginVo bindAccount(AccountBind command);

    /**
     * gitlab回调
     */
    LoginVo gitlabCallback(String code, String state);

    /**
     * GitHub回调认证
     */
    LoginVo githubCallback(String code, String state);

    /**
     * 微信回调认证
     */
    LoginVo wechatCallback(String code, String state);

    /**
     * QQ回调认证
     */
    LoginVo qqCallback(String code, String state);

    /**
     * Bilibili回调认证
     */
    LoginVo bilibiliCallback(String code, String state);

    /**
     * 获取授权服务配置
     */
    SysAuthProvider getOauth(ProviderCode providerCode);

    /**
     * 修改授权服务配置
     */
    void editOauth(ProviderCode providerCode, OAuthConfigUpdate oauthConfig);

    /**
     * 用户列表
     */
    Page<OAuthUserVo> listUser(ProviderCode providerCode, OAuthUserQuery userQuery);

    /**
     * 修改身份状态
     */
    void updateIdentityStatus(ProviderCode providerCode, Long identityId, EnableStatus status);

    /**
     * 删除身份绑定
     */
    void deleteIdentity(ProviderCode providerCode, Long identityId);
}
