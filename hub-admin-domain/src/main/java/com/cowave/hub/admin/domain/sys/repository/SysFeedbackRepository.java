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
package com.cowave.hub.admin.domain.sys.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cowave.hub.admin.domain.sys.entity.SysFeedback;
import com.cowave.hub.admin.domain.sys.entity.SysFeedbackComment;
import com.cowave.hub.admin.domain.sys.entity.SysFeedbackLike;
import com.cowave.hub.admin.domain.sys.repository.facade.SysFeedbackRepositoryFacade;

/**
 * @author shanhuiming
 */
public interface SysFeedbackRepository extends SysFeedbackRepositoryFacade, IService<SysFeedback> {

    /**
     * 点赞计数 +1
     */
    void incrLikeCount(Long feedbackId);

    /**
     * 点赞计数 -1
     */
    void decrLikeCount(Long feedbackId);

    /**
     * 回复计数 +1
     */
    void incrReplyCount(Long feedbackId);

    /**
     * 回复计数 -1
     */
    void decrReplyCount(Long feedbackId);

    /**
     * 评论详情
     */
    SysFeedbackComment getComment(Long commentId);

    /**
     * 新增评论
     */
    void saveComment(SysFeedbackComment comment);

    /**
     * 删除评论
     */
    void removeComment(Long commentId);

    /**
     * 删除留言评论
     */
    void removeComments(Long feedbackId);

    /**
     * 评论点赞计数 +1
     */
    void incrCommentLikes(Long commentId);

    /**
     * 评论点赞计数 -1
     */
    void decrCommentLikes(Long commentId);

    /**
     * 是否已点赞
     */
    boolean existsLike(int targetType, Long targetId, String userCode);

    /**
     * 点赞
     */
    void saveLike(SysFeedbackLike like);

    /**
     * 取消点赞
     */
    void removeLike(int targetType, Long targetId, String userCode);

    /**
     * 删除留言点赞记录
     */
    void removeLikesByFeedback(Long feedbackId);

    /**
     * 删除评论点赞记录
     */
    void removeLikesByComment(Long commentId);
}
