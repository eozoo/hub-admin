package com.cowave.hub.admin.domain.auth.biz;

import com.cowave.hub.admin.domain.auth.entity.SysUserToken;
import com.cowave.hub.admin.domain.auth.entity.SysUserTokenMenu;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysUserTokenBiz {

    /**
     * 创建令牌及权限
     */
    void create(SysUserToken token, List<SysUserTokenMenu> menus);

    /**
     * 更新签名后的令牌值
     */
    void updateValue(SysUserToken token);

    /**
     * 删除令牌及权限
     */
    void delete(Integer tenantId, Integer userId, Integer tokenId);
}
