package com.cowave.hub.admin.domain.sys2.repository.facade;

import com.cowave.hub.admin.domain.sys2.entity.SysAttach;

/**
 * @author shanhuiming
 */
public interface SysAttachRepositoryFacade {

    /**
     * 查询用户最新的有效头像附件
     */
    SysAttach queryLatestUserAvatar(String userId);
}
