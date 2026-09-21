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
package com.cowave.hub.admin.domain.rbac2.repository.facade;

import com.cowave.hub.admin.domain.rbac2.entity.SysUser;

/**
 * @author shanhuiming
 */
public interface SysUserRepositoryFacade {

    /**
     * 按id查询用户
     */
    SysUser queryById(Integer userId);

    /**
     * 按账号查询用户
     */
    SysUser queryByAccount(String userAccount);

    /**
     * 检查账号是否已被使用，包含逻辑删除用户
     */
    boolean existsAccountIncludingDeleted(String userAccount);

}
