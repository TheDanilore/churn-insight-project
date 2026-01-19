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

    public JobConsumerService(QueueService queueService, PredictionService predictionService, FileParserService fileParserService) {
        this.queueService = queueService;
        this.predictionService = predictionService;
        this.fileParserService = fileParserService;
    }

    /**
     * Procesa trabajos de la cola cada 5 segundos
     * Similar a un Laravel Queue Worker
     */
    @Scheduled(fixedDelay = 5000) // 5 segundos
    public void processJobs() {
        BatchJobDTO job = queueService.dequeueJob();

        if (job == null) {
            // Cola vacía, no hacer nada
            return;
        }

        try {
            logger.info("🔄 Procesando trabajo: {} (ID: {})", job.getFileName(), job.getJobId());
            job.markAsProcessing();
            queueService.updateJobStatus(job);

            // Procesar el archivo (CSV o Excel)
            int totalRecords = 0;
            int processedRecords = 0;
            int failedRecords = 0;

            // Parsear el archivo según su formato
            List<String[]> rows = fileParserService.parseFile(job.getFileContent(), job.getFileName());

            for (int i = 0; i < rows.size(); i++) {
                String[] values = rows.get(i);

                // Saltar encabezado (primera fila)
                if (i == 0) {
                    continue;
                }

                totalRecords++;

                try {
                    // Validar que tenga al menos 9 columnas (3 info + 6 predicción)
                    if (values.length < 9) {
                        logger.warn("⚠️ Fila {} tiene menos de 9 columnas (encontradas: {})", i + 1, values.length);
                        failedRecords++;
                        continue;
                    }

                    // Crear request DTO - las primeras 3 columnas son info del cliente (opcional)
                    // Índices: 0=clientName, 1=email, 2=phone, 3=antiguedad, 4=contrato, 5=cargosMensuales, 6=soporteTecnico, 7=servicioInternet, 8=metodoPago
                    ChurnRequestDTO request = ChurnRequestDTO.of(
                            Integer.parseInt(values[3].trim()),           // antiguedad
                            values[4].trim(),                            // contrato
                            Double.parseDouble(values[5].trim()),        // cargosMensuales
                            values[6].trim(),                            // soporteTecnico
                            values[7].trim(),                            // servicioInternet
                            values[8].trim()                             // metodoPago
                    );

                    // Obtener predicción
                    ChurnResponseDTO response = predictionService.obtenerPrediccion(request);
                    processedRecords++;

                } catch (Exception e) {
                    logger.error("❌ Error procesando fila {} en trabajo {}: {}", i + 1, job.getJobId(), e.getMessage());
                    failedRecords++;
                }
            }

            // Marcar como completado
            job.markAsCompleted(totalRecords, processedRecords, failedRecords);
            queueService.updateJobStatus(job);

            logger.info("✅ Trabajo completado: {} - Total: {}, Procesados: {}, Fallidos: {}",
                    job.getJobId(), totalRecords, processedRecords, failedRecords);

        } catch (Exception e) {
            logger.error("❌ Error procesando trabajo {}: {}", job.getJobId(), e.getMessage(), e);
            job.markAsFailed(e.getMessage());
            queueService.updateJobStatus(job);
        }
    }
}
