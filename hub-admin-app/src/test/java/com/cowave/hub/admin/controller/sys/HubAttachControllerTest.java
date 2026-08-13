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

import com.cowave.hub.admin.SpringTest;
import com.cowave.hub.admin.domain.sys.entity.SysAttach;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.util.List;

import static com.cowave.hub.admin.domain.sys.enums.AttachType.AVATAR;
import static com.cowave.hub.admin.domain.sys.enums.OpModule.SYSTEM_USER;

/**
 * @author shanhuiming
 */
public class HubAttachControllerTest extends SpringTest {

    /**
     * 登录 -> 上传 -> 列表 -> 预览 -> 下载 -> 删除 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/attach
     * get /api/v1/attach
     * get /api/v1/attach/preview/{attachId}
     * get /api/v1/attach/{attachId}
     * delete /api/v1/attach/{attachIds}
     * delete /api/v1/auth/logout
     */
    @Test
    public void attach() throws Exception {
        // 登录
        String body = """
                {
                "tenantId" : "cowave",
                "userAccount" : "liubei",
                "passWord" : "12345678"
                }
                """;
        MvcResult mvcResult = mockPost("/api/v1/auth/public/logon", body);
        String accessToken = "Bearer " + readString(mvcResult, "/data/accessToken");
        // 上传
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("ownerId", "2");
        params.set("ownerModule", SYSTEM_USER);
        params.set("attachType", AVATAR.getVal());
        mockImport("/api/v1/attach", params, "source/cw.jpg", accessToken);
        // 列表
        mvcResult = mockGet("/api/v1/attach?ownerId=2&ownerModule=" + SYSTEM_USER + "&attachType=" + AVATAR.getVal(), accessToken);
        List<SysAttach> attachList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Long attachId = attachList.get(0).getAttachId();
        // 预览
        mockGet("/api/v1/attach/preview/" + attachId, accessToken);
        // 下载
        mvcResult = mockGet("/api/v1/attach/" + attachId, accessToken);
        try (FileOutputStream out = new FileOutputStream("target/cw.jpg");
             ByteArrayInputStream in = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray())) {
            StreamUtils.copy(in, out);
        }
        // 删除
        mockDelete("/api/v1/attach/" + attachId, accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
