package com.cowave.hub.admin.domain.auth.repository;

import com.cowave.hub.admin.domain.auth.entity.SysUserToken;
import com.cowave.hub.admin.domain.auth.entity.SysUserTokenMenu;
import com.cowave.hub.admin.domain.auth.repository.facade.SysUserTokenRepositoryFacade;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysUserTokenRepository extends SysUserTokenRepositoryFacade {

    /**
     * 创建令牌及权限
     */
    void create(SysUserToken token, List<SysUserTokenMenu> menus);

    /**
     * 保存签发的令牌值
     */
    void updateValue(SysUserToken token);

    /**
     * 删除指定用户的令牌及权限
     */
    void delete(Integer tenantId, Integer userId, Integer tokenId);
}
