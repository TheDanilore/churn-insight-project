package com.churninsight.repository;

import com.churninsight.entity.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredictionRepository extends JpaRepository<Prediction, Long> {
    long countByPrediction(String prediction);
    long count();
}
