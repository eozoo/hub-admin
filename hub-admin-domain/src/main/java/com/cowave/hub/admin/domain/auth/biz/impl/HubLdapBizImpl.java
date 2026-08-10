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

import com.cowave.hub.admin.domain.auth.biz.HubLdapBiz;
import com.cowave.hub.admin.domain.auth.entity.HubLdap;
import com.cowave.hub.admin.domain.auth.entity.HubLdapUser;
import com.cowave.hub.admin.domain.auth.repository.HubLdapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class HubLdapBizImpl implements HubLdapBiz {

    private final HubLdapRepository ldapRepository;

    @Override
    public void editLdap(HubLdap hubLdap) {
        ldapRepository.removeById(hubLdap.getTenantId());
        ldapRepository.save(hubLdap);
    }

    @Override
    public void saveLdapUser(HubLdapUser ldapUser) {
        ldapRepository.saveLdapUser(ldapUser);
    }

    @Override
    public void updateLdapUserById(HubLdapUser ldapUser) {
        ldapRepository.updateLdapUserById(ldapUser);
    }
}
