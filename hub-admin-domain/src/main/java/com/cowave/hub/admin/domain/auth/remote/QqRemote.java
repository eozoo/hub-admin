package com.cowave.hub.admin.domain.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.bo.QqUser;

/**
 * @author shanhuiming
 */
public interface QqRemote {

    /**
     * 回调获取QQ用户信息
     */
    QqUser getUser(SysAuthProvider provider, String code);
}
