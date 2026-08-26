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
import com.cowave.hub.admin.domain.sys.entity.SysAttach;
import com.cowave.hub.admin.domain.sys.enums.AttachType;
import com.cowave.hub.admin.domain.sys.entity.query.AttachQuery;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysAttachRepositoryFacade {

    /**
     * 分页查询
     */
    Page<SysAttach> queryPage(AttachQuery query);

    /**
     * 列表查询(id)
     */
    List<SysAttach> queryByIds(List<Long> attachIds);

    /**
     * 详情
     */
    SysAttach queryById(Long attachId);

    /**
     * 宿主附件列表
     */
    List<SysAttach> listByOwner(String ownerId, String ownerModule, AttachType attachType);

    /**
     * 匿名md5查询（任意一个）
     */
    SysAttach queryAnyByMd5(String md5);

    /**
     * 租户md5查询（任意一个）
     */
    SysAttach queryByMd5(String tenantId, String md5);

    /**
     * 租户md5统计
     */
    long countByMd5(String tenantId, String md5);
}
