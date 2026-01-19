package com.churninsight.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchJobDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String jobId;           // UUID único del trabajo
    private String fileName;        // Nombre del archivo
    private byte[] fileContent;     // Contenido del archivo
    private Long userId;            // Usuario que lo envió
    private String status;          // PENDING, PROCESSING, COMPLETED, FAILED
    private Integer totalRecords;   // Total de registros a procesar
    private Integer processedRecords;
    private Integer failedRecords;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String errorMessage;    // Si falla

    public static BatchJobDTO create(String fileName, byte[] fileContent, Long userId) {
        BatchJobDTO job = new BatchJobDTO();
        job.setJobId(java.util.UUID.randomUUID().toString());
        job.setFileName(fileName);
        job.setFileContent(fileContent);
        job.setUserId(userId);
        job.setStatus("PENDING");
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        return job;
    }

    public void markAsProcessing() {
        this.status = "PROCESSING";
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsCompleted(int total, int processed, int failed) {
        this.status = "COMPLETED";
        this.totalRecords = total;
        this.processedRecords = processed;
        this.failedRecords = failed;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsFailed(String error) {
        this.status = "FAILED";
        this.errorMessage = error;
        this.updatedAt = LocalDateTime.now();
    }
}
