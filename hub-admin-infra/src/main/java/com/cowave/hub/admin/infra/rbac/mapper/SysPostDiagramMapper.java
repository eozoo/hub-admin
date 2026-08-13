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
package com.cowave.hub.admin.infra.rbac.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cowave.hub.admin.domain.rbac.entity.SysPostDiagram;
import com.cowave.hub.admin.domain.rbac.entity.pto.DiagramNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author shanhuiming
 */
@Mapper
public interface SysPostDiagramMapper extends BaseMapper<SysPostDiagram> {

    /**
     * 岗位树节点列表
     */
    List<DiagramNode> listDiagramNodes(String tenantId);

    /**
     * 下级岗位id列表
     */
    List<Integer> childIds(Integer postId);
}
