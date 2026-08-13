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
import com.cowave.zoo.http.client.response.Response;
import com.cowave.hub.admin.domain.sys.entity.SysNotice;
import com.cowave.hub.admin.domain.sys.entity.SysNoticeLike;
import com.cowave.hub.admin.domain.sys.entity.vo.NoticeCommentVo;
import com.cowave.hub.admin.domain.sys.entity.vo.NoticeVo;
import com.cowave.hub.admin.domain.sys.entity.pto.NoticeListPto;
import com.cowave.hub.admin.domain.sys.entity.vo.NoticeUserVo;
import com.cowave.hub.admin.domain.sys.entity.command.NoticeCommentCreate;
import com.cowave.hub.admin.domain.sys.entity.command.NoticeCreate;
import com.cowave.hub.admin.domain.sys.entity.query.NoticeQuery;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysNoticeService {

    /**
     * 列表
     */
    Response.Page<NoticeVo> list(String tenantId, NoticeQuery query);

    /**
     * 详情
     */
    NoticeVo info(String tenantId, Long noticeId);

    /**
     * 新增
     */
    void add(String tenantId, NoticeCreate notice) throws Exception;

    /**
     * 删除
     */
    void delete(String tenantId, List<Long> noticeIds) throws Exception;

    /**
     * 修改
     */
    void edit(String tenantId, NoticeCreate notice) throws Exception;

    /**
     * 发布
     */
    void publish(String tenantId, Long noticeId);

    /**
     * 已读列表
     */
    Response.Page<NoticeUserVo> queryNoticeReaders(String tenantId, Long noticeId);

    /**
     * 消息列表
     */
    Page<NoticeListPto> queryMsgList();

    /**
     * 阅读消息
     */
    void msgRead(Long noticeId);

    /**
     * 删除消息
     */
    void msgDelete(Long msgId);

    /**
     * 未读消息计数
     */
    long msgUnReadCount(String userCode);

    /**
     * 发送通知
     */
    void sendUserNotice(SysNotice notice, Integer userId);

    /**
     * 流程通知
     */
    void sendFlowNotice(String processName, String taskName, Integer startUser, Integer assigneeUser);

    /**
     * 消息好评差评
     */
    void msgLike(Long noticeId, Integer likeStatus);

    /**
     * 查询评论列表
     */
    List<NoticeCommentVo> queryCommentList(Long noticeId);

    /**
     * 新增评论/回复
     */
    void commentAdd(NoticeCommentCreate commentCreate);

    /**
     * 删除评论
     */
    void commentDelete(Long commentId);

    /**
     * 评论点赞/取消
     */
    boolean commentLike(Long commentId);

    /**
     * 查询点赞人列表
     */
    List<SysNoticeLike> queryLikers(Integer targetType, Long targetId);
}
