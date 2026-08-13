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
package com.cowave.hub.admin.infra.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.hub.admin.domain.sys.entity.SysNotice;
import com.cowave.hub.admin.domain.sys.enums.NoticeStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysNoticeMapper extends BaseMapper<SysNotice> {

    /**
     * 更新待读总数
     */
    void updateMsgStat(@Param("noticeId") Long noticeId, @Param("noticeStatus") NoticeStatus noticeStatus, @Param("publishTime") Date publishTime);

    /**
     * 更新已读统计
     */
    void updateReadStat(Long noticeId);

    /**
     * 新用户更新消息统计
     */
    void updateNoticeStatForNewUser();
}
