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

import com.cowave.hub.admin.domain.auth.entity.command.UserRegister;
import com.cowave.hub.admin.domain.auth.entity.query.OnlineQuery;
import com.cowave.hub.admin.domain.auth.entity.vo.AuthVo;
import com.cowave.hub.admin.domain.auth.entity.vo.OnlineVo;
import com.cowave.hub.admin.domain.rbac.entity.vo.Route;
import com.cowave.zoo.framework.access.security.AccessUserDetails;
import com.cowave.zoo.http.client.response.Response;

import java.io.IOException;
import java.util.List;

/**
 * 鉴权服务
 *
 * @author shanhuiming
 */
public interface AuthService {

    /**
     * 注册
     */
    String register(UserRegister userRegister);

    /**
     * 登录（密码）
     */
    AccessUserDetails login(String tenantId, String userAccount, String passwd);

    /**
     * MFA二次认证
     */
    AccessUserDetails mfa(String mfaToken, String mfaCode);

    /**
     * 退出
     */
    void logout() throws IOException;

    /**
     * 在线用户列表
     */
    Response.Page<OnlineVo> onlineList(OnlineQuery query);

    /**
     * 撤销Access令牌
     */
    void revokeAccess(String tenantId, String authType, String userAccount, String accessId);

    /**
     * 撤销Refresh令牌
     */
    void revokeRefresh(String tenantId, String authType, String userAccount);

    /**
     * 刷新令牌
     */
    AccessUserDetails refresh(String refreshToken) throws Exception;

    /**
     * 登录信息
     */
    AuthVo getAuth() throws Exception;

    /**
     * 菜单权限
     */
    List<Route> menus();
}
