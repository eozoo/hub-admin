package com.cowave.hub.admin.domain.auth.entity.command;

import com.cowave.hub.admin.domain.rbac2.entity.pto.PermitScopePto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class ApiTokenCreate {

    /**
     * 令牌名称
     */
    @NotBlank(message = "{admin.token.name.null}")
    private String tokenName;

    /**
     * 到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

    /**
     * IP 访问限制
     */
    private String ipRule;

    /**
     * 操作权限与数据范围
     */
    private List<PermitScopePto> menuScopes = new ArrayList<>();
}
