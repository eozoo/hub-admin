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
package com.cowave.hub.admin.infra.sys.dao;

import com.cowave.hub.admin.domain.rbac.entity.HubScope;
import com.cowave.hub.admin.domain.sys.entity.HubOperation;
import com.cowave.hub.admin.domain.sys.entity.query.OperationQuery;
import com.cowave.hub.admin.domain.sys.repository.HubOperationRepository;
import com.cowave.hub.admin.infra.rbac.mapper.HubScopeMapper;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.helper.es.EsHelper;
import com.cowave.zoo.http.client.response.Response;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.cowave.zoo.framework.access.security.Permission.ROLE_ADMIN;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class HubOperationDao implements HubOperationRepository {
    private static final String SCOPE_PERSON = "personal";
    private static final String SCOPE_DEPT = "dept";
    private static final String MAPPING_PROPERTIES = """
            {
                "mappings": {
                    "properties": {
                        "opTime": {
                            "type": "date",
                            "format": "yyyy-MM-dd HH:mm:ss||epoch_millis"
                        }
                    }
                }
            }
            """;

    private final EsHelper esHelper;
    private final HubScopeMapper scopeMapper;

    @PostConstruct
    public void indexInit() {
        esHelper.indexCreate(HubOperation.INDEX_NAME, MAPPING_PROPERTIES);
        esHelper.indexSetting(HubOperation.INDEX_NAME, Settings.builder().put("index.max_result_window", 25000));
    }

    @Override
    public Response.Page<HubOperation> queryPage(String tenantId, OperationQuery query, boolean isPage) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.termsQuery("access.accessTenantId", tenantId));
        if (StringUtils.isNotBlank(query.getOpModule())) {
            boolQuery.filter(QueryBuilders.termsQuery("opModule.keyword", query.getOpModule()));
        }
        if (StringUtils.isNotBlank(query.getOpType())) {
            boolQuery.filter(QueryBuilders.termsQuery("opType.keyword", query.getOpType()));
        }

        String currentScope = resolveCurrentScope();
        if (StringUtils.isNotBlank(currentScope)) {
            if (SCOPE_PERSON.equals(currentScope)) {
                boolQuery.filter(QueryBuilders.termsQuery("access.accessUserAccount", Access.userAccount()));
            } else if (SCOPE_DEPT.equals(currentScope)) {
                boolQuery.filter(QueryBuilders.termsQuery("access.accessDeptId", List.of(Access.deptId())));
            }
        }

        if (query.getBeginTime() != null || query.getEndTime() != null) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("opTime");
            if (query.getBeginTime() != null) {
                rangeQuery.gte(query.getBeginTime());
            }
            if (query.getEndTime() != null) {
                rangeQuery.lte(query.getEndTime().getTime());
            }
            boolQuery.filter(rangeQuery);
        }

        if (StringUtils.isNotBlank(query.getOpUser())) {
            BoolQueryBuilder orCondition = QueryBuilders.boolQuery();
            orCondition.should(QueryBuilders.wildcardQuery("access.accessUserName", query.getOpUser()));
            orCondition.should(QueryBuilders.wildcardQuery("access.accessUserAccount", query.getOpUser()));
            boolQuery.filter(orCondition);
        }

        SearchSourceBuilder source = new SearchSourceBuilder();
        if (boolQuery.hasClauses()){
            source.query(boolQuery);
        }
        source.sort("opTime", SortOrder.DESC);
        if (isPage) {
            source.from(Access.pageOffset()).size(Access.pageSize());
        }
        return esHelper.query(HubOperation.INDEX_NAME, source, HubOperation.class);
    }

    @Override
    public void save(HubOperation hubOperation) {
        esHelper.insert(HubOperation.INDEX_NAME, hubOperation);
    }

    @Override
    public void delete(List<String> ids) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        String currentScope = resolveCurrentScope();
        if (StringUtils.isNotBlank(currentScope)) {
            if (SCOPE_PERSON.equals(currentScope)) {
                boolQuery.filter(QueryBuilders.termsQuery("access.accessUserAccount", Access.userAccount()));
            } else if (SCOPE_DEPT.equals(currentScope)) {
                boolQuery.filter(QueryBuilders.termsQuery("access.accessDeptId", List.of(Access.deptId())));
            }
        }
        boolQuery.filter(QueryBuilders.termsQuery("_id", ids));
        esHelper.deleteByQuery(HubOperation.INDEX_NAME, boolQuery, true);
    }

    @Override
    public void clean(String tenantId) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termsQuery("access.accessTenantId", tenantId));
        esHelper.deleteByQuery(HubOperation.INDEX_NAME, boolQuery, true);
    }

    private String resolveCurrentScope() {
        List<String> roleCodes = Access.userRoles();
        if (CollectionUtils.isEmpty(roleCodes) || roleCodes.contains(ROLE_ADMIN)) {
            return null;
        }
        List<HubScope> list = scopeMapper.listScopeByPermit("monitor:operlog:scope", roleCodes);
        return list.isEmpty() ? null : list.get(0).getScopeModule();
    }
}
