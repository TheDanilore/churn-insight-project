package com.churninsight.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder; 
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder 
@NoArgsConstructor
@AllArgsConstructor
public class BatchJobDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String jobId;
    private String fileName;
    private byte[] fileContent;
    private Long userId;
    private String status;
    private Integer totalRecords;
    private Integer processedRecords;
    private Integer failedRecords;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt; 
    
    private String errorMessage;

    public static BatchJobDTO create(String fileName, byte[] fileContent, Long userId) {
        BatchJobDTO job = new BatchJobDTO();
        job.setJobId(UUID.randomUUID().toString());
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
        
        LocalDateTime now = LocalDateTime.now();
        this.updatedAt = now;
        this.completedAt = now; // Guardamos la fecha de fin
    }

    public void markAsFailed(String error) {
        this.status = "FAILED";
        this.errorMessage = error;
        
        LocalDateTime now = LocalDateTime.now();
        this.updatedAt = now;
        this.completedAt = now; // Un fallo también es una finalización
    }
}