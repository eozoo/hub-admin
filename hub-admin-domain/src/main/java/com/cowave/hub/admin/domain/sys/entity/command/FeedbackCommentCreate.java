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
package com.cowave.hub.admin.domain.sys.entity.command;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新增留言评论/回复请求
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class FeedbackCommentCreate {

    /**
     * 留言id
     */
    @NotNull(message = "{admin.feedback.id.null}")
    private Long feedbackId;

    /**
     * 父评论id（0或null表示顶级评论）
     */
    private Long parentId;

    /**
     * 被回复用户账号
     */
    private String replyToCode;

    /**
     * 被回复用户名称
     */
    private String replyToName;

    /**
     * 评论内容
     */
    @NotBlank(message = "{admin.feedback.comment.content.null}")
    private String content;
}
