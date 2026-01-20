package com.churninsight.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.churninsight.backend.dto.BatchJobDTO;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, BatchJobDTO> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, BatchJobDTO> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 1. Configurar ObjectMapper con soporte para Fechas (Java 8+)
        // Esto es lo que arregla el error 500 de serialización
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // 2. Configurar el serializador JSON
        Jackson2JsonRedisSerializer<BatchJobDTO> serializer = new Jackson2JsonRedisSerializer<>(objectMapper,
                BatchJobDTO.class);

        // 3. Asignar serializadores
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
