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
package com.cowave.hub.admin.domain.rbac.entity.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * @author ruoyi
 */
@NoArgsConstructor
@Getter
@Setter
public class RouteMeta {

    /**
     * 设置路由在侧边栏和面包屑中展示的名字
     */
    private String title;

    /**
     * 设置路由图标，对应路径src/assets/icons/svg
     */
    private String icon;

    /**
     * 设置为true，则不会被 <keep-alive>缓存
     */
    private boolean noCache;

    /**
     * 内链地址（http(s)://开头）
     */
    private String link;

    public RouteMeta(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }

    public RouteMeta(String title, String icon, String link) {
        this.title = title;
        this.icon = icon;
        this.link = link;
    }

    public RouteMeta(String title, String icon, boolean noCache, String link) {
        this.title = title;
        this.icon = icon;
        this.noCache = noCache;
        this.link = link;
    }
}
