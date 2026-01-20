package com.churninsight.backend.service;

import com.churninsight.backend.dto.BatchJobDTO;
import com.churninsight.backend.model.BatchJob;
import com.churninsight.backend.repository.BatchJobRepository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class QueueService {

    private static final String BATCH_QUEUE_KEY = "batch:queue";
    private static final String JOB_STATUS_KEY = "job:status:";
    private static final long EXPIRY_TIME = 24; // horas

    private final RedisTemplate<String, BatchJobDTO> redisTemplate;
    private final BatchJobRepository batchJobRepository;

    public QueueService(RedisTemplate<String, BatchJobDTO> redisTemplate, BatchJobRepository batchJobRepository) {
        this.redisTemplate = redisTemplate;
        this.batchJobRepository = batchJobRepository;
    }

    public void enqueueJob(BatchJobDTO job) {
        // 1. Redis (Memoria rápida para el worker)
        redisTemplate.opsForList().rightPush(BATCH_QUEUE_KEY, job);

        redisTemplate.opsForValue().set(
                JOB_STATUS_KEY + job.getJobId(),
                job,
                EXPIRY_TIME,
                TimeUnit.HOURS);

        // 2. PostgreSQL (Historial persistente)
        BatchJob entity = new BatchJob();
        entity.setJobId(job.getJobId());
        entity.setFileName(job.getFileName());
        entity.setStatus("PENDING");
        entity.setCreatedAt(LocalDateTime.now());
        batchJobRepository.save(entity);

        System.out.println("Trabajo encolado y guardado en historial: " + job.getJobId());
    }

    public BatchJobDTO dequeueJob() {
        return redisTemplate.opsForList().leftPop(BATCH_QUEUE_KEY);
    }

    /**
     * Prioriza la lectura desde PostgreSQL para asegurar datos persistentes
     * y la fecha de finalización correcta.
     */
    public BatchJobDTO getJobStatus(String jobId) {
        // 1. Intentar buscar en Base de Datos (La fuente de la verdad)
        Optional<BatchJob> dbJobOpt = batchJobRepository.findById(jobId);

        if (dbJobOpt.isPresent()) {
            BatchJob entity = dbJobOpt.get();

            // Mapeo manual de Entidad -> DTO (O usa MapStruct si lo tienes)
            return BatchJobDTO.builder()
                    .jobId(entity.getJobId())
                    .fileName(entity.getFileName())
                    .status(entity.getStatus())
                    .totalRecords(entity.getTotalRecords())
                    .processedRecords(entity.getProcessedRecords())
                    .failedRecords(entity.getFailedRecords())
                    .createdAt(entity.getCreatedAt())
                    // Aquí obtenemos la fecha real de finalización guardada en DB
                    .completedAt(entity.getCompletedAt())
                    .errorMessage(entity.getErrorMessage())
                    .build();
        }

        // 2. Fallback: Si no está en DB (muy raro, quizás latencia), buscar en Redis
        return redisTemplate.opsForValue().get(JOB_STATUS_KEY + jobId);
    }

    public void updateJobStatus(BatchJobDTO job) {
        redisTemplate.opsForValue().set(
                JOB_STATUS_KEY + job.getJobId(),
                job,
                EXPIRY_TIME,
                TimeUnit.HOURS);
    }

    /**
     * Actualiza el historial en PostgreSQL
     */
    public void updateJobInDatabase(BatchJobDTO jobDto) {
        batchJobRepository.findById(jobDto.getJobId()).ifPresent(entity -> {
            entity.setStatus(jobDto.getStatus());
            entity.setTotalRecords(jobDto.getTotalRecords());
            entity.setProcessedRecords(jobDto.getProcessedRecords());
            entity.setFailedRecords(jobDto.getFailedRecords());
            entity.setErrorMessage(jobDto.getErrorMessage());

            if ("COMPLETED".equals(jobDto.getStatus()) || "FAILED".equals(jobDto.getStatus())) {
                entity.setCompletedAt(LocalDateTime.now());
            }
            batchJobRepository.save(entity);
        });
    }

    public Long getQueueSize() {
        return redisTemplate.opsForList().size(BATCH_QUEUE_KEY);
    }
}