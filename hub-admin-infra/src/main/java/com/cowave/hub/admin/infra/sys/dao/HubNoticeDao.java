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
import com.cowave.hub.admin.domain.sys.entity.HubNotice;
import com.cowave.hub.admin.domain.sys.entity.HubNoticeComment;
import com.cowave.hub.admin.domain.sys.entity.HubNoticeLike;
import com.cowave.hub.admin.domain.sys.entity.HubNoticeUser;
import com.cowave.hub.admin.domain.sys.entity.pto.NoticeListPto;
import com.cowave.hub.admin.domain.sys.entity.query.NoticeQuery;
import com.cowave.hub.admin.domain.sys.enums.NoticeStatus;
import com.cowave.hub.admin.domain.sys.repository.HubNoticeRepository;
import com.cowave.hub.admin.infra.sys.mapper.HubNoticeCommentMapper;
import com.cowave.hub.admin.infra.sys.mapper.HubNoticeLikeMapper;
import com.cowave.hub.admin.infra.sys.mapper.HubNoticeMapper;
import com.cowave.hub.admin.infra.sys.mapper.HubNoticeUserMapper;
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
public class HubNoticeDao extends ServiceImpl<HubNoticeMapper, HubNotice> implements HubNoticeRepository {

    private final HubNoticeCommentMapper noticeCommentMapper;

    private final HubNoticeLikeMapper noticeLikeMapper;

    private final HubNoticeUserMapper noticeUserMapper;

    @Override
    public Page<HubNotice> queryPage(String tenantId, NoticeQuery query) {
        return lambdaQuery()
                .eq(HubNotice::getTenantId, tenantId)
                .eq(!Access.isAdminUser(), HubNotice::getCreateBy, Access.userCode())
                .eq(query.getNoticeType() != null, HubNotice::getNoticeType, query.getNoticeType())
                .eq(query.getNoticeStatus() != null, HubNotice::getNoticeStatus, query.getNoticeStatus())
                .like(StringUtils.isNotBlank(query.getNoticeTitle()), HubNotice::getNoticeTitle, query.getNoticeTitle())
                .orderByDesc(HubNotice::getCreateTime)
                .page(Access.page());
    }

    @Override
    public HubNotice queryById(String tenantId, Long noticeId) {
        return lambdaQuery()
                .eq(HubNotice::getTenantId, tenantId)
                .eq(HubNotice::getNoticeId, noticeId)
                .one();
    }

    @Override
    public String queryNameById(Long noticeId) {
        return lambdaQuery()
                .eq(HubNotice::getNoticeId, noticeId)
                .select(HubNotice::getNoticeTitle)
                .oneOpt().map(HubNotice::getNoticeTitle).orElse(null);
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
        lambdaUpdate().eq(HubNotice::getNoticeId, noticeId).set(HubNotice::getNoticeStatus, noticeStatus).update();
    }

    @Override
    public void updateNotice(HubNotice hubNotice) {
        lambdaUpdate().eq(HubNotice::getNoticeId, hubNotice.getNoticeId())
                .set(HubNotice::getUpdateBy, Access.userCode())
                .set(HubNotice::getUpdateTime, new Date())
                .set(HubNotice::getNoticeTitle, hubNotice.getNoticeTitle())
                .set(HubNotice::getNoticeType, hubNotice.getNoticeType())
                .set(HubNotice::getNoticeLevel, hubNotice.getNoticeLevel())
                .set(HubNotice::getContent, hubNotice.getContent())
                .set(HubNotice::getGoalsAll, hubNotice.getGoalsAll())
                .set(HubNotice::getGoalsDept, hubNotice.getGoalsDept())
                .set(HubNotice::getGoalsRole, hubNotice.getGoalsRole())
                .set(HubNotice::getGoalsUser, hubNotice.getGoalsUser())
                .update();
    }

    @Override
    public Page<NoticeListPto> queryMsgPage(String userCode) {
        return noticeUserMapper.msgList(Access.page(), userCode);
    }

    @Override
    public Long countUnReadByUser(String userCode) {
        return noticeUserMapper.selectCount(new LambdaQueryWrapper<HubNoticeUser>()
                .eq(HubNoticeUser::getUserCode, userCode)
                .eq(HubNoticeUser::getReadStatus, com.cowave.hub.admin.domain.sys.enums.NoticeReadStatus.UNREAD_PUBLISH));
    }

    @Override
    public Page<HubNoticeUser> queryUserPageByNoticeId(Long noticeId) {
        return noticeUserMapper.selectPage(Access.page(), new LambdaQueryWrapper<HubNoticeUser>()
                .eq(HubNoticeUser::getNoticeId, noticeId));
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
        return noticeUserMapper.update(null, new LambdaUpdateWrapper<HubNoticeUser>()
                .eq(HubNoticeUser::getUserCode, userCode)
                .eq(HubNoticeUser::getNoticeId, noticeId)
                .eq(HubNoticeUser::getReadStatus, com.cowave.hub.admin.domain.sys.enums.NoticeReadStatus.UNREAD_PUBLISH)
                .set(HubNoticeUser::getReadStatus, com.cowave.hub.admin.domain.sys.enums.NoticeReadStatus.READ_PUBLISH)
                .set(HubNoticeUser::getReadTime, readTime)) > 0;
    }

    @Override
    public void removeByNoticeId(Long noticeId) {
        noticeUserMapper.delete(new LambdaQueryWrapper<HubNoticeUser>()
                .eq(HubNoticeUser::getNoticeId, noticeId));
    }

    @Override
    public void updateLikeStatus(String userCode, Long noticeId, Integer likeStatus) {
        noticeUserMapper.update(null, new LambdaUpdateWrapper<HubNoticeUser>()
                .eq(HubNoticeUser::getUserCode, userCode)
                .eq(HubNoticeUser::getNoticeId, noticeId)
                .set(HubNoticeUser::getLikeStatus, likeStatus));
    }

    @Override
    public void incrCommentCount(String userCode, Long noticeId) {
        noticeUserMapper.update(null, new LambdaUpdateWrapper<HubNoticeUser>()
                .eq(HubNoticeUser::getUserCode, userCode)
                .eq(HubNoticeUser::getNoticeId, noticeId)
                .setSql("comment_count = comment_count + 1"));
    }

    @Override
    public void decrCommentCount(String userCode, Long noticeId) {
        noticeUserMapper.update(null, new LambdaUpdateWrapper<HubNoticeUser>()
                .eq(HubNoticeUser::getUserCode, userCode)
                .eq(HubNoticeUser::getNoticeId, noticeId)
                .setSql("comment_count = greatest(comment_count - 1, 0)"));
    }

    @Override
    public List<String> getUserCodesByNoticeId(Long noticeId) {
        List<HubNoticeUser> list = noticeUserMapper.selectList(new LambdaQueryWrapper<HubNoticeUser>()
                .eq(HubNoticeUser::getNoticeId, noticeId)
                .select(HubNoticeUser::getUserCode));
        return Collections.copyToList(list, HubNoticeUser::getUserCode);
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
    public List<HubNoticeComment> listComments(Long noticeId) {
        return noticeCommentMapper.selectList(new LambdaQueryWrapper<HubNoticeComment>()
                .eq(HubNoticeComment::getNoticeId, noticeId)
                .orderByAsc(HubNoticeComment::getCreateTime));
    }

    @Override
    public HubNoticeComment getComment(Long commentId) {
        return noticeCommentMapper.selectById(commentId);
    }

    @Override
    public void saveComment(HubNoticeComment comment) {
        noticeCommentMapper.insert(comment);
    }

    @Override
    public void removeComment(Long commentId) {
        noticeCommentMapper.deleteById(commentId);
    }

    @Override
    public void incrCommentLikes(Long commentId) {
        noticeCommentMapper.update(null, new LambdaUpdateWrapper<HubNoticeComment>()
                .setSql("like_count = like_count + 1")
                .eq(HubNoticeComment::getId, commentId));
    }

    @Override
    public void decrCommentLikes(Long commentId) {
        noticeCommentMapper.update(null, new LambdaUpdateWrapper<HubNoticeComment>()
                .setSql("like_count = greatest(like_count - 1, 0)")
                .eq(HubNoticeComment::getId, commentId));
    }

    @Override
    public List<HubNoticeLike> listLikes(Integer targetType, Long targetId) {
        return noticeLikeMapper.selectList(new LambdaQueryWrapper<HubNoticeLike>()
                .eq(HubNoticeLike::getTargetType, targetType)
                .eq(HubNoticeLike::getTargetId, targetId));
    }

    @Override
    public List<HubNoticeLike> likedTargets(Integer targetType, Set<Long> targetIds, String userCode) {
        return noticeLikeMapper.selectList(new LambdaQueryWrapper<HubNoticeLike>()
                .eq(HubNoticeLike::getTargetType, targetType)
                .in(HubNoticeLike::getTargetId, targetIds)
                .eq(HubNoticeLike::getUserCode, userCode));
    }

    @Override
    public boolean existsLike(Integer targetType, Long targetId, String userCode) {
        return noticeLikeMapper.exists(new LambdaQueryWrapper<HubNoticeLike>()
                .eq(HubNoticeLike::getTargetType, targetType)
                .eq(HubNoticeLike::getTargetId, targetId)
                .eq(HubNoticeLike::getUserCode, userCode));
    }

    @Override
    public void saveLike(HubNoticeLike like) {
        noticeLikeMapper.insert(like);
    }

    @Override
    public void removeLike(Integer targetType, Long targetId, String userCode) {
        noticeLikeMapper.delete(new LambdaQueryWrapper<HubNoticeLike>()
                .eq(HubNoticeLike::getTargetType, targetType)
                .eq(HubNoticeLike::getTargetId, targetId)
                .eq(HubNoticeLike::getUserCode, userCode));
    }
}
