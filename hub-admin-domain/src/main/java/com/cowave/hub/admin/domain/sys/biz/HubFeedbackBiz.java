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
package com.cowave.hub.admin.domain.sys.biz;

import com.cowave.hub.admin.domain.sys.entity.HubFeedback;
import com.cowave.hub.admin.domain.sys.entity.HubFeedbackComment;
import com.cowave.hub.admin.domain.sys.entity.HubFeedbackLike;
import com.cowave.hub.admin.domain.sys.entity.command.FeedbackCommentCreate;

import java.util.List;

/**
 * HubFeedback聚合根Command操作
 *
 * @see HubFeedback
 * @see HubFeedbackComment
 * @see HubFeedbackLike
 *
 * @author shanhuiming
 */
public interface HubFeedbackBiz {

    /**
     * 新增留言
     */
    void createFeedback(String tenantId, String userCode, String userName,
                        Integer score, String content, List<String> images);

    /**
     * 新增评论/回复
     */
    void createComment(FeedbackCommentCreate command, String userCode, String userName);

    /**
     * 删除留言
     */
    void deleteFeedback(Long id, String currentUserCode, boolean isAdmin);

    /**
     * 删除评论/回复
     */
    void deleteComment(Long commentId, String currentUserCode, boolean isAdmin);

    /**
     * 留言点赞 / 取消点赞
     */
    boolean toggleFeedbackLike(Long feedbackId, String userCode, String userName);

    /**
     * 评论点赞 / 取消点赞
     */
    boolean toggleCommentLike(Long commentId, String userCode, String userName);
}
