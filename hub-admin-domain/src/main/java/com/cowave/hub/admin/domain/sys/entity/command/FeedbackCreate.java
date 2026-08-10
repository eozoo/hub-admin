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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 新增评分留言请求
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class FeedbackCreate {

    /**
     * 评分 1-5
     */
    @NotNull(message = "{admin.feedback.score.null}")
    @Min(value = 1, message = "{admin.feedback.score.range}")
    @Max(value = 5, message = "{admin.feedback.score.range}")
    private Integer score;

    /**
     * 留言内容
     */
    @NotBlank(message = "{admin.feedback.content.null}")
    private String content;

    /**
     * 图片url列表（最多9张）
     */
    private List<String> images;
}
