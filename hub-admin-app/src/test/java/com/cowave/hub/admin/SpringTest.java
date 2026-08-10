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
package com.cowave.hub.admin;

import com.cowave.zoo.framework.access.AccessProperties;
import com.cowave.zoo.framework.access.filter.AccessFilter;
import com.cowave.zoo.framework.access.filter.AccessIdGenerator;
import com.cowave.zoo.framework.access.security.BearerTokenFilter;
import com.cowave.zoo.framework.access.security.BearerTokenService;
import com.cowave.zoo.framework.helper.redis.RedisHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author shanhuiming
 */
@ContextConfiguration(classes = SpringTestConfiguration.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SpringTest {

    public static final PostgreSQLContainer<?> PG = new PostgreSQLContainer<>(
            "postgres:13.1").withExposedPorts(5432)
            .withDatabaseName("hub-admin").withUsername("postgres").withPassword("postgres")
            .withCommand("postgres", "-c", "max_connections=1000")
            .withEnv("POSTGRES_MAX_CONNECTIONS", "1000");

    public static final GenericContainer<?> REDIS = new GenericContainer<>(
            "redis:7.0").withExposedPorts(6379);

    public static final GenericContainer<?> ZK = new GenericContainer<>(
            "zookeeper:3.8.1").withExposedPorts(2181);

    public static final KafkaContainer KAFKA = new KafkaContainer(DockerImageName.parse(
            "confluentinc/cp-kafka:6.2.1")).dependsOn(ZK);

    public static final MinIOContainer MINIO = new MinIOContainer(
            "minio/minio:RELEASE.2023-09-04T19-57-37Z").withExposedPorts(9000)
            .withUserName("admin").withPassword("admin123");

    public static final GenericContainer<?> ES = new GenericContainer<>(
            "elastic/elasticsearch:7.14.0").withExposedPorts(9200)
            .withEnv("discovery.type", "single-node")
            .withEnv("xpack.security.enabled", "false");

    static {
        PG.start();
        ES.start();
        REDIS.start();
        ZK.start();
        KAFKA.start();
        MINIO.start();
    }

    @Autowired
    protected RedisHelper redisHelper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccessProperties accessProperties;
    @Autowired
    private BearerTokenService bearerTokenService;
    @Autowired
    private WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @PostConstruct
    public void init() {
        String[] ignoreUrls = {
                "/api/v1/flow/designer/**",
                "/designer/*",
                "/favicon.ico",
                "/actuator/*",
                "/doc/*"};
        Map<String, Set<String>> anonymousUrls = new HashMap<>();
        anonymousUrls.put("POST", Set.of(
                "/api/v1/auth/public/logon",
                "/api/v1/auth/public/login",
                "/api/v1/auth/public/mfa",
                "/api/v1/auth/public/ldap",
                "/api/v1/auth/public/register",
                "/api/v1/oauth/client/authorize/token"));
        anonymousUrls.put("GET", Set.of(
                "/api/v1/auth/public/captcha",
                "/api/v1/auth/public/captcha/email",
                "/api/v1/auth/public/refresh",
                "/api/v1/auth/public/gitlab",
                "/api/v1/oauth/callback/**",
                "/api/v1/oauth/client/redirect/**",
                "/api/v1/oauth/client/authorize/refresh",
                "/api/v1/flow/designer/**"));
        AccessIdGenerator accessIdGenerator = new AccessIdGenerator("");
        AccessFilter accessFilter = new AccessFilter(null, accessIdGenerator, accessProperties, objectMapper);
        BearerTokenFilter bearerTokenFilter = new BearerTokenFilter(true, bearerTokenService, ignoreUrls, anonymousUrls);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true), "/*")
                .addFilter(accessFilter, "/api/v1/*")
                .addFilter(bearerTokenFilter, "/api/v1/*")
                .build();
    }

    protected String writeString(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    protected String readString(MvcResult result, String jsonPath) throws Exception {
        return objectMapper.readTree(result.getResponse().getContentAsString()).at(jsonPath).asText();
    }

    protected <T> T readData(MvcResult result, String jsonPath, TypeReference<T> typeRef) throws Exception {
        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString()).at(jsonPath);
        if (node.isMissingNode() || node.isNull()) {
            return null;
        }
        return objectMapper.convertValue(node, typeRef);
    }

    protected MvcResult mockPost(String url, String content) throws Exception {
        return mockPost(url, content, null);
    }

    protected MvcResult mockPost(String url, String content, String accessToken) throws Exception {
        return mockPost(url, content, accessToken, 200);
    }

    protected MvcResult mockPost(String url, String content, String accessToken, int httpStatus) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        if (StringUtils.hasText(accessToken)) {
            requestBuilder.header("Authorization", accessToken);
        }
        return this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(httpStatus))
                .andReturn();
    }

    protected MvcResult mockGet(String url) throws Exception {
        return mockGet(url, null);
    }

    protected MvcResult mockGet(String url, String accessToken) throws Exception {
        return mockGet(url, accessToken, 200);
    }

    protected MvcResult mockGet(String url, String accessToken, int httpStatus) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(url)
                .accept(MediaType.APPLICATION_JSON);
        if (StringUtils.hasText(accessToken)) {
            requestBuilder.header("Authorization", accessToken);
        }
        return this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(httpStatus))
                .andReturn();
    }

    protected MvcResult mockPatch(String url, String content) throws Exception {
        return mockPatch(url, content, null);
    }

    protected MvcResult mockPatch(String url, String content, String accessToken) throws Exception {
        return mockPatch(url, content, accessToken, 200);
    }

    protected MvcResult mockPatch(String url, String content, String accessToken, int httpStatus) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        if (StringUtils.hasText(accessToken)) {
            requestBuilder.header("Authorization", accessToken);
        }
        return this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(httpStatus))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(httpStatus))
                .andReturn();
    }

    protected MvcResult mockDelete(String url) throws Exception {
        return mockDelete(url, null);
    }

    protected MvcResult mockDelete(String url, String accessToken) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(url)
                .accept(MediaType.APPLICATION_JSON);
        if (StringUtils.hasText(accessToken)) {
            requestBuilder.header("Authorization", accessToken);
        }
        return this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andReturn();
    }

    protected void mockExport(String url, String content, String filePath, String accessToken) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
        if (StringUtils.hasText(accessToken)) {
            requestBuilder.header("Authorization", accessToken);
        }
        if (content != null) {
            requestBuilder.content(content);
        }
        this.mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(mvcResult -> {
                    try (FileOutputStream out = new FileOutputStream(filePath);
                         ByteArrayInputStream in = new ByteArrayInputStream(mvcResult.getResponse().getContentAsByteArray())) {
                        StreamUtils.copy(in, out);
                    }
                });
    }

    protected void mockImport(String url, MultiValueMap<String, String> params, String classPath, String accessToken) throws Exception {
        ClassPathResource resource = new ClassPathResource(classPath);
        try (InputStream inputStream = resource.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            StreamUtils.copy(inputStream, outputStream);
            MockMultipartFile file = new MockMultipartFile("file", "test.x",
                    MediaType.MULTIPART_FORM_DATA_VALUE, outputStream.toByteArray());
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart(url)
                    .file(file)
                    .header("Authorization", accessToken);
            if (params != null) {
                requestBuilder.params(params);
            }
            mockMvc.perform(requestBuilder)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();
        }
    }
}
