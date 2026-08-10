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
import com.cowave.hub.admin.domain.sys.entity.*;
import com.cowave.hub.admin.domain.sys.entity.pto.NoticeListPto;
import com.cowave.hub.admin.domain.sys.entity.query.NoticeQuery;

import java.util.List;

/**
 * HubNotice聚合根Query操作
 *
 * @see HubNotice
 * @see HubNoticeComment
 * @see HubNoticeLike
 * @see HubNoticeUser
 *
 * @author shanhuiming
 */
public interface HubNoticeRepositoryFacade {

    /**
     * 列表
     */
    Page<HubNotice> queryPage(String tenantId, NoticeQuery query);

    /**
     * 详情
     */
    HubNotice queryById(String tenantId, Long noticeId);

    /**
     * 查询标题
     */
    String queryNameById(Long noticeId);

    /**
     * 消息列表
     */
    Page<NoticeListPto> queryMsgPage(String userCode);

    /**
     * 消息目标用户列表
     */
    Page<HubNoticeUser> queryUserPageByNoticeId(Long noticeId);

    /**
     * 未读消息计数
     */
    Long countUnReadByUser(String userCode);

    /**
     * 评论列表
     */
    List<HubNoticeComment> listComments(Long noticeId);

    /**
     * 点赞用户列表
     */
    List<HubNoticeLike> listLikes(Integer targetType, Long targetId);

    /**
     * 用户已点赞的目标列表
     */
    List<HubNoticeLike> likedTargets(Integer targetType, java.util.Set<Long> targetIds, String userCode);
}
