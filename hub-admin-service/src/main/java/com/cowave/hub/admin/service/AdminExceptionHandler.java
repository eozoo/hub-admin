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
package com.cowave.hub.admin.service;

import com.alibaba.fastjson.JSON;
import com.cowave.hub.admin.domain.sys.biz.SysAlarmBiz;
import com.cowave.zoo.http.client.response.Response;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.access.AccessExceptionHandler;
import com.cowave.zoo.framework.configuration.ApplicationProperties;
import com.cowave.hub.admin.domain.sys.entity.pto.AlarmPto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求告警
 *
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Component
public class AdminExceptionHandler implements AccessExceptionHandler {

    private final ApplicationProperties applicationProperties;

    private final SysAlarmBiz alarmBiz;

    @Override
    public void handler(Exception e, int status, Response<Void> response) {
        if(response.getCode().startsWith("4")){
            // 忽略4xx客户端错误
            return;
        }

        Map<String, Object> content = new HashMap<>();
        content.put("requestId", Access.accessId());
        content.put("requestUrl", Access.accessUrl());
        content.put("requestParam", Access.getAccessLogParams());
        content.put("responseCode", response.getCode());
        content.put("responseMsg", response.getMsg());
        content.put("responseData", response.getData());

        AlarmPto alarmPto = new AlarmPto();
        String group = applicationProperties.getName();
        String type = "access_failed";

        String param = "";
        if(Access.getAccessLogParams() != null){
            param = JSON.toJSONString(Access.getAccessLogParams());
        }
        String md5 = DigestUtils.md5Hex(Access.accessUrl() + param);
        alarmPto.recordAlarm(md5, group, type, Access.accessUrl(), content);
        alarmPto.setAlarmLevel(3);
        alarmBiz.recordAlarm(alarmPto);
    }
}
