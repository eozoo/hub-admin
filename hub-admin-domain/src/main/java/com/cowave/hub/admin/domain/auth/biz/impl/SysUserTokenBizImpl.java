package com.cowave.hub.admin.domain.auth.biz.impl;

import com.cowave.hub.admin.domain.auth.biz.SysUserTokenBiz;
import com.cowave.hub.admin.domain.auth.entity.SysUserToken;
import com.cowave.hub.admin.domain.auth.entity.SysUserTokenMenu;
import com.cowave.hub.admin.domain.auth.repository.SysUserTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
public class SysUserTokenBizImpl implements SysUserTokenBiz {
    private final SysUserTokenRepository repository;

    @Override
    public void create(SysUserToken token, List<SysUserTokenMenu> menus) {
        repository.create(token, menus);
    }

    @Override
    public void updateValue(SysUserToken token) {
        repository.updateValue(token);
    }

    @Override
    public void delete(Integer tenantId, Integer userId, Integer tokenId) {
        repository.delete(tenantId, userId, tokenId);
    }
}
