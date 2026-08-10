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
package com.cowave.hub.admin.controller.rbac;

import com.cowave.hub.admin.SpringTest;
import com.cowave.hub.admin.domain.rbac.entity.HubMenu;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
public class HubMenuControllerTest extends SpringTest {

    /**
     * 登录 -> 新增菜单 -> 列表验证 -> 详情 -> 修改 -> 详情验证 -> 导出 -> 删除 -> 列表验证 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/menu
     * get /api/v1/menu
     * get /api/v1/menu/{menuId}
     * patch /api/v1/menu
     * get /api/v1/menu/{menuId}
     * post /api/v1/menu/export
     * delete /api/v1/menu/{menuId}
     * get /api/v1/menu
     * delete /api/v1/auth/logout
     */
    @Test
    public void menuCrud() throws Exception {
        // 登录
        String body = """
                {
                "tenantId" : "cowave",
                "userAccount" : "liubei",
                "passWord" : "12345678"
                }
                """;
        MvcResult mvcResult = mockPost("/api/v1/auth/public/logon", body);
        String accessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        // 新增菜单（挂在 parentId=1 系统管理下）
        String menuName = "test-menu-crud";
        body = """
                {
                    "parentId": 1,
                    "menuName": "%s",
                    "menuType": "C",
                    "menuOrder": 99,
                    "menuStatus": 1
                }
                """.formatted(menuName);
        mockPost("/api/v1/menu", body, accessToken);
        // 列表，验证新增
        mvcResult = mockGet("/api/v1/menu?menuName=" + menuName + "&page=1&pageSize=100", accessToken);
        List<HubMenu> menuList = readData(mvcResult, "/data/list", new TypeReference<List<HubMenu>>() {});
        Assertions.assertEquals(1, menuList.size());
        HubMenu menu = menuList.get(0);
        Integer menuId = menu.getMenuId();
        Assertions.assertEquals(menuName, menu.getMenuName());
        Assertions.assertEquals("C", menu.getMenuType());
        // 详情
        mvcResult = mockGet("/api/v1/menu/" + menuId, accessToken);
        HubMenu detail = readData(mvcResult, "/data", new TypeReference<HubMenu>() {});
        Assertions.assertEquals(menuId, detail.getMenuId());
        Assertions.assertEquals(menuName, detail.getMenuName());
        // 修改菜单
        String updatedName = "test-menu-crud-updated";
        body = """
                {
                    "menuId": %d,
                    "parentId": 1,
                    "menuName": "%s",
                    "menuType": "C",
                    "menuOrder": 100,
                    "menuStatus": 0
                }
                """.formatted(menuId, updatedName);
        mockPatch("/api/v1/menu", body, accessToken);
        // 详情，验证修改
        mvcResult = mockGet("/api/v1/menu/" + menuId, accessToken);
        detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(updatedName, detail.getMenuName());
        Assertions.assertEquals(100, detail.getMenuOrder());
        Assertions.assertEquals(EnableStatus.DISABLE, detail.getMenuStatus());
        // 导出菜单
        mockExport("/api/v1/menu/export", null, "target/menu.xlsx", accessToken);
        // 删除菜单
        mockDelete("/api/v1/menu/" + menuId, accessToken);
        // 列表，验证删除
        mvcResult = mockGet("/api/v1/menu?menuName=" + updatedName + "&page=1&pageSize=100", accessToken);
        menuList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(0, menuList.size());
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }

    /**
     * 登录 -> 菜单树 -> 列表 -> 详情 -> 退出登录
     * post /api/v1/auth/public/logon
     * get /api/v1/menu/tree
     * get /api/v1/menu
     * get /api/v1/menu/{menuId}
     * delete /api/v1/auth/logout
     */
    @Test
    public void menuTree() throws Exception {
        // 登录
        String body = """
                {
                "tenantId" : "cowave",
                "userAccount" : "liubei",
                "passWord" : "12345678"
                }
                """;
        MvcResult mvcResult = mockPost("/api/v1/auth/public/logon", body);
        String accessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        // 菜单树
        mvcResult = mockGet("/api/v1/menu/tree", accessToken);
        List<Map<String, Object>> tree = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertFalse(tree.isEmpty(), "菜单树至少应有1个根节点");
        // 菜单列表
        mvcResult = mockGet("/api/v1/menu?page=1&pageSize=100", accessToken);
        List<HubMenu> menuList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(menuList.isEmpty(), "菜单列表至少应有1条");
        // 菜单详情（查预置菜单 menuId=5 用户管理）
        mvcResult = mockGet("/api/v1/menu/5", accessToken);
        HubMenu detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertNotNull(detail);
        Assertions.assertNotNull(detail.getMenuName(), "菜单名称不应为空");
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
