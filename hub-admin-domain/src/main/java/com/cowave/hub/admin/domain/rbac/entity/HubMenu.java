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
package com.cowave.hub.admin.domain.rbac.entity;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.cowave.zoo.framework.access.security.AccessInfoSetter;
import com.cowave.hub.admin.domain.rbac.enums.EnableStatus;
import com.cowave.hub.admin.domain.rbac.enums.converter.EnableStatusConverter;
import com.cowave.hub.admin.domain.rbac.enums.YesNo;
import com.cowave.hub.admin.domain.rbac.enums.converter.YesNoConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.cowave.hub.admin.domain.rbac.enums.YesNo.YES;

/**
 * @author shanhuiming
 */
@ExcelIgnoreUnannotated
@HeadRowHeight(20)
@HeadFontStyle(fontHeightInPoints = 10)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT)
@Getter
@Setter
public class HubMenu implements AccessInfoSetter {

    /**
     * 上级菜单id
     */
    @ExcelProperty("上级菜单编号")
    private Integer parentId;

    /**
     * 菜单id
     */
    @TableId(type = IdType.AUTO)
    @ExcelProperty("菜单编号")
    private Integer menuId;

	/**
     * 租户id
     */
    private String tenantId;

	/**
	 * 菜单模块
	 */
	private String menuModule;

    /**
     * 菜单名称
     */
    @NotBlank(message = "{admin.menu.name.null}")
	@ExcelProperty("菜单名称")
    private String menuName;

    /**
     * 菜单状态
     */
    @ExcelProperty(value = "菜单状态", converter = EnableStatusConverter.class)
    private EnableStatus menuStatus;

    /**
     * 菜单排序
     */
    @ExcelProperty("菜单排序")
    private Integer menuOrder;

    /**
     * 菜单类型
     */
    @NotBlank(message = "{admin.menu.type.null}")
	@ExcelProperty("菜单类型")
    private String menuType;

    /**
     * 菜单路由
     */
    @ColumnWidth(30)
	@ExcelProperty("菜单路由")
    private String menuPath;

    /**
     * 路由参数
     */
    @ExcelProperty("路由参数")
    private String menuParam;

    /**
     * 菜单图标
     */
    @ExcelProperty("菜单图标")
    private String menuIcon;

    /**
     * 菜单权限符
     */
    @ColumnWidth(30)
	@ExcelProperty("权限符")
    private String menuPermit;

    /**
     * 组件路径
     */
    @ColumnWidth(30)
	@ExcelProperty("组件路径")
    private String component;

    /**
     * 是否内部链接
     */
    @ExcelProperty(value = "是否内部链接", converter = YesNoConverter.class)
    private YesNo isFrame;

    /**
     * 是否缓存
     */
    @ExcelProperty(value = "是否缓存", converter = YesNoConverter.class)
    private YesNo isCache;

    /**
     * 是否显示
     */
    @ExcelProperty(value = "是否显示", converter = YesNoConverter.class)
    private YesNo isVisible;

    /**
     * 是否受保护
     */
    @ExcelProperty(value = "是否受保护的", converter = YesNoConverter.class)
    private YesNo isProtected;

    /**
     * 备注
     */
    @ExcelProperty("备注")
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
	private List<HubMenu> children = new ArrayList<>();

    /**
	 * 是否为内链组件
	 */
	public boolean ifInnerLink() {
		return YES == isFrame && StringUtils.startsWithAny(menuPath, "http://", "https://");
	}

	/**
	 * 是否为菜单内部跳转
	 */
	public boolean ifMenuFrame(){
		return parentId == 0L && "C".equals(menuType) && YES == isFrame;
	}

	/**
	 * 是否为parent_view组件
	 */
	public boolean ifParentView() {
		return parentId != 0L && "M".equals(menuType);
	}

	/**
	 * 路由名称
	 */
	public String routeName() {
		String routerName = StringUtils.capitalize(menuPath);
		if (ifMenuFrame()) {
			routerName = "";
		}
		return routerName;
	}

	/**
	 * 路由地址
	 */
	public String routePath() {
		String routerPath = menuPath;
		// 内链打开外网方式
		if (parentId != 0L && ifInnerLink()) {
			routerPath = routerPath.replace("http://", "");
			routerPath = routerPath.replace("https://", "");
		}
		// 非外链并且是一级目录
		if (parentId == 0L && "M".equals(menuType) && YES == isFrame) {
			routerPath = "/" + menuPath;
		} else if (ifMenuFrame()) {
			routerPath = "/";
		}
		return routerPath;
	}

	/**
	 * 路由组件
	 */
	public String routeComponent() {
		String routeComponent = "Layout";
		if (StringUtils.isNotEmpty(component) && !ifMenuFrame()) {
			routeComponent = component;
		} else if (StringUtils.isEmpty(component) && parentId != 0L && ifInnerLink()) {
			routeComponent = "InnerLink";
		} else if (StringUtils.isEmpty(component) && ifParentView()) {
			routeComponent = "ParentView";
		}
		return routeComponent;
	}
}
