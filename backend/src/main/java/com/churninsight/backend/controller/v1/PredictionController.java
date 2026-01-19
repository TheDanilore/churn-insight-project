package com.churninsight.backend.controller.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Parameter;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;

import com.churninsight.backend.dto.ChurnRequestDTO;
import com.churninsight.backend.dto.ChurnResponseDTO;
import com.churninsight.backend.dto.BatchPredictionResponse;
import com.churninsight.backend.dto.BatchJobDTO;
import com.churninsight.backend.service.PredictionService;
import com.churninsight.backend.service.QueueService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/predictions") // Buena práctica: Incluye el RECURSO en la base, no solo la versión
@Tag(name = "Predicciones", description = "Endpoints para consultar el modelo de IA") // Título en Swagger
public class PredictionController {

        private static final Logger logger = LoggerFactory.getLogger(PredictionController.class);
        private final PredictionService predictionService;
        private final QueueService queueService;

        // Inyección de dependencias por constructor
        public PredictionController(PredictionService predictionService, QueueService queueService) {
                this.predictionService = predictionService;
                this.queueService = queueService;
        }

        // Endpoint limpio: POST /api/v1/predictions (en lugar de /api/v1/predict)
        // En REST, se usan sustantivos (recursos), no verbos en la URL.
        // El verbo HTTP (POST) ya indica la acción "Crear/Predecir".
        @Operation(summary = "Predecir Churn", description = "Analiza los datos de un cliente y devuelve la probabilidad de que cancele el servicio. "
                        + "Campos opcionales: client_name (nombre), email, phone. "
                        + "Campos requeridos: antiguedad (0-72), contrato, cargos_mensuales (18.25-118.75), soporte_tecnico, servicio_internet, metodo_pago")
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

        @Operation(summary = "Carga masiva de predicciones desde CSV o Excel", description = "Procesa múltiples clientes desde archivo CSV o Excel (.xlsx, .xls). "
                        + "Formato esperado: clientName, email, phone (opcionales), antiguedad, contrato, cargosMensuales, soporteTecnico, servicioInternet, metodoPago (requeridos). "
                        + "Descarga un template en /api/v1/predictions/template/csv o /api/v1/predictions/template/excel")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "202", description = "✅ Archivo encolado correctamente"),
                        @ApiResponse(responseCode = "400", description = "❌ Archivo inválido (formato no soportado o vacío)"),
                        @ApiResponse(responseCode = "500", description = "⚠️ Error interno del servidor")
        })
        @PostMapping(value = "/batch",
                        consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
                if (!lowerFileName.endsWith(".csv") && !lowerFileName.endsWith(".xlsx") && !lowerFileName.endsWith(".xls")) {
                        return ResponseEntity.badRequest()
                                        .body(BatchPredictionResponse.error("Solo se aceptan archivos .csv, .xlsx o .xls"));
                }

                // 3. Tamaño máximo 10MB (configurable en application.properties)
                if (file.getSize() > 10 * 1024 * 1024) {
                        return ResponseEntity.badRequest()
                                        .body(BatchPredictionResponse.error("El archivo excede 10MB"));
                }

                try {
                        byte[] fileContent = file.getBytes();

                        // Crear trabajo y encolar en Redis
                        BatchJobDTO job = BatchJobDTO.create(file.getOriginalFilename(), fileContent, userId);
                        queueService.enqueueJob(job);

                        logger.info("📝 Trabajo encolado: {} (ID: {})", fileName, job.getJobId());

                        // Devolver 202 Accepted con el ID del trabajo
                        return ResponseEntity.accepted()
                                        .body(BatchPredictionResponse.success(
                                                        file.getOriginalFilename(),
                                                        0, 0, 0));

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

                return ResponseEntity.ok(job);
        }

        /**
         * Descarga template CSV con el formato correcto para importar
         */
        @Operation(summary = "Descargar template CSV", description = "Descarga un archivo CSV de plantilla con el formato correcto para importar predicciones en lote. "
                        + "Columnas: clientName (nombre), email, phone, antiguedad (meses), contrato, cargosMensuales, soporteTecnico, servicioInternet, metodoPago")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "✅ Template descargado")
        })
        @GetMapping("/template/csv")
        public ResponseEntity<byte[]> downloadCsvTemplate() {
                logger.info("📥 Descargando template CSV");

                // Crear contenido CSV
                StringBuilder csv = new StringBuilder();
                csv.append("clientName,email,phone,antiguedad,contrato,cargosMensuales,soporteTecnico,servicioInternet,metodoPago\n");
                csv.append("Juan Pérez,juan@example.com,+34-555-0101,24,Month-to-month,65.50,Yes,Fiber optic,Electronic check\n");
                csv.append("María García,maria@example.com,+34-555-0102,36,One year,85.25,No,DSL,Bank transfer (automatic)\n");
                csv.append("Carlos López,carlos@example.com,+34-555-0103,12,Two year,45.75,Yes,No,Credit card (automatic)\n");
                csv.append("Ana Martínez,ana@example.com,+34-555-0104,48,Month-to-month,95.00,No,Fiber optic,Mailed check\n");
                csv.append("Pedro Rodríguez,pedro@example.com,+34-555-0105,60,One year,55.25,Yes,DSL,Electronic check\n");

                byte[] content = csv.toString().getBytes();

                return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"template_churn.csv\"")
                                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
                                .body(content);
        }

        /**
         * Descarga template Excel con el formato correcto para importar
         */
        @Operation(summary = "Descargar template Excel", description = "Descarga un archivo Excel (.xlsx) de plantilla con el formato correcto para importar predicciones en lote. "
                        + "Columnas: clientName (nombre), email, phone, antiguedad (meses), contrato, cargosMensuales, soporteTecnico, servicioInternet, metodoPago")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "✅ Template descargado")
        })
        @GetMapping("/template/excel")
        public ResponseEntity<byte[]> downloadExcelTemplate() {
                logger.info("📥 Descargando template Excel");

                try {
                        // Crear workbook
                        Workbook workbook = new XSSFWorkbook();
                        Sheet sheet = workbook.createSheet("Datos");

                        // Crear headers
                        Row headerRow = sheet.createRow(0);
                        String[] headers = {"clientName", "email", "phone", "antiguedad", "contrato", "cargosMensuales", "soporteTecnico", "servicioInternet", "metodoPago"};
                        for (int i = 0; i < headers.length; i++) {
                                headerRow.createCell(i).setCellValue(headers[i]);
                        }

                        // Crear filas de ejemplo
                        String[][] exampleData = {
                                {"Juan Pérez", "juan@example.com", "+34-555-0101", "24", "Month-to-month", "65.50", "Yes", "Fiber optic", "Electronic check"},
                                {"María García", "maria@example.com", "+34-555-0102", "36", "One year", "85.25", "No", "DSL", "Bank transfer (automatic)"},
                                {"Carlos López", "carlos@example.com", "+34-555-0103", "12", "Two year", "45.75", "Yes", "No", "Credit card (automatic)"},
                                {"Ana Martínez", "ana@example.com", "+34-555-0104", "48", "Month-to-month", "95.00", "No", "Fiber optic", "Mailed check"},
                                {"Pedro Rodríguez", "pedro@example.com", "+34-555-0105", "60", "One year", "55.25", "Yes", "DSL", "Electronic check"}
                        };

                        for (int i = 0; i < exampleData.length; i++) {
                                Row row = sheet.createRow(i + 1);
                                for (int j = 0; j < exampleData[i].length; j++) {
                                        row.createCell(j).setCellValue(exampleData[i][j]);
                                }
                        }

                        // Auto-ajustar columnas
                        for (int i = 0; i < headers.length; i++) {
                                sheet.autoSizeColumn(i);
                        }

                        // Convertir a byte array
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        workbook.write(bos);
                        workbook.close();

                        byte[] content = bos.toByteArray();

                        return ResponseEntity.ok()
                                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"template_churn.xlsx\"")
                                        .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                                        .body(content);

                } catch (Exception e) {
                        logger.error("❌ Error generando template Excel: {}", e.getMessage(), e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
        }

}