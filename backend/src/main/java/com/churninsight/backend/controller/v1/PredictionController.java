package com.churninsight.backend.controller.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Parameter;

// 3 IMPORTACIONES NUEVAS:
import com.churninsight.backend.dto.ChurnRequestDTO;
import com.churninsight.backend.dto.ChurnResponseDTO;
import com.churninsight.backend.service.PredictionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/predictions") // Buena práctica: Incluye el RECURSO en la base, no solo la versión
@Tag(name = "Predicciones", description = "Endpoints para consultar el modelo de IA") // Título en Swagger
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
    @Operation(summary = "Predecir Churn", description = "Analiza los datos de un cliente y devuelve la probabilidad de que cancele el servicio.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "✅ Predicción exitosa"),
            @ApiResponse(responseCode = "400", description = "❌ Datos inválidos (Revisar antigüedad negativa o campos vacíos)"),
            @ApiResponse(responseCode = "503", description = "⚠️ Servicio de IA no disponible")
    })
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

    // ### EL NUEVO MÉTODO BATCH ###

    @Operation(
            summary = "Carga masiva de predicciones desde CSV",
            description = "Procesa múltiples clientes desde archivo CSV. "
                    + "Formato: tenure,monthlyCharges,totalCharges,contractType,paymentMethod"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Archivo procesado exitosamente",
                    content = @Content(schema = @Schema(implementation = BatchPredictionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Archivo inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PostMapping(
            value = "/batch",  // Nueva ruta: /api/v1/predict/batch
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BatchPredictionResponse> predictBatch(
            @Parameter(
                    description = "Archivo CSV con datos de clientes",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "ID de usuario para auditoría", required = false)
            @RequestParam(value = "userId", required = false) Long userId) {

        log.info("📤 Batch upload recibido: {} ({} bytes)",
                file.getOriginalFilename(), file.getSize());

        // ===== VALIDACIONES BÁSICAS =====
        // 1. Archivo no vacío
        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(BatchPredictionResponse.error("El archivo está vacío"));
        }

        // 2. Solo CSV
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".csv")) {
            return ResponseEntity.badRequest()
                    .body(BatchPredictionResponse.error("Solo se aceptan archivos .csv"));
        }

        // 3. Tamaño máximo 10MB (configurable en application.properties)
        if (file.getSize() > 10 * 1024 * 1024) {
            return ResponseEntity.badRequest()
                    .body(BatchPredictionResponse.error("El archivo excede 10MB"));
        }

        try {
            // Delegar el procesamiento al servicio
            BatchPredictionResponse response = predictionService.processBatchFile(file, userId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ Error en batch: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BatchPredictionResponse.error("Error: " + e.getMessage()));
        }
    }

}