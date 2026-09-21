package com.cowave.hub.admin.domain.auth.entity.vo;

import com.cowave.hub.admin.domain.auth.enums.ProviderCode;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.SysAuthIdentity;
import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class IdentityBindingVo {

    /**
     * 提供方编码
     */
    private ProviderCode providerCode;

    /**
     * 提供方启用状态
     */
    private EnableStatus status;

    /**
     * 是否已绑定
     */
    private boolean bound;

    /**
     * 外部账号
     */
    private String userAccount;

    /**
     * 外部用户名称
     */
    private String userName;

    /**
     * 头像地址
     */
    private String userAvatar;

    /**
     * 邮箱
     */
    private String userEmail;

    /**
     * 绑定时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public static IdentityBindingVo from(ProviderCode providerCode, EnableStatus status, SysAuthIdentity identity) {
        IdentityBindingVo binding = new IdentityBindingVo();
        binding.setProviderCode(providerCode);
        binding.setStatus(status);
        binding.setBound(identity != null);
        if (identity != null) {
            binding.setUserAccount(identity.getUserAccount());
            binding.setUserName(identity.getUserName());
            binding.setUserAvatar(identity.getUserAvatar());
            binding.setUserEmail(identity.getUserEmail());
            binding.setCreateTime(identity.getCreateTime());
        }
        return binding;
    }
}
