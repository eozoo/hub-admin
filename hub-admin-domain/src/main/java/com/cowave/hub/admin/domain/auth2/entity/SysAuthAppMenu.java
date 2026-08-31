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
package com.cowave.hub.admin.domain.auth2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 认证应用菜单
 *
 * @author shanhuiming
 */
@Getter
@Setter
@TableName("sys_auth_app_menu")
public class SysAuthAppMenu {

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
     * 应用id
     */
    private Integer appId;

    /**
     * 菜单模块
     */
    private String menuModule;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单顺序
     */
    private Integer menuOrder;

    /**
     * 权限标识
     */
    private String menuPermit;

    /**
     * 菜单路径
     */
    private String menuPath;

    /**
     * 路由参数
     */
    private String menuParam;

    /**
     * 菜单类型：M目录、C菜单、B按钮
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
     * 是否受保护 1是 0否
     */
    private Integer isProtected;

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
}
