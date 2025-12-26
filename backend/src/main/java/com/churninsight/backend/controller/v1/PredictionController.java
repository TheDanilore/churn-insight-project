package com.churninsight.backend.controller.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.churninsight.backend.dto.ChurnRequestDTO;
import com.churninsight.backend.dto.ChurnResponseDTO;
import com.churninsight.backend.service.PredictionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/predictions") // Buena práctica: Incluye el RECURSO en la base, no solo la versión
public class PredictionController {

    private static final Logger logger = LoggerFactory.getLogger(PredictionController.class);
    private final PredictionService predictionService;

    // Inyección de dependencias por constructor
    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    // Endpoint limpio: POST /api/v1/predictions (en lugar de /api/v1/predict)
    // En REST, se usan sustantivos (recursos), no verbos en la URL.
    // El verbo HTTP (POST) ya indica la acción "Crear/Predecir".
    @PostMapping
    public ResponseEntity<ChurnResponseDTO> predictChurn(
            @Valid @RequestBody ChurnRequestDTO request) {
        logger.info("📩 [V1] Recibiendo solicitud de predicción...");

        // Llamamos al servicio (que a su vez llama a Python)
        ChurnResponseDTO response = predictionService.obtenerPrediccion(request);

        logger.info("📤 [V1] Respuesta generada.");

        // Devolvemos HTTP 200 OK con el JSON de respuesta
        return ResponseEntity.ok(response);
    }
}