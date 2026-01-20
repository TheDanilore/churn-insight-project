package com.churninsight.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.churninsight.backend.model.PredictionHistory;

public interface PredictionHistoryRepository extends JpaRepository<PredictionHistory, Long>, JpaSpecificationExecutor<PredictionHistory> {
    // No necesitas definir métodos extra, findAll(Spec, Pageable) ya viene incluido

        // Spring Boot leerá esto y creará automáticamente: "SELECT * FROM prediction_history WHERE job_id = ?"
    List<PredictionHistory> findByJobId(String jobId);
}