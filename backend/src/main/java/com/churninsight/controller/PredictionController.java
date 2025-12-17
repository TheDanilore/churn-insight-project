package com.churninsight.controller;

import com.churninsight.dto.ClientDataRequest;
import com.churninsight.dto.PredictionResponse;
import com.churninsight.service.PredictionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class PredictionController {
    
    @Autowired
    private PredictionService predictionService;
    
    @PostMapping("/predict")
    public ResponseEntity<PredictionResponse> predict(@Valid @RequestBody ClientDataRequest request) {
        log.info("Recibida solicitud de predicción para cliente: {}", request.getCustomerId());
        try {
            PredictionResponse response = predictionService.predict(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error en predicción: ", e);
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        log.info("Obteniendo estadísticas");
        try {
            Object stats = predictionService.getStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error al obtener estadísticas: ", e);
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Backend is running");
    }
}
