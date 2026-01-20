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
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

        /**
         * Descarga template CSV con 10 filas de ejemplo variadas
         */
        /**
         * Descarga template CSV con 20 filas de ejemplo variadas y validadas
         */
        @Operation(summary = "Descargar template CSV", description = "Descarga plantilla CSV con 20 casos de prueba variados para estrés y validación.")
        @GetMapping("/template/csv")
        public ResponseEntity<byte[]> downloadCsvTemplate() {
                logger.info("📥 Descargando template CSV");

                StringBuilder csv = new StringBuilder();
                // Header
                csv.append("clientName,email,phone,antiguedad,contrato,cargosMensuales,soporteTecnico,servicioInternet,metodoPago\n");

                // DATOS DE EJEMPLO (20 Filas: Alta, Media, Baja Probabilidad y Casos Borde)

                // 1-5: Alto Riesgo (Clientes nuevos, fibra, pago manual)
                csv.append("Juan Pérez,juan@test.com,+34-555-001,1,Month-to-month,70.35,No,Fiber optic,Electronic check\n");
                csv.append("Lucía Fernández,lucia@test.com,+34-555-002,2,Month-to-month,99.90,No,Fiber optic,Electronic check\n");
                csv.append("Jorge Hernández,jorge@test.com,+34-555-003,3,Month-to-month,75.00,No,Fiber optic,Electronic check\n");
                csv.append("Marta Gomez,marta@test.com,+34-555-004,5,Month-to-month,85.50,No,Fiber optic,Mailed check\n");
                csv.append("Roberto Diaz,roberto@test.com,+34-555-005,1,Month-to-month,69.90,No,Fiber optic,Electronic check\n");

                // 6-10: Bajo Riesgo (Clientes antiguos, contratos largos, automáticos)
                csv.append("María García,maria@test.com,+34-555-006,72,Two year,25.50,No internet service,No,Credit card (automatic)\n");
                csv.append("Pedro Rodríguez,pedro@test.com,+34-555-007,60,Two year,60.00,Yes,DSL,Bank transfer (automatic)\n");
                csv.append("Sofía Ramírez,sofia@test.com,+34-555-008,65,Two year,45.00,Yes,DSL,Credit card (automatic)\n");
                csv.append("Antonio Ruiz,antonio@test.com,+34-555-009,50,One year,55.00,Yes,DSL,Credit card (automatic)\n");
                csv.append("Isabel Torres,isabel@test.com,+34-555-010,48,Two year,20.00,No internet service,No,Bank transfer (automatic)\n");

                // 11-15: Riesgo Medio / Casos Mixtos
                csv.append("Carlos López,carlos@test.com,+34-555-011,12,One year,98.50,No,Fiber optic,Bank transfer (automatic)\n");
                csv.append("Ana Martínez,ana@test.com,+34-555-012,24,One year,80.20,Yes,Fiber optic,Mailed check\n");
                csv.append("Miguel Torres,miguel@test.com,+34-555-013,18,Month-to-month,70.00,No,DSL,Electronic check\n");
                csv.append("Elena Díaz,elena@test.com,+34-555-014,10,One year,50.50,Yes,DSL,Mailed check\n");
                csv.append("Javier Castro,javier@test.com,+34-555-015,15,Month-to-month,65.00,Yes,DSL,Electronic check\n");

                // 16-20: Casos Borde (Sin internet, Antigüedad 0, Cargos mínimos/máximos)
                csv.append("Laura Gil,laura@test.com,+34-555-016,0,Month-to-month,19.00,No internet service,No,Mailed check\n"); // Nuevo
                                                                                                                                 // cliente
                csv.append("Ricardo Morales,ricardo@test.com,+34-555-017,70,Two year,118.00,Yes,Fiber optic,Bank transfer (automatic)\n"); // VIP
                csv.append("Carmen Vega,carmen@test.com,+34-555-018,22,Month-to-month,45.00,No,DSL,Electronic check\n");
                csv.append("Luis Navarro,luis@test.com,+34-555-019,30,One year,90.00,No,Fiber optic,Credit card (automatic)\n");
                csv.append("Patricia Soler,patricia@test.com,+34-555-020,40,Two year,25.00,No internet service,No,Mailed check\n");

                byte[] content = csv.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);

                return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"template_churn.csv\"")
                                .header(HttpHeaders.CONTENT_TYPE, "text/csv;charset=UTF-8")
                                .body(content);
        }

        /**
         * Descarga template Excel con 20 filas de ejemplo variadas y validadas
         */
        @Operation(summary = "Descargar template Excel", description = "Descarga plantilla Excel con 20 casos de prueba variados.")
        @GetMapping("/template/excel")
        public ResponseEntity<byte[]> downloadExcelTemplate() {
                logger.info("📥 Descargando template Excel");

                try (Workbook workbook = new XSSFWorkbook()) {
                        Sheet sheet = workbook.createSheet("Datos Clientes");

                        // ESTILOS
                        CellStyle headerStyle = workbook.createCellStyle();
                        Font headerFont = workbook.createFont();
                        headerFont.setBold(true);
                        headerStyle.setFont(headerFont);

                        // HEADER
                        Row headerRow = sheet.createRow(0);
                        String[] headers = { "clientName", "email", "phone", "antiguedad", "contrato",
                                        "cargosMensuales", "soporteTecnico", "servicioInternet", "metodoPago" };

                        for (int i = 0; i < headers.length; i++) {
                                Cell cell = headerRow.createCell(i);
                                cell.setCellValue(headers[i]);
                                cell.setCellStyle(headerStyle);
                        }

                        // DATOS DE EJEMPLO (20 Filas estrictamente validadas)
                        // soporteTecnico: Yes, No, No internet service
                        // servicioInternet: DSL, Fiber optic, No
                        String[][] exampleData = {
                                        // 1-5: Alto Riesgo
                                        { "Juan Pérez", "juan@test.com", "555-001", "1", "Month-to-month", "70.35",
                                                        "No", "Fiber optic", "Electronic check" },
                                        { "Lucía Fernández", "lucia@test.com", "555-002", "2", "Month-to-month",
                                                        "99.90", "No", "Fiber optic", "Electronic check" },
                                        { "Jorge Hernández", "jorge@test.com", "555-003", "3", "Month-to-month",
                                                        "75.00", "No", "Fiber optic", "Electronic check" },
                                        { "Marta Gomez", "marta@test.com", "555-004", "5", "Month-to-month", "85.50",
                                                        "No", "Fiber optic", "Mailed check" },
                                        { "Roberto Diaz", "roberto@test.com", "555-005", "1", "Month-to-month", "69.90",
                                                        "No", "Fiber optic", "Electronic check" },

                                        // 6-10: Bajo Riesgo
                                        { "María García", "maria@test.com", "555-006", "72", "Two year", "25.50",
                                                        "No internet service", "No", "Credit card (automatic)" },
                                        { "Pedro Rodríguez", "pedro@test.com", "555-007", "60", "Two year", "60.00",
                                                        "Yes", "DSL", "Bank transfer (automatic)" },
                                        { "Sofía Ramírez", "sofia@test.com", "555-008", "65", "Two year", "45.00",
                                                        "Yes", "DSL", "Credit card (automatic)" },
                                        { "Antonio Ruiz", "antonio@test.com", "555-009", "50", "One year", "55.00",
                                                        "Yes", "DSL", "Credit card (automatic)" },
                                        { "Isabel Torres", "isabel@test.com", "555-010", "48", "Two year", "20.00",
                                                        "No internet service", "No", "Bank transfer (automatic)" },

                                        // 11-15: Riesgo Medio / Mixto
                                        { "Carlos López", "carlos@test.com", "555-011", "12", "One year", "98.50", "No",
                                                        "Fiber optic", "Bank transfer (automatic)" },
                                        { "Ana Martínez", "ana@test.com", "555-012", "24", "One year", "80.20", "Yes",
                                                        "Fiber optic", "Mailed check" },
                                        { "Miguel Torres", "miguel@test.com", "555-013", "18", "Month-to-month",
                                                        "70.00", "No", "DSL", "Electronic check" },
                                        { "Elena Díaz", "elena@test.com", "555-014", "10", "One year", "50.50", "Yes",
                                                        "DSL", "Mailed check" },
                                        { "Javier Castro", "javier@test.com", "555-015", "15", "Month-to-month",
                                                        "65.00", "Yes", "DSL", "Electronic check" },

                                        // 16-20: Casos Borde
                                        { "Laura Gil", "laura@test.com", "555-016", "0", "Month-to-month", "19.00",
                                                        "No internet service", "No", "Mailed check" },
                                        { "Ricardo Morales", "ricardo@test.com", "555-017", "70", "Two year", "118.00",
                                                        "Yes", "Fiber optic", "Bank transfer (automatic)" },
                                        { "Carmen Vega", "carmen@test.com", "555-018", "22", "Month-to-month", "45.00",
                                                        "No", "DSL", "Electronic check" },
                                        { "Luis Navarro", "luis@test.com", "555-019", "30", "One year", "90.00", "No",
                                                        "Fiber optic", "Credit card (automatic)" },
                                        { "Patricia Soler", "patricia@test.com", "555-020", "40", "Two year", "25.00",
                                                        "No internet service", "No", "Mailed check" }
                        };

                        // Llenar filas
                        for (int i = 0; i < exampleData.length; i++) {
                                Row row = sheet.createRow(i + 1);
                                for (int j = 0; j < exampleData[i].length; j++) {
                                        // Convertir números para columnas específicas (3: Antigüedad, 5: Cargos)
                                        if (j == 3 || j == 5) {
                                                try {
                                                        double val = Double.parseDouble(exampleData[i][j]);
                                                        row.createCell(j).setCellValue(val);
                                                } catch (NumberFormatException e) {
                                                        row.createCell(j).setCellValue(exampleData[i][j]);
                                                }
                                        } else {
                                                row.createCell(j).setCellValue(exampleData[i][j]);
                                        }
                                }
                        }

                        // Auto-ajustar columnas
                        for (int i = 0; i < headers.length; i++) {
                                sheet.autoSizeColumn(i);
                        }

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        workbook.write(bos);

                        return ResponseEntity.ok()
                                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                                        "attachment; filename=\"template_churn.xlsx\"")
                                        .header(HttpHeaders.CONTENT_TYPE,
                                                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                                        .body(bos.toByteArray());

                } catch (IOException e) {
                        logger.error("❌ Error generando template Excel: {}", e.getMessage(), e);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
        }

}