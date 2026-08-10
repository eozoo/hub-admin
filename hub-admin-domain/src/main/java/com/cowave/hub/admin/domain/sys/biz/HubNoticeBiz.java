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

import com.cowave.hub.admin.domain.sys.entity.HubNotice;
import com.cowave.hub.admin.domain.sys.entity.HubNoticeComment;
import com.cowave.hub.admin.domain.sys.entity.HubNoticeLike;
import com.cowave.hub.admin.domain.sys.entity.HubNoticeUser;
import com.cowave.hub.admin.domain.sys.entity.command.NoticeCommentCreate;
import com.cowave.hub.admin.domain.sys.entity.command.NoticeCreate;

import java.util.List;

/**
 * HubNotice聚合根Command操作
 *
 * @see HubNotice
 * @see HubNoticeComment
 * @see HubNoticeLike
 * @see HubNoticeUser
 *
 * @author shanhuiming
 */
public interface HubNoticeBiz {

    /**
     * 新增公告（含附件关联）
     *
     * @return 内容中已移除的附件ID列表（外部删除）
     */
    List<Long> createNotice(NoticeCreate notice);

    /**
     * 修改公告（校验归属 + 未发布 + 附件同步）
     *
     * @return 内容中已移除的附件ID列表（外部删除）
     */
    List<Long> editNotice(String tenantId, NoticeCreate notice, String currentUserCode, boolean isAdmin);

    /**
     * 删除公告（按状态处理：草稿→删，已发布→撤回，已撤回→删）
     *
     * @return 需要清理的附件ID列表（外部处理）
     */
    List<Long> deleteNotice(String tenantId, Long noticeId, String currentUserCode, boolean isAdmin);

    /**
     * 发布公告（状态变更 + 生成已读记录）
     *
     * @return 接收推送的用户列表
     */
    List<String> publishNotice(String tenantId, Long noticeId, String currentUserCode, boolean isAdmin);

    /**
     * 阅读消息
     */
    void msgRead(String userCode, Long noticeId);

    /**
     * 删除消息
     */
    void msgDelete(String userCode, Long msgId);

    /**
     * 消息好评差评
     */
    void msgLike(String userCode, Long noticeId, Integer likeStatus);

    /**
     * 新增评论/回复
     */
    void createComment(NoticeCommentCreate command, String userCode, String userName);

    /**
     * 删除评论（校验归属）
     */
    void deleteComment(Long commentId, String currentUserCode, boolean isAdmin);

    /**
     * 评论点赞/取消
     */
    boolean toggleCommentLike(Long commentId, String userCode, String userName);

    /**
     * 初始化新用户的通知消息
     */
    void initNoticeMsgForNewUser(String userCode);

    /**
     * 新用户加入后更新通知统计
     */
    void updateNoticeStatForNewUser();

    void sendFlowNotice(String processName, String taskName, String startUser, String startUserName, String assigneeUser);
}
