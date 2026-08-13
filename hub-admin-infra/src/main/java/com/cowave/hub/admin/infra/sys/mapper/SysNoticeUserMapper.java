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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.entity.SysNoticeUser;
import com.cowave.hub.admin.domain.sys.entity.pto.NoticeListPto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysNoticeUserMapper extends BaseMapper<SysNoticeUser> {

    /**
     * 通知所有用户
     */
    void insertReadOfAll(@Param("tenantId") String tenantId, @Param("noticeId") Long noticeId);

    /**
     * 通知部门用户
     */
    void insertReadOfDept(@Param("tenantId") String tenantId, @Param("noticeId") Long noticeId, @Param("list") List<Integer> list);

    /**
     * 通知角色用户
     */
    void insertReadOfRole(@Param("tenantId") String tenantId, @Param("noticeId") Long noticeId, @Param("list") List<Integer> list);

    /**
     * 通知指定用户
     */
    void insertReadOfUser(@Param("tenantId") String tenantId, @Param("noticeId") Long noticeId, @Param("list") List<Integer> list);

    /**
     * 消息列表
     */
    Page<NoticeListPto> msgList(Page<NoticeListPto> page, @Param("userCode") String userCode);

    /**
     * 删除消息
     */
    void msgDelete(@Param("userCode") String userCode, @Param("msgId") Long msgId);

    /**
     * 新用户的消息
     */
    void initNoticeMsgForNewUser(String userCode);

}
