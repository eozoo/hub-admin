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
package com.cowave.hub.admin.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.cowave.hub.admin.domain.sys.entity.HubOperation;
import com.cowave.hub.admin.service.sys.HubOperationService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class HubOperationConsumer {

    private final HubOperationService operationService;

    @KafkaListener(topics = {"${spring.access.oplog-kafka:hub-oplog}"})
    public void consume(ConsumerRecord<?, ?> message) {
        HubOperation operation = JSON.parseObject(String.valueOf(message.value()), HubOperation.class);
        operationService.saveLog(operation);
    }
}
