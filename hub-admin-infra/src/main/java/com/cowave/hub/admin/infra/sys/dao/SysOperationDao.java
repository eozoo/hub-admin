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

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.cowave.hub.admin.domain.rbac.entity.SysScope;
import com.cowave.hub.admin.domain.sys.entity.SysOperation;
import com.cowave.hub.admin.domain.sys.entity.query.OperationQuery;
import com.cowave.hub.admin.domain.sys.repository.SysOperationRepository;
import com.cowave.hub.admin.infra.rbac.mapper.SysScopeMapper;
import com.cowave.zoo.framework.access.Access;
import com.cowave.zoo.framework.helper.es.EsHelper;
import com.cowave.zoo.http.client.response.Response;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shanhuiming
 */
@RequiredArgsConstructor
@Repository
public class SysOperationDao implements SysOperationRepository {
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
    private final SysScopeMapper scopeMapper;

    @PostConstruct
    public void indexInit() {
        esHelper.indexCreate(SysOperation.INDEX_NAME, MAPPING_PROPERTIES);
        esHelper.indexSetting(SysOperation.INDEX_NAME, 25000);
    }

    @Override
    public Response.Page<SysOperation> queryPage(String tenantId, OperationQuery query, boolean isPage) {
        List<Query> filters = new ArrayList<>();
        filters.add(termQuery("access.accessTenantId", tenantId));
        if (StringUtils.isNotBlank(query.getOpModule())) {
            filters.add(termQuery("opModule.keyword", query.getOpModule()));
        }
        if (StringUtils.isNotBlank(query.getOpType())) {
            filters.add(termQuery("opType.keyword", query.getOpType()));
        }

        String currentScope = resolveCurrentScope();
        if (StringUtils.isNotBlank(currentScope)) {
            if (SCOPE_PERSON.equals(currentScope)) {
                filters.add(termQuery("access.accessUserAccount", Access.userAccount()));
            } else if (SCOPE_DEPT.equals(currentScope)) {
                filters.add(termQuery("access.accessDeptId", String.valueOf(Access.deptId())));
            }
        }

        if (query.getBeginTime() != null || query.getEndTime() != null) {
            filters.add(Query.of(builder -> builder.range(range -> range.number(number -> {
                number.field("opTime");
                if (query.getBeginTime() != null) {
                    number.gte((double) query.getBeginTime().getTime());
                }
                if (query.getEndTime() != null) {
                    number.lte((double) query.getEndTime().getTime());
                }
                return number;
            }))));
        }

        if (StringUtils.isNotBlank(query.getOpUser())) {
            filters.add(Query.of(builder -> builder.bool(bool -> bool
                    .should(wildcardQuery("access.accessUserName", query.getOpUser()))
                    .should(wildcardQuery("access.accessUserAccount", query.getOpUser()))
                    .minimumShouldMatch("1"))));
        }

        Query esQuery = Query.of(builder -> builder.bool(bool -> bool.filter(filters)));
        int from = isPage ? Access.pageOffset() : -1;
        int size = isPage ? Access.pageSize() : -1;
        return esHelper.query(SysOperation.INDEX_NAME, esQuery, from, size, "opTime", SysOperation.class);
    }

    @Override
    public void save(SysOperation sysOperation) {
        esHelper.insert(SysOperation.INDEX_NAME, sysOperation);
    }

    @Override
    public void delete(List<String> ids) {
        List<Query> filters = new ArrayList<>();
        String currentScope = resolveCurrentScope();
        if (StringUtils.isNotBlank(currentScope)) {
            if (SCOPE_PERSON.equals(currentScope)) {
                filters.add(termQuery("access.accessUserAccount", Access.userAccount()));
            } else if (SCOPE_DEPT.equals(currentScope)) {
                filters.add(termQuery("access.accessDeptId", String.valueOf(Access.deptId())));
            }
        }
        filters.add(Query.of(builder -> builder.terms(terms -> terms.field("_id")
                .terms(values -> values.value(ids.stream().map(FieldValue::of).toList())))));
        esHelper.deleteByQuery(SysOperation.INDEX_NAME, boolQuery(filters), true);
    }

    @Override
    public void clean(String tenantId) {
        esHelper.deleteByQuery(SysOperation.INDEX_NAME,
                boolQuery(List.of(termQuery("access.accessTenantId", tenantId))), true);
    }

    private Query boolQuery(List<Query> filters) {
        return Query.of(builder -> builder.bool(bool -> bool.filter(filters)));
    }

    private Query termQuery(String field, String value) {
        return Query.of(builder -> builder.term(term -> term.field(field).value(value)));
    }

    private Query wildcardQuery(String field, String value) {
        return Query.of(builder -> builder.wildcard(wildcard -> wildcard.field(field).value(value)));
    }

    private String resolveCurrentScope() {
        List<Integer> scopeIds = Access.scopeIds();
        if (CollectionUtils.isEmpty(scopeIds)) {
            return null;
        }
        SysScope scope = scopeMapper.selectById(scopeIds.get(0));
        if (scope == null || scope.getScopeContent() == null) {
            return null;
        }
        Object scopeType = scope.getScopeContent().get("scope");
        return scopeType == null ? null : scopeType.toString();
    }
}
