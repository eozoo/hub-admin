package com.cowave.hub.admin.domain.rbac2.entity.pto;

import com.cowave.hub.admin.domain.rbac2.entity.SysScope;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class ApiPermitMenu {

    /**
     * 菜单 ID
     */
    private Integer menuId;

    /**
     * 父菜单 ID
     */
    private Integer parentId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单类型
     */
    private String menuType;

    /**
     * 权限符
     */
    private String menuPermit;

    /**
     * 角色授权数据范围
     */
    private Integer scopeId;

    /**
     * 可选数据范围
     */
    private List<SysScope> scopes;
}
