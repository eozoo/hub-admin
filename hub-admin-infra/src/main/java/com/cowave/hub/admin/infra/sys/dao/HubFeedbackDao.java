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
import com.cowave.hub.admin.domain.sys.entity.HubFeedback;
import com.cowave.hub.admin.domain.sys.entity.HubFeedbackComment;
import com.cowave.hub.admin.domain.sys.entity.HubFeedbackLike;
import com.cowave.hub.admin.domain.sys.entity.pto.FeedbackStatPto;
import com.cowave.hub.admin.domain.sys.entity.query.FeedbackQuery;
import com.cowave.hub.admin.domain.sys.repository.HubFeedbackRepository;
import com.cowave.hub.admin.infra.sys.mapper.HubFeedbackCommentMapper;
import com.cowave.hub.admin.infra.sys.mapper.HubFeedbackLikeMapper;
import com.cowave.hub.admin.infra.sys.mapper.HubFeedbackMapper;
import com.cowave.zoo.framework.access.Access;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class HubFeedbackDao extends ServiceImpl<HubFeedbackMapper, HubFeedback> implements HubFeedbackRepository {

    private final HubFeedbackCommentMapper feedbackCommentMapper;

    private final HubFeedbackLikeMapper feedbackLikeMapper;

    @Override
    public FeedbackStatPto queryStat(String tenantId) {
        FeedbackStatPto stat = baseMapper.queryStat(tenantId);
        if (stat == null) {
            stat = new FeedbackStatPto();
            stat.setTotal(0L);
            stat.setAvgScore(0.0);
        }
        Long[] scoreCount = new Long[6];
        for (int s = 1; s <= 5; s++) {
            scoreCount[s] = lambdaQuery()
                    .eq(HubFeedback::getTenantId, tenantId)
                    .eq(HubFeedback::getScore, s)
                    .count();
        }
        stat.setScoreCount(scoreCount);
        return stat;
    }

    @Override
    public Page<HubFeedback> queryPage(String tenantId, FeedbackQuery query) {
        return lambdaQuery()
                .eq(HubFeedback::getTenantId, tenantId)
                .eq(query.getScore() != null, HubFeedback::getScore, query.getScore())
                .orderByDesc(HubFeedback::getCreateTime)
                .page(Access.page());
    }

    @Override
    public void incrLikeCount(Long feedbackId) {
        lambdaUpdate()
                .eq(HubFeedback::getId, feedbackId)
                .setSql("like_count = like_count + 1")
                .set(HubFeedback::getUpdateTime, new Date())
                .update();
    }

    @Override
    public void decrLikeCount(Long feedbackId) {
        lambdaUpdate()
                .eq(HubFeedback::getId, feedbackId)
                .setSql("like_count = greatest(like_count - 1, 0)")
                .set(HubFeedback::getUpdateTime, new Date())
                .update();
    }

    @Override
    public void incrReplyCount(Long feedbackId) {
        lambdaUpdate()
                .eq(HubFeedback::getId, feedbackId)
                .setSql("reply_count = reply_count + 1")
                .set(HubFeedback::getUpdateTime, new Date())
                .update();
    }

    @Override
    public void decrReplyCount(Long feedbackId) {
        lambdaUpdate()
                .eq(HubFeedback::getId, feedbackId)
                .setSql("reply_count = greatest(reply_count - 1, 0)")
                .set(HubFeedback::getUpdateTime, new Date())
                .update();
    }

    @Override
    public List<HubFeedbackComment> listComments(Long feedbackId) {
        return feedbackCommentMapper.selectList(new LambdaQueryWrapper<HubFeedbackComment>()
                .eq(HubFeedbackComment::getFeedbackId, feedbackId)
                .orderByAsc(HubFeedbackComment::getCreateTime));
    }

    @Override
    public HubFeedbackComment getComment(Long commentId) {
        return feedbackCommentMapper.selectById(commentId);
    }

    @Override
    public void saveComment(HubFeedbackComment comment) {
        feedbackCommentMapper.insert(comment);
    }

    @Override
    public void removeComment(Long commentId) {
        feedbackCommentMapper.deleteById(commentId);
    }

    @Override
    public void removeComments(Long feedbackId) {
        feedbackCommentMapper.delete(new LambdaQueryWrapper<HubFeedbackComment>()
                .eq(HubFeedbackComment::getFeedbackId, feedbackId));
    }

    @Override
    public void incrCommentLikes(Long commentId) {
        feedbackCommentMapper.update(null, new LambdaUpdateWrapper<HubFeedbackComment>()
                .setSql("like_count = like_count + 1")
                .eq(HubFeedbackComment::getId, commentId));
    }

    @Override
    public void decrCommentLikes(Long commentId) {
        feedbackCommentMapper.update(null, new LambdaUpdateWrapper<HubFeedbackComment>()
                .setSql("like_count = greatest(like_count - 1, 0)")
                .eq(HubFeedbackComment::getId, commentId));
    }

    @Override
    public boolean existsLike(int targetType, Long targetId, String userCode) {
        return feedbackLikeMapper.exists(new LambdaQueryWrapper<HubFeedbackLike>()
                .eq(HubFeedbackLike::getTargetType, targetType)
                .eq(HubFeedbackLike::getTargetId, targetId)
                .eq(HubFeedbackLike::getUserCode, userCode));
    }

    @Override
    public List<Long> likedTargetIds(int targetType, String userCode) {
        List<HubFeedbackLike> list = feedbackLikeMapper.selectList(new LambdaQueryWrapper<HubFeedbackLike>()
                .eq(HubFeedbackLike::getTargetType, targetType)
                .eq(HubFeedbackLike::getUserCode, userCode)
                .select(HubFeedbackLike::getTargetId));
        return list.stream().map(HubFeedbackLike::getTargetId).toList();
    }

    @Override
    public List<HubFeedbackLike> listLikes(int targetType, Long targetId) {
        return feedbackLikeMapper.selectList(new LambdaQueryWrapper<HubFeedbackLike>()
                .eq(HubFeedbackLike::getTargetType, targetType)
                .eq(HubFeedbackLike::getTargetId, targetId)
                .orderByAsc(HubFeedbackLike::getCreateTime));
    }

    @Override
    public void saveLike(HubFeedbackLike like) {
        feedbackLikeMapper.insert(like);
    }

    @Override
    public void removeLike(int targetType, Long targetId, String userCode) {
        feedbackLikeMapper.delete(new LambdaQueryWrapper<HubFeedbackLike>()
                .eq(HubFeedbackLike::getTargetType, targetType)
                .eq(HubFeedbackLike::getTargetId, targetId)
                .eq(HubFeedbackLike::getUserCode, userCode));
    }

    @Override
    public void removeLikesByFeedback(Long feedbackId) {
        feedbackLikeMapper.delete(new LambdaQueryWrapper<HubFeedbackLike>()
                .eq(HubFeedbackLike::getTargetType, LIKE_FEEDBACK)
                .eq(HubFeedbackLike::getTargetId, feedbackId));
    }

    @Override
    public void removeLikesByComment(Long commentId) {
        feedbackLikeMapper.delete(new LambdaQueryWrapper<HubFeedbackLike>()
                .eq(HubFeedbackLike::getTargetType, LIKE_COMMENT)
                .eq(HubFeedbackLike::getTargetId, commentId));
    }
}
