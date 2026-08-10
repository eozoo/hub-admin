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

import com.cowave.hub.admin.domain.sys.biz.HubFeedbackBiz;
import com.cowave.hub.admin.domain.sys.entity.HubFeedback;
import com.cowave.hub.admin.domain.sys.entity.HubFeedbackComment;
import com.cowave.hub.admin.domain.sys.entity.HubFeedbackLike;
import com.cowave.hub.admin.domain.sys.entity.command.FeedbackCommentCreate;
import com.cowave.hub.admin.domain.sys.repository.HubFeedbackRepository;
import com.cowave.zoo.http.client.asserts.Asserts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

import static com.cowave.hub.admin.domain.sys.repository.HubFeedbackRepository.LIKE_COMMENT;
import static com.cowave.hub.admin.domain.sys.repository.HubFeedbackRepository.LIKE_FEEDBACK;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubFeedbackBizImpl implements HubFeedbackBiz {

    private final HubFeedbackRepository feedbackRepository;

    @Override
    public void createFeedback(String tenantId, String userCode, String userName,
                               Integer score, String content, List<String> images) {
        HubFeedback feedback = new HubFeedback();
        feedback.setTenantId(tenantId);
        feedback.setUserCode(userCode);
        feedback.setUserName(userName);
        feedback.setScore(score);
        feedback.setContent(content);
        if (!CollectionUtils.isEmpty(images)) {
            feedback.setImages(String.join(",", images));
        }
        feedback.setLikeCount(0);
        feedback.setReplyCount(0);
        feedback.setCreateTime(new Date());
        feedback.setUpdateTime(new Date());
        feedbackRepository.save(feedback);
    }

    @Override
    public void createComment(FeedbackCommentCreate command, String userCode, String userName) {
        HubFeedback feedback = feedbackRepository.getById(command.getFeedbackId());
        Asserts.notNull(feedback, "{admin.feedback.not.found}");

        HubFeedbackComment comment = new HubFeedbackComment();
        comment.setFeedbackId(command.getFeedbackId());
        comment.setParentId(command.getParentId() != null ? command.getParentId() : 0L);
        comment.setReplyToCode(command.getReplyToCode());
        comment.setReplyToName(command.getReplyToName());
        comment.setUserCode(userCode);
        comment.setUserName(userName);
        comment.setContent(command.getContent());
        comment.setLikeCount(0);
        comment.setCreateTime(new Date());
        feedbackRepository.saveComment(comment);

        feedbackRepository.incrReplyCount(command.getFeedbackId());
    }

    @Override
    public void deleteFeedback(Long id, String currentUserCode, boolean isAdmin) {
        HubFeedback feedback = feedbackRepository.getById(id);
        Asserts.notNull(feedback, "{admin.feedback.not.found}");
        Asserts.isTrue(isAdmin || currentUserCode.equals(feedback.getUserCode()),
                "{admin.feedback.delete.denied}");

        List<HubFeedbackComment> comments = feedbackRepository.listComments(id);
        for (HubFeedbackComment c : comments) {
            feedbackRepository.removeLikesByComment(c.getId());
        }
        feedbackRepository.removeComments(id);
        feedbackRepository.removeLikesByFeedback(id);
        feedbackRepository.removeById(id);
    }

    @Override
    public void deleteComment(Long commentId, String currentUserCode, boolean isAdmin) {
        HubFeedbackComment comment = feedbackRepository.getComment(commentId);
        Asserts.notNull(comment, "{admin.feedback.comment.not.found}");
        Asserts.isTrue(isAdmin || currentUserCode.equals(comment.getUserCode()),
                "{admin.feedback.delete.denied}");

        List<HubFeedbackComment> children = feedbackRepository.listComments(comment.getFeedbackId())
                .stream().filter(c -> commentId.equals(c.getParentId())).toList();
        for (HubFeedbackComment child : children) {
            feedbackRepository.removeComment(child.getId());
            feedbackRepository.removeLikesByComment(child.getId());
            feedbackRepository.decrReplyCount(comment.getFeedbackId());
        }
        feedbackRepository.removeComment(commentId);
        feedbackRepository.removeLikesByComment(commentId);
        feedbackRepository.decrReplyCount(comment.getFeedbackId());
    }

    @Override
    public boolean toggleFeedbackLike(Long feedbackId, String userCode, String userName) {
        HubFeedback feedback = feedbackRepository.getById(feedbackId);
        Asserts.notNull(feedback, "{admin.feedback.not.found}");

        boolean already = feedbackRepository.existsLike(LIKE_FEEDBACK, feedbackId, userCode);
        if (already) {
            feedbackRepository.removeLike(LIKE_FEEDBACK, feedbackId, userCode);
            feedbackRepository.decrLikeCount(feedbackId);
            return false;
        }

        HubFeedbackLike like = new HubFeedbackLike();
        like.setTargetType(LIKE_FEEDBACK);
        like.setTargetId(feedbackId);
        like.setUserCode(userCode);
        like.setUserName(userName);
        like.setCreateTime(new Date());
        feedbackRepository.saveLike(like);
        feedbackRepository.incrLikeCount(feedbackId);
        return true;
    }

    @Override
    public boolean toggleCommentLike(Long commentId, String userCode, String userName) {
        HubFeedbackComment comment = feedbackRepository.getComment(commentId);
        Asserts.notNull(comment, "{admin.feedback.comment.not.found}");

        boolean already = feedbackRepository.existsLike(LIKE_COMMENT, commentId, userCode);
        if (already) {
            feedbackRepository.removeLike(LIKE_COMMENT, commentId, userCode);
            feedbackRepository.decrCommentLikes(commentId);
            return false;
        }

        HubFeedbackLike like = new HubFeedbackLike();
        like.setTargetType(LIKE_COMMENT);
        like.setTargetId(commentId);
        like.setUserCode(userCode);
        like.setUserName(userName);
        like.setCreateTime(new Date());
        feedbackRepository.saveLike(like);
        feedbackRepository.incrCommentLikes(commentId);
        return true;
    }
}
