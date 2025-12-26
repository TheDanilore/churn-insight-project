package com.churninsight.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ChurnInsight API")
                        .version("1.0.0")
                        .description("API de Inteligencia Artificial para predecir la fidelidad de clientes.")
                        .contact(new Contact()
                                .name("Equipo Backend")
                                .email("dev@churninsight.com")));
    }
}
