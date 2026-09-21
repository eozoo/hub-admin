package com.cowave.hub.admin.infra.sys2.store;

import com.cowave.hub.admin.domain.sys2.entity.SysAttach;
import com.cowave.hub.admin.domain.sys2.store.SysAttachStore;
import com.cowave.zoo.framework.helper.minio.MinioHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class SysAttachStore2Impl implements SysAttachStore {
    private final MinioHelper minioHelper;

    @Override
    public String preview(SysAttach attach) throws Exception {
        String bucket = attach.getBucketName() != null
                ? attach.getBucketName() : String.valueOf(attach.getTenantId());
        return minioHelper.preview(bucket, attach.getAttachPath());
    }
}
