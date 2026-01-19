<script setup>
import { computed } from 'vue'

// --- PROPS ---
const props = defineProps({
  jobStatus: Object,
})

// --- EMITS ---
defineEmits(['new-upload'])

// --- COMPUTED ---
const successRate = computed(() => {
  if (!props.jobStatus?.totalRecords) return 0
  return Math.round((props.jobStatus?.processedRecords / props.jobStatus?.totalRecords) * 100)
})

const isSuccess = computed(() => props.jobStatus?.status === 'COMPLETED' && props.jobStatus?.failedRecords === 0)

const resultClass = computed(() => {
  if (isSuccess.value) return 'success'
  if (props.jobStatus?.failedRecords > 0) return 'warning'
  return 'error'
})

const resultIcon = computed(() => {
  if (isSuccess.value) return '🎉'
  if (props.jobStatus?.failedRecords > 0) return '⚠️'
  return '❌'
})

const resultTitle = computed(() => {
  if (isSuccess.value) return 'Procesamiento Exitoso'
  if (props.jobStatus?.failedRecords > 0) return 'Completado con Errores'
  return 'Error en el Procesamiento'
})
</script>

<template>
  <div class="results-section">
    <!-- RESULTADO GENERAL -->
    <div :class="['result-header', resultClass]">
      <div class="result-icon">{{ resultIcon }}</div>
      <div class="result-content">
        <h2>{{ resultTitle }}</h2>
        <p v-if="isSuccess">Todos los registros se procesaron correctamente</p>
        <p v-else-if="props.jobStatus?.failedRecords > 0">
          {{ props.jobStatus.processedRecords }} registros procesados correctamente,
          {{ props.jobStatus.failedRecords }} con errores
        </p>
        <p v-else>{{ props.jobStatus?.errorMessage }}</p>
      </div>
    </div>

    <!-- RESUMEN DE RESULTADOS -->
    <div class="results-summary">
      <div class="summary-card">
        <div class="summary-metric">
          <span class="metric-icon">📊</span>
          <span class="metric-label">Total Procesado</span>
          <span class="metric-value">{{ props.jobStatus?.totalRecords }}</span>
        </div>
      </div>

      <div class="summary-card success">
        <div class="summary-metric">
          <span class="metric-icon">✅</span>
          <span class="metric-label">Exitosos</span>
          <span class="metric-value">{{ props.jobStatus?.processedRecords }}</span>
          <span class="metric-percentage">{{ successRate }}%</span>
        </div>
      </div>

      <div v-if="props.jobStatus?.failedRecords > 0" class="summary-card error">
        <div class="summary-metric">
          <span class="metric-icon">❌</span>
          <span class="metric-label">Fallidos</span>
          <span class="metric-value">{{ props.jobStatus.failedRecords }}</span>
          <span class="metric-percentage">{{ 100 - successRate }}%</span>
        </div>
      </div>
    </div>

    <!-- DESGLOSE DETALLADO -->
    <div class="details-box">
      <h3>Desglose Detallado</h3>
      <div class="details-grid">
        <div class="detail-item">
          <strong>Total de Registros:</strong>
          <span>{{ props.jobStatus?.totalRecords }}</span>
        </div>
        <div class="detail-item">
          <strong>Registros Procesados:</strong>
          <span class="success-text">{{ props.jobStatus?.processedRecords }}</span>
        </div>
        <div class="detail-item" v-if="props.jobStatus?.failedRecords > 0">
          <strong>Registros Fallidos:</strong>
          <span class="error-text">{{ props.jobStatus.failedRecords }}</span>
        </div>
        <div class="detail-item">
          <strong>Tasa de Éxito:</strong>
          <span>{{ successRate }}%</span>
        </div>
      </div>
    </div>

    <!-- PRÓXIMOS PASOS -->
    <div class="next-steps">
      <h3>¿Qué hacer ahora?</h3>
      <div class="steps-list">
        <div v-if="isSuccess" class="step-item success">
          <span class="step-icon">🎯</span>
          <div>
            <strong>¡Perfecto!</strong>
            <p>Todos tus datos fueron procesados correctamente. Puedes descargar el reporte completo si es necesario.</p>
          </div>
        </div>

        <div v-else class="step-item warning">
          <span class="step-icon">🔍</span>
          <div>
            <strong>Revisa los registros fallidos</strong>
            <p>Algunos registros tuvieron errores de validación. Corrige los datos y vuelve a intentarlo.</p>
          </div>
        </div>

        <div class="step-item">
          <span class="step-icon">📁</span>
          <div>
            <strong>Cargar más datos</strong>
            <p>Puedes procesar otro lote de clientes sin problemas.</p>
          </div>
        </div>
      </div>
    </div>

    <!-- BOTÓN DE NUEVA CARGA -->
    <div class="action-buttons">
      <button @click="$emit('new-upload')" class="btn-primary">
        📁 Cargar Otro Archivo
      </button>
      <button class="btn-secondary">
        📥 Descargar Reporte
      </button>
    </div>
  </div>
</template>

<style scoped>
.results-section {
  animation: fadeIn 0.4s ease;
}

/* ENCABEZADO DE RESULTADO */
.result-header {
  background: white;
  border-radius: 16px;
  padding: 40px;
  display: flex;
  align-items: center;
  gap: 24px;
  margin-bottom: 30px;
  border-left: 6px solid;
  box-shadow: var(--shadow-md);
}

.result-header.success {
  border-left-color: var(--success-color);
}

.result-header.warning {
  border-left-color: #ffce56;
}

.result-header.error {
  border-left-color: var(--danger-color);
}

.result-icon {
  font-size: 3rem;
  flex-shrink: 0;
}

.result-content h2 {
  margin: 0 0 8px 0;
  font-size: 1.8rem;
  color: var(--text-primary);
}

.result-content p {
  margin: 0;
  color: var(--text-secondary);
  font-size: 1rem;
}

/* RESUMEN */
.results-summary {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 30px;
}

.summary-card {
  background: white;
  border-radius: 12px;
  padding: 24px;
  border: 2px solid var(--border-color);
  box-shadow: var(--shadow-sm);
  transition: all 0.3s ease;
}

.summary-card.success {
  border-color: var(--success-color);
  background: rgba(72, 187, 120, 0.03);
}

.summary-card.error {
  border-color: var(--danger-color);
  background: rgba(245, 101, 101, 0.03);
}

.summary-metric {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  text-align: center;
}

.metric-icon {
  font-size: 2.5rem;
}

.metric-label {
  color: var(--text-secondary);
  font-size: 0.9rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.metric-value {
  font-size: 2.5rem;
  font-weight: 800;
  color: var(--text-primary);
}

.metric-percentage {
  font-size: 1rem;
  color: var(--primary-color);
  font-weight: 600;
}

/* DETALLES */
.details-box {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 30px;
  border: 2px solid var(--border-color);
}

.details-box h3 {
  margin: 0 0 16px 0;
  font-size: 1.2rem;
  color: var(--text-primary);
}

.details-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid var(--border-color);
}

.detail-item:last-child {
  border-bottom: none;
}

.detail-item strong {
  color: var(--text-primary);
}

.detail-item span {
  color: var(--text-secondary);
  font-weight: 600;
}

.success-text {
  color: var(--success-color) !important;
}

.error-text {
  color: var(--danger-color) !important;
}

/* PRÓXIMOS PASOS */
.next-steps {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 30px;
}

.next-steps h3 {
  margin: 0 0 20px 0;
  font-size: 1.2rem;
  color: var(--text-primary);
}

.steps-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.step-item {
  display: flex;
  gap: 16px;
  padding: 16px;
  background: var(--bg-light);
  border-radius: 8px;
  border-left: 4px solid var(--border-color);
}

.step-item.success {
  border-left-color: var(--success-color);
  background: rgba(72, 187, 120, 0.05);
}

.step-item.warning {
  border-left-color: #ffce56;
  background: rgba(255, 206, 86, 0.05);
}

.step-icon {
  font-size: 1.8rem;
  flex-shrink: 0;
}

.step-item strong {
  color: var(--text-primary);
  display: block;
  margin-bottom: 4px;
}

.step-item p {
  margin: 0;
  color: var(--text-secondary);
  font-size: 0.9rem;
}

/* BOTONES */
.action-buttons {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.btn-primary,
.btn-secondary {
  flex: 1;
  min-width: 200px;
  padding: 16px 24px;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.btn-primary {
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4);
}

.btn-secondary {
  background: white;
  color: var(--primary-color);
  border: 2px solid var(--primary-color);
}

.btn-secondary:hover {
  background: var(--primary-color);
  color: white;
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

@media (max-width: 768px) {
  .result-header {
    flex-direction: column;
    text-align: center;
    padding: 24px;
  }

  .result-content h2 {
    font-size: 1.5rem;
  }

  .results-summary {
    grid-template-columns: 1fr;
  }

  .action-buttons {
    flex-direction: column;
  }

  .btn-primary,
  .btn-secondary {
    width: 100%;
  }
}
</style>
