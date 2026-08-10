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

import com.cowave.zoo.framework.helper.datasource.DynamicDataSource;
import com.cowave.zoo.framework.helper.redis.connection.LettuceRedisConnectionConfiguration;
import io.lettuce.core.resource.DefaultClientResources;
import io.minio.MinioClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestClient;
import org.apache.http.HttpHost;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shanhuiming
 */
@TestConfiguration
public class SpringTestConfiguration {

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(SpringTest.PG.getDriverClassName());
        dataSourceBuilder.url(SpringTest.PG.getJdbcUrl());
        dataSourceBuilder.username(SpringTest.PG.getUsername());
        dataSourceBuilder.password(SpringTest.PG.getPassword());
        DataSource dataSource = dataSourceBuilder.build();
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("primary", dataSource);
        return new DynamicDataSource(dataSource, dataSourceMap);
    }

    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder().endpoint(SpringTest.MINIO.getS3URL()).credentials("admin", "admin123").build();
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(ObjectProvider<RedisSentinelConfiguration> sentinelConfigurationProvider,
                                   ObjectProvider<RedisClusterConfiguration> clusterConfigurationProvider,
                                   ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers){
        RedisProperties redisProperties = new RedisProperties();
        redisProperties.setHost(SpringTest.REDIS.getHost());
        redisProperties.setPort(SpringTest.REDIS.getMappedPort(6379));
        LettuceRedisConnectionConfiguration redisConnectionConfiguration = new LettuceRedisConnectionConfiguration(
                redisProperties, sentinelConfigurationProvider, clusterConfigurationProvider);
        return redisConnectionConfiguration.redisConnectionFactory(builderCustomizers, DefaultClientResources.create());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactory() {
        KafkaProperties properties = new KafkaProperties();
        properties.setBootstrapServers(List.of(SpringTest.KAFKA.getBootstrapServers()));
        properties.getConsumer().setGroupId("hub-admin");
        properties.getConsumer().setAutoOffsetReset("earliest");
        ConsumerFactory<String, Object> consumerFactory = new DefaultKafkaConsumerFactory<>(properties.buildConsumerProperties());
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public KafkaTemplate<?, ?> kafkaTemplate() {
        KafkaProperties properties = new KafkaProperties();
        properties.setBootstrapServers(List.of(SpringTest.KAFKA.getBootstrapServers()));
        ProducerFactory<?, ?> producerFactory = new DefaultKafkaProducerFactory<>(properties.buildProducerProperties());
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public KafkaAdmin privateKafkaAdmin(){
        KafkaProperties properties = new KafkaProperties();
        properties.setBootstrapServers(List.of(SpringTest.KAFKA.getBootstrapServers()));
        KafkaAdmin kafkaAdmin = new KafkaAdmin(properties.buildAdminProperties());
        kafkaAdmin.setFatalIfBrokerNotAvailable(properties.getAdmin().isFailFast());
        return kafkaAdmin;
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(SpringTest.ES.getHost(), SpringTest.ES.getMappedPort(9200))));
    }
}
