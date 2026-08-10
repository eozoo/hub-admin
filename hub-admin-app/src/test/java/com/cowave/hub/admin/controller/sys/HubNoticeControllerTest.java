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
import com.cowave.hub.admin.domain.sys.entity.HubNoticeLike;
import com.cowave.hub.admin.domain.sys.entity.vo.NoticeCommentVo;
import com.cowave.hub.admin.domain.sys.entity.vo.NoticeVo;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static com.cowave.hub.admin.domain.sys.enums.NoticeStatus.*;

/**
 * @author shanhuiming
 */
public class HubNoticeControllerTest extends SpringTest {

    /**
     * 登录 -> 新增草稿 -> 列表验证 -> 详情 -> 修改 -> 发布(全员) -> 详情验证已发布
     *     -> 消息列表 -> 阅读消息 -> 消息好评 -> 已读列表
     *     -> 删除(撤回) -> 详情验证已撤回 -> 再次删除 -> 列表验证删除 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/notice
     * get /api/v1/notice
     * get /api/v1/notice/{noticeId}
     * patch /api/v1/notice
     * get /api/v1/notice/{noticeId}
     * patch /api/v1/notice/publish/{noticeId}
     * get /api/v1/notice/{noticeId}
     * get /api/v1/notice/msg
     * patch /api/v1/notice/msg/read/{noticeId}
     * patch /api/v1/notice/msg/like/{noticeId}
     * get /api/v1/notice/readers
     * delete /api/v1/notice/{noticeIds}
     * get /api/v1/notice/{noticeId}
     * delete /api/v1/notice/{noticeIds}
     * get /api/v1/notice
     * delete /api/v1/auth/logout
     */
    @Test
    public void noticeLifecycle() throws Exception {
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
        // 新增通知草稿（全员公告，方便后续发布）
        String noticeTitle = "test notice lifecycle";
        body = """
                {
                    "noticeTitle": "%s",
                    "noticeType": 1,
                    "noticeLevel": 0,
                    "content": "test notice content",
                    "isSystem": 0,
                    "goalsAll": 1
                }
                """.formatted(noticeTitle);
        mockPost("/api/v1/notice", body, accessToken);
        // 列表，验证新增
        mvcResult = mockGet("/api/v1/notice?noticeTitle=" + noticeTitle, accessToken);
        List<NoticeVo> noticeList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, noticeList.size());
        NoticeVo notice = noticeList.get(0);
        Long noticeId = notice.getNoticeId();
        Assertions.assertEquals(noticeTitle, notice.getNoticeTitle());
        // 详情，验证草稿状态
        mvcResult = mockGet("/api/v1/notice/" + noticeId, accessToken);
        NoticeVo detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(noticeId, detail.getNoticeId());
        Assertions.assertEquals(DRAFT, detail.getNoticeStatus(), "初始状态应为0(草稿)");
        // 修改通知
        String updatedTitle = "test notice lifecycle updated";
        body = """
                {
                    "noticeId": %d,
                    "noticeTitle": "%s",
                    "noticeType": 1,
                    "noticeLevel": 0,
                    "content": "test notice content updated",
                    "isSystem": 0,
                    "goalsAll": 1
                }
                """.formatted(noticeId, updatedTitle);
        mockPatch("/api/v1/notice", body, accessToken);
        // 详情，验证修改
        mvcResult = mockGet("/api/v1/notice/" + noticeId, accessToken);
        detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(updatedTitle, detail.getNoticeTitle());
        Assertions.assertEquals("test notice content updated", detail.getContent());
        // 发布通知（全员）
        mockPatch("/api/v1/notice/publish/" + noticeId, "", accessToken);
        // 详情，验证已发布
        mvcResult = mockGet("/api/v1/notice/" + noticeId, accessToken);
        detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(PUBLISH, detail.getNoticeStatus(), "发布后状态应为1(已发布)");
        Assertions.assertNotNull(detail.getPublishTime(), "发布时间应不为空");
        // 消息列表，验证liubei收到消息
        mvcResult = mockGet("/api/v1/notice/msg", accessToken);
        List<Map<String, Object>> msgList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertFalse(msgList.isEmpty(), "发布后liubei至少收到1条消息");
        // 阅读消息
        mockPatch("/api/v1/notice/msg/read/" + noticeId, "", accessToken);
        // 消息好评
        mockPatch("/api/v1/notice/msg/like/" + noticeId + "?likeStatus=1", "", accessToken);
        // 已读列表
        mockGet("/api/v1/notice/readers?noticeId=" + noticeId, accessToken);
        // 删除通知——第一次撤回（已发布→已撤回）
        mockDelete("/api/v1/notice/" + noticeId, accessToken);
        mvcResult = mockGet("/api/v1/notice/" + noticeId, accessToken);
        detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(RECALL, detail.getNoticeStatus(), "撤回后状态应为2(已撤回)");
        // 删除通知——第二次彻底删除（已撤回→删除）
        mockDelete("/api/v1/notice/" + noticeId, accessToken);
        // 列表，验证删除
        mvcResult = mockGet("/api/v1/notice?noticeTitle=" + updatedTitle, accessToken);
        noticeList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(0, noticeList.size());
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }

    /**
     * 登录 -> 新增草稿 -> 发布(全员) -> 新增评论 -> 评论列表 -> 评论点赞 -> 点赞人列表
     *     -> 取消点赞 -> 评论列表验证 -> 未读计数 -> 删除评论 -> 删除消息 -> 删除通知(撤回) -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/notice
     * patch /api/v1/notice/publish/{noticeId}
     * post /api/v1/notice/msg/comment
     * get /api/v1/notice/msg/comment/{noticeId}
     * post /api/v1/notice/msg/comment/like/{commentId}
     * get /api/v1/notice/msg/likers
     * post /api/v1/notice/msg/comment/like/{commentId}
     * get /api/v1/notice/msg/comment/{noticeId}
     * get /api/v1/notice/msg/unread
     * delete /api/v1/notice/msg/comment/{commentId}
     * delete /api/v1/notice/msg/{msgId}
     * delete /api/v1/notice/{noticeIds}
     * delete /api/v1/auth/logout
     */
    @Test
    public void noticeComment() throws Exception {
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
        String noticeTitle = "test notice comment";
        body = """
                {
                    "noticeTitle": "%s",
                    "noticeType": 1,
                    "noticeLevel": 0,
                    "content": "test notice for comment",
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
        // 新增评论
        body = """
                {
                    "noticeId": %d,
                    "content": "test comment content"
                }
                """.formatted(noticeId);
        mockPost("/api/v1/notice/msg/comment", body, accessToken);
        // 评论列表，获取commentId
        mvcResult = mockGet("/api/v1/notice/msg/comment/" + noticeId, accessToken);
        List<NoticeCommentVo> commentList = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertFalse(commentList.isEmpty(), "评论列表至少应有1条");
        Long commentId = commentList.get(0).getId();
        Assertions.assertEquals("test comment content", commentList.get(0).getContent());
        // 评论点赞
        mvcResult = mockPost("/api/v1/notice/msg/comment/like/" + commentId, "{}", accessToken);
        String liked = readString(mvcResult, "/data");
        Assertions.assertEquals("true", liked, "首次点赞应返回true");
        // 点赞人列表
        mvcResult = mockGet("/api/v1/notice/msg/likers?targetType=2&targetId=" + commentId, accessToken);
        List<HubNoticeLike> likers = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertFalse(likers.isEmpty(), "点赞后至少应有1条点赞记录");
        // 取消评论点赞
        mvcResult = mockPost("/api/v1/notice/msg/comment/like/" + commentId, "{}", accessToken);
        liked = readString(mvcResult, "/data");
        Assertions.assertEquals("false", liked, "取消点赞应返回false");
        // 点赞人列表验证已取消
        mvcResult = mockGet("/api/v1/notice/msg/likers?targetType=2&targetId=" + commentId, accessToken);
        likers = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(0, likers.size(), "取消点赞后点赞列表应为空");
        // 未读消息计数
        mockGet("/api/v1/notice/msg/unread", accessToken);
        // 删除评论
        mockDelete("/api/v1/notice/msg/comment/" + commentId, accessToken);
        // 评论列表验证删除
        mvcResult = mockGet("/api/v1/notice/msg/comment/" + noticeId, accessToken);
        commentList = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(0, commentList.size(), "删除评论后评论列表应为空");
        // 删除消息（msgId即noticeId）
        mockDelete("/api/v1/notice/msg/" + noticeId, accessToken);
        // 删除通知（撤回）
        mockDelete("/api/v1/notice/" + noticeId, accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
