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

import com.cowave.hub.admin.domain.sys.biz.SysAttachBiz;
import com.cowave.hub.admin.domain.sys.entity.SysAttach;
import com.cowave.hub.admin.domain.sys.enums.AttachType;
import com.cowave.hub.admin.domain.sys.repository.SysAttachRepository;
import com.cowave.hub.admin.domain.sys.store.SysAttachStore;
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
public class SysAttachBizImpl implements SysAttachBiz {
    private final SysAttachStore attachStore;
    private final SysAttachRepository attachRepository;

    @Override
    public void uploadAttach(MultipartFile file, SysAttach attach) throws Exception {
        attachRepository.save(attach);
        attachStore.upload(file, attach);
        attach.setViewUrl(attachStore.preview(attach));
    }

    @Override
    public void downloadAttach(HttpServletResponse response, SysAttach attach) throws Exception {
        attachStore.download(response, attach);
    }

    @Override
    public String previewAttach(SysAttach sysAttach) throws Exception {
        return attachStore.preview(sysAttach);
    }

    @Override
    public void removeAttach(SysAttach attach) throws Exception {
        attachRepository.removeById(attach.getAttachId());
        attachStore.remove(attach);
    }

    @Override
    public void removeAttachByIds(List<Long> attachIds) throws Exception {
        if (CollectionUtils.isEmpty(attachIds)) {
            return;
        }
        List<SysAttach> attachList = attachRepository.listByIds(attachIds);
        for (SysAttach attach : attachList) {
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
        List<SysAttach> list = attachRepository.listByOwner(ownerId, ownerModule, attachType);
        for (int i = reserve; i < list.size(); i++) {
            SysAttach attach = list.get(i);
            removeAttach(attach);
        }
    }

    @Override
    public SysAttach previewLatestByOwner(String ownerId, String ownerModule, AttachType attachType) throws Exception {
        SysAttach attach = attachRepository.queryLatestByOwner(ownerId, ownerModule, attachType);
        if (attach != null) {
            attach.setViewUrl(previewAttach(attach));
        }
        return attach;
    }
}
