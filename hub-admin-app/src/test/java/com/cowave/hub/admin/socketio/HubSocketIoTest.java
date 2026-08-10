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
package com.cowave.hub.admin.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.cowave.hub.admin.SpringTest;
import com.cowave.hub.admin.domain.sys.entity.vo.NoticeVo;
import com.cowave.hub.admin.socketio.event.GetNoticeCountEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.cowave.hub.admin.domain.AdminSocketIoNames.EVENT_SERVER_NOTICE_COUNT;
import static com.cowave.hub.admin.domain.sys.enums.NoticeStatus.PUBLISH;
import static org.mockito.Mockito.*;

/**
 * @author shanhuiming
 */
public class HubSocketIoTest extends SpringTest {

    @Autowired
    private GetNoticeCountEvent getNoticeCountEvent;

    /**
     * 登录 -> 新增通知 -> 发布(全员) -> 消息列表验证未读 -> SocketIO getNoticeCount事件 -> 验证未读计数 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/notice
     * patch /api/v1/notice/publish/{noticeId}
     * get /api/v1/notice/msg
     * (Socket.IO /notice getNoticeCount)
     * delete /api/v1/auth/logout
     */
    @Test
    public void getNoticeCount() throws Exception {
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
        // 新增通知草稿
        String noticeTitle = "test-socketio-notice";
        body = """
                {
                    "noticeTitle": "%s",
                    "noticeType": 1,
                    "noticeLevel": 0,
                    "content": "socketio test content",
                    "isSystem": 0,
                    "goalsAll": 1
                }
                """.formatted(noticeTitle);
        mockPost("/api/v1/notice", body, accessToken);
        // 列表获取noticeId
        mvcResult = mockGet("/api/v1/notice?noticeTitle=" + noticeTitle, accessToken);
        List<NoticeVo> noticeList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, noticeList.size());
        Long noticeId = noticeList.get(0).getNoticeId();
        // 发布通知（全员）
        mockPatch("/api/v1/notice/publish/" + noticeId, "", accessToken);
        // 验证已发布
        mvcResult = mockGet("/api/v1/notice/" + noticeId, accessToken);
        NoticeVo detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(PUBLISH, detail.getNoticeStatus());
        // 消息列表验证未读
        Thread.sleep(1000);
        mvcResult = mockGet("/api/v1/notice/msg", accessToken);
        List<?> msgList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(msgList.isEmpty(), "发布后应有未读消息");
        // SocketIO getNoticeCount事件
        SocketIOClient mockClient = mock(SocketIOClient.class);
        getNoticeCountEvent.onData(mockClient, "cowave-sys-liubei", null);
        // 确认event响应
        verify(mockClient).sendEvent(eq(EVENT_SERVER_NOTICE_COUNT), anyLong());
        // 删除通知（撤回+彻底删除）
        mockDelete("/api/v1/notice/" + noticeId, accessToken);
        mockDelete("/api/v1/notice/" + noticeId, accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
