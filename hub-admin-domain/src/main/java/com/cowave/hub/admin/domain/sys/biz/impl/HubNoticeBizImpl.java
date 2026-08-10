/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.domain.sys.biz.impl;

import com.cowave.hub.admin.domain.sys.biz.HubNoticeBiz;
import com.cowave.hub.admin.domain.sys.entity.*;
import com.cowave.hub.admin.domain.sys.entity.command.NoticeCommentCreate;
import com.cowave.hub.admin.domain.sys.entity.command.NoticeCreate;
import com.cowave.hub.admin.domain.sys.entity.command.NoticeCreateAttach;
import com.cowave.hub.admin.domain.sys.enums.NoticeLevel;
import com.cowave.hub.admin.domain.sys.enums.NoticeStatus;
import com.cowave.hub.admin.domain.sys.enums.NoticeType;
import com.cowave.hub.admin.domain.sys.repository.HubAttachRepository;
import com.cowave.hub.admin.domain.sys.repository.HubNoticeRepository;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.zoo.http.client.constants.HttpCode.FORBIDDEN;
import static com.cowave.zoo.http.client.constants.HttpCode.NOT_FOUND;
import static com.cowave.hub.admin.domain.sys.enums.NoticeStatus.*;
import static com.cowave.hub.admin.domain.rbac.enums.YesNo.*;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubNoticeBizImpl implements HubNoticeBiz {

    private final HubNoticeRepository noticeRepository;

    private final HubAttachRepository attachRepository;

    @Override
    public List<Long> createNotice(NoticeCreate notice) {
        noticeRepository.save(notice);
        return syncAttaches(notice);
    }

    @Override
    public List<Long> editNotice(String tenantId, NoticeCreate notice, String currentUserCode, boolean isAdmin) {
        HttpAsserts.notNull(notice.getNoticeId(), BAD_REQUEST, "{admin.notice.id.null}");

        HubNotice pre = noticeRepository.queryById(tenantId, notice.getNoticeId());
        HttpAsserts.notNull(pre, NOT_FOUND, "{admin.notice.not.exist}", notice.getNoticeId());
        HttpAsserts.equals(DRAFT, pre.getNoticeStatus(), BAD_REQUEST, "{admin.notice.edit.unpublish}");

        if (!isAdmin) {
            HttpAsserts.equals(pre.getCreateBy(), currentUserCode, FORBIDDEN, "{admin.notice.edit.self}");
        }
        noticeRepository.updateNotice(notice);
        return syncAttaches(notice);
    }

    @Override
    public List<Long> deleteNotice(String tenantId, Long noticeId, String currentUserCode, boolean isAdmin) {
        HubNotice notice = noticeRepository.queryById(tenantId, noticeId);
        if (notice == null) {
            return Collections.emptyList();
        }
        if (!isAdmin) {
            HttpAsserts.equals(notice.getCreateBy(), currentUserCode, FORBIDDEN, "{admin.notice.delete.self}");
        }

        NoticeStatus status = notice.getNoticeStatus();
        if (DRAFT == status) {
            noticeRepository.removeById(noticeId);
            return getAttachIds(noticeId);
        } else if (PUBLISH == status) {
            noticeRepository.updateStatus(noticeId, RECALL);
            return Collections.emptyList();
        } else if (RECALL == status) {
            noticeRepository.removeById(noticeId);
            noticeRepository.removeByNoticeId(noticeId);
            return getAttachIds(noticeId);
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> publishNotice(String tenantId, Long noticeId, String currentUserCode, boolean isAdmin) {
        HubNotice notice = noticeRepository.queryById(tenantId, noticeId);
        HttpAsserts.notNull(notice, NOT_FOUND, "{admin.notice.not.exist}", noticeId);
        if (!isAdmin) {
            HttpAsserts.equals(notice.getCreateBy(), currentUserCode, FORBIDDEN, "{admin.notice.publish.self}");
        }
        if (DRAFT != notice.getNoticeStatus()) {
            return Collections.emptyList();
        }

        if (YES == notice.getGoalsAll()) {
            noticeRepository.insertReadOfAll(tenantId, noticeId);
        } else {
            noticeRepository.insertReadOfDept(tenantId, noticeId, notice.getGoalsDept());
            noticeRepository.insertReadOfRole(tenantId, noticeId, notice.getGoalsRole());
            noticeRepository.insertReadOfUser(tenantId, noticeId, notice.getGoalsUser());
        }
        noticeRepository.updateMsgStat(noticeId, PUBLISH, new Date());

        return noticeRepository.getUserCodesByNoticeId(noticeId);
    }

    @Override
    public void msgRead(String userCode, Long noticeId) {
        boolean updated = noticeRepository.updateReadStatus(userCode, noticeId, new Date());
        if (updated) {
            noticeRepository.updateReadStat(noticeId);
        }
    }

    @Override
    public void msgDelete(String userCode, Long msgId) {
        noticeRepository.msgDelete(userCode, msgId);
    }

    @Override
    public void msgLike(String userCode, Long noticeId, Integer likeStatus) {
        noticeRepository.updateLikeStatus(userCode, noticeId, likeStatus);
    }

    @Override
    public void createComment(NoticeCommentCreate command, String userCode, String userName) {
        HubNoticeComment comment = new HubNoticeComment();
        comment.setNoticeId(command.getNoticeId());
        comment.setParentId(command.getParentId() != null ? command.getParentId() : 0L);
        comment.setReplyToCode(command.getReplyToCode());
        comment.setReplyToName(command.getReplyToName());
        comment.setUserCode(userCode);
        comment.setUserName(userName);
        comment.setContent(command.getContent());
        comment.setLikeCount(0);
        comment.setCreateTime(new Date());
        noticeRepository.saveComment(comment);
        noticeRepository.incrCommentCount(userCode, command.getNoticeId());
    }

    @Override
    public void deleteComment(Long commentId, String currentUserCode, boolean isAdmin) {
        HubNoticeComment comment = noticeRepository.getComment(commentId);
        if (comment == null) {
            return;
        }
        if (!isAdmin) {
            HttpAsserts.equals(comment.getUserCode(), currentUserCode, FORBIDDEN, "{admin.feedback.comment.delete.denied}");
        }
        noticeRepository.removeComment(commentId);
        noticeRepository.decrCommentCount(comment.getUserCode(), comment.getNoticeId());
    }

    @Override
    public boolean toggleCommentLike(Long commentId, String userCode, String userName) {
        boolean exists = noticeRepository.existsLike(2, commentId, userCode);
        if (exists) {
            noticeRepository.removeLike(2, commentId, userCode);
            noticeRepository.decrCommentLikes(commentId);
            return false;
        }

        HubNoticeLike like = new HubNoticeLike();
        like.setTargetType(2);
        like.setTargetId(commentId);
        like.setUserCode(userCode);
        like.setUserName(userName);
        like.setCreateTime(new Date());
        noticeRepository.saveLike(like);
        noticeRepository.incrCommentLikes(commentId);
        return true;
    }

    /**
     * 同步内容中的附件：存在的绑定到公告，不存在的返回给外部删除
     */
    private List<Long> syncAttaches(NoticeCreate notice) {
        List<NoticeCreateAttach> attaches = notice.getAttaches();
        if (attaches == null) {
            return Collections.emptyList();
        }
        String content = notice.getContent();
        List<Long> removeIds = new ArrayList<>();
        for (NoticeCreateAttach attach : attaches) {
            if (content != null && content.contains(attach.getAttachPath())) {
                attachRepository.updateOwner(String.valueOf(notice.getNoticeId()), attach.getAttachId());
            } else {
                removeIds.add(attach.getAttachId());
            }
        }
        return removeIds;
    }

    private List<Long> getAttachIds(Long noticeId) {
        List<HubAttach> attaches = attachRepository.listByOwner(
                String.valueOf(noticeId), "SYSTEM_NOTICE", null);
        return attaches.stream().map(HubAttach::getAttachId).toList();
    }

    @Override
    public void initNoticeMsgForNewUser(String userCode) {
        noticeRepository.initNoticeMsgForNewUser(userCode);
    }

    @Override
    public void updateNoticeStatForNewUser() {
        noticeRepository.updateNoticeStatForNewUser();
    }

    @Override
    public void sendFlowNotice(String processName, String taskName, String startUser, String startUserName, String assigneeUser) {
        HubNotice notice = new HubNotice();
        notice.setNoticeStatus(NoticeStatus.PUBLISH);
        notice.setCreateBy(Access.userCode());
        notice.setNoticeType(NoticeType.PRESS);
        notice.setNoticeLevel(NoticeLevel.COMMON);
        notice.setIsSystem(NO);
        notice.setStatTotal(1);
        notice.setStatRead(0);
        notice.setCreateTime(new Date());
        notice.setPublishTime(new Date());
        notice.setNoticeTitle(startUserName + "的" + processName + "[" + taskName + "]");
        notice.setContent("<p>催办提醒: </p><p>" + startUserName + "的" + processName + "[" + taskName + "]</p>");
        noticeRepository.save(notice);
    }
}
