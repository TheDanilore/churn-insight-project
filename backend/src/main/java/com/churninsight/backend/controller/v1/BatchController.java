package com.churninsight.backend.controller.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Parameter;

import com.churninsight.backend.model.BatchJob;
import com.churninsight.backend.model.PredictionHistory;
import com.churninsight.backend.repository.BatchJobRepository;
import com.churninsight.backend.dto.BatchPredictionResponse;
import com.churninsight.backend.dto.BatchJobDTO;
import com.churninsight.backend.service.BatchService;
import com.churninsight.backend.service.QueueService;
import com.churninsight.backend.specification.BatchJobSpecifications;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/predictions") // Buena práctica: Incluye el RECURSO en la base, no solo la versión
@Tag(name = "Batch Jobs", description = "Endpoints para gestionar trabajos por lotes") // Título en Swagger
public class BatchController {

        private final BatchJobRepository batchJobRepository;
        private static final Logger logger = LoggerFactory.getLogger(PredictionController.class);

        private final QueueService queueService;
        private final BatchService batchService;

        public BatchController(BatchJobRepository batchJobRepository, QueueService queueService,
                        BatchService batchService) {
                this.queueService = queueService;
                this.batchService = batchService;
                this.batchJobRepository = batchJobRepository;
        }

        // Endpoint: /api/v1/predictions/batch/history
        @GetMapping("/batch/history")
        public ResponseEntity<Page<BatchJob>> getHistory(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        // Filtros opcionales
                        @RequestParam(required = false) String status,
                        @RequestParam(required = false) String format,
                        @RequestParam(required = false) LocalDate dateFrom, // Spring convierte string 'yyyy-MM-dd' a
                                                                            // LocalDate automático
                        @RequestParam(required = false) LocalDate dateTo) {
                // 1. Configurar Paginación
                Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

                // 2. Crear Especificación con los filtros
                Specification<BatchJob> spec = BatchJobSpecifications.withFilters(status, format, dateFrom, dateTo);

                // 3. Ejecutar consulta dinámica
                return ResponseEntity.ok(batchJobRepository.findAll(spec, pageable));
        }

        // MÉTODO BATCH

        @Operation(summary = "Carga masiva de predicciones desde CSV o Excel", description = "Procesa múltiples clientes desde archivo CSV o Excel (.xlsx, .xls). "
                        + "Formato esperado: clientName, email, phone (opcionales), antiguedad, contrato, cargosMensuales, soporteTecnico, servicioInternet, metodoPago (requeridos). "
                        + "Descarga un template en /api/v1/predictions/template/csv o /api/v1/predictions/template/excel")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "202", description = "Archivo encolado correctamente"),
                        @ApiResponse(responseCode = "400", description = "Archivo inválido (formato no soportado o vacío)"),
                        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        })
        @PostMapping(value = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<BatchPredictionResponse> predictBatch(
                        @Parameter(description = "Archivo CSV o Excel con datos de clientes", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestParam("file") MultipartFile file,

                        @Parameter(description = "ID de usuario para auditoría", required = false) @RequestParam(value = "userId", required = false) Long userId) {

                logger.info("📤 Batch upload recibido: {} ({} bytes)",
                                file.getOriginalFilename(), file.getSize());

                // ===== VALIDACIONES BÁSICAS =====
                // 1. Archivo no vacío
                if (file.isEmpty()) {
                        return ResponseEntity.badRequest()
                                        .body(BatchPredictionResponse.error("El archivo está vacío"));
                }

                // 2. Validar extensión (CSV, XLSX, XLS)
                String fileName = file.getOriginalFilename();
                if (fileName == null) {
                        return ResponseEntity.badRequest()
                                        .body(BatchPredictionResponse.error("No se especificó nombre de archivo"));
                }

                String lowerFileName = fileName.toLowerCase();
                if (!lowerFileName.endsWith(".csv") && !lowerFileName.endsWith(".xlsx")
                                && !lowerFileName.endsWith(".xls")) {
                        return ResponseEntity.badRequest()
                                        .body(BatchPredictionResponse
                                                        .error("Solo se aceptan archivos .csv, .xlsx o .xls"));
                }

                // 3. Tamaño máximo 10MB (configurable en application.properties)
                if (file.getSize() > 10 * 1024 * 1024) {
                        return ResponseEntity.badRequest()
                                        .body(BatchPredictionResponse.error("El archivo excede 10MB"));
                }

                try {
                        byte[] fileContent = file.getBytes();

                        // 1. Crear el trabajo
                        BatchJobDTO job = BatchJobDTO.create(file.getOriginalFilename(), fileContent, userId);

                        // 2. Encolar
                        queueService.enqueueJob(job);

                        logger.info("📝 Trabajo encolado: {} (ID: {})", fileName, job.getJobId());

                        // 3. CORRECCIÓN: Usar .queued() pasando el jobId
                        return ResponseEntity.accepted()
                                        .body(BatchPredictionResponse.queued(
                                                        job.getJobId(),
                                                        file.getOriginalFilename()));

                } catch (IOException e) {
                        logger.error("❌ Error al leer archivo: {}", e.getMessage(), e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body(BatchPredictionResponse
                                                        .error("Error al leer archivo: " + e.getMessage()));
                }
        }

        /**
         * Obtiene el estado de un trabajo en la cola
         */
        @Operation(summary = "Obtener estado de procesamiento", description = "Consulta el estado de un trabajo en la cola")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Estado obtenido"),
                        @ApiResponse(responseCode = "404", description = "Trabajo no encontrado")
        })
        @GetMapping("/batch/status/{jobId}")
        public ResponseEntity<?> getJobStatus(
                        @Parameter(description = "ID del trabajo", required = true) @PathVariable String jobId) {

                BatchJobDTO job = queueService.getJobStatus(jobId);

                if (job == null) {
                        return ResponseEntity.notFound().build();
                }

                return ResponseEntity.ok(BatchPredictionResponse.fromJob(job));
        }

        /**
         * Obtiene los resultados paginados y filtrados de un lote
         */
        @Operation(summary = "Obtener resultados de un lote (Paginado)")
        @GetMapping("/batch/results/{jobId}")
        public ResponseEntity<Page<PredictionHistory>> getBatchResults(
                        @PathVariable String jobId,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false) String search, // Para buscar por nombre/email
                        @RequestParam(required = false) String alerta // Para filtrar por riesgo (ALTA/MEDIA/BAJA)
        ) {
                Page<PredictionHistory> results = batchService.getBatchResultsPaged(jobId, page, size, search, alerta);
                return ResponseEntity.ok(results);
        }

        @Operation(summary = "Descargar reporte Excel de resultados")
        @GetMapping("/batch/results/{jobId}/export")
        public ResponseEntity<byte[]> exportBatchResults(@PathVariable String jobId) {
                try {
                        byte[] excelContent = batchService.generateExcelReport(jobId);

                        return ResponseEntity.ok()
                                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                                        "attachment; filename=\"reporte_" + jobId + ".xlsx\"")
                                        .header(HttpHeaders.CONTENT_TYPE,
                                                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                                        .body(excelContent);
                } catch (IOException e) {
                        return ResponseEntity.internalServerError().build();
                }
        }
}
