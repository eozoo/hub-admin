package com.cowave.hub.admin.domain.rbac2.biz;

import com.cowave.hub.admin.domain.rbac2.entity.SysUser;

/**
 * @author shanhuiming
 */
public interface SysUserBiz {

    /**
     * 创建全局用户
     */
    void createUser(SysUser user);

    /**
     * 更新用户个人资料
     */
    void updateProfile(SysUser user);

    /**
     * 设置用户 MFA 密钥，传入 null 表示关闭
     */
    void updateMfa(Integer userId, String mfaKey);
}
