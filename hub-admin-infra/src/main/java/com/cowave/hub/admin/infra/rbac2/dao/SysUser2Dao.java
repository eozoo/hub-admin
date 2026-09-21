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
package com.cowave.hub.admin.infra.rbac2.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.rbac2.entity.SysUser;
import com.cowave.hub.admin.domain.rbac2.repository.SysUserRepository;
import com.cowave.hub.admin.infra.rbac2.mapper.SysUser2Mapper;
import com.cowave.zoo.framework.access.Access;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Repository
public class SysUser2Dao extends ServiceImpl<SysUser2Mapper, SysUser> implements SysUserRepository {

    @Override
    public void createUser(SysUser user) {
        baseMapper.insert(user);
    }

    @Override
    public boolean existsAccountIncludingDeleted(String userAccount) {
        return baseMapper.countAccountIncludingDeleted(userAccount) > 0;
    }

    @Override
    public void updateProfile(SysUser user) {
        lambdaUpdate()
                .eq(SysUser::getUserId, user.getUserId())
                .set(SysUser::getUserName, user.getUserName())
                .set(SysUser::getUserSex, user.getUserSex())
                .set(SysUser::getUserPhone, user.getUserPhone())
                .set(SysUser::getUserEmail, user.getUserEmail())
                .set(SysUser::getUpdateBy, Access.userAccount())
                .set(SysUser::getUpdateTime, new Date())
                .update();
    }

    @Override
    public SysUser queryById(Integer userId) {
        return getById(userId);
    }

    @Override
    public SysUser queryByAccount(String userAccount) {
        return lambdaQuery()
                .eq(SysUser::getUserAccount, userAccount)
                .one();
    }

    @Override
    public void updateMfa(Integer userId, String mfaKey) {
        lambdaUpdate()
                .eq(SysUser::getUserId, userId)
                .set(SysUser::getMfa, mfaKey)
                .update();
    }
}
