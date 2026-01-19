package com.churninsight.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.churninsight.backend.dto.BatchJobDTO;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, BatchJobDTO> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, BatchJobDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Serialización de claves (String)
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Serialización de valores (JSON)
        Jackson2JsonRedisSerializer<BatchJobDTO> jacksonSerializer = 
            new Jackson2JsonRedisSerializer<>(BatchJobDTO.class);
        ObjectMapper objectMapper = new ObjectMapper();
        jacksonSerializer.setObjectMapper(objectMapper);
        
        template.setValueSerializer(jacksonSerializer);
        template.setHashValueSerializer(jacksonSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
