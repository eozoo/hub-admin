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
package com.cowave.hub.admin.service.sys.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.biz.SysAttachBiz;
import com.cowave.hub.admin.domain.sys.entity.SysAttach;
import com.cowave.hub.admin.domain.sys.entity.command.AttachUpload;
import com.cowave.hub.admin.domain.sys.entity.query.AttachQuery;
import com.cowave.hub.admin.domain.sys.repository.facade.SysAttachRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.domain.sys.repository.facade.SysNoticeRepositoryFacade;
import com.cowave.hub.admin.service.sys.SysAttachService;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.tools.DateUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

import static com.cowave.hub.admin.domain.sys.enums.OpModule.*;
import static com.cowave.zoo.http.client.constants.HttpCode.NOT_FOUND;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysAttachServiceImpl implements SysAttachService {
    private final SysAttachBiz attachBiz;
    private final SysAttachRepositoryFacade attachRepositoryFacade;
    private final SysUserRepositoryFacade userRepositoryFacade;
    private final SysTenantRepositoryFacade tenantRepositoryFacade;
    private final SysNoticeRepositoryFacade noticeRepositoryFacade;

    @Override
    public Page<SysAttach> page(String tenantId, AttachQuery query) {
        Page<SysAttach> page = attachRepositoryFacade.queryPage(query);
        for (SysAttach attach : page.getRecords()) {
            String ownerId = attach.getOwnerId();
            if (StringUtils.isBlank(ownerId)) {
                continue;
            }

            String ownerName = null;
            String module = attach.getOwnerModule();
            if (SYSTEM_USER.equals(module)) {
                ownerName = userRepositoryFacade.queryNameById(Integer.valueOf(ownerId));
            } else if (SYSTEM_NOTICE.equals(module)) {
                ownerName = noticeRepositoryFacade.queryNameById(Long.valueOf(ownerId));
            } else if (SYSTEM_TENANT.equals(module)) {
                ownerName = tenantRepositoryFacade.queryNameById(ownerId);
            }
            attach.setOwnerName(ownerName);
        }
        return page;
    }

    @Override
    public SysAttach upload(MultipartFile file, AttachUpload upload) throws Exception {
        String fileName = file.getOriginalFilename();
        SysAttach attach = new SysAttach();
        attach.setAttachName(fileName);
        attach.setAttachSize(file.getSize());
        attach.setOwnerId(upload.getOwnerId());
        attach.setOwnerModule(upload.getOwnerModule());
        attach.setAttachType(upload.getAttachType());
        attach.setIsPrivate(upload.getIsPrivate());
        attach.setCreateBy(Access.userCode());
        attach.setUpdateBy(Access.userCode());
        attach.setCreateTime(Access.accessTime());
        attach.setUpdateTime(Access.accessTime());
        attach.setTenantId(upload.getTenantId());
        if (StringUtils.isBlank(upload.getTenantId())) {
            attach.setTenantId(Access.tenantId());
        }
        String filePath = upload.getAttachType() + File.separator
                + DateUtils.format("yyyy-MM") + File.separator + IdUtil.randomUUID() + "." + fileName;
        attach.setAttachPath(filePath);
        attachBiz.uploadAttach(file, attach);
        return attach;
    }

    @Override
    public void download(HttpServletResponse response, Long attachId) throws Exception {
        SysAttach attach = attachRepositoryFacade.queryById(attachId);
        HttpAsserts.notNull(attach, NOT_FOUND, "{admin.attach.not.exist}");
        attachBiz.downloadAttach(response, attach);
    }

    @Override
    public String preview(Long attachId) throws Exception {
        SysAttach attach = attachRepositoryFacade.queryById(attachId);
        HttpAsserts.notNull(attach, NOT_FOUND, "{admin.attach.not.exist}");
        return preview(attach);
    }

    @Override
    public String preview(SysAttach attach) throws Exception {
        return attachBiz.previewAttach(attach);
    }

    @Override
    public void delete(List<Long> attachIds) throws Exception {
        List<SysAttach> attachList = attachRepositoryFacade.queryByIds(attachIds);
        for(SysAttach attach : attachList){
            attachBiz.removeAttach(attach);
        }
    }
}
