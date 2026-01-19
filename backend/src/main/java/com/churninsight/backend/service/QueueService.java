package com.churninsight.backend.service;

import com.churninsight.backend.dto.BatchJobDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class QueueService {

    private static final String BATCH_QUEUE_KEY = "batch:queue";
    private static final String JOB_STATUS_KEY = "job:status:";
    private static final long EXPIRY_TIME = 24; // horas

    private final RedisTemplate<String, BatchJobDTO> redisTemplate;

    public QueueService(RedisTemplate<String, BatchJobDTO> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Encola un trabajo en Redis (como Laravel Queue)
     */
    public void enqueueJob(BatchJobDTO job) {
        // Guardar el trabajo en una cola (lista FIFO)
        redisTemplate.opsForList().rightPush(BATCH_QUEUE_KEY, job);
        
        // Guardar estado en hash con expiración
        redisTemplate.opsForValue().set(
            JOB_STATUS_KEY + job.getJobId(), 
            job, 
            EXPIRY_TIME, 
            TimeUnit.HOURS
        );
        
        System.out.println("✅ Trabajo encolado: " + job.getJobId() + " - " + job.getFileName());
    }

    /**
     * Obtiene el siguiente trabajo de la cola
     */
    public BatchJobDTO dequeueJob() {
        return redisTemplate.opsForList().leftPop(BATCH_QUEUE_KEY);
    }

    /**
     * Obtiene el estado de un trabajo por ID
     */
    public BatchJobDTO getJobStatus(String jobId) {
        return redisTemplate.opsForValue().get(JOB_STATUS_KEY + jobId);
    }

    /**
     * Actualiza el estado de un trabajo
     */
    public void updateJobStatus(BatchJobDTO job) {
        redisTemplate.opsForValue().set(
            JOB_STATUS_KEY + job.getJobId(), 
            job, 
            EXPIRY_TIME, 
            TimeUnit.HOURS
        );
    }

    /**
     * Obtiene el tamaño de la cola
     */
    public Long getQueueSize() {
        return redisTemplate.opsForList().size(BATCH_QUEUE_KEY);
    }
}
