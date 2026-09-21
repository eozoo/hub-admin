package com.cowave.hub.admin.domain.auth.entity.vo;

import com.cowave.hub.admin.domain.auth.entity.SysAuthIdentity;
import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * OAuth提供方身份展示信息
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class OAuthUserVo {

    /**
     * 身份ID
     */
    private Long id;

    /**
     * 关联的本地用户ID
     */
    private Integer userId;

    /**
     * 外部系统稳定身份标识
     */
    private String externalSubject;

    /**
     * 外部身份头像
     */
    private String userAvatar;

    /**
     * OAuth提供方账号
     */
    private String userAccount;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 邮箱
     */
    private String userEmail;

    /**
     * 身份状态
     */
    private EnableStatus authStatus;

    /**
     * 最近登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date lastLoginTime;

    /**
     * 最近同步时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date lastSyncTime;

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

    public static OAuthUserVo from(SysAuthIdentity identity) {
        OAuthUserVo vo = new OAuthUserVo();
        vo.setId(identity.getId());
        vo.setUserId(identity.getUserId());
        vo.setExternalSubject(identity.getExternalSubject());
        vo.setUserAvatar(identity.getUserAvatar());
        vo.setUserAccount(identity.getUserAccount());
        vo.setUserName(identity.getUserName());
        vo.setUserEmail(identity.getUserEmail());
        vo.setAuthStatus(identity.getAuthStatus());
        vo.setLastLoginTime(identity.getLastLoginTime());
        vo.setLastSyncTime(identity.getLastSyncTime());
        vo.setCreateTime(identity.getCreateTime());
        vo.setUpdateTime(identity.getUpdateTime());
        return vo;
    }
}
