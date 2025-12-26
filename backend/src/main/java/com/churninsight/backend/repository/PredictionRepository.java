package com.churninsight.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.churninsight.backend.model.PredictionHistory;

@Repository
public interface PredictionRepository extends JpaRepository<PredictionHistory, Long> {
    // Se puede agregar métodos en el futuro, ej:
    // List<PredictionHistory> findByAlerta(String alerta);
}