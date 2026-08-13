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
import com.cowave.hub.admin.domain.sys.entity.SysFeedback;
import com.cowave.hub.admin.domain.sys.entity.SysFeedbackComment;
import com.cowave.hub.admin.domain.sys.entity.SysFeedbackLike;
import com.cowave.hub.admin.domain.sys.entity.pto.FeedbackStatPto;
import com.cowave.hub.admin.domain.sys.entity.query.FeedbackQuery;
import com.cowave.hub.admin.domain.sys.repository.SysFeedbackRepository;
import com.cowave.hub.admin.infra.sys.mapper.SysFeedbackCommentMapper;
import com.cowave.hub.admin.infra.sys.mapper.SysFeedbackLikeMapper;
import com.cowave.hub.admin.infra.sys.mapper.SysFeedbackMapper;
import com.cowave.zoo.framework.access.Access;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class SysFeedbackDao extends ServiceImpl<SysFeedbackMapper, SysFeedback> implements SysFeedbackRepository {

    private final SysFeedbackCommentMapper feedbackCommentMapper;

    private final SysFeedbackLikeMapper feedbackLikeMapper;

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
                    .eq(SysFeedback::getTenantId, tenantId)
                    .eq(SysFeedback::getScore, s)
                    .count();
        }
        stat.setScoreCount(scoreCount);
        return stat;
    }

    @Override
    public Page<SysFeedback> queryPage(String tenantId, FeedbackQuery query) {
        return lambdaQuery()
                .eq(SysFeedback::getTenantId, tenantId)
                .eq(query.getScore() != null, SysFeedback::getScore, query.getScore())
                .orderByDesc(SysFeedback::getCreateTime)
                .page(Access.page());
    }

    @Override
    public void incrLikeCount(Long feedbackId) {
        lambdaUpdate()
                .eq(SysFeedback::getId, feedbackId)
                .setSql("like_count = like_count + 1")
                .set(SysFeedback::getUpdateTime, new Date())
                .update();
    }

    @Override
    public void decrLikeCount(Long feedbackId) {
        lambdaUpdate()
                .eq(SysFeedback::getId, feedbackId)
                .setSql("like_count = greatest(like_count - 1, 0)")
                .set(SysFeedback::getUpdateTime, new Date())
                .update();
    }

    @Override
    public void incrReplyCount(Long feedbackId) {
        lambdaUpdate()
                .eq(SysFeedback::getId, feedbackId)
                .setSql("reply_count = reply_count + 1")
                .set(SysFeedback::getUpdateTime, new Date())
                .update();
    }

    @Override
    public void decrReplyCount(Long feedbackId) {
        lambdaUpdate()
                .eq(SysFeedback::getId, feedbackId)
                .setSql("reply_count = greatest(reply_count - 1, 0)")
                .set(SysFeedback::getUpdateTime, new Date())
                .update();
    }

    @Override
    public List<SysFeedbackComment> listComments(Long feedbackId) {
        return feedbackCommentMapper.selectList(new LambdaQueryWrapper<SysFeedbackComment>()
                .eq(SysFeedbackComment::getFeedbackId, feedbackId)
                .orderByAsc(SysFeedbackComment::getCreateTime));
    }

    @Override
    public SysFeedbackComment getComment(Long commentId) {
        return feedbackCommentMapper.selectById(commentId);
    }

    @Override
    public void saveComment(SysFeedbackComment comment) {
        feedbackCommentMapper.insert(comment);
    }

    @Override
    public void removeComment(Long commentId) {
        feedbackCommentMapper.deleteById(commentId);
    }

    @Override
    public void removeComments(Long feedbackId) {
        feedbackCommentMapper.delete(new LambdaQueryWrapper<SysFeedbackComment>()
                .eq(SysFeedbackComment::getFeedbackId, feedbackId));
    }

    @Override
    public void incrCommentLikes(Long commentId) {
        feedbackCommentMapper.update(null, new LambdaUpdateWrapper<SysFeedbackComment>()
                .setSql("like_count = like_count + 1")
                .eq(SysFeedbackComment::getId, commentId));
    }

    @Override
    public void decrCommentLikes(Long commentId) {
        feedbackCommentMapper.update(null, new LambdaUpdateWrapper<SysFeedbackComment>()
                .setSql("like_count = greatest(like_count - 1, 0)")
                .eq(SysFeedbackComment::getId, commentId));
    }

    @Override
    public boolean existsLike(int targetType, Long targetId, String userCode) {
        return feedbackLikeMapper.exists(new LambdaQueryWrapper<SysFeedbackLike>()
                .eq(SysFeedbackLike::getTargetType, targetType)
                .eq(SysFeedbackLike::getTargetId, targetId)
                .eq(SysFeedbackLike::getUserCode, userCode));
    }

    @Override
    public List<Long> likedTargetIds(int targetType, String userCode) {
        List<SysFeedbackLike> list = feedbackLikeMapper.selectList(new LambdaQueryWrapper<SysFeedbackLike>()
                .eq(SysFeedbackLike::getTargetType, targetType)
                .eq(SysFeedbackLike::getUserCode, userCode)
                .select(SysFeedbackLike::getTargetId));
        return list.stream().map(SysFeedbackLike::getTargetId).toList();
    }

    @Override
    public List<SysFeedbackLike> listLikes(int targetType, Long targetId) {
        return feedbackLikeMapper.selectList(new LambdaQueryWrapper<SysFeedbackLike>()
                .eq(SysFeedbackLike::getTargetType, targetType)
                .eq(SysFeedbackLike::getTargetId, targetId)
                .orderByAsc(SysFeedbackLike::getCreateTime));
    }

    @Override
    public void saveLike(SysFeedbackLike like) {
        feedbackLikeMapper.insert(like);
    }

    @Override
    public void removeLike(int targetType, Long targetId, String userCode) {
        feedbackLikeMapper.delete(new LambdaQueryWrapper<SysFeedbackLike>()
                .eq(SysFeedbackLike::getTargetType, targetType)
                .eq(SysFeedbackLike::getTargetId, targetId)
                .eq(SysFeedbackLike::getUserCode, userCode));
    }

    @Override
    public void removeLikesByFeedback(Long feedbackId) {
        feedbackLikeMapper.delete(new LambdaQueryWrapper<SysFeedbackLike>()
                .eq(SysFeedbackLike::getTargetType, LIKE_FEEDBACK)
                .eq(SysFeedbackLike::getTargetId, feedbackId));
    }

    @Override
    public void removeLikesByComment(Long commentId) {
        feedbackLikeMapper.delete(new LambdaQueryWrapper<SysFeedbackLike>()
                .eq(SysFeedbackLike::getTargetType, LIKE_COMMENT)
                .eq(SysFeedbackLike::getTargetId, commentId));
    }
}
