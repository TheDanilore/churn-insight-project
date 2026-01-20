package com.churninsight.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "batch_jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchJob {
    
    @Id
    @Column(name = "job_id")
    private String jobId; // Usamos el UUID como Primary Key

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "status")
    private String status; // PENDING, PROCESSING, COMPLETED, FAILED

    @Column(name = "total_records")
    private Integer totalRecords = 0;

    @Column(name = "processed_records")
    private Integer processedRecords = 0;

    @Column(name = "failed_records")
    private Integer failedRecords = 0;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}