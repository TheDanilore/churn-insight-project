package com.churninsight.backend.service;

import com.churninsight.backend.dto.BatchJobDTO;
import com.churninsight.backend.dto.ChurnRequestDTO;
import com.churninsight.backend.dto.ChurnResponseDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class JobConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(JobConsumerService.class);

    private final QueueService queueService;
    private final PredictionService predictionService;
    private final FileParserService fileParserService;
    private final BatchService batchService;

    public JobConsumerService(QueueService queueService, PredictionService predictionService,
            FileParserService fileParserService, BatchService batchService) {
        this.queueService = queueService;
        this.predictionService = predictionService;
        this.fileParserService = fileParserService;
        this.batchService = batchService;
    }

    /**
     * Procesa trabajos de la cola cada 5 segundos
     * Similar a un Laravel Queue Worker
     */
    @Scheduled(fixedDelay = 5000) // Cada 5 segundos
    public void processJobs() {
        BatchJobDTO job = queueService.dequeueJob();

        if (job == null)
            return;

        try {
            logger.info("🔄 Procesando trabajo: {} (ID: {})", job.getFileName(), job.getJobId());
            job.markAsProcessing();
            queueService.updateJobStatus(job);

            // Parsear el archivo
            List<String[]> rows = fileParserService.parseFile(job.getFileContent(), job.getFileName());

            // LISTAS PARA ACUMULAR RESULTADOS (Para guardar en BD después)
            List<ChurnRequestDTO> batchInputs = new java.util.ArrayList<>();
            List<ChurnResponseDTO> batchOutputs = new java.util.ArrayList<>();

            int totalRecords = 0;
            int processedRecords = 0;
            int failedRecords = 0;

            for (int i = 0; i < rows.size(); i++) {
                String[] values = rows.get(i);

                if (i == 0)
                    continue; // Saltar encabezado

                totalRecords++;

                try {
                    // Validar columnas
                    if (values.length < 9) {
                        failedRecords++;
                        continue;
                    }

                    // 1. EXTRAER DATOS (Incluyendo info del cliente: Indices 0, 1, 2)
                    String clientName = values[0].trim();
                    String email = values[1].trim();
                    String phone = values[2].trim();

                    Integer antiguedad = Integer.parseInt(values[3].trim());
                    String contrato = values[4].trim();
                    Double cargos = Double.parseDouble(values[5].trim());
                    String soporte = values[6].trim();
                    String internet = values[7].trim();
                    String pago = values[8].trim();

                    // 2. CREAR DTO COMPLETO (Usamos el método 'of' que incluye datos personales)
                    ChurnRequestDTO request = ChurnRequestDTO.of(
                            clientName, email, phone,
                            antiguedad, contrato, cargos, soporte, internet, pago);

                    // 3. OBTENER PREDICCIÓN
                    ChurnResponseDTO response = predictionService.calcularPrediccion(request);

                    // 4. GUARDAR EN MEMORIA TEMPORAL
                    batchInputs.add(request);
                    batchOutputs.add(response);

                    processedRecords++;

                } catch (Exception e) {
                    logger.error("❌ Error fila {}: {}", i + 1, e.getMessage());
                    failedRecords++;
                }
            }

            // 5. GUARDADO MASIVO EN LA BASE DE DATOS
            if (!batchInputs.isEmpty()) {
                // Aquí usamos el JobID para etiquetar estos registros
                batchService.saveBatchResults(batchInputs, batchOutputs, job.getJobId());
            }

            // Finalizar trabajo
            job.markAsCompleted(totalRecords, processedRecords, failedRecords);
            queueService.updateJobStatus(job);

            queueService.updateJobStatus(job); // Actualiza Redis
            queueService.updateJobInDatabase(job); // Actualiza PostgreSQL (Historial)

            logger.info("Trabajo completado: {}", job.getJobId());

        } catch (Exception e) {
            logger.error("Error fatal job {}: {}", job.getJobId(), e.getMessage());
            job.markAsFailed(e.getMessage());
            queueService.updateJobStatus(job);
        }
    }
}
