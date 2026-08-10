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
package com.cowave.hub.admin.domain.sys.repository.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.entity.HubFeedback;
import com.cowave.hub.admin.domain.sys.entity.HubFeedbackComment;
import com.cowave.hub.admin.domain.sys.entity.HubFeedbackLike;
import com.cowave.hub.admin.domain.sys.entity.pto.FeedbackStatPto;
import com.cowave.hub.admin.domain.sys.entity.query.FeedbackQuery;

import java.util.List;

/**
 * HubFeedback聚合根Query操作
 *
 * @see HubFeedback
 * @see HubFeedbackComment
 * @see HubFeedbackLike
 *
 * @author shanhuiming
 */
public interface HubFeedbackRepositoryFacade {

    /** 点赞目标类型：留言 */
    int LIKE_FEEDBACK = 1;

    /** 点赞目标类型：评论 */
    int LIKE_COMMENT = 2;

    /**
     * 评分统计（总数 + 平均分 + 各分值人数）
     */
    FeedbackStatPto queryStat(String tenantId);

    /**
     * 留言列表
     */
    Page<HubFeedback> queryPage(String tenantId, FeedbackQuery query);

    /**
     * 留言评论列表
     */
    List<HubFeedbackComment> listComments(Long feedbackId);

    /**
     * 用户已点赞的目标列表
     */
    List<Long> likedTargetIds(int targetType, String userCode);

    /**
     * 给目标点赞的用户列表
     */
    List<HubFeedbackLike> listLikes(int targetType, Long targetId);
}
