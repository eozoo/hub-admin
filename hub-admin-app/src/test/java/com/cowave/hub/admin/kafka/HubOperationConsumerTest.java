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
import com.cowave.hub.admin.domain.rbac.enums.SuccessStatus;
import com.cowave.hub.admin.domain.sys.entity.HubOperation;
import com.cowave.zoo.framework.access.security.AccessInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.List;

/**
 * @author shanhuiming
 */
public class HubOperationConsumerTest extends SpringTest {

    @Autowired
    private KafkaTemplate<Object, Object> kafkaTemplate;

    /**
     * 发送操作日志Kafka消息 -> 登录 -> 查询操作日志验证消费 -> 退出登录
     * post /api/v1/auth/public/logon
     * get /api/v1/oplog
     * delete /api/v1/auth/logout
     */
    @Test
    public void consumeOperation() throws Exception {
        // 发送操作日志Kafka消息
        String testModule = "单元测试模块";
        AccessInfo accessInfo = new AccessInfo();
        accessInfo.setAccessTenantId("cowave");
        HubOperation operation = new HubOperation();
        operation.setAccess(accessInfo);
        operation.setOpModule(testModule);
        operation.setOpType("单元测试类型");
        operation.setOpAction("CREATE");
        operation.setOpTime(new Date());
        operation.setOpStatus(SuccessStatus.SUCCESS);
        kafkaTemplate.send("hub-oplog", JSON.toJSONString(operation)).get();
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
        // 查询操作日志验证消费
        mvcResult = mockGet("/api/v1/oplog?page=1&pageSize=100&opModule=" + testModule, accessToken);
        List<HubOperation> opList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(opList.isEmpty());
        Assertions.assertTrue(opList.stream().anyMatch(o -> testModule.equals(o.getOpModule())));
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
