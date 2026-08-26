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
package com.cowave.hub.admin.domain.sys.biz;

import com.cowave.hub.admin.domain.sys.entity.SysAttach;
import com.cowave.hub.admin.domain.sys.enums.AttachType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysAttachBiz {

    /**
     * 上传附件
     */
    void uploadAttach(MultipartFile file, SysAttach attach) throws Exception;

    /**
     * 下载附件
     */
    void downloadAttach(HttpServletResponse response, SysAttach attach) throws Exception;

    /**
     * 预览附件地址
     */
    String previewAttach(SysAttach sysAttach) throws Exception;

    /**
     * 预览附件流
     */
    void previewStream(HttpServletResponse response, SysAttach attach) throws Exception;

    /**
     * 删除附件
     */
    void removeAttach(SysAttach attach) throws Exception;

    /**
     * 删除附件
     */
    void removeAttachByIds(List<Long> attachIds) throws Exception;

    /**
     * 更新附件宿主
     */
    void updateOwner(String ownerId, Long attachId);

    /**
     * 清除附件宿主
     */
    void clearOwner(String ownerId, String ownerModule, AttachType attachType, Long attachId);

    /**
     * 保留最近N个附件，删除其余
     */
    void reserveByOwner(String ownerId, String ownerModule, AttachType attachType, int reserve) throws Exception;

    /**
     * 宿主最新附件
     */
    SysAttach previewLatestByOwner(String ownerId, String ownerModule, AttachType attachType) throws Exception;
}
