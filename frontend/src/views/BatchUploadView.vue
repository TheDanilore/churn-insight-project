<template>
  <MainLayout>

    <div class="batch-container">
      <!-- ENCABEZADO -->
      <div class="batch-header">
        <div class="header-content">
          <h1>Carga Masiva de Predicciones</h1>
          <p>Procesa múltiples clientes en una sola carga. Descarga el template, complétalo y sube el archivo.</p>
        </div>
      </div>

      <!-- INDICADOR DE PASOS -->
      <div class="steps-indicator">
        <div :class="['step', { active: currentStep === 'upload', completed: currentStep !== 'upload' }]">
          <span class="step-number">1</span>
          <span class="step-label">Cargar Archivo</span>
        </div>
        <div class="step-line"></div>
        <div :class="['step', { active: currentStep === 'processing', completed: currentStep === 'results' }]">
          <span class="step-number">2</span>
          <span class="step-label">Procesando</span>
        </div>
        <div class="step-line"></div>
        <div :class="['step', { active: currentStep === 'results' }]">
          <span class="step-number">3</span>
          <span class="step-label">Resultados</span>
        </div>
      </div>

      <!-- CONTENIDO DINÁMICO -->
      <div class="batch-content">
        <!-- PASO 1: CARGA DE ARCHIVO -->
        <BatchUploadForm v-if="currentStep === 'upload'" @file-selected="handleFileSelected"
          @job-queued="handleJobQueued" />

        <!-- PASO 2: PROCESAMIENTO -->
        <BatchProgress v-else-if="currentStep === 'processing'" :job-id="jobId" :file-name="fileName"
          :job-status="jobStatus" />

        <div class="batch-content-results" v-else-if="currentStep === 'results'">
          <!-- PASO 3: RESULTADOS -->
          <BatchResults v-if="currentStep === 'results'" :job-status="jobStatus" @new-upload="handleReset" />
        </div>
      </div>
    </div>
  </MainLayout>

</template>

<script setup>
import { ref, onUnmounted } from 'vue'
import BatchUploadForm from '@/components/batch-upload/BatchUploadForm.vue'
import BatchProgress from '@/components/batch-upload/BatchProgress.vue'
import BatchResults from '@/components/batch/BatchResults.vue'
import MainLayout from '@/components/layouts/MainLayout.vue'
import { getJobStatusRequest } from '@/services/churnService'

// --- ESTADO ---
const currentStep = ref('upload') // 'upload', 'processing', 'results'
const jobId = ref(null)
const fileName = ref(null)
const jobStatus = ref(null)
const pollingInterval = ref(null)

// --- MÉTODOS ---
const handleFileSelected = (file) => {
  fileName.value = file.name
}

const handleJobQueued = (id) => {
  jobId.value = id

  // Inicializamos el estado del job para mostrar en progreso
  // Esto evita que el componente de progreso reciba 'null'
  // y no lance la advertencia mientras espera el primer polling.
  jobStatus.value = {
    jobId: id,
    fileName: fileName.value,
    status: 'PENDING',
    totalRecords: 0,
    processedRecords: 0,
    failedRecords: 0,
    message: 'Iniciando...'
  }

  currentStep.value = 'processing'
  startPolling()
}

const startPolling = () => {
  // Polling cada 2 segundos
  pollingInterval.value = setInterval(async () => {
    try {
      // Usamos el servicio (Axios) en lugar de fetch manual.
      // Esto asegura que use la URL correcta (localhost:8080) y no la del frontend.
      const data = await getJobStatusRequest(jobId.value)

      jobStatus.value = data

      // Si el trabajo terminó, detener polling y avanzar
      if (data.status === 'COMPLETED' || data.status === 'FAILED') {
        clearInterval(pollingInterval.value)
        currentStep.value = 'results'
      }

    } catch (error) {
      console.error('Error obteniendo estado:', error)
      // Opcional: Si falla muchas veces, podrías mostrar un error en pantalla
    }
  }, 2000)
}

const handleReset = () => {
  currentStep.value = 'upload'
  jobId.value = null
  fileName.value = null
  jobStatus.value = null
  if (pollingInterval.value) {
    clearInterval(pollingInterval.value)
  }
}

// Limpiar polling al desmontar
onUnmounted(() => {
  if (pollingInterval.value) {
    clearInterval(pollingInterval.value)
  }
})
</script>

<style scoped>
.batch-container {
  min-height: 100vh;
  background: linear-gradient(135deg, var(--bg-light) 0%, var(--bg-white) 100%);
  padding: 2rem 2rem;
}

[data-theme="dark"] .batch-container {
  background: linear-gradient(135deg, var(--bg-dark) 0%, var(--bg-white) 100%);
}

.batch-header {
  text-align: center;
  margin-bottom: 50px;
}

.header-content h1 {
  font-size: 2.5rem;
  font-weight: 800;
  color: var(--text-primary);
  margin: 0 0 12px 0;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.header-content p {
  font-size: 1.1rem;
  color: var(--text-secondary);
  margin: 0;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

/* INDICADOR DE PASOS */
.steps-indicator {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 50px;
  gap: 0;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  transition: all 0.3s ease;
  opacity: 0.5;
}

.step.active {
  opacity: 1;
}

.step.completed {
  opacity: 0.8;
}

.step-number {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background: var(--bg-white);
  font-weight: 700;
  font-size: 1.2rem;
  border: 2px solid var(--border-color);
  color: var(--text-secondary);
  transition: all 0.3s ease;
}

.step.active .step-number {
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: var(--text-inverse);
  border-color: var(--secondary-color);
  box-shadow: 0 4px 12px rgba(139, 92, 246, 0.3);
}

.step.completed .step-number {
  background: var(--success-color);
  color: white;
  border-color: var(--success-color);
}

.step-label {
  font-size: 0.9rem;
  font-weight: 600;
  color: var(--text-secondary);
  white-space: nowrap;
}

.step.active .step-label {
  color: var(--secondary-color);
}

.step.completed .step-label {
  color: var(--success-color);
}

.step-line {
  width: 60px;
  height: 3px;
  background: var(--border-color);
  margin: 0 20px;
  transition: all 0.3s ease;
}

[data-theme="dark"] .step-line {
  background: var(--secondary-color);
  opacity: 0.5;
}

/* CONTENIDO */
.batch-content {
  margin: 0 auto;
  animation: fadeIn 0.4s ease;
  max-width: 900px;
}

.batch-content-results{
    max-width: none;
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
  .batch-header {
    margin-bottom: 40px;
  }

  .header-content h1 {
    font-size: 2rem;
  }

  .header-content p {
    font-size: 1rem;
  }

  .steps-indicator {
    margin-bottom: 40px;
    gap: 10px;
  }

  .step-line {
    width: 40px;
    margin: 0 10px;
  }

  .step-number {
    width: 40px;
    height: 40px;
    font-size: 1rem;
  }

  .step-label {
    font-size: 0.8rem;
  }
}

@media (max-width: 480px) {
  .batch-container {
    padding: 20px 16px;
  }

  .batch-header {
    margin-bottom: 30px;
  }

  .header-content h1 {
    font-size: 1.5rem;
  }

  .steps-indicator {
    flex-direction: column;
    gap: 20px;
    margin-bottom: 30px;
  }

  .step-line {
    width: 3px;
    height: 40px;
    margin: -10px 0;
  }
}
</style>