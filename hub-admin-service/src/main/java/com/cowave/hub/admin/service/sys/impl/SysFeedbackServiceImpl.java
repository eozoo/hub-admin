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
package com.cowave.hub.admin.service.sys.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.biz.SysAttachBiz;
import com.cowave.hub.admin.domain.sys.biz.SysFeedbackBiz;
import com.cowave.hub.admin.domain.sys.entity.SysAttach;
import com.cowave.hub.admin.domain.sys.entity.SysFeedback;
import com.cowave.hub.admin.domain.sys.entity.SysFeedbackComment;
import com.cowave.hub.admin.domain.sys.entity.SysFeedbackLike;
import com.cowave.hub.admin.domain.sys.entity.command.FeedbackCommentCreate;
import com.cowave.hub.admin.domain.sys.entity.command.FeedbackCreate;
import com.cowave.hub.admin.domain.sys.entity.pto.FeedbackStatPto;
import com.cowave.hub.admin.domain.sys.entity.query.FeedbackQuery;
import com.cowave.hub.admin.domain.sys.entity.vo.FeedbackCommentVo;
import com.cowave.hub.admin.domain.sys.entity.vo.FeedbackVo;
import com.cowave.hub.admin.domain.sys.repository.facade.SysAttachRepositoryFacade;
import com.cowave.hub.admin.domain.sys.repository.facade.SysFeedbackRepositoryFacade;
import com.cowave.hub.admin.service.sys.SysFeedbackService;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.tools.Converts;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.cowave.hub.admin.domain.sys.repository.facade.SysFeedbackRepositoryFacade.LIKE_COMMENT;
import static com.cowave.hub.admin.domain.sys.repository.facade.SysFeedbackRepositoryFacade.LIKE_FEEDBACK;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
public class SysFeedbackServiceImpl implements SysFeedbackService {

    private final SysFeedbackBiz feedbackBiz;

    private final SysFeedbackRepositoryFacade feedbackRepositoryFacade;

    private final SysAttachBiz attachBiz;

    private final SysAttachRepositoryFacade attachRepositoryFacade;

    @Override
    public FeedbackStatPto stat(String tenantId) {
        return feedbackRepositoryFacade.queryStat(tenantId);
    }

    @Override
    public Page<FeedbackVo> list(String tenantId, FeedbackQuery query) throws Exception {
        Set<Long> likedSet = likedIds(LIKE_FEEDBACK);
        Page<SysFeedback> page = feedbackRepositoryFacade.queryPage(tenantId, query);
        // 处理点赞，和图片
        List<FeedbackVo> voList = new ArrayList<>();
        for(SysFeedback feedback : page.getRecords()){
            FeedbackVo vo = Converts.copyProperties(feedback, FeedbackVo.class);
            vo.setLiked(likedSet.contains(feedback.getId()));
            vo.setImages(fillImages(feedback.getImages()));
            voList.add(vo);
        }
        Page<FeedbackVo> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(voList);
        return result;
    }

    private String fillImages(String images) throws Exception {
        if (StringUtils.isBlank(images)) {
            return images;
        }
        List<Long> attachIds = Arrays.stream(images.split(","))
                .filter(StringUtils::isNotBlank)
                .map(Long::valueOf)
                .toList();
        if (attachIds.isEmpty()) {
            return images;
        }

        Map<Long, String> viewUrlMap = new HashMap<>();
        for (SysAttach attach : attachRepositoryFacade.queryByIds(attachIds)) {
            viewUrlMap.put(attach.getAttachId(), attachBiz.previewAttach(attach));
        }
        return attachIds.stream()
                .map(id -> viewUrlMap.getOrDefault(id, ""))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<SysFeedbackLike> queryLikes(int targetType, Long targetId) {
        return feedbackRepositoryFacade.listLikes(targetType, targetId);
    }

    @Override
    public List<FeedbackCommentVo> queryComments(Long feedbackId) {
        Set<Long> likedSet = likedIds(LIKE_COMMENT);
        List<SysFeedbackComment> list = feedbackRepositoryFacade.listComments(feedbackId);
        return list.stream().map(comment -> {
            FeedbackCommentVo vo = Converts.copyProperties(comment, FeedbackCommentVo.class);
            vo.setIsMine(comment.getUserCode() != null && comment.getUserCode().equals(Access.userCode()));
            vo.setLiked(likedSet.contains(comment.getId()));
            return vo;
        }).toList();
    }

    @Override
    public void add(String tenantId, FeedbackCreate feedbackCreate) {
        feedbackBiz.createFeedback(tenantId, Access.userCode(), Access.userName(),
                feedbackCreate.getScore(), feedbackCreate.getContent(), feedbackCreate.getImages());
    }

    @Override
    public void delete(Long id) {
        feedbackBiz.deleteFeedback(id, Access.userCode(), Access.isAdminUser());
    }

    @Override
    public void addComment(FeedbackCommentCreate commentCreate) {
        feedbackBiz.createComment(commentCreate, Access.userCode(), Access.userName());
    }

    @Override
    public void deleteComment(Long commentId) {
        feedbackBiz.deleteComment(commentId, Access.userCode(), Access.isAdminUser());
    }

    @Override
    public boolean toggleFeedbackLike(Long feedbackId) {
        return feedbackBiz.toggleFeedbackLike(feedbackId, Access.userCode(), Access.userName());
    }

    @Override
    public boolean toggleCommentLike(Long commentId) {
        return feedbackBiz.toggleCommentLike(commentId, Access.userCode(), Access.userName());
    }

    private Set<Long> likedIds(int targetType) {
        List<Long> ids = feedbackRepositoryFacade.likedTargetIds(targetType, Access.userCode());
        return ids != null ? new HashSet<>(ids) : Collections.emptySet();
    }
}
