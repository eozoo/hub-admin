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
package com.cowave.hub.admin.domain.sys.entity.query;

import com.cowave.hub.admin.domain.sys.enums.NoticeStatus;
import com.cowave.hub.admin.domain.sys.enums.NoticeType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class NoticeQuery {

    /**
	 * 公告标题
	 */
	private String noticeTitle;

    /**
	 * 公告类型
	 */
	private NoticeType noticeType;

    /**
	 * 公告状态
	 */
    private NoticeStatus noticeStatus;
}
