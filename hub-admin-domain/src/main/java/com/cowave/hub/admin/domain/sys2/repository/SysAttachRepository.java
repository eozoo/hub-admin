package com.cowave.hub.admin.domain.sys2.repository;

import com.cowave.hub.admin.domain.sys2.repository.facade.SysAttachRepositoryFacade;

/**
 * @author shanhuiming
 */
public interface SysAttachRepository extends SysAttachRepositoryFacade {

    /**
     * 保留最近的头像附件，软删除更早的记录
     */
    void reserveUserAvatars(String userId, int reserve);
}
