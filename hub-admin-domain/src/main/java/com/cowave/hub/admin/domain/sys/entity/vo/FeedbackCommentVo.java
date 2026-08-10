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
package com.cowave.hub.admin.domain.sys.entity.vo;

import com.cowave.hub.admin.domain.sys.entity.HubFeedbackComment;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class FeedbackCommentVo extends HubFeedbackComment {

    /**
     * 是否本人评论
     */
    private Boolean isMine;

    /**
     * 当前用户是否已点赞该评论
     */
    private Boolean liked;
}
