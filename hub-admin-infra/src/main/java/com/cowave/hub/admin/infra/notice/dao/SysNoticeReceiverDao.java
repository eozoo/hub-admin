package com.cowave.hub.admin.infra.notice.dao;

import com.cowave.hub.admin.domain.notice.repository.SysNoticeReceiverRepository;
import com.cowave.hub.admin.infra.notice.mapper.SysNoticeReceiverMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 通知接收记录仓储实现。
 */
@Repository
@RequiredArgsConstructor
public class SysNoticeReceiverDao implements SysNoticeReceiverRepository {

    private final SysNoticeReceiverMapper noticeReceiverMapper;

    @Override
    public long countUnread(Integer tenantId, Integer userId) {
        return noticeReceiverMapper.countUnread(tenantId, userId);
    }
}
