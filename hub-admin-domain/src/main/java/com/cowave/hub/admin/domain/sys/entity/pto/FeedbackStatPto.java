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
package com.cowave.hub.admin.domain.sys.entity.pto;

import lombok.Getter;
import lombok.Setter;

/**
 * 评分统计
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class FeedbackStatPto {

    /**
     * 评分总数
     */
    private Long total;

    /**
     * 平均分（保留一位小数）
     */
    private Double avgScore;

    /**
     * 各分值人数：下标0不用，1~5对应1星到5星
     */
    private Long[] scoreCount;
}
