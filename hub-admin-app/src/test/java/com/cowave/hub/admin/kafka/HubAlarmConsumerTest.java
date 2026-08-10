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
package com.cowave.hub.admin.kafka;

import com.alibaba.fastjson.JSON;
import com.cowave.hub.admin.SpringTest;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmPto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * @author shanhuiming
 */
public class HubAlarmConsumerTest extends SpringTest {

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * 发送告警Kafka消息 -> 登录 -> 查询告警列表验证消费 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/alarm/list
     * delete /api/v1/auth/logout
     */
    @Test
    public void consumeAlarm() throws Exception {
        // 发送告警Kafka消息
        String alarmCode = UUID.randomUUID().toString();
        AlarmPto alarmPto = new AlarmPto();
        alarmPto.recordAlarm(alarmCode, "单元测试告警源", "1", "单元测试告警描述", "{}");
        kafkaTemplate.send("hub-alarm", JSON.toJSONString(alarmPto)).get();
        Thread.sleep(1000); // 等待消费
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
        // 查询告警列表验证消费
        mvcResult = mockPost("/api/v1/alarm/list", "{}", accessToken);
        List<AlarmPto> alarmList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(alarmList.isEmpty());
        Assertions.assertTrue(alarmList.stream().anyMatch(a -> alarmCode.equals(a.getAlarmCode())));
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
