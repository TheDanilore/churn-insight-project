package com.churninsight.backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public Map<String, String> checkHealth() {
        return Map.of(
                "status", "OK",
                "message", "Backend Spring Boot corriendo exitosamente sin docker 🚀",
                "version", "1.0.0");
    }
}