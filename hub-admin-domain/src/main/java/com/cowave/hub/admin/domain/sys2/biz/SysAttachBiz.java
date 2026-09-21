package com.cowave.hub.admin.domain.sys2.biz;

/**
 * @author shanhuiming
 */
public interface SysAttachBiz {

    /**
     * 获取用户最新头像的预览地址
     */
    String previewLatestAvatar(String userId) throws Exception;

    /**
     * 保留用户最近的头像附件
     */
    void reserveUserAvatars(String userId, int reserve);
}
