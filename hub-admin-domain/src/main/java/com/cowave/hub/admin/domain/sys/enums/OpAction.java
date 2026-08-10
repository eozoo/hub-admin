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
package com.cowave.hub.admin.domain.sys.enums;

/**
 * @author shanhuiming
 */
public final class OpAction {

    /**
     * 创建
     */
    public static final String CREATE = "op_create";

    /**
     * 删除
     */
    public static final String DELETE = "op_delete";

    /**
     * 修改
     */
    public static final String EDIT = "op_edit";

    /**
     * 登录
     */
    public static final String LOGIN = "op_login";

    /**
     * OAuth授权
     */
    public static final String LOGIN_OAUTH = "op_login_oauth";

    /**
     * 强制退出
     */
    public static final String LOGOUT_FORCE = "op_logout_force";

    /**
     * 角色授权
     */
    public static final String GRANT = "op_grant";

    /**
     * 修改状态
     */
    public static final String STATUS = "op_status";

    /**
     * 修改密码
     */
    public static final String PASSWD = "op_passwd";
}
