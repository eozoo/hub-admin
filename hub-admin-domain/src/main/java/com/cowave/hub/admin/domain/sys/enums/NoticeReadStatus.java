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
package com.cowave.hub.admin.domain.sys.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.cowave.zoo.tools.EnumVal;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author shanhuiming
 */
@Getter
@RequiredArgsConstructor
public enum NoticeReadStatus implements EnumVal<Integer> {

    /**
     * 消息未读
     */
    UNREAD_PUBLISH(0),

    /**
     * 消息未读删除
     */
    UNREAD_DELETE(1),

    /**
     * 消息未读撤回
     */
    UNREAD_RECALL(2),

    /**
     * 消息已读
     */
    READ_PUBLISH(10),

    /**
     * 消息已读删除
     */
    READ_DELETE(11),

    /**
     * 消息已读撤回
     */
    READ_RECALL(22);

    @EnumValue
    @JsonValue
    private final Integer val;
}
