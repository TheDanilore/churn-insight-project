<template>
  <div class="progress-section" v-if="jobStatus">

    <div class="job-info">
      <div class="info-header">
        <h2>{{ statusIcon }} {{ statusText }}</h2>
        <small class="id-text">ID: {{ jobStatus.jobId }}</small>
      </div>
      <p class="file-name">
        Archivo: <strong>{{ jobStatus.fileName }}</strong>
      </p>
    </div>

    <div class="progress-container">
      <div class="progress-label">
        <span>Progreso</span>
        <span>{{ progressPercentage }}%</span>
      </div>

      <div class="progress-bar">
        <div class="progress-fill" :class="{ 'animated-stripe': isProcessing }"
          :style="{ width: progressPercentage + '%', backgroundColor: statusColor }"></div>
      </div>

      <div class="progress-text">
        {{ jobStatus.processedRecords || 0 }} / {{ jobStatus.totalRecords || 0 }} registros
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card">
        <span class="stat-icon">📋</span>
        <div class="stat-content">
          <small>Total</small>
          <strong>{{ jobStatus.totalRecords || 0 }}</strong>
        </div>
      </div>

      <div class="stat-card success">
        <span class="stat-icon">✅</span>
        <div class="stat-content">
          <small>Éxitos</small>
          <strong>{{ jobStatus.processedRecords || 0 }}</strong>
        </div>
      </div>

      <div class="stat-card error" v-if="(jobStatus.failedRecords || 0) > 0">
        <span class="stat-icon">❌</span>
        <div class="stat-content">
          <small>Fallidos</small>
          <strong>{{ jobStatus.failedRecords }}</strong>
        </div>
      </div>
    </div>

    <div v-if="isCompleted" class="message-box success">
      🎉 {{ jobStatus.message }}
    </div>

    <div v-if="isFailed" class="message-box error">
      ⚠️ {{ jobStatus.message }}
    </div>

    <div v-if="isProcessing || isPending" class="loading-animation">
      <div class="spinner"></div>
      <p v-if="isPending">Encolando trabajo...</p>
      <p v-else>Procesando ({{ progressPercentage }}%)...</p>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  jobStatus: {
    type: Object,
    required: true
  }
})

// --- COMPUTED STATES ---
const status = computed(() => props.jobStatus?.status || 'PENDING')

const isPending = computed(() => status.value === 'PENDING')
const isProcessing = computed(() => status.value === 'PROCESSING')
const isCompleted = computed(() => status.value === 'COMPLETED')
const isFailed = computed(() => status.value === 'FAILED' || props.jobStatus?.success === false)

// --- VISUALES ---
const statusText = computed(() => {
  const map = {
    'PENDING': 'En Cola',
    'PROCESSING': 'Procesando',
    'COMPLETED': 'Completado',
    'FAILED': 'Error'
  }
  return map[status.value] || status.value
})

const statusIcon = computed(() => {
  if (isCompleted.value) return '✅'
  if (isFailed.value) return '❌'
  if (isPending.value) return '⏳'
  return '⚙️'
})

const statusColor = computed(() => {
  if (isCompleted.value) return '#10b981'
  if (isFailed.value) return '#ef4444'
  if (isPending.value) return '#f59e0b'
  return '#6366f1'
})

const progressPercentage = computed(() => {
  const total = props.jobStatus?.totalRecords || 0
  const processed = props.jobStatus?.processedRecords || 0
  if (total === 0) return 0
  const pct = Math.round((processed / total) * 100)
  return Math.min(pct, 100)
})
</script>

<style scoped>
.progress-section {
  background: var(--bg-white);
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  animation: fadeIn 0.4s ease;
  font-family: 'Inter', sans-serif;
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

.id-text {
  color: var(--text-secondary);
  font-family: 'Monaco', monospace;
  background: var(--bg-light);
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.85rem;
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
  height: 10px;
  background: var(--bg-light);
  /* Fondo de la barra vacía */
  border-radius: 6px;
  overflow: hidden;
  margin-bottom: 8px;
}

.progress-fill {
  height: 100%;
  transition: width 0.3s ease;
  border-radius: 6px;
  /* Fondo sólido por defecto, el color viene de inline-style */
}

.animated-stripe {
  background-image: linear-gradient(45deg,
      rgba(255, 255, 255, 0.15) 25%,
      transparent 25%,
      transparent 50%,
      rgba(255, 255, 255, 0.15) 50%,
      rgba(255, 255, 255, 0.15) 75%,
      transparent 75%,
      transparent);
  background-size: 1rem 1rem;
  animation: progress-stripe 1s linear infinite;
}

.progress-text {
  font-size: 0.9rem;
  color: var(--text-secondary);
  text-align: right;
}

/* ESTADÍSTICAS */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
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
  transition: transform 0.2s ease;
}

.stat-card.success {
  border-left-color: #10b981;
}

.stat-card.error {
  border-left-color: #ef4444;
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
  font-size: 0.8rem;
  text-transform: uppercase;
  font-weight: 600;
}

.stat-content strong {
  color: var(--text-primary);
  font-size: 1.5rem;
  font-weight: 700;
}

/* MENSAJES */
.message-box {
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 20px;
  font-weight: 500;
  text-align: center;
}

.message-box.success {
  background: rgba(16, 185, 129, 0.1);
  color: #059669;
  border: 1px solid #10b981;
}

.message-box.error {
  background: rgba(239, 68, 68, 0.1);
  color: #dc2626;
  border: 1px solid #ef4444;
}

/* SPINNER */
.loading-animation {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
  padding: 20px 0;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid var(--bg-light);
  border-top-color: #6366f1;
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

@keyframes progress-stripe {
  from {
    background-position: 1rem 0;
  }

  to {
    background-position: 0 0;
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* =========================================
   🌑 DARK MODE OVERRIDES
   ========================================= */

/* Usamos :global() para que funcione dentro de scoped CSS */
[data-theme='dark'] .progress-section {
  background-color: var(--bg-card, #1e293b);
  border: 1px solid var(--border-color, #334155);
}

[data-theme='dark'] .stat-card {
  background-color: var(--bg-lighter, #0f172a);
  /* Fondo más oscuro para tarjetas */
  border-left-width: 4px;
  /* Mantiene el borde de color */
}

[data-theme='dark'] .id-text {
  background-color: var(--bg-lighter, #0f172a);
  color: #94a3b8;
}

[data-theme='dark'] .progress-bar {
  background-color: var(--bg-lighter, #0f172a);
  /* Fondo de la barra vacía oscuro */
}

[data-theme='dark'] .job-info {
  border-bottom-color: var(--border-color, #334155);
}

/* Ajustes de texto para modo oscuro si las variables no son suficientes */
[data-theme='dark'] .message-box.success {
  background: rgba(16, 185, 129, 0.2);
  color: #6ee7b7;
  /* Texto verde más claro */
}

[data-theme='dark'] .message-box.error {
  background: rgba(239, 68, 68, 0.2);
  color: #fca5a5;
  /* Texto rojo más claro */
}
</style>