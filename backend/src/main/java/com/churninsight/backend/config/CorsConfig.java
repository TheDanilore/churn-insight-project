package com.churninsight.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    // Leemos la URL del frontend desde application.properties
    // (que a su vez viene del docker-compose/.env)
    // Valor por defecto: http://localhost:5173
    @Value("${frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**") // 1. Aplica a todas las rutas de la API
                        .allowedOrigins(frontendUrl) // 2. Solo permite a nuestro Frontend (Seguridad)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 3. Verbos permitidos
                        .allowedHeaders("*") // 4. Permitir todos los headers (Authorization, Content-Type)
                        .allowCredentials(true); // 5. Permitir cookies/credenciales
            }
        };
    }
}