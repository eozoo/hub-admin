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
import com.cowave.hub.admin.domain.sys.entity.HubNotice;
import com.cowave.hub.admin.domain.sys.entity.HubNoticeComment;
import com.cowave.hub.admin.domain.sys.entity.HubNoticeLike;
import com.cowave.hub.admin.domain.sys.enums.NoticeStatus;
import com.cowave.hub.admin.domain.sys.repository.facade.HubNoticeRepositoryFacade;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
public interface HubNoticeRepository extends HubNoticeRepositoryFacade, IService<HubNotice> {

    /**
     * 更新消息统计（发布时）
     */
    void updateMsgStat(Long noticeId, NoticeStatus noticeStatus, Date publishTime);

    /**
     * 更新已读统计
     */
    void updateReadStat(Long noticeId);

    /**
     * 新用户更新消息统计
     */
    void updateNoticeStatForNewUser();

    /**
     * 更新公告状态
     */
    void updateStatus(Long noticeId, NoticeStatus noticeStatus);

    /**
     * 更新公告信息
     */
    void updateNotice(HubNotice hubNotice);

    /**
     * 删除消息
     */
    void msgDelete(String userCode, Long msgId);

    /**
     * 新用户初始化消息
     */
    void initNoticeMsgForNewUser(String userCode);

    /**
     * 更新已读状态
     */
    boolean updateReadStatus(String userCode, Long noticeId, Date readTime);

    /**
     * 删除公告消息
     */
    void removeByNoticeId(Long noticeId);

    /**
     * 更新好评差评
     */
    void updateLikeStatus(String userCode, Long noticeId, Integer likeStatus);

    /**
     * 评论计数 +1
     */
    void incrCommentCount(String userCode, Long noticeId);

    /**
     * 评论计数 -1
     */
    void decrCommentCount(String userCode, Long noticeId);

    /**
     * 获取推送用户列表
     */
    List<String> getUserCodesByNoticeId(Long noticeId);

    /**
     * 通知所有用户
     */
    void insertReadOfAll(String tenantId, Long noticeId);

    /**
     * 通知指定部门
     */
    void insertReadOfDept(String tenantId, Long noticeId, List<Integer> list);

    /**
     * 通知指定角色
     */
    void insertReadOfRole(String tenantId, Long noticeId, List<Integer> list);

    /**
     * 通知指定用户
     */
    void insertReadOfUser(String tenantId, Long noticeId, List<Integer> list);

    /**
     * 评论详情
     */
    HubNoticeComment getComment(Long commentId);

    /**
     * 新增评论
     */
    void saveComment(HubNoticeComment comment);

    /**
     * 删除评论
     */
    void removeComment(Long commentId);

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
    boolean existsLike(Integer targetType, Long targetId, String userCode);

    /**
     * 点赞
     */
    void saveLike(HubNoticeLike like);

    /**
     * 取消点赞
     */
    void removeLike(Integer targetType, Long targetId, String userCode);
}
