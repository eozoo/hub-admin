package com.cowave.hub.admin.domain.sys2.biz.impl;

import com.cowave.hub.admin.domain.sys2.biz.SysAttachBiz;
import com.cowave.hub.admin.domain.sys2.entity.SysAttach;
import com.cowave.hub.admin.domain.sys2.repository.SysAttachRepository;
import com.cowave.hub.admin.domain.sys2.store.SysAttachStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class SysAttachBiz2Impl implements SysAttachBiz {
    private final SysAttachRepository attachRepository;
    private final SysAttachStore attachStore;

    @Override
    public String previewLatestAvatar(String userId) throws Exception {
        SysAttach avatar = attachRepository.queryLatestUserAvatar(userId);
        if (avatar == null) {
            return null;
        }
        return attachStore.preview(avatar);
    }

    @Override
    public void reserveUserAvatars(String userId, int reserve) {
        attachRepository.reserveUserAvatars(userId, reserve);
    }
}
