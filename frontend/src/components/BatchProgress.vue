<script setup>
import { computed } from 'vue'

// --- PROPS ---
const props = defineProps({
  jobId: String,
  fileName: String,
  jobStatus: Object,
})

// --- COMPUTED ---
const isCompleted = computed(() => props.jobStatus?.status === 'COMPLETED')
const isFailed = computed(() => props.jobStatus?.status === 'FAILED')
const isProcessing = computed(() => props.jobStatus?.status === 'PROCESSING')

const progress = computed(() => {
  if (!props.jobStatus?.totalRecords) return 0
  return Math.round((props.jobStatus?.processedRecords / props.jobStatus?.totalRecords) * 100)
})

const statusColor = computed(() => {
  if (isCompleted.value) return '#48bb78'
  if (isFailed.value) return '#f56565'
  return '#667eea'
})

const statusIcon = computed(() => {
  if (isCompleted.value) return '✅'
  if (isFailed.value) return '❌'
  return '⏳'
})

const statusText = computed(() => {
  if (isCompleted.value) return 'Completado'
  if (isFailed.value) return 'Error'
  return 'Procesando'
})
</script>

<template>
  <div class="progress-section">
    <!-- INFORMACIÓN DEL TRABAJO -->
    <div class="job-info">
      <div class="info-header">
        <h2>{{ statusIcon }} {{ statusText }}</h2>
        <small>ID: {{ jobId }}</small>
      </div>
      <p class="file-name">Archivo: <strong>{{ fileName }}</strong></p>
    </div>

    <!-- BARRA DE PROGRESO -->
    <div class="progress-container">
      <div class="progress-label">
        <span>Progreso</span>
        <span>{{ progress }}%</span>
      </div>
      <div class="progress-bar">
        <div class="progress-fill" :style="{ width: progress + '%', backgroundColor: statusColor }"></div>
      </div>
      <div class="progress-text">
        {{ jobStatus?.processedRecords ?? 0 }} / {{ jobStatus?.totalRecords ?? 0 }} registros procesados
      </div>
    </div>

    <!-- ESTADÍSTICAS -->
    <div v-if="jobStatus" class="stats-grid">
      <div class="stat-card">
        <span class="stat-icon">📋</span>
        <div class="stat-content">
          <small>Total de Registros</small>
          <strong>{{ jobStatus.totalRecords }}</strong>
        </div>
      </div>

      <div class="stat-card success">
        <span class="stat-icon">✅</span>
        <div class="stat-content">
          <small>Procesados</small>
          <strong>{{ jobStatus.processedRecords }}</strong>
        </div>
      </div>

      <div class="stat-card error" v-if="jobStatus.failedRecords > 0">
        <span class="stat-icon">❌</span>
        <div class="stat-content">
          <small>Fallidos</small>
          <strong>{{ jobStatus.failedRecords }}</strong>
        </div>
      </div>
    </div>

    <!-- MENSAJE DE ERROR -->
    <div v-if="isFailed" class="error-message">
      <h3>Error durante el procesamiento</h3>
      <p>{{ jobStatus?.errorMessage }}</p>
    </div>

    <!-- ANIMACIÓN DE CARGA -->
    <div v-if="isProcessing" class="loading-animation">
      <div class="spinner"></div>
      <p>Procesando tu archivo...</p>
    </div>
  </div>
</template>

<style scoped>
.progress-section {
  background: white;
  border-radius: 16px;
  padding: 40px;
  box-shadow: var(--shadow-md);
  animation: fadeIn 0.4s ease;
}

.job-info {
  margin-bottom: 40px;
  padding-bottom: 30px;
  border-bottom: 2px solid var(--border-color);
}

.info-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 12px;
}

.info-header h2 {
  margin: 0;
  font-size: 1.8rem;
  color: var(--text-primary);
}

.info-header small {
  color: var(--text-secondary);
  font-family: 'Monaco', monospace;
  background: var(--bg-light);
  padding: 4px 8px;
  border-radius: 4px;
}

.file-name {
  margin: 0;
  color: var(--text-secondary);
  word-break: break-all;
}

.file-name strong {
  color: var(--primary-color);
}

/* BARRA DE PROGRESO */
.progress-container {
  margin-bottom: 40px;
}

.progress-label {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  font-weight: 600;
  color: var(--text-primary);
}

.progress-bar {
  width: 100%;
  height: 8px;
  background: var(--bg-light);
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 8px;
}
[data-theme="dark"] .progress-bar {
  background: var(--bg-lighter);
}

.progress-fill {
  height: 100%;
  transition: width 0.3s ease;
  border-radius: 4px;
  background: linear-gradient(90deg, var(--secondary-color), var(--primary-color));
}

.progress-text {
  font-size: 0.9rem;
  color: var(--text-secondary);
  text-align: center;
}

/* ESTADÍSTICAS */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 40px;
}

.stat-card {
  background: var(--bg-light);
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  border-left: 4px solid var(--border-color);
  transition: all 0.3s ease;
}
[data-theme="dark"] .stat-card {
  background: var(--bg-white);
}

.stat-card.success {
  border-left-color: var(--success-color);
}

.stat-card.error {
  border-left-color: var(--danger-color);
}

.stat-icon {
  font-size: 2rem;
}

.stat-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-content small {
  color: var(--text-secondary);
  font-size: 0.85rem;
}

.stat-content strong {
  color: var(--text-primary);
  font-size: 1.5rem;
  font-weight: 700;
}

/* MENSAJE DE ERROR */
.error-message {
  background: rgba(245, 101, 101, 0.1);
  border: 2px solid var(--danger-color);
  border-radius: 12px;
  padding: 24px;
  color: var(--danger-color);
}

.error-message h3 {
  margin: 0 0 12px 0;
  font-size: 1.2rem;
}

.error-message p {
  margin: 0;
  word-break: break-word;
}

/* ANIMACIÓN DE CARGA */
.loading-animation {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 20px 0;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 4px solid var(--bg-light);
  border-top-color: var(--secondary-color);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

.loading-animation p {
  color: var(--text-secondary);
  font-weight: 500;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 600px) {
  .progress-section {
    padding: 24px;
  }

  .info-header h2 {
    font-size: 1.5rem;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>
