package com.cowave.hub.admin.domain.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.bo.WechatUser;

/**
 * @author shanhuiming
 */
public interface WechatRemote {

    /**
     * 回调获取微信用户信息
     */
    WechatUser getUser(SysAuthProvider provider, String code);
}
