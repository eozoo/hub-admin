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
import com.cowave.hub.admin.domain.sys.entity.HubAttach;
import com.cowave.hub.admin.domain.sys.entity.query.AttachQuery;
import com.cowave.hub.admin.domain.sys.enums.AttachType;
import com.cowave.hub.admin.domain.sys.repository.HubAttachRepository;
import com.cowave.hub.admin.infra.sys.mapper.HubAttachMapper;
import com.cowave.zoo.framework.access.Access;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
public class HubAttachDao extends ServiceImpl<HubAttachMapper, HubAttach> implements HubAttachRepository {

    @Override
    public HubAttach queryById(Long attachId) {
        return getById(attachId);
    }

    @Override
    public Page<HubAttach> queryPage(AttachQuery query) {
        return lambdaQuery()
                .eq(query.getOwnerId() != null, HubAttach::getOwnerId, query.getOwnerId())
                .eq(query.getOwnerModule() != null, HubAttach::getOwnerModule, query.getOwnerModule())
                .eq(query.getAttachType() != null, HubAttach::getAttachType, query.getAttachType())
                .ge(query.getBeginTime() != null, HubAttach::getCreateTime, query.getBeginTime())
                .le(query.getEndTime() != null, HubAttach::getCreateTime, query.getEndTime())
                .orderByDesc(HubAttach::getCreateTime)
                .page(Access.page());
    }

    @Override
    public List<HubAttach> queryByIds(List<Long> attachIds) {
        return listByIds(attachIds);
    }

    @Override
    public List<HubAttach> listByOwner(String ownerId, String ownerModule, AttachType attachType) {
        return lambdaQuery()
                .eq(HubAttach::getOwnerId, ownerId)
                .eq(HubAttach::getOwnerModule, ownerModule)
                .eq(attachType != null, HubAttach::getAttachType, attachType)
                .orderByDesc(HubAttach::getCreateTime)
                .list();
    }

    @Override
    public HubAttach queryLatestByOwner(String ownerId, String ownerModule, AttachType attachType) {
        return lambdaQuery()
                .eq(ownerId != null, HubAttach::getOwnerId, ownerId)
                .eq(ownerModule != null, HubAttach::getOwnerModule, ownerModule)
                .eq(attachType != null, HubAttach::getAttachType, attachType)
                .orderByDesc(HubAttach::getCreateTime)
                .last("LIMIT 1").one();
    }

    @Override
    public void updateOwner(String ownerId, Long attachId) {
        lambdaUpdate()
                .eq(HubAttach::getAttachId, attachId)
                .set(HubAttach::getOwnerId, ownerId)
                .update();
    }

    @Override
    public void clearOwner(String ownerId, String ownerModule, AttachType attachType, Long attachId) {
        lambdaUpdate()
                .eq(HubAttach::getOwnerId, ownerId)
                .eq(HubAttach::getOwnerModule, ownerModule)
                .eq(HubAttach::getAttachType, attachType)
                .eq(attachId != null, HubAttach::getAttachId, attachId)
                .set(HubAttach::getOwnerId, null)
                .update();
    }
}
