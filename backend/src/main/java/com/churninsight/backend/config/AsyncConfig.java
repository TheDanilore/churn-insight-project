package com.churninsight.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "batchProcessExecutor")
    public Executor batchProcessExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);           // Mínimo de threads
        executor.setMaxPoolSize(5);            // Máximo de threads
        executor.setQueueCapacity(100);        // Cola de espera
        executor.setThreadNamePrefix("batch-"); // Nombre para debug
        executor.initialize();
        return executor;
    }
}
