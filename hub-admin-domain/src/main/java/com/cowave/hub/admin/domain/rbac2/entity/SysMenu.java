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
package com.cowave.hub.admin.domain.rbac2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 菜单信息
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysMenu {

    /**
     * 菜单id
     */
    @TableId(type = IdType.AUTO)
    private Integer menuId;

    /**
     * 父菜单id
     */
    private Integer parentId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 所属模块编码
     */
    private String menuModule;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单顺序
     */
    private String menuOrder;

    /**
     * 权限标识
     */
    private String menuPermit;

    /**
     * 菜单路径
     */
    private String menuPath;

    /**
     * 路径参数
     */
    private String menuParam;

    /**
     * 菜单类型：M:目录 C:菜单 B:按钮
     */
    private String menuType;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 菜单状态 1启用 2停用
     */
    private Integer menuStatus;

    /**
     * 是否内部链接 1是 0否
     */
    private Integer isFrame;

    /**
     * 是否缓存 1是 0否
     */
    private Integer isCache;

    /**
     * 是否显示 1是 0否
     */
    private Integer isVisible;

    /**
     * 是否受保护的菜单 1是 0否
     */
    private Integer isProtected;

    /**
     * 是否新租户模板数据 1是 0否，仅system租户有效，复制后置0
     */
    private Integer isTemplate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();

    public boolean ifInnerLink() {
        return Objects.equals(isFrame, 1)
                && StringUtils.startsWithAny(menuPath, "http://", "https://");
    }

    public boolean ifMenuFrame() {
        return Objects.equals(parentId, 0)
                && "C".equals(menuType)
                && Objects.equals(isFrame, 1);
    }

    public boolean ifParentView() {
        return !Objects.equals(parentId, 0) && "M".equals(menuType);
    }

    public String routeName() {
        return ifMenuFrame() ? "" : StringUtils.capitalize(menuPath);
    }

    public String routePath() {
        String routePath = menuPath;
        if (!Objects.equals(parentId, 0) && ifInnerLink()) {
            routePath = StringUtils.removeStart(routePath, "http://");
            routePath = StringUtils.removeStart(routePath, "https://");
        }
        if (Objects.equals(parentId, 0)
                && "M".equals(menuType)
                && Objects.equals(isFrame, 1)) {
            routePath = "/" + menuPath;
        } else if (ifMenuFrame()) {
            routePath = "/";
        }
        return routePath;
    }

    public String routeComponent() {
        if (StringUtils.isNotEmpty(component) && !ifMenuFrame()) {
            return component;
        }
        if (StringUtils.isEmpty(component) && !Objects.equals(parentId, 0) && ifInnerLink()) {
            return "InnerLink";
        }
        if (StringUtils.isEmpty(component) && ifParentView()) {
            return "ParentView";
        }
        return "Layout";
    }
}
