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
package com.cowave.hub.admin.domain.sys2.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 通用附件，支持租户、用户、通知、反馈和流程等业务宿主
 *
 * @author shanhuiming
 */
@Getter
@Setter
public class SysAttach {

    /**
     * 附件id
     */
    @TableId(type = IdType.AUTO)
    private Long attachId;

    /**
     * 租户id
     */
    private Integer tenantId;

    /**
     * 宿主模块，如tenant、user、notice、feedback、flow
     */
    private String ownerModule;

    /**
     * 宿主业务id，允许上传后再绑定
     */
    private String ownerId;

    /**
     * 附件业务类型，如logo、avatar、image、document
     */
    private String attachType;

    /**
     * 附件名称
     */
    private String attachName;

    /**
     * 文件扩展名
     */
    private String fileType;

    /**
     * 文件MIME类型
     */
    private String contentType;

    /**
     * 附件大小，单位字节
     */
    private Long attachSize;

    /**
     * 附件存储路径或对象键
     */
    private String attachPath;

    /**
     * 存储类型，如local、minio
     */
    private String storageType;

    /**
     * 对象存储桶名称
     */
    private String bucketName;

    /**
     * 文件内容MD5
     */
    private String md5;

    /**
     * 是否私有 0否 1是
     */
    private Integer isPrivate;

    /**
     * 同一宿主下排序
     */
    private Integer sort;

    /**
     * 是否删除 0否 1是
     */
    private Integer isDeleted;

    /**
     * 过期时间，临时上传文件可用于清理
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
