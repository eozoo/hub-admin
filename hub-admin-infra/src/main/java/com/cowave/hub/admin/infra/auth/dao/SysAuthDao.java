/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package com.cowave.hub.admin.infra.auth.dao;

import com.cowave.zoo.http.client.asserts.HttpAsserts;
import static com.cowave.zoo.http.client.constants.HttpCode.BAD_REQUEST;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cowave.hub.admin.domain.auth.entity.SysAuthProvider;
import com.cowave.hub.admin.domain.auth.entity.SysAuthPasswd;
import com.cowave.hub.admin.domain.auth.entity.SysAuthIdentity;
import com.cowave.hub.admin.domain.auth.entity.SysAuthLdap;
import com.cowave.hub.admin.domain.auth.enums.PasswdAlgo;
import com.cowave.hub.admin.domain.auth.enums.PasswdSource;
import com.cowave.hub.admin.domain.auth.enums.ProviderCode;
import com.cowave.hub.admin.domain.auth.enums.ProviderType;
import com.cowave.hub.admin.domain.rbac2.enums.EnableStatus;
import com.cowave.zoo.framework.access.Access;
import org.apache.commons.lang3.StringUtils;
import com.cowave.hub.admin.domain.auth.repository.SysAuthRepository;
import com.cowave.hub.admin.infra.auth.mapper.SysAuthPasswdMapper;
import com.cowave.hub.admin.infra.auth.mapper.SysAuthIdentityMapper;
import com.cowave.hub.admin.infra.auth.mapper.SysAuthLdapMapper;
import com.cowave.hub.admin.infra.auth.mapper.SysAuthProviderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
@Repository
@RequiredArgsConstructor
public class SysAuthDao extends ServiceImpl<SysAuthProviderMapper, SysAuthProvider> implements SysAuthRepository {
    private final SysAuthPasswdMapper authPasswdMapper;
    private final SysAuthIdentityMapper authIdentityMapper;
    private final SysAuthLdapMapper authLdapMapper;

    @Override
    public SysAuthLdap queryLdap() {
        List<SysAuthLdap> configs = authLdapMapper.selectList(new LambdaQueryWrapper<SysAuthLdap>()
                .orderByAsc(SysAuthLdap::getLdapId).last("LIMIT 1"));
        return configs.isEmpty() ? null : configs.get(0);
    }

    @Override
    public Page<SysAuthIdentity> queryLdapUsers(String userAccount) {
        SysAuthLdap config = queryLdap();
        Page<SysAuthIdentity> page = Access.page();
        if (config == null) {
            return page;
        }
        return authIdentityMapper.selectPage(page, new LambdaQueryWrapper<SysAuthIdentity>()
                .eq(SysAuthIdentity::getIdentityType, ProviderType.LDAP)
                .eq(SysAuthIdentity::getLdapId, config.getLdapId())
                .like(StringUtils.isNotBlank(userAccount), SysAuthIdentity::getUserAccount, userAccount));
    }

    @Override
    public void saveLdap(SysAuthLdap config) {
        if (config.getLdapId() == null) {
            authLdapMapper.insert(config);
        } else {
            authLdapMapper.updateById(config);
        }
    }

    @Override
    public SysAuthLdap queryEnabledLdap() {
        SysAuthLdap config = queryLdap();
        return config != null && config.getLdapStatus() == EnableStatus.ENABLE ? config : null;
    }

    @Override
    public SysAuthIdentity queryLdapIdentity(Integer ldapId, String externalSubject) {
        return authIdentityMapper.selectOne(new LambdaQueryWrapper<SysAuthIdentity>()
                .eq(SysAuthIdentity::getLdapId, ldapId)
                .eq(SysAuthIdentity::getExternalSubject, externalSubject));
    }

    @Override
    public SysAuthProvider queryOauthProviderByCode(ProviderCode providerCode) {
        return lambdaQuery()
                .eq(SysAuthProvider::getProviderCode, providerCode)
                .eq(SysAuthProvider::getProviderType, ProviderType.OAUTH)
                .one();
    }

    @Override
    public SysAuthIdentity queryUserLdapIdentity(Integer ldapId, Integer userId) {
        return authIdentityMapper.selectOne(new LambdaQueryWrapper<SysAuthIdentity>()
                .eq(SysAuthIdentity::getIdentityType, ProviderType.LDAP)
                .eq(SysAuthIdentity::getLdapId, ldapId)
                .eq(SysAuthIdentity::getUserId, userId)
                .orderByDesc(SysAuthIdentity::getLastSyncTime)
                .orderByDesc(SysAuthIdentity::getId)
                .last("limit 1"));
    }

    @Override
    public SysAuthProvider queryOauthProviderById(Integer providerId) {
        return lambdaQuery()
                .eq(SysAuthProvider::getProviderId, providerId)
                .eq(SysAuthProvider::getProviderType, ProviderType.OAUTH)
                .one();
    }

    @Override
    public Page<SysAuthIdentity> queryProviderUsers(Integer providerId, String userAccount) {
        return authIdentityMapper.selectPage(Access.page(), new LambdaQueryWrapper<SysAuthIdentity>()
                .eq(SysAuthIdentity::getIdentityType, ProviderType.OAUTH)
                .eq(SysAuthIdentity::getProviderId, providerId)
                .like(StringUtils.isNotBlank(userAccount), SysAuthIdentity::getUserAccount, userAccount));
    }

    @Override
    public void saveOauthProvider(SysAuthProvider provider) {
        if (provider.getProviderId() == null) {
            save(provider);
            return;
        }
        boolean updated = lambdaUpdate()
                .eq(SysAuthProvider::getProviderId, provider.getProviderId())
                .set(SysAuthProvider::getClientId, provider.getClientId())
                .set(SysAuthProvider::getClientSecret, provider.getClientSecret())
                .set(SysAuthProvider::getAuthUrl, provider.getAuthUrl())
                .set(SysAuthProvider::getRedirectUrl, provider.getRedirectUrl())
                .set(SysAuthProvider::getAuthScope, provider.getAuthScope())
                .set(SysAuthProvider::getStatus, provider.getStatus())
                .set(SysAuthProvider::getUpdateBy, provider.getUpdateBy())
                .set(SysAuthProvider::getUpdateTime, provider.getUpdateTime())
                .update();
        if (!updated) {
            throw new IllegalStateException("OAuth provider not found: " + provider.getProviderId());
        }
    }

    @Override
    public SysAuthIdentity queryProviderIdentity(Integer providerId, String externalSubject) {
        return authIdentityMapper.selectOne(new LambdaQueryWrapper<SysAuthIdentity>()
                .eq(SysAuthIdentity::getProviderId, providerId)
                .eq(SysAuthIdentity::getExternalSubject, externalSubject));
    }

    @Override
    public void createExternalIdentity(SysAuthIdentity identity) {
        authIdentityMapper.insert(identity);
    }

    @Override
    public SysAuthIdentity queryIdentityById(Long identityId) {
        return authIdentityMapper.selectById(identityId);
    }

    @Override
    public void updateLdapIdentity(SysAuthIdentity identity) {
        authIdentityMapper.updateById(identity);
    }

    @Override
    public void updateProviderIdentity(SysAuthIdentity identity) {
        authIdentityMapper.updateById(identity);
    }

    @Override
    public void updateProviderIdentityStatus(Integer providerId, Long identityId, EnableStatus status) {
        int updated = authIdentityMapper.update(null, new LambdaUpdateWrapper<SysAuthIdentity>()
                .eq(SysAuthIdentity::getId, identityId)
                .eq(SysAuthIdentity::getProviderId, providerId)
                .eq(SysAuthIdentity::getIdentityType, ProviderType.OAUTH)
                .set(SysAuthIdentity::getAuthStatus, status)
                .set(SysAuthIdentity::getUpdateTime, new Date()));
        HttpAsserts.isTrue(updated == 1, BAD_REQUEST, "{admin.auth.identity.not.exist}");
    }

    @Override
    public void deleteProviderIdentity(Integer providerId, Long identityId) {
        int deleted = authIdentityMapper.delete(new LambdaQueryWrapper<SysAuthIdentity>()
                .eq(SysAuthIdentity::getId, identityId)
                .eq(SysAuthIdentity::getProviderId, providerId)
                .eq(SysAuthIdentity::getIdentityType, ProviderType.OAUTH));
        HttpAsserts.isTrue(deleted == 1, BAD_REQUEST, "{admin.auth.identity.not.exist}");
    }

    @Override
    public void updateLdapIdentityStatus(Integer ldapId, Long identityId, EnableStatus status) {
        int updated = authIdentityMapper.update(null, new LambdaUpdateWrapper<SysAuthIdentity>()
                .eq(SysAuthIdentity::getId, identityId)
                .eq(SysAuthIdentity::getLdapId, ldapId)
                .eq(SysAuthIdentity::getIdentityType, ProviderType.LDAP)
                .set(SysAuthIdentity::getAuthStatus, status)
                .set(SysAuthIdentity::getUpdateTime, new Date()));
        HttpAsserts.isTrue(updated == 1, BAD_REQUEST, "{admin.auth.identity.not.exist}");
    }

    @Override
    public void deleteLdapIdentity(Integer ldapId, Long identityId) {
        int deleted = authIdentityMapper.delete(new LambdaQueryWrapper<SysAuthIdentity>()
                .eq(SysAuthIdentity::getId, identityId)
                .eq(SysAuthIdentity::getLdapId, ldapId)
                .eq(SysAuthIdentity::getIdentityType, ProviderType.LDAP));
        HttpAsserts.isTrue(deleted == 1, BAD_REQUEST, "{admin.auth.identity.not.exist}");
    }

    @Override
    public void createPasswd(SysAuthPasswd passwd) {
        authPasswdMapper.insert(passwd);
    }

    @Override
    public List<SysAuthProvider> queryEnabledOauthProviders() {
        return lambdaQuery()
                .eq(SysAuthProvider::getProviderType, ProviderType.OAUTH)
                .eq(SysAuthProvider::getStatus, EnableStatus.ENABLE)
                .orderByAsc(SysAuthProvider::getProviderSort)
                .list();
    }

    @Override
    public List<SysAuthProvider> queryOauthProviders() {
        return lambdaQuery().eq(SysAuthProvider::getProviderType, ProviderType.OAUTH)
                .orderByAsc(SysAuthProvider::getProviderSort).list();
    }

    @Override
    public List<SysAuthIdentity> queryUserProviderIdentities(Integer userId) {
        return authIdentityMapper.selectList(new LambdaQueryWrapper<SysAuthIdentity>()
                .eq(SysAuthIdentity::getUserId, userId)
                .isNotNull(SysAuthIdentity::getProviderId)
                .orderByDesc(SysAuthIdentity::getLastSyncTime)
                .orderByDesc(SysAuthIdentity::getId));
    }

    @Override
    public SysAuthPasswd queryCurrentPasswd(Integer userId) {
        Date now = new Date();
        return authPasswdMapper.selectOne(new LambdaQueryWrapper<SysAuthPasswd>()
                .eq(SysAuthPasswd::getUserId, userId)
                .eq(SysAuthPasswd::getIsCurrent, 1)
                .le(SysAuthPasswd::getEffectiveTime, now)
                .and(wrapper -> wrapper.isNull(SysAuthPasswd::getExpireTime)
                        .or().gt(SysAuthPasswd::getExpireTime, now))
                .isNull(SysAuthPasswd::getInvalidTime));
    }

    @Transactional
    @Override
    public void changePasswd(Integer userId, String encodedPasswd, String userAccount) {
        Date now = new Date();
        int updated = authPasswdMapper.update(null, new LambdaUpdateWrapper<SysAuthPasswd>()
                .eq(SysAuthPasswd::getUserId, userId)
                .eq(SysAuthPasswd::getIsCurrent, 1)
                .set(SysAuthPasswd::getIsCurrent, 0)
                .set(SysAuthPasswd::getInvalidTime, now)
                .set(SysAuthPasswd::getUpdateBy, userAccount)
                .set(SysAuthPasswd::getUpdateTime, now));
        if (updated != 1) {
            throw new IllegalStateException("Current password record not found for user: " + userId);
        }

        SysAuthPasswd password = new SysAuthPasswd();
        password.setUserId(userId);
        password.setPasswdHash(encodedPasswd);
        password.setPasswdAlgo(PasswdAlgo.BCRYPT);
        password.setIsCurrent(1);
        password.setNeedChange(0);
        password.setEffectiveTime(now);
        password.setChangeSource(PasswdSource.SELF);
        password.setCreateBy(userAccount);
        password.setCreateTime(now);
        authPasswdMapper.insert(password);
    }
}
