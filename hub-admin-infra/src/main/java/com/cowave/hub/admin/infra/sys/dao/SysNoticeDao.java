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
package com.cowave.hub.admin.infra.sys.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.sys.entity.SysNotice;
import com.cowave.hub.admin.domain.sys.entity.SysNoticeComment;
import com.cowave.hub.admin.domain.sys.entity.SysNoticeLike;
import com.cowave.hub.admin.domain.sys.entity.SysNoticeUser;
import com.cowave.hub.admin.domain.sys.entity.pto.NoticeListPto;
import com.cowave.hub.admin.domain.sys.entity.query.NoticeQuery;
import com.cowave.hub.admin.domain.sys.enums.NoticeStatus;
import com.cowave.hub.admin.domain.sys.repository.SysNoticeRepository;
import com.cowave.hub.admin.infra.sys.mapper.SysNoticeCommentMapper;
import com.cowave.hub.admin.infra.sys.mapper.SysNoticeLikeMapper;
import com.cowave.hub.admin.infra.sys.mapper.SysNoticeMapper;
import com.cowave.hub.admin.infra.sys.mapper.SysNoticeUserMapper;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.tools.Collections;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class SysNoticeDao extends ServiceImpl<SysNoticeMapper, SysNotice> implements SysNoticeRepository {

    private final SysNoticeCommentMapper noticeCommentMapper;

    private final SysNoticeLikeMapper noticeLikeMapper;

    private final SysNoticeUserMapper noticeUserMapper;

    @Override
    public Page<SysNotice> queryPage(String tenantId, NoticeQuery query) {
        return lambdaQuery()
                .eq(SysNotice::getTenantId, tenantId)
                .eq(!Access.isAdminUser(), SysNotice::getCreateBy, Access.userCode())
                .eq(query.getNoticeType() != null, SysNotice::getNoticeType, query.getNoticeType())
                .eq(query.getNoticeStatus() != null, SysNotice::getNoticeStatus, query.getNoticeStatus())
                .like(StringUtils.isNotBlank(query.getNoticeTitle()), SysNotice::getNoticeTitle, query.getNoticeTitle())
                .orderByDesc(SysNotice::getCreateTime)
                .page(Access.page());
    }

    @Override
    public SysNotice queryById(String tenantId, Long noticeId) {
        return lambdaQuery()
                .eq(SysNotice::getTenantId, tenantId)
                .eq(SysNotice::getNoticeId, noticeId)
                .one();
    }

    @Override
    public String queryNameById(Long noticeId) {
        return lambdaQuery()
                .eq(SysNotice::getNoticeId, noticeId)
                .select(SysNotice::getNoticeTitle)
                .oneOpt().map(SysNotice::getNoticeTitle).orElse(null);
    }

    @Override
    public void updateMsgStat(Long noticeId, NoticeStatus noticeStatus, Date publishTime) {
        baseMapper.updateMsgStat(noticeId, noticeStatus, publishTime);
    }

    @Override
    public void updateReadStat(Long noticeId) {
        baseMapper.updateReadStat(noticeId);
    }

    @Override
    public void updateNoticeStatForNewUser() {
        baseMapper.updateNoticeStatForNewUser();
    }

    @Override
    public void updateStatus(Long noticeId, NoticeStatus noticeStatus) {
        lambdaUpdate().eq(SysNotice::getNoticeId, noticeId).set(SysNotice::getNoticeStatus, noticeStatus).update();
    }

    @Override
    public void updateNotice(SysNotice sysNotice) {
        lambdaUpdate().eq(SysNotice::getNoticeId, sysNotice.getNoticeId())
                .set(SysNotice::getUpdateBy, Access.userCode())
                .set(SysNotice::getUpdateTime, new Date())
                .set(SysNotice::getNoticeTitle, sysNotice.getNoticeTitle())
                .set(SysNotice::getNoticeType, sysNotice.getNoticeType())
                .set(SysNotice::getNoticeLevel, sysNotice.getNoticeLevel())
                .set(SysNotice::getContent, sysNotice.getContent())
                .set(SysNotice::getGoalsAll, sysNotice.getGoalsAll())
                .set(SysNotice::getGoalsDept, sysNotice.getGoalsDept())
                .set(SysNotice::getGoalsRole, sysNotice.getGoalsRole())
                .set(SysNotice::getGoalsUser, sysNotice.getGoalsUser())
                .update();
    }

    @Override
    public Page<NoticeListPto> queryMsgPage(String userCode) {
        return noticeUserMapper.msgList(Access.page(), userCode);
    }

    @Override
    public Long countUnReadByUser(String userCode) {
        return noticeUserMapper.selectCount(new LambdaQueryWrapper<SysNoticeUser>()
                .eq(SysNoticeUser::getUserCode, userCode)
                .eq(SysNoticeUser::getReadStatus, com.cowave.hub.admin.domain.sys.enums.NoticeReadStatus.UNREAD_PUBLISH));
    }

    @Override
    public Page<SysNoticeUser> queryUserPageByNoticeId(Long noticeId) {
        return noticeUserMapper.selectPage(Access.page(), new LambdaQueryWrapper<SysNoticeUser>()
                .eq(SysNoticeUser::getNoticeId, noticeId));
    }

    @Override
    public void msgDelete(String userCode, Long msgId) {
        noticeUserMapper.msgDelete(userCode, msgId);
    }

    @Override
    public void initNoticeMsgForNewUser(String userCode) {
        noticeUserMapper.initNoticeMsgForNewUser(userCode);
    }

    @Override
    public boolean updateReadStatus(String userCode, Long noticeId, Date readTime) {
        return noticeUserMapper.update(null, new LambdaUpdateWrapper<SysNoticeUser>()
                .eq(SysNoticeUser::getUserCode, userCode)
                .eq(SysNoticeUser::getNoticeId, noticeId)
                .eq(SysNoticeUser::getReadStatus, com.cowave.hub.admin.domain.sys.enums.NoticeReadStatus.UNREAD_PUBLISH)
                .set(SysNoticeUser::getReadStatus, com.cowave.hub.admin.domain.sys.enums.NoticeReadStatus.READ_PUBLISH)
                .set(SysNoticeUser::getReadTime, readTime)) > 0;
    }

    @Override
    public void removeByNoticeId(Long noticeId) {
        noticeUserMapper.delete(new LambdaQueryWrapper<SysNoticeUser>()
                .eq(SysNoticeUser::getNoticeId, noticeId));
    }

    @Override
    public void updateLikeStatus(String userCode, Long noticeId, Integer likeStatus) {
        noticeUserMapper.update(null, new LambdaUpdateWrapper<SysNoticeUser>()
                .eq(SysNoticeUser::getUserCode, userCode)
                .eq(SysNoticeUser::getNoticeId, noticeId)
                .set(SysNoticeUser::getLikeStatus, likeStatus));
    }

    @Override
    public void incrCommentCount(String userCode, Long noticeId) {
        noticeUserMapper.update(null, new LambdaUpdateWrapper<SysNoticeUser>()
                .eq(SysNoticeUser::getUserCode, userCode)
                .eq(SysNoticeUser::getNoticeId, noticeId)
                .setSql("comment_count = comment_count + 1"));
    }

    @Override
    public void decrCommentCount(String userCode, Long noticeId) {
        noticeUserMapper.update(null, new LambdaUpdateWrapper<SysNoticeUser>()
                .eq(SysNoticeUser::getUserCode, userCode)
                .eq(SysNoticeUser::getNoticeId, noticeId)
                .setSql("comment_count = greatest(comment_count - 1, 0)"));
    }

    @Override
    public List<String> getUserCodesByNoticeId(Long noticeId) {
        List<SysNoticeUser> list = noticeUserMapper.selectList(new LambdaQueryWrapper<SysNoticeUser>()
                .eq(SysNoticeUser::getNoticeId, noticeId)
                .select(SysNoticeUser::getUserCode));
        return Collections.copyToList(list, SysNoticeUser::getUserCode);
    }

    @Override
    public void insertReadOfAll(String tenantId, Long noticeId) {
        noticeUserMapper.insertReadOfAll(tenantId, noticeId);
    }

    @Override
    public void insertReadOfDept(String tenantId, Long noticeId, List<Integer> list) {
        noticeUserMapper.insertReadOfDept(tenantId, noticeId, list);
    }

    @Override
    public void insertReadOfRole(String tenantId, Long noticeId, List<Integer> list) {
        noticeUserMapper.insertReadOfRole(tenantId, noticeId, list);
    }

    @Override
    public void insertReadOfUser(String tenantId, Long noticeId, List<Integer> list) {
        noticeUserMapper.insertReadOfUser(tenantId, noticeId, list);
    }

    @Override
    public List<SysNoticeComment> listComments(Long noticeId) {
        return noticeCommentMapper.selectList(new LambdaQueryWrapper<SysNoticeComment>()
                .eq(SysNoticeComment::getNoticeId, noticeId)
                .orderByAsc(SysNoticeComment::getCreateTime));
    }

    @Override
    public SysNoticeComment getComment(Long commentId) {
        return noticeCommentMapper.selectById(commentId);
    }

    @Override
    public void saveComment(SysNoticeComment comment) {
        noticeCommentMapper.insert(comment);
    }

    @Override
    public void removeComment(Long commentId) {
        noticeCommentMapper.deleteById(commentId);
    }

    @Override
    public void incrCommentLikes(Long commentId) {
        noticeCommentMapper.update(null, new LambdaUpdateWrapper<SysNoticeComment>()
                .setSql("like_count = like_count + 1")
                .eq(SysNoticeComment::getId, commentId));
    }

    @Override
    public void decrCommentLikes(Long commentId) {
        noticeCommentMapper.update(null, new LambdaUpdateWrapper<SysNoticeComment>()
                .setSql("like_count = greatest(like_count - 1, 0)")
                .eq(SysNoticeComment::getId, commentId));
    }

    @Override
    public List<SysNoticeLike> listLikes(Integer targetType, Long targetId) {
        return noticeLikeMapper.selectList(new LambdaQueryWrapper<SysNoticeLike>()
                .eq(SysNoticeLike::getTargetType, targetType)
                .eq(SysNoticeLike::getTargetId, targetId));
    }

    @Override
    public List<SysNoticeLike> likedTargets(Integer targetType, Set<Long> targetIds, String userCode) {
        return noticeLikeMapper.selectList(new LambdaQueryWrapper<SysNoticeLike>()
                .eq(SysNoticeLike::getTargetType, targetType)
                .in(SysNoticeLike::getTargetId, targetIds)
                .eq(SysNoticeLike::getUserCode, userCode));
    }

    @Override
    public boolean existsLike(Integer targetType, Long targetId, String userCode) {
        return noticeLikeMapper.exists(new LambdaQueryWrapper<SysNoticeLike>()
                .eq(SysNoticeLike::getTargetType, targetType)
                .eq(SysNoticeLike::getTargetId, targetId)
                .eq(SysNoticeLike::getUserCode, userCode));
    }

    @Override
    public void saveLike(SysNoticeLike like) {
        noticeLikeMapper.insert(like);
    }

    @Override
    public void removeLike(Integer targetType, Long targetId, String userCode) {
        noticeLikeMapper.delete(new LambdaQueryWrapper<SysNoticeLike>()
                .eq(SysNoticeLike::getTargetType, targetType)
                .eq(SysNoticeLike::getTargetId, targetId)
                .eq(SysNoticeLike::getUserCode, userCode));
    }
}
