package com.cowave.hub.admin.domain.auth.repository.facade;

import com.cowave.hub.admin.domain.auth.entity.SysUserToken;
import com.cowave.hub.admin.domain.auth.entity.SysUserTokenMenu;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysUserTokenRepositoryFacade {

    /**
     * 查询当前用户在租户下的令牌
     */
    List<SysUserToken> queryByUser(Integer tenantId, Integer userId);

    /**
     * 查询令牌权限
     */
    List<SysUserTokenMenu> queryMenus(Integer tenantId, Integer tokenId);

    /**
     * 查询用户拥有的令牌
     */
    SysUserToken queryOwned(Integer tenantId, Integer userId, Integer tokenId);
}
