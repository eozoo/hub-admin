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
package com.cowave.hub.admin.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.biz.SysNoticeBiz;
import com.cowave.hub.admin.domain.sys.entity.*;
import com.cowave.hub.admin.domain.sys.entity.command.NoticeCommentCreate;
import com.cowave.hub.admin.domain.sys.entity.command.NoticeCreate;
import com.cowave.hub.admin.domain.sys.entity.pto.NoticeListPto;
import com.cowave.hub.admin.domain.sys.entity.query.NoticeQuery;
import com.cowave.hub.admin.domain.sys.entity.vo.NoticeUserVo;
import com.cowave.hub.admin.domain.sys.entity.vo.NoticeVo;
import com.cowave.hub.admin.domain.sys.entity.vo.NoticeCommentVo;
import com.cowave.hub.admin.domain.sys.repository.facade.SysNoticeRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.domain.sys.biz.SysAttachBiz;
import com.cowave.hub.admin.service.sys.SysNoticeService;
import com.cowave.zoo.framework.access.Access;
import com.cowave.hub.admin.domain.sys.sender.NoticePushSender;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.tools.Converts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cowave.hub.admin.domain.sys.enums.NoticeLevel.COMMON;
import static com.cowave.hub.admin.domain.sys.enums.NoticeStatus.PUBLISH;
import static com.cowave.hub.admin.domain.sys.enums.NoticeType.PRESS;
import static com.cowave.hub.admin.domain.rbac.enums.YesNo.NO;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
public class SysNoticeServiceImpl implements SysNoticeService {
    private final NoticePushSender noticePushSender;
    private final SysAttachBiz attachBiz;
    private final SysNoticeBiz noticeBiz;
    private final SysNoticeRepositoryFacade noticeRepositoryFacade;
    private final SysUserRepositoryFacade userRepositoryFacade;

    @Override
    public Response.Page<NoticeVo> list(String tenantId, NoticeQuery query) {
        Page<SysNotice> page = noticeRepositoryFacade.queryPage(tenantId, query);
        Map<String, String> codeNameMap = resolveCodeNameMap(page.getRecords(), SysNotice::getCreateBy);
        List<NoticeVo> list = page.getRecords().stream().map(n -> {
            NoticeVo vo = Converts.copyProperties(n, NoticeVo.class);
            vo.setCreateUserName(codeNameMap.get(n.getCreateBy()));
            return vo;
        }).toList();
        return new Response.Page<>(list, page.getTotal());
    }

    @Override
    public NoticeVo info(String tenantId, Long noticeId) {
        SysNotice notice = noticeRepositoryFacade.queryById(tenantId, noticeId);
        NoticeVo vo = Converts.copyProperties(notice, NoticeVo.class);
        vo.setCreateUserName(userRepositoryFacade.queryNameByCode(notice.getCreateBy()));
        return vo;
    }

    @Override
    public void add(String tenantId, NoticeCreate notice) throws Exception {
        notice.setTenantId(tenantId);
        List<Long> attachIds = noticeBiz.createNotice(notice);
        attachBiz.removeAttachByIds(attachIds);
    }

    @Override
    public Response.Page<NoticeUserVo> queryNoticeReaders(String tenantId, Long noticeId) {
        Page<SysNoticeUser> page = noticeRepositoryFacade.queryUserPageByNoticeId(noticeId);
        Map<String, String> codeNameMap = resolveCodeNameMap(page.getRecords(), SysNoticeUser::getUserCode);

        List<NoticeUserVo> list = page.getRecords().stream().map(u -> {
            NoticeUserVo vo = Converts.copyProperties(u, NoticeUserVo.class);
            vo.setUserName(codeNameMap.get(u.getUserCode()));
            return vo;
        }).toList();
        return new Response.Page<>(list, page.getTotal());
    }

    @Override
    public Page<NoticeListPto> queryMsgList() {
        Page<NoticeListPto> page = noticeRepositoryFacade.queryMsgPage(Access.userCode());
        Map<String, String> codeNameMap = resolveCodeNameMap(page.getRecords(), NoticeListPto::getCreateBy);
        page.getRecords().forEach(dto -> dto.setCreateBy(codeNameMap.get(dto.getCreateBy())));
        return page;
    }

    @Override
    public long msgUnReadCount(String userCode) {
        return noticeRepositoryFacade.countUnReadByUser(userCode);
    }

    @Override
    public List<NoticeCommentVo> queryCommentList(Long noticeId) {
        List<SysNoticeComment> comments = noticeRepositoryFacade.listComments(noticeId);
        if (CollectionUtils.isEmpty(comments)) {
            return List.of();
        }

        String userCode = Access.userCode();
        Set<Long> commentIds = comments.stream().map(SysNoticeComment::getId).collect(Collectors.toSet());
        Set<Long> likedIds = noticeRepositoryFacade.likedTargets(2, commentIds, userCode).stream()
                .map(SysNoticeLike::getTargetId).collect(Collectors.toSet());
        return comments.stream().map(c -> {
            NoticeCommentVo vo = Converts.copyProperties(c, NoticeCommentVo.class);
            vo.setLiked(likedIds.contains(c.getId()));
            vo.setIsMine(userCode.equals(c.getUserCode()));
            return vo;
        }).toList();
    }

    @Override
    public List<SysNoticeLike> queryLikers(Integer targetType, Long targetId) {
        return noticeRepositoryFacade.listLikes(targetType, targetId);
    }

    @Override
    public void edit(String tenantId, NoticeCreate notice) throws Exception {
        List<Long> attachIds = noticeBiz.editNotice(tenantId, notice, Access.userCode(), Access.isAdminUser());
        attachBiz.removeAttachByIds(attachIds);
    }

    @Override
    public void delete(String tenantId, List<Long> noticeIds) throws Exception {
        for (Long noticeId : noticeIds) {
            List<Long> attachIds = noticeBiz.deleteNotice(tenantId, noticeId, Access.userCode(), Access.isAdminUser());
            attachBiz.removeAttachByIds(attachIds);
        }
    }

    @Override
    public void publish(String tenantId, Long noticeId) {
        List<String> userCodes = noticeBiz.publishNotice(tenantId, noticeId, Access.userCode(), Access.isAdminUser());
        if (!userCodes.isEmpty()) {
            SysNotice notice = noticeRepositoryFacade.queryById(tenantId, noticeId);
            noticePushSender.sendNewNotice(userCodes, notice.getNoticeTitle());
        }
    }

    @Override
    public void msgRead(Long noticeId) {
        noticeBiz.msgRead(Access.userCode(), noticeId);
    }

    @Override
    public void msgDelete(Long msgId) {
        noticeBiz.msgDelete(Access.userCode(), msgId);
    }

    @Override
    public void msgLike(Long noticeId, Integer likeStatus) {
        noticeBiz.msgLike(Access.userCode(), noticeId, likeStatus);
    }

    @Override
    public void commentAdd(NoticeCommentCreate commentCreate) {
        noticeBiz.createComment(commentCreate, Access.userCode(), Access.userName());
    }

    @Override
    public void commentDelete(Long commentId) {
        noticeBiz.deleteComment(commentId, Access.userCode(), Access.isAdminUser());
    }

    @Override
    public boolean commentLike(Long commentId) {
        return noticeBiz.toggleCommentLike(commentId, Access.userCode(), Access.userName());
    }

    @Override
    public void sendUserNotice(SysNotice notice, Integer userId) {
        // noticeRepository.save(notice);
    }

    @Override
    public void sendFlowNotice(String processName, String taskName, Integer startUser, Integer assigneeUser) {
        String startUserName = userRepositoryFacade.queryNameById(startUser);
        SysNotice notice = new SysNotice();
        notice.setNoticeStatus(PUBLISH);
        notice.setCreateBy(Access.userCode());
        notice.setNoticeType(PRESS);
        notice.setNoticeLevel(COMMON);
        notice.setIsSystem(NO);
        notice.setStatTotal(1);
        notice.setStatRead(0);
        notice.setCreateTime(new Date());
        notice.setPublishTime(new Date());
        notice.setNoticeTitle(startUserName + "的" + processName + "[" + taskName + "]");
        notice.setContent("<p>催办提醒: </p><p>" + startUserName + "的" + processName + "[" + taskName + "]</p>");
        // noticeRepository.save(notice);
    }

    private <T> Map<String, String> resolveCodeNameMap(List<T> list, Function<T, String> codeExtractor) {
        Set<String> codes = new HashSet<>();
        for (T item : list) {
            String code = codeExtractor.apply(item);
            if (code != null) {
                codes.add(code);
            }
        }
        return userRepositoryFacade.queryCodeNameMap(codes);
    }
}
