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
package com.cowave.hub.admin.infra.auth.dao;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cowave.hub.admin.domain.auth.entity.HubOAuthApp;
import com.cowave.hub.admin.domain.auth.entity.HubOAuthAppMenu;
import com.cowave.hub.admin.domain.rbac.entity.*;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.domain.auth.repository.HubOAuthAppRepository;
import com.cowave.hub.admin.infra.auth.mapper.HubOAuthAppMapper;
import com.cowave.hub.admin.infra.auth.mapper.HubOAuthAppMenuMapper;
import com.cowave.hub.admin.infra.rbac.mapper.*;
import com.cowave.zoo.framework.access.Access;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class HubOAuthAppDao extends ServiceImpl<HubOAuthAppMapper, HubOAuthApp> implements HubOAuthAppRepository {
    private final HubOAuthAppMenuMapper oauthAppMenuMapper;
    private final HubRoleAppMapper roleAppMapper;
    private final HubRoleAppMenuMapper roleAppMenuMapper;

    @Override
    public Page<HubOAuthApp> queryPage(String tenantId, String clientName) {
        return lambdaQuery()
                .eq(HubOAuthApp::getTenantId, tenantId)
                .like(StringUtils.isNotBlank(clientName), HubOAuthApp::getClientName, clientName)
                .orderByDesc(HubOAuthApp::getCreateTime)
                .page(Access.page());
    }

    @Override
    public HubOAuthApp queryByClientId(String clientId) {
        return lambdaQuery().eq(HubOAuthApp::getClientId, clientId).one();
    }

    @Override
    public List<HubOAuthApp> queryListByTenantId(String tenantId) {
        return lambdaQuery().eq(HubOAuthApp::getTenantId, tenantId).list();
    }

    @Override
    public List<HubOAuthApp> queryListByIds(Set<Integer> appIds) {
        return listByIds(appIds);
    }

    @Override
    public List<HubOAuthAppMenu> queryListMenus(Integer appId, String menuName, EnableStatus menuStatus) {
        return oauthAppMenuMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HubOAuthAppMenu>()
                .eq(HubOAuthAppMenu::getAppId, appId)
                .eq(menuStatus != null, HubOAuthAppMenu::getMenuStatus, menuStatus)
                .like(StringUtils.isNotBlank(menuName), HubOAuthAppMenu::getMenuName, menuName)
                .orderByAsc(HubOAuthAppMenu::getParentId, HubOAuthAppMenu::getMenuOrder));
    }

    @Override
    public List<Integer> queryRoleAppIdsByRoleId(Integer roleId) {
        return roleAppMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HubRoleApp>()
                        .eq(HubRoleApp::getRoleId, roleId)
                        .select(HubRoleApp::getAppId))
                .stream().map(HubRoleApp::getAppId).toList();
    }

    @Override
    public List<HubRoleApp> queryRoleAppsByRoleIdList(List<Integer> roleIdList) {
        return roleAppMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HubRoleApp>()
                .in(HubRoleApp::getRoleId, roleIdList));
    }

    @Override
    public void removeByIds(String tenantId, List<Integer> ids) {
        lambdaUpdate()
                .eq(HubOAuthApp::getTenantId, tenantId)
                .in(HubOAuthApp::getId, ids)
                .remove();
    }

    @Override
    public void removeMenusByAppIds(List<Integer> appIds) {
        oauthAppMenuMapper.delete(new LambdaUpdateWrapper<HubOAuthAppMenu>()
                .in(HubOAuthAppMenu::getAppId, appIds));
    }

    @Override
    public void removeRoleAppsByRoleId(Integer roleId) {
        roleAppMapper.delete(new LambdaUpdateWrapper<HubRoleApp>()
                .eq(HubRoleApp::getRoleId, roleId));
    }

    @Override
    public void removeRoleAppsByAppIds(List<Integer> appIds) {
        roleAppMapper.delete(new LambdaUpdateWrapper<HubRoleApp>()
                .in(HubRoleApp::getAppId, appIds));
    }

    @Override
    public void removeRoleAppMenusByAppIds(List<Integer> appIds) {
        roleAppMenuMapper.delete(new LambdaUpdateWrapper<HubRoleAppMenu>()
                .in(HubRoleAppMenu::getAppId, appIds));
    }

    @Override
    public void saveRoleAppsBatch(List<HubRoleApp> list) {
        for (HubRoleApp ra : list) {
            roleAppMapper.insert(ra);
        }
    }
}
