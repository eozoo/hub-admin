package com.cowave.hub.admin.domain.auth.entity.vo;

import com.cowave.hub.admin.domain.auth.entity.SysUserToken;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class ApiTokenVo extends SysUserToken {

    /**
     * 最近访问 IP
     */
    private String accessIp;

    /**
     * 最近访问路径
     */
    private String accessUrl;

    /**
     * 最近访问时间
     */
    private Date accessTime;

    /**
     * 授权权限符
     */
    private List<String> permits;

}
