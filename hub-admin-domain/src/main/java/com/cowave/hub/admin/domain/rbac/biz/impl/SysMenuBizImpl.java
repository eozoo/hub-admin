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
package com.cowave.hub.admin.domain.rbac.biz.impl;

import com.cowave.hub.admin.domain.rbac.biz.SysMenuBiz;
import com.cowave.hub.admin.domain.rbac.entity.SysMenu;
import com.cowave.hub.admin.domain.rbac.repository.SysMenuRepository;
import com.cowave.zoo.http.client.asserts.HttpAsserts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;
import static com.cowave.zoo.http.client.constants.HttpCode.NOT_FOUND;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysMenuBizImpl implements SysMenuBiz {

    private final SysMenuRepository menuRepository;

    @Override
    public void createMenu(SysMenu sysMenu) {
        menuRepository.save(sysMenu);
    }

    @Override
    public void deleteMenu(Integer menuId) {
        SysMenu preMenu = menuRepository.queryById(menuId);
        if (preMenu == null) {
            return;
        }
        menuRepository.loopDeleteMenuRoles(menuId);
        menuRepository.loopDeleteMenus(menuId);
    }

    @Override
    public void editMenu(SysMenu sysMenu) {
        HttpAsserts.notNull(sysMenu.getMenuId(), BAD_REQUEST, "{admin.menu.id.null}");

        SysMenu preMenu = menuRepository.queryById(sysMenu.getMenuId());
        HttpAsserts.notNull(preMenu, NOT_FOUND, "{admin.menu.not.exist}", sysMenu.getMenuId());

        if (!"C".equals(sysMenu.getMenuType())) {
            sysMenu.setComponent(null);
        }
        menuRepository.updateMenu(sysMenu);
    }
}
