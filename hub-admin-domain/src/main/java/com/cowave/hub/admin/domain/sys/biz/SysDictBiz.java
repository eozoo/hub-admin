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
package com.cowave.hub.admin.domain.sys.biz;

import com.cowave.hub.admin.domain.sys.entity.command.DictCreate;

import java.util.List;

/**
 * @author shanhuiming
 */
public interface SysDictBiz {

    /**
     * 新增字典
     */
    void saveDict(DictCreate dictCreate);

    /**
     * 修改字典
     */
    void editDict(DictCreate dictCreate);

    /**
     * 删除字典
     */
    void deleteDicts(List<Integer> dictIds);
}
