package com.cowave.hub.admin.domain.auth.entity.vo;

import com.cowave.hub.admin.domain.auth.entity.SysAuthIdentity;
import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class LdapUserVo {

    /**
     * Ldap身份ID
     */
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 电话
     */
    private String userPhone;

    /**
     * 邮箱
     */
    private String userEmail;

    /**
     * 部门
     */
    private String userDept;

    /**
     * 岗位
     */
    private String userPost;

    /**
     * 上级用户
     */
    private String userLeader;

    /**
     * 身份启用状态
     */
    private EnableStatus authStatus;

    /**
     * 最近登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date lastLoginTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date updateTime;

    public static LdapUserVo from(SysAuthIdentity identity) {
        LdapUserVo vo = new LdapUserVo();
        vo.setId(identity.getId());
        vo.setAuthStatus(identity.getAuthStatus());
        vo.setLastLoginTime(identity.getLastLoginTime());
        vo.setUserAccount(identity.getUserAccount());
        vo.setUserName(identity.getUserName());
        vo.setUserPhone(identity.getUserPhone());
        vo.setUserEmail(identity.getUserEmail());
        vo.setUserDept(identity.getUserDept());
        vo.setUserPost(identity.getUserPost());
        vo.setUserLeader(identity.getUserLeader());
        vo.setCreateTime(identity.getCreateTime());
        vo.setUpdateTime(identity.getUpdateTime());
        return vo;
    }
}
