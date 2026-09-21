/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.cowave.hub.admin.domain.rbac2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;
import com.cowave.hub.admin.domain.rbac2.enums.TenantType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 租户信息
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysTenant {

    /**
     * 租户 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer tenantId;

    /**
     * 对外使用的唯一租户编码
     */
    private String tenantCode;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 租户类型
     */
    private TenantType tenantType;

    /**
     * 租户域名
     */
    private String tenantDomain;

    /**
     * 用户数量上限
     */
    private Integer userLimit;

    /**
     * 当前用户数量
     */
    private Integer userCount;

    /**
     * 租户启停状态
     */
    private EnableStatus status;

    /**
     * 租户到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

    /**
     * 页面展示标题
     */
    private String title;

    /**
     * 首页视图
     */
    private String viewIndex;

    /**
     * 租户联系人
     */
    private String tenantUser;

    /**
     * 租户联系地址
     */
    private String tenantAddr;

    /**
     * 租户联系电话
     */
    private String tenantPhone;

    /**
     * 租户联系邮箱
     */
    private String tenantEmail;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人账号
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人账号
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
