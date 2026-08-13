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
package com.cowave.hub.admin.service.sys;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.entity.SysFeedbackLike;
import com.cowave.hub.admin.domain.sys.entity.vo.FeedbackCommentVo;
import com.cowave.hub.admin.domain.sys.entity.vo.FeedbackVo;
import com.cowave.hub.admin.domain.sys.entity.pto.FeedbackStatPto;
import com.cowave.hub.admin.domain.sys.entity.command.FeedbackCommentCreate;
import com.cowave.hub.admin.domain.sys.entity.command.FeedbackCreate;
import com.cowave.hub.admin.domain.sys.entity.query.FeedbackQuery;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysFeedbackService {

    /**
     * 评分统计
     */
    FeedbackStatPto stat(String tenantId);

    /**
     * 留言列表
     */
    Page<FeedbackVo> list(String tenantId, FeedbackQuery query);

    /**
     * 新增留言
     */
    void add(String tenantId, FeedbackCreate feedbackCreate);

    /**
     * 删除留言（本人或管理员）
     */
    void delete(Long id);

    /**
     * 留言点赞 / 取消点赞（幂等）
     */
    boolean toggleFeedbackLike(Long feedbackId);

    /**
     * 评论点赞 / 取消点赞（幂等）
     */
    boolean toggleCommentLike(Long commentId);

    /**
     * 查询点赞用户列表
     */
    List<SysFeedbackLike> queryLikes(int targetType, Long targetId);

    /**
     * 新增评论 / 回复
     */
    void addComment(FeedbackCommentCreate commentCreate);

    /**
     * 查询留言评论列表（含点赞状态）
     */
    List<FeedbackCommentVo> queryComments(Long feedbackId);

    /**
     * 删除评论（本人或管理员）
     */
    void deleteComment(Long commentId);
}
