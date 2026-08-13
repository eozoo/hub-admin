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
import com.cowave.hub.admin.domain.sys.entity.SysDict;
import com.cowave.hub.admin.domain.sys.entity.pto.DictPto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

/**
 * @author shanhuiming
 */
public class HubDictControllerTest extends SpringTest {

    /**
     * 登录 -> 新增字典 -> 列表验证新增 -> 详情 -> 修改 -> 详情验证修改 -> 删除 -> 列表验证删除 -> 退出登录
     * post /api/v1/auth/public/logon
     * post /api/v1/dict
     * get /api/v1/dict
     * get /api/v1/dict/{dictId}
     * patch /api/v1/dict
     * get /api/v1/dict/{dictId}
     * delete /api/v1/dict/{dictIds}
     * get /api/v1/dict
     * delete /api/v1/auth/logout
     */
    @Test
    public void dictCrud() throws Exception {
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
        // 新增字典（挂在 op_action 类型下，属于 sys 分组）
        String dictCode = "test_dict_crud";
        body = """
                {
                    "typeCode": "op_action",
                    "dictCode": "%s",
                    "dictName": "test dict crud",
                    "dictValue": "0",
                    "valueType": "int",
                    "valueParser": "com.cowave.zoo.framework.helper.redis.dict.DefaultValueParser",
                    "dictOrder": 99,
                    "status": 1
                }
                """.formatted(dictCode);
        mockPost("/api/v1/dict", body, accessToken);
        // 列表，验证新增
        mvcResult = mockGet("/api/v1/dict?dictCode=" + dictCode, accessToken);
        List<DictPto> dictList = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(1, dictList.size());
        DictPto dict = dictList.get(0);
        Long dictId = dict.getId();
        Assertions.assertEquals(dictCode, dict.getDictCode());
        Assertions.assertEquals("test dict crud", dict.getDictName());
        Assertions.assertEquals("op_action", dict.getTypeCode());
        // 详情
        mvcResult = mockGet("/api/v1/dict/" + dictId, accessToken);
        DictPto detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(dictId, detail.getId());
        Assertions.assertEquals(dictCode, detail.getDictCode());
        Assertions.assertEquals("test dict crud", detail.getDictName());
        // 修改字典
        String updatedCode = "test_dict_crud_updated";
        body = """
                {
                    "id": %d,
                    "typeCode": "op_action",
                    "dictCode": "%s",
                    "dictName": "test dict crud updated",
                    "dictValue": "1",
                    "valueType": "int",
                    "valueParser": "com.cowave.zoo.framework.helper.redis.dict.DefaultValueParser",
                    "dictOrder": 100,
                    "status": 0
                }
                """.formatted(dictId, updatedCode);
        mockPatch("/api/v1/dict", body, accessToken);
        // 详情，验证修改
        mvcResult = mockGet("/api/v1/dict/" + dictId, accessToken);
        detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(updatedCode, detail.getDictCode());
        Assertions.assertEquals("test dict crud updated", detail.getDictName());
        Assertions.assertEquals("1", detail.getDictValue().toString());
        Assertions.assertEquals(100, detail.getDictOrder());
        // 删除字典
        mockDelete("/api/v1/dict/" + dictId, accessToken);
        // 列表，验证删除
        mvcResult = mockGet("/api/v1/dict?dictCode=" + updatedCode, accessToken);
        dictList = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertEquals(0, dictList.size());
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }

    /**
     * 登录 -> 列表 -> 详情 -> 按编码获取 -> 按类型获取列表 -> 按分组获取列表 -> 按分组获取类型选项 -> 导出 -> 退出登录
     * post /api/v1/auth/public/logon
     * get /api/v1/dict
     * get /api/v1/dict/{dictId}
     * get /api/v1/dict/code/{dictCode}
     * get /api/v1/dict/type/{typeCode}
     * get /api/v1/dict/group/{groupCode}
     * get /api/v1/dict/group/types/{groupCode}
     * post /api/v1/dict/export
     * delete /api/v1/auth/logout
     */
    @Test
    public void dictQuery() throws Exception {
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
        // 字典列表（从列表获取一个已存在的dictId用于后续详情查询）
        mvcResult = mockGet("/api/v1/dict", accessToken);
        List<DictPto> dictList = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertFalse(dictList.isEmpty(), "预置字典数据至少应有1条");
        Long dictId = dictList.get(0).getId();
        // 字典详情
        mvcResult = mockGet("/api/v1/dict/" + dictId, accessToken);
        DictPto detail = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertNotNull(detail);
        Assertions.assertNotNull(detail.getDictCode(), "字典码不应为空");
        // 按编码获取字典（使用预置的 job_task_bean，dictCode=job_task_bean）
        mvcResult = mockGet("/api/v1/dict/code/job_task_bean", accessToken);
        SysDict dictByCode = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertNotNull(dictByCode);
        Assertions.assertEquals("job_task_bean", dictByCode.getDictCode());
        // 按类型获取字典列表（job_task 类型下有6个任务类型字典项）
        mvcResult = mockGet("/api/v1/dict/type/job_task", accessToken);
        List<DictPto> typeList = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertTrue(typeList.size() >= 6, "job_task类型至少包含6条任务类型字典");
        // 按分组获取字典列表（job 分组包含 job_task、job_route、job_block、job_misfire 四类字典项）
        mvcResult = mockGet("/api/v1/dict/group/job", accessToken);
        List<DictPto> groupList = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertTrue(groupList.size() >= 20, "job分组至少包含20条字典项");
        // 按分组获取类型选项（job 分组下应有4个类型）
        mvcResult = mockGet("/api/v1/dict/group/types/job", accessToken);
        List<DictPto> typeOptions = readData(mvcResult, "/data", new TypeReference<>() {});
        Assertions.assertTrue(typeOptions.size() >= 4, "job分组至少包含4个类型选项");
        // 导出字典
        mockExport("/api/v1/dict/export", null, "target/dict.xlsx", accessToken);
        // 退出登录
        mockDelete("/api/v1/auth/logout", accessToken);
    }
}
