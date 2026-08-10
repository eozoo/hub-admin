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
import com.cowave.hub.admin.domain.rbac.biz.HubTenantBiz;
import com.cowave.hub.admin.domain.rbac.biz.HubUserBiz;
import com.cowave.hub.admin.domain.rbac.entity.HubTenant;
import com.cowave.hub.admin.domain.rbac.entity.HubUser;
import com.cowave.hub.admin.domain.rbac.entity.command.*;
import com.cowave.hub.admin.domain.rbac.entity.pto.TenantManagerPto;
import com.cowave.hub.admin.domain.rbac.entity.query.TenantQuery;
import com.cowave.hub.admin.domain.rbac.enums.UserType;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubTenantRepositoryFacade;
import com.cowave.hub.admin.domain.rbac.repository.facade.HubUserRepositoryFacade;
import com.cowave.hub.admin.domain.sys.biz.HubAttachBiz;
import com.cowave.hub.admin.domain.sys.biz.HubConfigBiz;
import com.cowave.hub.admin.domain.sys.entity.vo.SelectOptionVo;
import com.cowave.hub.admin.service.rbac.HubTenantService;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import com.cowave.zoo.tools.Collections;
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
public class HubTenantServiceImpl implements HubTenantService {
    private final HubTenantBiz tenantBiz;
    private final HubTenantRepositoryFacade tenantRepositoryFacade;
    private final HubUserBiz userBiz;
    private final HubUserRepositoryFacade userRepositoryFacade;
    private final HubAttachBiz attachBiz;
    private final HubConfigBiz configBiz;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<HubTenant> page(TenantQuery query) {
        return tenantRepositoryFacade.queryPage(query);
    }

    @Override
    public HubTenant info(String tenantId) {
        return tenantRepositoryFacade.queryById(tenantId);
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
        } else {
            tenantCreate.setLogo(null);
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

        HubUser hubUser = managerCreate.newSysUser();
        hubUser.setUserPasswd(passwordEncoder.encode(managerCreate.getUserPasswd()));
        userBiz.createTenantManager(hubUser);
        userBiz.saveUserRole(hubUser.getUserId(), 1);
        userBiz.saveUserDiagram(hubUser.getUserId(), 0, managerCreate.getTenantId());
    }

    @Override
    public void removeManager(TenantManagerRemove managerRemove) {
        userBiz.removeTenantManager(managerRemove);
    }

    @Override
    public List<SelectOptionVo> queryTenantOptions() {
        List<HubTenant> tenantList = tenantRepositoryFacade.queryList();
        return Collections.copyToList(tenantList, t -> new SelectOptionVo(t.getTenantId(), t.getTitle()));
    }
}
