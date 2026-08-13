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
package com.cowave.hub.admin.controller.sys;

import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.Access;
import com.cowave.hub.admin.domain.sys.entity.SysFeedbackLike;
import com.cowave.hub.admin.domain.sys.entity.vo.FeedbackCommentVo;
import com.cowave.hub.admin.domain.sys.entity.vo.FeedbackVo;
import com.cowave.hub.admin.domain.sys.entity.pto.FeedbackStatPto;
import com.cowave.hub.admin.domain.sys.entity.command.FeedbackCommentCreate;
import com.cowave.hub.admin.domain.sys.entity.command.FeedbackCreate;
import com.cowave.hub.admin.domain.sys.entity.query.FeedbackQuery;
import com.cowave.hub.admin.service.sys.SysFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统评分留言
 *
 * @order 19
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/feedback")
public class SysFeedbackController {

    private final SysFeedbackService feedbackService;

    /**
     * 评分统计
     */
    @GetMapping("/stat")
    public Response<FeedbackStatPto> stat() {
        return Response.success(feedbackService.stat(Access.tenantId()));
    }

    /**
     * 留言列表
     */
    @GetMapping
    public Response<Response.Page<FeedbackVo>> list(FeedbackQuery query) {
        return Response.page(feedbackService.list(Access.tenantId(), query));
    }

    /**
     * 新增留言
     */
    @PostMapping
    public Response<Void> add(@Validated @RequestBody FeedbackCreate feedbackCreate) {
        feedbackService.add(Access.tenantId(), feedbackCreate);
        return Response.success();
    }

    /**
     * 删除留言
     *
     * @param id 留言id
     */
    @DeleteMapping("/{id}")
    public Response<Void> delete(@NotNull(message = "{admin.feedback.id.null}") @PathVariable Long id) {
        feedbackService.delete(id);
        return Response.success();
    }

    /**
     * 留言点赞 / 取消点赞
     *
     * @param id 留言id
     */
    @PatchMapping("/like/feedback/{id}")
    public Response<Boolean> likeFeedback(@NotNull(message = "{admin.feedback.id.null}") @PathVariable Long id) {
        return Response.success(feedbackService.toggleFeedbackLike(id));
    }

    /**
     * 评论点赞 / 取消点赞
     *
     * @param id 评论id
     */
    @PatchMapping("/like/comment/{id}")
    public Response<Boolean> likeComment(@NotNull(message = "{admin.feedback.comment.id.null}") @PathVariable Long id) {
        return Response.success(feedbackService.toggleCommentLike(id));
    }

    /**
     * 查询点赞用户列表
     *
     * @param targetType 1-留言 2-评论
     * @param targetId   目标id
     */
    @GetMapping("/like")
    public Response<List<SysFeedbackLike>> likes(@NotNull Integer targetType, @NotNull Long targetId) {
        return Response.success(feedbackService.queryLikes(targetType, targetId));
    }

    /**
     * 新增评论 / 回复
     */
    @PostMapping("/comment")
    public Response<Void> addComment(@Validated @RequestBody FeedbackCommentCreate commentCreate) {
        feedbackService.addComment(commentCreate);
        return Response.success();
    }

    /**
     * 评论列表（含点赞状态）
     *
     * @param feedbackId 留言id
     */
    @GetMapping("/comment")
    public Response<List<FeedbackCommentVo>> listComments(@NotNull(message = "{admin.feedback.id.null}") Long feedbackId) {
        return Response.success(feedbackService.queryComments(feedbackId));
    }

    /**
     * 删除评论
     *
     * @param commentId 评论id
     */
    @DeleteMapping("/comment/{commentId}")
    public Response<Void> deleteComment(@NotNull(message = "{admin.feedback.comment.id.null}") @PathVariable Long commentId) {
        feedbackService.deleteComment(commentId);
        return Response.success();
    }
}
