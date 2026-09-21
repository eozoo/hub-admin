package com.cowave.hub.admin.domain.auth.remote;

import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.bo.BilibiliUser;

/**
 * @author shanhuiming
 */
public interface BilibiliRemote {

    /**
     * 回调获取Bilibili用户信息
     */
    BilibiliUser getUser(SysAuthProvider provider, String code);
}
