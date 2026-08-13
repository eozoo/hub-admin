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
package com.cowave.hub.admin.domain.auth.biz.impl;

import com.cowave.hub.admin.domain.auth.biz.SysLdapBiz;
import com.cowave.hub.admin.domain.auth.entity.SysLdap;
import com.cowave.hub.admin.domain.auth.entity.SysLdapUser;
import com.cowave.hub.admin.domain.auth.repository.SysLdapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysLdapBizImpl implements SysLdapBiz {

    private final SysLdapRepository ldapRepository;

    @Override
    public void editLdap(SysLdap sysLdap) {
        ldapRepository.removeById(sysLdap.getTenantId());
        ldapRepository.save(sysLdap);
    }

    @Override
    public void saveLdapUser(SysLdapUser ldapUser) {
        ldapRepository.saveLdapUser(ldapUser);
    }

    @Override
    public void updateLdapUserById(SysLdapUser ldapUser) {
        ldapRepository.updateLdapUserById(ldapUser);
    }
}
