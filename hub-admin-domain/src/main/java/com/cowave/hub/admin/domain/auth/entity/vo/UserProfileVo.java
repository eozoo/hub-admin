package com.cowave.hub.admin.domain.auth.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * 个人中心资料
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class UserProfileVo {

    /**
     * 当前租户ID
     */
    private Integer tenantId;

    /**
     * 当前租户名称
     */
    private String tenantName;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 当前租户下的用户编码
     */
    private String userCode;

    /**
     * 当前租户下的用户类型
     */
    private String userType;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户性别
     */
    private Integer userSex;

    /**
     * 用户头像预览地址
     */
    private String avatar;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 当前租户角色名称
     */
    private List<String> roles;

    /**
     * 当前租户部门与岗位名称
     */
    private List<String> depts;

    /**
     * 当前租户汇报对象名称
     */
    private List<String> parents;

    /**
     * 用户创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
