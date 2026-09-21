package com.cowave.hub.admin.domain.rbac2.biz.impl;

import com.cowave.hub.admin.domain.rbac2.biz.SysUserBiz;
import com.cowave.hub.admin.domain.rbac2.entity.SysUser;
import com.cowave.hub.admin.domain.rbac2.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class SysUserBiz2Impl implements SysUserBiz {
    private final SysUserRepository userRepository;

    @Override
    public void createUser(SysUser user) {
        userRepository.createUser(user);
    }

    @Override
    public void updateProfile(SysUser user) {
        userRepository.updateProfile(user);
    }

    @Override
    public void updateMfa(Integer userId, String mfaKey) {
        userRepository.updateMfa(userId, mfaKey);
    }
}
