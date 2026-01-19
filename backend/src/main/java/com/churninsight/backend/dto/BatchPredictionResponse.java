package com.churninsight.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchPredictionResponse {
    private boolean success;
    private String message;
    private String fileName;
    private Integer totalRecords;
    private Integer processedRecords;
    private Integer failedRecords;
    private LocalDateTime processedAt;

    // Constructor para éxito
    public static BatchPredictionResponse success(String fileName, Integer total,
                                                  Integer processed, Integer failed) {
        BatchPredictionResponse response = new BatchPredictionResponse();
        response.setSuccess(true);
        response.setMessage("Archivo procesado exitosamente");
        response.setFileName(fileName);
        response.setTotalRecords(total);
        response.setProcessedRecords(processed);
        response.setFailedRecords(failed);
        response.setProcessedAt(LocalDateTime.now());
        return response;
    }

    // Constructor para error
    public static BatchPredictionResponse error(String errorMessage) {
        BatchPredictionResponse response = new BatchPredictionResponse();
        response.setSuccess(false);
        response.setMessage(errorMessage);
        response.setProcessedAt(LocalDateTime.now());
        return response;
    }
}