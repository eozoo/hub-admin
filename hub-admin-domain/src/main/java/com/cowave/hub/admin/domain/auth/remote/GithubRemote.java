package com.cowave.hub.admin.domain.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.bo.GithubUser;

/**
 * @author shanhuiming
 */
public interface GithubRemote {

    /**
     * 回调获取GitHub用户信息
     */
    GithubUser getUser(SysAuthProvider provider, String code);
}
