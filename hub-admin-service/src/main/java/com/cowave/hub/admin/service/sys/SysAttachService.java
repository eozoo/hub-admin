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
package com.cowave.hub.admin.service.sys;

import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cowave.hub.admin.domain.sys.entity.SysAttach;
import com.cowave.hub.admin.domain.sys.entity.query.AttachQuery;
import com.cowave.hub.admin.domain.sys.entity.command.AttachUpload;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysAttachService {

    /**
     * 匿名获取图片流
     */
    void image(HttpServletResponse response, String md5) throws Exception;

    /**
     * 分页
     */
    Page<SysAttach> page(String tenantId, AttachQuery query);

    /**
     * 上传
     */
    SysAttach upload(MultipartFile file, AttachUpload attachUpload) throws Exception;

    /**
     * 下载
     */
    void download(HttpServletResponse response, Long attachId) throws Exception;

    /**
     * 预览
     */
    String preview(Long attachId) throws Exception;

    /**
     * 预览
     */
    String preview(SysAttach sysAttach) throws Exception;

    /**
     * 删除
     */
    void delete(List<Long> attachIds) throws Exception;
}
