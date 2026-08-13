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
import com.cowave.hub.admin.domain.sys.entity.SysFeedbackLike;
import com.cowave.hub.admin.domain.sys.entity.pto.FeedbackStatPto;
import com.cowave.hub.admin.domain.sys.entity.vo.FeedbackCommentVo;
import com.cowave.hub.admin.domain.sys.entity.vo.FeedbackVo;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

/**
 * 系统评分留言
 *
 * @author shanhuiming
 */
public class HubFeedbackControllerTest extends SpringTest {

    /**
     * 登录 -> 新增留言 -> 列表验证 -> 留言点赞 -> 点赞人列表 -> 取消点赞
     *     -> 新增评论 -> 评论列表 -> 评论点赞 -> 评论点赞人列表 -> 取消评论点赞
     *     -> 统计 -> 删除评论 -> 删除留言 -> 列表验证删除 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/feedback
     * get /api/v1/feedback
     * patch /api/v1/feedback/like/feedback/{id}
     * get /api/v1/feedback/like
     * patch /api/v1/feedback/like/feedback/{id}
     * post /api/v1/feedback/comment
     * get /api/v1/feedback/comment
     * patch /api/v1/feedback/like/comment/{id}
     * get /api/v1/feedback/like
     * patch /api/v1/feedback/like/comment/{id}
     * get /api/v1/feedback/stat
     * delete /api/v1/feedback/comment/{commentId}
     * delete /api/v1/feedback/{id}
     * get /api/v1/feedback
     * delete /api/v1/auth/logout
     */
    @Test
    public void feedbackLifecycle() throws Exception {
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
        // 新增留言
        body = """
                {
                    "score": 5,
                    "content": "test feedback content"
                }
                """;
        mockPost("/api/v1/feedback", body, accessToken);
        // 列表，验证新增
        mvcResult = mockGet("/api/v1/feedback?page=1&pageSize=100", accessToken);
        List<FeedbackVo> feedbackList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, feedbackList.size());
        FeedbackVo feedback = feedbackList.get(0);
        Long feedbackId = feedback.getId();
        Assertions.assertEquals("test feedback content", feedback.getContent());
        Assertions.assertEquals(5, feedback.getScore());
        // 留言点赞
        mvcResult = mockPatch("/api/v1/feedback/like/feedback/" + feedbackId, "", accessToken);
        Assertions.assertTrue(readData(mvcResult, "/data", new TypeReference<Boolean>() {}), "首次点赞应返回true");
        // 点赞人列表
        mvcResult = mockGet("/api/v1/feedback/like?targetType=1&targetId=" + feedbackId, accessToken);
        List<SysFeedbackLike> likers = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertFalse(likers.isEmpty(), "点赞后至少应有1条点赞记录");
        Assertions.assertEquals("cowave-sys-liubei", likers.get(0).getUserCode());
        // 取消点赞
        mvcResult = mockPatch("/api/v1/feedback/like/feedback/" + feedbackId, "", accessToken);
        Assertions.assertFalse(readData(mvcResult, "/data", new TypeReference<Boolean>() {}), "取消点赞应返回false");
        // 新增评论
        body = """
                {
                    "feedbackId": %d,
                    "content": "test comment content"
                }
                """.formatted(feedbackId);
        mockPost("/api/v1/feedback/comment", body, accessToken);
        // 评论列表
        mvcResult = mockGet("/api/v1/feedback/comment?feedbackId=" + feedbackId, accessToken);
        List<FeedbackCommentVo> commentList = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertFalse(commentList.isEmpty(), "评论列表至少应有1条");
        FeedbackCommentVo comment = commentList.get(0);
        Long commentId = comment.getId();
        Assertions.assertEquals("test comment content", comment.getContent());
        Assertions.assertTrue(comment.getIsMine(), "liubei自己的评论isMine应为true");
        // 评论点赞
        mvcResult = mockPatch("/api/v1/feedback/like/comment/" + commentId, "", accessToken);
        Assertions.assertTrue(readData(mvcResult, "/data", new TypeReference<Boolean>() {}), "首次评论点赞应返回true");
        // 评论点赞人列表
        mvcResult = mockGet("/api/v1/feedback/like?targetType=2&targetId=" + commentId, accessToken);
        List<SysFeedbackLike> commentLikers = readData(mvcResult, "/data", new TypeReference<List<SysFeedbackLike>>() {});
        Assertions.assertFalse(commentLikers.isEmpty(), "评论点赞后至少应有1条记录");
        // 取消评论点赞
        mvcResult = mockPatch("/api/v1/feedback/like/comment/" + commentId, "", accessToken);
        Assertions.assertFalse(readData(mvcResult, "/data", new TypeReference<Boolean>() {}), "取消评论点赞应返回false");
        // 评分统计
        mvcResult = mockGet("/api/v1/feedback/stat", accessToken);
        FeedbackStatPto stat = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertNotNull(stat);
        Assertions.assertTrue(stat.getTotal() >= 1);
        // 删除评论
        mockDelete("/api/v1/feedback/comment/" + commentId, accessToken);
        // 删除留言
        mockDelete("/api/v1/feedback/" + feedbackId, accessToken);
        // 列表，验证删除
        mvcResult = mockGet("/api/v1/feedback?page=1&pageSize=100", accessToken);
        feedbackList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(0, feedbackList.size());
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }

    /**
     * 登录 -> 新增5分留言 -> 新增3分留言 -> 新增1分留言 -> 列表验证(3条)
     *     -> 按评分筛选 -> 统计验证 -> 逐一删除 -> 列表验证清空 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/feedback (5分)
     * post /api/v1/feedback (3分)
     * post /api/v1/feedback (1分)
     * get /api/v1/feedback
     * get /api/v1/feedback?score=5
     * get /api/v1/feedback/stat
     * delete /api/v1/feedback/{id1}
     * delete /api/v1/feedback/{id2}
     * delete /api/v1/feedback/{id3}
     * get /api/v1/feedback
     * delete /api/v1/auth/logout
     */
    @Test
    public void feedbackStat() throws Exception {
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
        // 新增5分留言
        body = "{\"score\": 5, \"content\": \"score 5 feedback\"}";
        mockPost("/api/v1/feedback", body, accessToken);
        // 新增3分留言
        body = "{\"score\": 3, \"content\": \"score 3 feedback\"}";
        mockPost("/api/v1/feedback", body, accessToken);
        // 新增1分留言
        body = "{\"score\": 1, \"content\": \"score 1 feedback\"}";
        mockPost("/api/v1/feedback", body, accessToken);
        // 列表，验证3条留言
        mvcResult = mockGet("/api/v1/feedback?page=1&pageSize=100", accessToken);
        List<FeedbackVo> feedbackList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(3, feedbackList.size());
        Long id1 = feedbackList.get(0).getId();
        Long id2 = feedbackList.get(1).getId();
        Long id3 = feedbackList.get(2).getId();
        // 按评分筛选（仅查5分）
        mvcResult = mockGet("/api/v1/feedback?score=5&page=1&pageSize=100", accessToken);
        List<FeedbackVo> score5List = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(1, score5List.size());
        Assertions.assertEquals(5, score5List.get(0).getScore());
        // 评分统计
        mvcResult = mockGet("/api/v1/feedback/stat", accessToken);
        FeedbackStatPto stat = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertNotNull(stat);
        Assertions.assertTrue(stat.getTotal() >= 3, "统计总数至少为3");
        Assertions.assertNotNull(stat.getAvgScore(), "平均分不应为空");
        // 逐一删除留言
        mockDelete("/api/v1/feedback/" + id1, accessToken);
        mockDelete("/api/v1/feedback/" + id2, accessToken);
        mockDelete("/api/v1/feedback/" + id3, accessToken);
        // 列表，验证清空
        mvcResult = mockGet("/api/v1/feedback?page=1&pageSize=100", accessToken);
        feedbackList = readData(mvcResult, "/data/list", new TypeReference<>() {});
        Assertions.assertEquals(0, feedbackList.size());
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
