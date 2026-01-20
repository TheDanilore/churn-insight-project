package com.churninsight.backend.repository;

import com.churninsight.backend.model.BatchJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BatchJobRepository extends JpaRepository<BatchJob, String>, JpaSpecificationExecutor<BatchJob> {
    
}
