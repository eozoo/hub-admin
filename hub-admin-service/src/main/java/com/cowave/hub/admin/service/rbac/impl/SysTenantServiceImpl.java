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
package com.cowave.hub.admin.service.rbac.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.rbac.biz.SysTenantBiz;
import com.cowave.hub.admin.domain.rbac.biz.SysUserBiz;
import com.cowave.hub.admin.domain.rbac.entity.SysTenant;
import com.cowave.hub.admin.domain.rbac.entity.SysUser;
import com.cowave.hub.admin.domain.rbac.entity.command.*;
import com.cowave.hub.admin.domain.rbac.entity.vo.TenantInfoVo;
import com.cowave.hub.admin.domain.rbac.entity.pto.TenantManagerPto;
import com.cowave.hub.admin.domain.rbac.entity.query.TenantQuery;
import com.cowave.hub.admin.domain.rbac.enums.UserType;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.SysUserRepositoryFacade;
import com.cowave.hub.admin.domain.sys.biz.SysAttachBiz;
import com.cowave.hub.admin.domain.sys.biz.SysConfigBiz;
import com.cowave.hub.admin.domain.sys.entity.SysAttach;
import com.cowave.hub.admin.domain.sys.entity.vo.SelectOptionVo;
import com.cowave.hub.admin.service.rbac.SysTenantService;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.tools.Collections;
import com.cowave.zoo.tools.Converts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cowave.hub.admin.domain.sys.enums.AttachType.LOGO;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_TENANT;
import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;

/**
 * @author shanhuiming
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysTenantServiceImpl implements SysTenantService {
    private final SysTenantBiz tenantBiz;
    private final SysTenantRepositoryFacade tenantRepositoryFacade;
    private final SysUserBiz userBiz;
    private final SysUserRepositoryFacade userRepositoryFacade;
    private final SysAttachBiz attachBiz;
    private final SysConfigBiz configBiz;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<SysTenant> page(TenantQuery query) {
        return tenantRepositoryFacade.queryPage(query);
    }

    @Override
    public TenantInfoVo info(String tenantId) throws Exception {
        TenantInfoVo vo = Converts.copyProperties(tenantRepositoryFacade.queryById(tenantId), TenantInfoVo.class);
        SysAttach attach = attachBiz.previewLatestByOwner(tenantId, SYSTEM_TENANT, LOGO);
        if (attach != null) {
            vo.setLogo(attach.getViewUrl());
            vo.setAttachId(attach.getAttachId());
        }
        return vo;
    }

    @Override
    public void create(TenantCreate tenantCreate) {
        tenantBiz.createTenant(tenantCreate);
        configBiz.resetTenantConfig(tenantCreate.getTenantId());
        attachBiz.updateOwner(tenantCreate.getTenantId(), tenantCreate.getAttachId());
    }

    @Override
    public void edit(TenantCreate tenantCreate) {
        attachBiz.clearOwner(tenantCreate.getTenantId(), SYSTEM_TENANT, LOGO, tenantCreate.getAttachId());
        if (tenantCreate.getAttachId() != null) {
            attachBiz.updateOwner(tenantCreate.getTenantId(), tenantCreate.getAttachId());
        }
        tenantBiz.editTenant(tenantCreate);
    }

    @Override
    public void updateStatus(TenantStatusUpdate statusUpdate) {
        tenantBiz.updateStatus(statusUpdate);
    }

    @Override
    public Page<TenantManagerPto> listManager(String tenantId) {
        return userRepositoryFacade.queryTenantManager(tenantId);
    }

    @Override
    public void createManager(TenantManagerCreate managerCreate) {
        long accountCount = userRepositoryFacade.countByAccount(
                managerCreate.getTenantId(), UserType.SYS, managerCreate.getUserAccount(), null);
        HttpAsserts.isTrue(accountCount == 0,
                BAD_REQUEST, "{admin.user.account.conflict}", managerCreate.getUserAccount());

        SysUser sysUser = managerCreate.newSysUser();
        sysUser.setUserPasswd(passwordEncoder.encode(managerCreate.getUserPasswd()));
        userBiz.saveUser(sysUser);
        userBiz.saveUserRole(sysUser.getUserId(), 1);
        userBiz.saveUserDiagram(sysUser.getUserId(), 0, managerCreate.getTenantId());
    }

    @Override
    public void removeManager(TenantManagerRemove managerRemove) {
        userBiz.removeTenantManager(managerRemove);
    }

    @Override
    public List<SelectOptionVo> queryTenantOptions() {
        List<SysTenant> tenantList = tenantRepositoryFacade.queryList();
        return Collections.copyToList(tenantList, t -> new SelectOptionVo(t.getTenantId(), t.getTitle()));
    }
}
