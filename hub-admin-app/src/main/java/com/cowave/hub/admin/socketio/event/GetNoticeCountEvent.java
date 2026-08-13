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
package com.cowave.hub.admin.socketio.event;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.cowave.hub.admin.service.sys.SysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.cowave.hub.admin.domain.AdminSocketIoNames.EVENT_SERVER_NOTICE_COUNT;

/**
 * @author shanhuiming
 */
@Component
@RequiredArgsConstructor
public class GetNoticeCountEvent implements DataListener<String> {

    private final SysNoticeService noticeService;

    @Override
    public void onData(SocketIOClient client, String userCode, AckRequest ackSender) {
        Long noticeCount = noticeService.msgUnReadCount(userCode);
        client.sendEvent(EVENT_SERVER_NOTICE_COUNT, noticeCount);
    }
}
