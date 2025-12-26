package com.churninsight.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // Spring leerá la línea "ai-service.url" de application.properties.
    // Si estás en Docker, traerá lo que diga el docker-compose.
    // Si estás en local, traerá "http://localhost:8000".
    @Value("${ai-service.url}")
    private String aiServiceUrl;

    @Bean
    public WebClient aiWebClient() { 
        
        // Usamos el método estático .builder()
        return WebClient.builder() 
                .baseUrl(aiServiceUrl)
                .build();
    }
}
