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
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmTypePto;
import com.cowave.hub.admin.domain.sys.entity.pto.DictPto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
public class HubAlarmControllerTest extends SpringTest {

    /**
     * 登录 -> 新增告警类型 -> 类型列表验证新增 -> 修改类型 -> 类型列表验证修改 -> 删除类型 -> 类型列表验证删除 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/alarm/type/add
     * post /api/v1/alarm/type/list
     * post /api/v1/alarm/type/edit
     * post /api/v1/alarm/type/list
     * get /api/v1/alarm/type/delete
     * post /api/v1/alarm/type/list
     * delete /api/v1/auth/logout
     */
    @Test
    public void alarmTypes() throws Exception {
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
        // 新增告警类型
        String typeName = "test-alarm-type";
        body = """
                {
                    "typeName": "%s",
                    "typeView": "/alarm/test/view",
                    "description": "test alarm type desc"
                }
                """.formatted(typeName);
        mockPost("/api/v1/alarm/type/add", body, accessToken);
        // 类型列表，验证新增
        body = """
                {
                    "page": 1,
                    "pageSize": 100,
                    "typeName": "%s"
                }
                """.formatted(typeName);
        mvcResult = mockPost("/api/v1/alarm/type/list", body, accessToken);
        List<AlarmTypePto> typeList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, typeList.size());
        AlarmTypePto type = typeList.get(0);
        Long typeId = type.getId();
        Assertions.assertEquals(typeName, type.getTypeName());
        Assertions.assertEquals("/alarm/test/view", type.getTypeView());
        Assertions.assertEquals("test alarm type desc", type.getDescription());
        // 修改告警类型
        String updatedName = "test-alarm-type-updated";
        body = """
                {
                    "id": %s,
                    "typeName": "%s",
                    "typeView": "/alarm/test/view/updated",
                    "description": "test alarm type desc updated"
                }
                """.formatted(typeId, updatedName);
        mockPost("/api/v1/alarm/type/edit", body, accessToken);
        // 类型列表，验证修改
        body = """
                {
                    "page": 1,
                    "pageSize": 100,
                    "typeName": "%s"
                }
                """.formatted(updatedName);
        mvcResult = mockPost("/api/v1/alarm/type/list", body, accessToken);
        typeList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, typeList.size());
        type = typeList.get(0);
        Assertions.assertEquals(updatedName, type.getTypeName());
        Assertions.assertEquals("/alarm/test/view/updated", type.getTypeView());
        Assertions.assertEquals("test alarm type desc updated", type.getDescription());
        // 删除告警类型
        mockGet("/api/v1/alarm/type/delete?id=" + typeId, accessToken);
        // 类型列表，验证删除
        body = """
                {
                    "page": 1,
                    "pageSize": 100,
                    "typeName": "%s"
                }
                """.formatted(updatedName);
        mvcResult = mockPost("/api/v1/alarm/type/list", body, accessToken);
        typeList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(0, typeList.size());
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }

    /**
     * 登录 -> 触发DuplicateKeyException(5xx) -> 告警列表 -> 告警详情 -> 告警处理
     *     -> 告警详情验证处理结果 -> 告警导出 -> 告警删除 -> 告警列表验证删除 -> 清理 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/dict (成功)
     * post /api/v1/dict (500, AdminExceptionHandler生成告警)
     * post /api/v1/alarm/list
     * get /api/v1/alarm/info
     * post /api/v1/alarm/handle
     * get /api/v1/alarm/info
     * post /api/v1/alarm/export
     * get /api/v1/alarm/delete
     * post /api/v1/alarm/list
     * get /api/v1/dict
     * delete /api/v1/dict/{dictId} (清理)
     * delete /api/v1/auth/logout
     */
    @Test
    public void alarmRecords() throws Exception {
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
        // 借助字典接口触发5xx: 先创建一条字典，再用相同dictCode创建第二条触发DuplicateKeyException(500)
        // AccessAdvice返回500 → AdminExceptionHandler自动记录告警
        String dictCode = "test_alarm_trigger";
        body = """
                {
                    "typeCode": "op_action",
                    "dictCode": "%s",
                    "dictName": "alarm trigger dict",
                    "dictValue": "0",
                    "valueType": "int",
                    "valueParser": "com.cowave.zoo.framework.helper.redis.dict.DefaultValueParser",
                    "dictOrder": 99,
                    "status": 1
                }
                """.formatted(dictCode);
        // 第一次创建成功
        mockPost("/api/v1/dict", body, accessToken);
        // 第二次创建，重复dictCode → DuplicateKeyException → 500
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/dict")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
                .content(body))
                .andExpect(MockMvcResultMatchers.status().is(500))
                .andReturn();
        // 告警列表，找到AdminExceptionHandler生成的告警
        body = """
                {
                    "page": 1,
                    "pageSize": 100,
                    "beginTime": "2020-01-01 00:00:00",
                    "endTime": "2099-12-31 23:59:59"
                }
                """;
        mvcResult = mockPost("/api/v1/alarm/list", body, accessToken);
        List<Map<String, Object>> alarmList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertTrue(alarmList.size() >= 1, "AdminExceptionHandler应生成至少1条告警");
        // 通过alarmDesc找到由dict接口触发的告警
        Long alarmId = null;
        for (Map<String, Object> alarm : alarmList) {
            if ("/api/v1/dict".equals(alarm.get("alarmDesc"))) {
                alarmId = ((Number) alarm.get("id")).longValue();
                break;
            }
        }
        Assertions.assertNotNull(alarmId, "应能找到由dict DuplicateKeyException触发的告警");
        // 告警详情，验证初始状态（0-未处理，3-重要）
        mvcResult = mockGet("/api/v1/alarm/info?id=" + alarmId, accessToken);
        Map<String, Object> alarmDetail = readData(mvcResult, "/data", new TypeReference<Map<String, Object>>() {});
        Assertions.assertNotNull(alarmDetail);
        Assertions.assertEquals(0, alarmDetail.get("alarmStatus"), "初始告警状态应为0(未处理)");
        Assertions.assertEquals(3, alarmDetail.get("alarmLevel"), "告警等级应为3(重要)");
        // 告警处理：标记为已解决
        body = """
                {
                    "id": %d,
                    "alarmStatus": 2,
                    "handleType": 1,
                    "handleMsg": "test handle: resolved"
                }
                """.formatted(alarmId);
        mockPost("/api/v1/alarm/handle", body, accessToken);
        // 告警详情，验证处理结果
        mvcResult = mockGet("/api/v1/alarm/info?id=" + alarmId, accessToken);
        alarmDetail = readData(mvcResult, "/data", new TypeReference<Map<String, Object>>() {});
        Assertions.assertEquals(2, alarmDetail.get("alarmStatus"), "处理后告警状态应为2(已解决)");
        Assertions.assertEquals("test handle: resolved", alarmDetail.get("resolveMsg"));
        Assertions.assertNotNull(alarmDetail.get("resolveTime"), "处理时间应不为空");
        // 告警导出
        mockExport("/api/v1/alarm/export", "beginTime=2020-01-01+00:00:00&endTime=2099-12-31+23:59:59", "target/alarm.xlsx", accessToken);
        // 告警删除
        mockGet("/api/v1/alarm/delete?id=" + alarmId, accessToken);
        // 告警列表，验证删除
        body = """
                {
                    "page": 1,
                    "pageSize": 100,
                    "beginTime": "2020-01-01 00:00:00",
                    "endTime": "2099-12-31 23:59:59"
                }
                """;
        mvcResult = mockPost("/api/v1/alarm/list", body, accessToken);
        alarmList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        final Long deletedAlarmId = alarmId;
        boolean found = alarmList.stream().anyMatch(a -> deletedAlarmId.equals(((Number) a.get("id")).longValue()));
        Assertions.assertFalse(found, "告警删除后列表中不应再出现该告警");
        // 清理触发告警用的测试字典
        mvcResult = mockGet("/api/v1/dict?dictCode=" + dictCode, accessToken);
        List<DictPto> cleanupList = readData(mvcResult, "/data", new TypeReference<>() {});
        if (!cleanupList.isEmpty()) {
            mockDelete("/api/v1/dict/" + cleanupList.get(0).getId(), accessToken);
        }
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
