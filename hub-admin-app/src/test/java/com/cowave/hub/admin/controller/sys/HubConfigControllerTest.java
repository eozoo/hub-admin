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
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
public class HubConfigControllerTest extends SpringTest {

    /**
     * 登录 -> 新增配置 -> 修改配置 -> 获取配置值 -> 详情 -> 列表 -> 导出
     *     -> 删除配置 -> 获取配置为空 -> 再次新增 -> 重置恢复 -> 获取配置为空 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/config
     * get /api/v1/config
     * patch /api/v1/config
     * get /api/v1/config/value/{configKey}
     * get /api/v1/config/{configId}
     * get /api/v1/config
     * post /api/v1/config/export
     * delete /api/v1/config/{configIds}
     * get /api/v1/config/value/{configKey}
     * post /api/v1/config
     * get /api/v1/config/reset
     * get /api/v1/config/value/{configKey}
     * delete /api/v1/auth/logout
     */
    @Test
    public void config() throws Exception {
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
        // 新增配置
        String configKey = "test.unit.config.key";
        body = """
                {
                    "configName": "测试配置",
                    "configKey": "%s",
                    "configValue": "test-value-1",
                    "valueType": "string"
                }
                """.formatted(configKey);
        mockPost("/api/v1/config", body, accessToken);
        // 查询列表获取configId
        mvcResult = mockGet("/api/v1/config?page=1&pageSize=100", accessToken);
        List<Map<String, Object>> configList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Integer configId = null;
        for (Map<String, Object> config : configList) {
            if (configKey.equals(config.get("configKey"))) {
                configId = (Integer) config.get("configId");
                break;
            }
        }
        // 修改配置
        body = """
                {
                    "configId": %d,
                    "configName": "测试配置-修改",
                    "configKey": "%s",
                    "configValue": "test-value-2",
                    "valueType": "string"
                }
                """.formatted(configId, configKey);
        mockPatch("/api/v1/config", body, accessToken);
        // 获取配置，验证修改生效
        mvcResult = mockGet("/api/v1/config/value/" + configKey, accessToken);
        Assertions.assertEquals("test-value-2", readString(mvcResult, "/data"));
        // 详情
        mockGet("/api/v1/config/" + configId, accessToken);
        // 列表
        mockGet("/api/v1/config", accessToken);
        // 导出
        body = """
                {
                "tenantId" : "cowave"
                }
                """;
        mockExport("/api/v1/config/export", body, "target/config.xlsx", accessToken);
        // 删除配置
        mockDelete("/api/v1/config/" + configId, accessToken);
        // 获取配置，应该为空
        mockGet("/api/v1/config/value/" + configKey, accessToken);
        // 再次新增配置
        body = """
                {
                    "configName": "测试配置2",
                    "configKey": "%s",
                    "configValue": "test-value-3",
                    "valueType": "string"
                }
                """.formatted(configKey);
        mockPost("/api/v1/config", body, accessToken);
        // 重置恢复
        mockGet("/api/v1/config/reset", accessToken);
        // 获取配置，应该为空
        mockGet("/api/v1/config/value/" + configKey, accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
