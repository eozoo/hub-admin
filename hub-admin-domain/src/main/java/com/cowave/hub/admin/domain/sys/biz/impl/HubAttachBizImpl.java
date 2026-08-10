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
package com.cowave.hub.admin.domain.sys.biz.impl;

import com.cowave.hub.admin.domain.sys.biz.HubAttachBiz;
import com.cowave.hub.admin.domain.sys.entity.HubAttach;
import com.cowave.hub.admin.domain.sys.enums.AttachType;
import com.cowave.hub.admin.domain.sys.repository.HubAttachRepository;
import com.cowave.hub.admin.domain.sys.store.HubAttachStore;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubAttachBizImpl implements HubAttachBiz {
    private final HubAttachStore attachStore;
    private final HubAttachRepository attachRepository;

    @Override
    public void uploadAttach(MultipartFile file, HubAttach attach) throws Exception {
        attachRepository.save(attach);
        attachStore.upload(file, attach);
        attach.setViewUrl(attachStore.preview(attach));
    }

    @Override
    public void downloadAttach(HttpServletResponse response, HubAttach attach) throws Exception {
        attachStore.download(response, attach);
    }

    @Override
    public String previewAttach(HubAttach hubAttach) throws Exception {
        return attachStore.preview(hubAttach);
    }

    @Override
    public void removeAttach(HubAttach attach) throws Exception {
        attachRepository.removeById(attach.getAttachId());
        attachStore.remove(attach);
    }

    @Override
    public void removeAttachByIds(List<Long> attachIds) throws Exception {
        if (CollectionUtils.isEmpty(attachIds)) {
            return;
        }
        List<HubAttach> attachList = attachRepository.listByIds(attachIds);
        for (HubAttach attach : attachList) {
            removeAttach(attach);
        }
    }

    @Override
    public void updateOwner(String ownerId, Long attachId) {
        attachRepository.updateOwner(ownerId, attachId);
    }

    @Override
    public void clearOwner(String ownerId, String ownerModule, AttachType attachType, Long attachId) {
        attachRepository.clearOwner(ownerId, ownerModule, attachType, attachId);
    }

    @Override
    public void reserveByOwner(String ownerId, String ownerModule, AttachType attachType, int reserve) throws Exception {
        List<HubAttach> list = attachRepository.listByOwner(ownerId, ownerModule, attachType);
        for (int i = reserve; i < list.size(); i++) {
            HubAttach attach = list.get(i);
            removeAttach(attach);
        }
    }

    @Override
    public HubAttach previewLatestByOwner(String ownerId, String ownerModule, AttachType attachType) throws Exception {
        HubAttach attach = attachRepository.queryLatestByOwner(ownerId, ownerModule, attachType);
        if (attach != null) {
            attach.setViewUrl(previewAttach(attach));
        }
        return attach;
    }
}
