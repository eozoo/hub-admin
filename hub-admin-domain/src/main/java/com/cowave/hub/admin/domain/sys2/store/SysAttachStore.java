package com.cowave.hub.admin.domain.sys2.store;

import com.cowave.hub.admin.domain.sys2.entity.SysAttach;

/**
 * @author shanhuiming
 */
public interface SysAttachStore {

    /**
     * 获取附件预览地址
     */
    String preview(SysAttach attach) throws Exception;
}
