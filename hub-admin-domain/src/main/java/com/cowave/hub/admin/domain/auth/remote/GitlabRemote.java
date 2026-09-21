package com.cowave.hub.admin.domain.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.bo.GitlabUser;

/**
 * @author shanhuiming
 */
public interface GitlabRemote {

    /**
     * 回调获取gitlab用户信息
     */
    GitlabUser getUser(SysAuthProvider provider, String code);
}
