package com.churninsight.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchPredictionResponse {
    private String jobId;
    private String status;
    private boolean success;
    private String message;
    private String fileName;
    
    // Contadores
    private Integer totalRecords;
    private Integer processedRecords;
    private Integer failedRecords;
    
    // Fechas
    private LocalDateTime processedAt; // Actúa como "Última actualización" o "Created At"
    private LocalDateTime completedAt; // La fecha de finalización real

    /**
     * Factory method para cuando se ENCOLA el trabajo (Respuesta inicial 202)
     */
    public static BatchPredictionResponse queued(String jobId, String fileName) {
        BatchPredictionResponse response = new BatchPredictionResponse();
        response.setJobId(jobId);
        response.setStatus("PENDING");
        response.setSuccess(true);
        response.setMessage("Archivo recibido y encolado para procesamiento.");
        response.setFileName(fileName);
        
        response.setTotalRecords(0);
        response.setProcessedRecords(0);
        response.setFailedRecords(0);
        
        response.setProcessedAt(LocalDateTime.now()); // Fecha de creación
        response.setCompletedAt(null); // Aún no termina
        
        return response;
    }

    /**
     * Factory method para errores inmediatos (ej: archivo vacío)
     */
    public static BatchPredictionResponse error(String errorMessage) {
        BatchPredictionResponse response = new BatchPredictionResponse();
        response.setSuccess(false);
        response.setMessage(errorMessage);
        response.setProcessedAt(LocalDateTime.now());
        return response;
    }

    /**
     * Convierte el Job (DTO interno/DB) en una Respuesta HTTP ligera.
     */
    public static BatchPredictionResponse fromJob(BatchJobDTO job) {
        BatchPredictionResponse response = new BatchPredictionResponse();

        response.setJobId(job.getJobId());
        response.setStatus(job.getStatus());
        response.setFileName(job.getFileName());
        
        // Protección contra nulos (Null Safety)
        response.setTotalRecords(job.getTotalRecords() != null ? job.getTotalRecords() : 0);
        response.setProcessedRecords(job.getProcessedRecords() != null ? job.getProcessedRecords() : 0);
        response.setFailedRecords(job.getFailedRecords() != null ? job.getFailedRecords() : 0);
        
        // Fechas
        response.setProcessedAt(job.getUpdatedAt()); // Último heartbeat / actualización
        
        // ✅ CORRECCIÓN CRÍTICA: Asignar la fecha de completado
        response.setCompletedAt(job.getCompletedAt()); 

        // Lógica visual para el frontend
        if ("COMPLETED".equals(job.getStatus())) {
            response.setSuccess(true);
            response.setMessage("Procesamiento completado.");
        } else if ("FAILED".equals(job.getStatus())) {
            response.setSuccess(false);
            response.setMessage("Error: " + (job.getErrorMessage() != null ? job.getErrorMessage() : "Error desconocido"));
        } else {
            // PENDING o PROCESSING
            response.setSuccess(true);
            response.setMessage("Procesando... " + response.getProcessedRecords() + " / " + response.getTotalRecords());
        }

        return response;
    }
}