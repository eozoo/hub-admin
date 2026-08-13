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
package com.cowave.hub.admin.infra.sys.store;

import com.cowave.hub.admin.domain.sys.entity.SysAttach;
import com.cowave.hub.admin.domain.sys.store.SysAttachStore;
import com.cowave.zoo.framework.helper.minio.MinioHelper;
import com.cowave.zoo.framework.helper.minio.MinioProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

import static com.cowave.hub.admin.domain.rbac.enums.YesNo.YES;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class SysAttachStoreImpl implements SysAttachStore {
    private final MinioHelper minioHelper;
    private final MinioProperties minioProperties;

    @Override
    public void upload(MultipartFile multipartFile, SysAttach attach) throws Exception {
        minioHelper.upload(multipartFile, attach.getTenantId(), attach.getAttachPath(), YES == attach.getIsPrivate());
    }

    @Override
    public void download(HttpServletResponse response, SysAttach attach) throws Exception {
        minioHelper.download(response, attach.getTenantId(), attach.getAttachPath(), attach.getAttachName());
    }

    @Override
    public String preview(SysAttach attach) throws Exception {
        if (YES == attach.getIsPrivate()) {
            return minioHelper.preview(attach.getTenantId(), attach.getAttachPath());
        } else {
            return minioProperties.getEndpoint() + File.separator + attach.getTenantId()
                    + File.separator + attach.getAttachPath();
        }
    }

    @Override
    public void remove(SysAttach attach) throws Exception {
        minioHelper.delete(attach.getTenantId(), attach.getAttachPath());
    }
}
