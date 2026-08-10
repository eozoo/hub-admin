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
import com.cowave.hub.admin.domain.sys.entity.HubNoticeLike;
import com.cowave.hub.admin.domain.sys.entity.vo.NoticeCommentVo;
import com.cowave.hub.admin.domain.sys.entity.vo.NoticeVo;
import com.cowave.hub.admin.domain.sys.entity.pto.NoticeListPto;
import com.cowave.hub.admin.domain.sys.entity.vo.NoticeUserVo;
import com.cowave.hub.admin.domain.sys.entity.command.NoticeCommentCreate;
import com.cowave.hub.admin.domain.sys.entity.command.NoticeCreate;
import com.cowave.hub.admin.domain.sys.entity.query.NoticeQuery;
import com.cowave.hub.admin.service.sys.HubNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 通知公告
 * @order 18
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notice")
public class HubNoticeController {

    private final HubNoticeService noticeService;

    /**
     * 列表
     */
    @GetMapping
    public Response<Response.Page<NoticeVo>> list(NoticeQuery query) {
        return Response.success(noticeService.list(Access.tenantId(), query));
    }

    /**
     * 详情
     *
     * @param noticeId 通知ID
     */
    @GetMapping("/{noticeId}")
    public Response<NoticeVo> info(@PathVariable Long noticeId) {
        return Response.success(noticeService.info(Access.tenantId(), noticeId));
    }

    /**
     * 新增
     */
    @PostMapping
    public Response<Void> add(@Validated @RequestBody NoticeCreate notice) throws Exception {
        noticeService.add(Access.tenantId(), notice);
        return Response.success();
    }

    /**
     * 删除
     *
     * @param noticeIds 通知Id列表
     */
    @DeleteMapping("/{noticeIds}")
    public Response<Void> delete(@PathVariable List<Long> noticeIds) throws Exception {
        noticeService.delete(Access.tenantId(), noticeIds);
        return Response.success();
    }

    /**
     * 修改
     */
    @PatchMapping
    public Response<Void> edit(@Validated @RequestBody NoticeCreate notice) throws Exception {
        noticeService.edit(Access.tenantId(), notice);
        return Response.success();
    }

    /**
     * 发布
     *
     * @param noticeId 通知Id
     */
    @PatchMapping("/publish/{noticeId}")
    public Response<Void> publish(@PathVariable Long noticeId) {
        noticeService.publish(Access.tenantId(), noticeId);
        return Response.success();
    }

    /**
     * 已读情况
     *
     * @param noticeId 公告ID
     */
    @GetMapping("/readers")
    public Response<Response.Page<NoticeUserVo>> getNoticeReaders(
            @NotNull(message = "{admin.notice.id.null}") Long noticeId) {
        return Response.success(noticeService.queryNoticeReaders(Access.tenantId(), noticeId));
    }

    /**
     * 消息列表
     */
    @GetMapping("/msg")
    public Response<Response.Page<NoticeListPto>> msgList() {
        return Response.page(noticeService.queryMsgList());
    }

    /**
     * 阅读消息
     *
     * @param noticeId 通知Id
     */
    @PatchMapping("/msg/read/{noticeId}")
    public Response<Void> msgRead(@PathVariable Long noticeId) {
        noticeService.msgRead(noticeId);
        return Response.success();
    }

    /**
     * 删除消息
     *
     * @param msgId 消息ID
     */
    @DeleteMapping("/msg/{msgId}")
    public Response<Void> msgDelete(@PathVariable Long msgId) {
        noticeService.msgDelete(msgId);
        return Response.success();
    }

    /**
     * 未读消息计数
     */
    @GetMapping("/msg/unread")
    public Response<Long> msgUnReadCount() {
        return Response.success(noticeService.msgUnReadCount(Access.userCode()));
    }

    /**
     * 消息好评差评（likeStatus: 1好评 2差评 0取消）
     *
     * @param noticeId   消息id
     * @param likeStatus 1好评 2差评 0取消
     */
    @PatchMapping("/msg/like/{noticeId}")
    public Response<Void> msgLike(@PathVariable Long noticeId,
                                   @NotNull(message = "likeStatus不能为空") @RequestParam Integer likeStatus) {
        noticeService.msgLike(noticeId, likeStatus);
        return Response.success();
    }

    /**
     * 评论列表
     *
     * @param noticeId 消息id
     */
    @GetMapping("/msg/comment/{noticeId}")
    public Response<List<NoticeCommentVo>> commentList(@PathVariable Long noticeId) {
        return Response.success(noticeService.queryCommentList(noticeId));
    }

    /**
     * 新增评论/回复
     */
    @PostMapping("/msg/comment")
    public Response<Void> commentAdd(@Validated @RequestBody NoticeCommentCreate commentCreate) {
        noticeService.commentAdd(commentCreate);
        return Response.success();
    }

    /**
     * 删除评论
     *
     * @param commentId 评论id
     */
    @DeleteMapping("/msg/comment/{commentId}")
    public Response<Void> commentDelete(@PathVariable Long commentId) {
        noticeService.commentDelete(commentId);
        return Response.success();
    }

    /**
     * 评论点赞/取消
     *
     * @param commentId 评论id
     */
    @PostMapping("/msg/comment/like/{commentId}")
    public Response<Boolean> commentLike(@PathVariable Long commentId) {
        return Response.success(noticeService.commentLike(commentId));
    }

    /**
     * 点赞人列表
     *
     * @param targetType 1消息 2评论
     * @param targetId   目标id
     */
    @GetMapping("/msg/likers")
    public Response<List<HubNoticeLike>> likers(
            @NotNull @RequestParam Integer targetType, @NotNull @RequestParam Long targetId) {
        return Response.success(noticeService.queryLikers(targetType, targetId));
    }
}
