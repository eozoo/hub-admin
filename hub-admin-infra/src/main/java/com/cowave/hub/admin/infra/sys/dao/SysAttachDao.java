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
package com.cowave.hub.admin.infra.sys.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.sys.entity.SysAttach;
import com.cowave.hub.admin.domain.sys.entity.query.AttachQuery;
import com.cowave.hub.admin.domain.sys.enums.AttachType;
import com.cowave.hub.admin.domain.sys.repository.SysAttachRepository;
import com.cowave.hub.admin.infra.sys.mapper.SysAttachMapper;
import com.cowave.zoo.framework.access.Access;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class SysAttachDao extends ServiceImpl<SysAttachMapper, SysAttach> implements SysAttachRepository {

    @Override
    public SysAttach queryById(Long attachId) {
        return getById(attachId);
    }

    @Override
    public Page<SysAttach> queryPage(AttachQuery query) {
        return lambdaQuery()
                .eq(query.getOwnerId() != null, SysAttach::getOwnerId, query.getOwnerId())
                .eq(query.getOwnerModule() != null, SysAttach::getOwnerModule, query.getOwnerModule())
                .eq(query.getAttachType() != null, SysAttach::getAttachType, query.getAttachType())
                .ge(query.getBeginTime() != null, SysAttach::getCreateTime, query.getBeginTime())
                .le(query.getEndTime() != null, SysAttach::getCreateTime, query.getEndTime())
                .orderByDesc(SysAttach::getCreateTime)
                .page(Access.page());
    }

    @Override
    public List<SysAttach> queryByIds(List<Long> attachIds) {
        return listByIds(attachIds);
    }

    @Override
    public List<SysAttach> listByOwner(String ownerId, String ownerModule, AttachType attachType) {
        return lambdaQuery()
                .eq(SysAttach::getOwnerId, ownerId)
                .eq(SysAttach::getOwnerModule, ownerModule)
                .eq(attachType != null, SysAttach::getAttachType, attachType)
                .orderByDesc(SysAttach::getCreateTime)
                .list();
    }

    @Override
    public SysAttach queryLatestByOwner(String ownerId, String ownerModule, AttachType attachType) {
        return lambdaQuery()
                .eq(ownerId != null, SysAttach::getOwnerId, ownerId)
                .eq(ownerModule != null, SysAttach::getOwnerModule, ownerModule)
                .eq(attachType != null, SysAttach::getAttachType, attachType)
                .orderByDesc(SysAttach::getCreateTime)
                .last("LIMIT 1").one();
    }

    @Override
    public void updateOwner(String ownerId, Long attachId) {
        lambdaUpdate()
                .eq(SysAttach::getAttachId, attachId)
                .set(SysAttach::getOwnerId, ownerId)
                .update();
    }

    @Override
    public void clearOwner(String ownerId, String ownerModule, AttachType attachType, Long attachId) {
        lambdaUpdate()
                .eq(SysAttach::getOwnerId, ownerId)
                .eq(SysAttach::getOwnerModule, ownerModule)
                .eq(SysAttach::getAttachType, attachType)
                .eq(attachId != null, SysAttach::getAttachId, attachId)
                .set(SysAttach::getOwnerId, null)
                .update();
    }
}
