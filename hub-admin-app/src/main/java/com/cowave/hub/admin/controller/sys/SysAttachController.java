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
package com.cowave.hub.admin.controller.sys;

import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.Access;
import com.cowave.hub.admin.domain.sys.entity.SysAttach;
import com.cowave.hub.admin.domain.sys.entity.query.AttachQuery;
import com.cowave.hub.admin.domain.sys.entity.command.AttachUpload;
import com.cowave.hub.admin.service.sys.SysAttachService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 附件
 * @order 8
 * @author shanhuiming
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/attach")
public class SysAttachController {

    private final SysAttachService attachService;

    /**
     * 上传
     */
    @PostMapping
    public Response<SysAttach> upload(MultipartFile file, AttachUpload attachUpload) throws Exception {
        return Response.success(attachService.upload(file, attachUpload));
    }

    /**
     * 下载
     *
     * @param attachId 附件id
     */
    @PreAuthorize("@permits.hasPermit('hub:attach:download')")
    @GetMapping("/{attachId}")
    public void download(HttpServletResponse response, @PathVariable Long attachId) throws Exception {
        attachService.download(response, attachId);
    }

    /**
     * 预览
     *
     * @param attachId 附件id
     */
    @PreAuthorize("@permits.hasPermit('hub:attach:preview')")
    @GetMapping("/preview/{attachId}")
    public Response<String> preview(@PathVariable Long attachId) throws Exception {
        return Response.success(attachService.preview(attachId));
    }

    /**
     * 列表
     */
    @PreAuthorize("@permits.hasPermit('hub:attach:query')")
    @GetMapping
    public Response<Response.Page<SysAttach>> page(AttachQuery query) throws Exception {
        return Response.page(attachService.page(Access.tenantId(), query));
    }

    /**
     * 删除
     *
     * @param attachIds 文件id列表
     */
    @PreAuthorize("@permits.hasPermit('hub:attach:delete')")
    @DeleteMapping("/{attachIds}")
    public Response<Void> delete(@PathVariable List<Long> attachIds) throws Exception {
        attachService.delete(attachIds);
        return Response.success();
    }
}
