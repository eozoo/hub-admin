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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class SysAttachStoreImpl implements SysAttachStore {
    private final MinioHelper minioHelper;

    @Override
    public void upload(MultipartFile multipartFile, SysAttach attach) throws Exception {
        minioHelper.upload(multipartFile, attach.getTenantId(), attach.getAttachPath(), true);
    }

    @Override
    public void download(HttpServletResponse response, SysAttach attach) throws Exception {
        minioHelper.download(response, attach.getTenantId(), attach.getAttachPath(), attach.getAttachName());
    }

    @Override
    public void remove(SysAttach attach) throws Exception {
        minioHelper.delete(attach.getTenantId(), attach.getAttachPath());
    }

    @Override
    public String preview(SysAttach attach) throws Exception {
        return minioHelper.preview(attach.getTenantId(), attach.getAttachPath());
    }

    @Override
    public void previewStream(HttpServletResponse response, SysAttach attach) throws Exception {
        response.setContentType(contentType(attach.getAttachName()));
        response.setHeader("Content-Disposition", "inline");
        try (InputStream in = minioHelper.getInputStream(attach.getTenantId(), attach.getAttachPath());
                OutputStream out = response.getOutputStream()) {
            StreamUtils.copy(in, out);
        }
    }

    private String contentType(String name) {
        if (name == null) {
            return "application/octet-stream";
        }
        String lower = name.toLowerCase();
        if (lower.endsWith(".png")) {
            return "image/png";
        } else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lower.endsWith(".gif")) {
            return "image/gif";
        } else if (lower.endsWith(".webp")) {
            return "image/webp";
        } else if (lower.endsWith(".svg")) {
            return "image/svg+xml";
        } else if (lower.endsWith(".bmp")) {
            return "image/bmp";
        }
        return "application/octet-stream";
    }
}
