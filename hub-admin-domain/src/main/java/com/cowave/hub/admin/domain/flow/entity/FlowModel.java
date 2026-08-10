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
package com.cowave.hub.admin.domain.flow.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.flowable.editor.constants.ModelDataJsonConstants;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ModelQuery;

import java.util.HashMap;

/**
 * @author shanhuiming
 */
@Getter
@Setter
public class FlowModel {

    /**
     * 流程key
     */
    private String key;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 分类
     */
    private String category;

    /**
     * 描述
     */
    private String description;

    public void fillModelQuery(ModelQuery modelQuery){
        if (StringUtils.isNotEmpty(key)) {
            modelQuery.modelKey(key);
        }
        if (StringUtils.isNotEmpty(name)) {
            modelQuery.modelName(name);
        }
    }

    public void fillModel(Model model){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode metaInfo = objectMapper.createObjectNode();
        metaInfo.put(ModelDataJsonConstants.MODEL_NAME, name);
        metaInfo.put(ModelDataJsonConstants.MODEL_REVISION, version);
        metaInfo.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        model.setMetaInfo(metaInfo.toString());
        model.setKey(key);
        model.setName(name);
        model.setVersion(version);
        model.setCategory(category);
    }

    public byte[] buildModelContent(String modelId) throws JsonProcessingException {
        HashMap<String, String> stencilset = new HashMap<>();
        stencilset.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");

        HashMap<String, String> properties = new HashMap<>();
        properties.put("process_id", key);
        properties.put("name", name);
        properties.put("category", category);

        HashMap<String, Object> content = new HashMap<>();
        content.put("resourceId", modelId);
        content.put("properties", properties);
        content.put("stencilset", stencilset);
        return new ObjectMapper().writeValueAsBytes(content);
    }
}
