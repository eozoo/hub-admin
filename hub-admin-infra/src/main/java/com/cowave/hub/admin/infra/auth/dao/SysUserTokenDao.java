package com.cowave.hub.admin.infra.auth.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cowave.hub.admin.domain.auth.entity.SysUserToken;
import com.cowave.hub.admin.domain.auth.entity.SysUserTokenMenu;
import com.cowave.hub.admin.domain.auth.repository.SysUserTokenRepository;
import com.cowave.hub.admin.infra.auth.mapper.SysUserTokenMapper;
import com.cowave.hub.admin.infra.auth.mapper.SysUserTokenMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
@RequiredArgsConstructor
public class SysUserTokenDao implements SysUserTokenRepository {
    private final SysUserTokenMapper userTokenMapper;
    private final SysUserTokenMenuMapper tokenMenuMapper;

    @Override
    public List<SysUserToken> queryByUser(Integer tenantId, Integer userId) {
        return userTokenMapper.selectList(new LambdaQueryWrapper<SysUserToken>()
                .eq(SysUserToken::getTenantId, tenantId)
                .eq(SysUserToken::getUserId, userId)
                .orderByDesc(SysUserToken::getCreateTime));
    }

    @Override
    public List<SysUserTokenMenu> queryMenus(Integer tenantId, Integer tokenId) {
        return tokenMenuMapper.selectList(new LambdaQueryWrapper<SysUserTokenMenu>()
                .eq(SysUserTokenMenu::getTenantId, tenantId)
                .eq(SysUserTokenMenu::getTokenId, tokenId));
    }

    @Override
    public SysUserToken queryOwned(Integer tenantId, Integer userId, Integer tokenId) {
        return userTokenMapper.selectOne(new LambdaQueryWrapper<SysUserToken>()
                .eq(SysUserToken::getTenantId, tenantId)
                .eq(SysUserToken::getUserId, userId)
                .eq(SysUserToken::getTokenId, tokenId));
    }

    @Override
    public void create(SysUserToken token, List<SysUserTokenMenu> menus) {
        userTokenMapper.insert(token);
        if (!menus.isEmpty()) {
            tokenMenuMapper.insertBatch(token.getTokenId(), menus);
        }
    }

    @Override
    public void updateValue(SysUserToken token) {
        userTokenMapper.updateById(token);
    }

    @Override
    public void delete(Integer tenantId, Integer userId, Integer tokenId) {
        tokenMenuMapper.delete(new LambdaUpdateWrapper<SysUserTokenMenu>()
                .eq(SysUserTokenMenu::getTenantId, tenantId)
                .eq(SysUserTokenMenu::getTokenId, tokenId));
        userTokenMapper.delete(new LambdaUpdateWrapper<SysUserToken>()
                .eq(SysUserToken::getTenantId, tenantId)
                .eq(SysUserToken::getUserId, userId)
                .eq(SysUserToken::getTokenId, tokenId));
    }
}
