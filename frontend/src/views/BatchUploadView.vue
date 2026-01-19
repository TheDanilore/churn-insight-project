<template>
  <div class="batch-upload-container">
    <div class="card">
      <div class="card-header">
        <h2>Importar Lote de Clientes</h2>
        <p class="description">
          Sube un archivo CSV con los datos de tus clientes para obtener predicciones masivas.
        </p>
      </div>
      <div
        class="drop-zone"
        :class="{ 'is-dragging': isDragging, 'has-file': selectedFile }"
        @dragover.prevent="isDragging = true"
        @dragleave.prevent="isDragging = false"
        @drop.prevent="
          handleFileSelect($event);
          isDragging = false
        "
        @click="$refs.fileInput.click()"
      >
        <input type="file" ref="fileInput" hidden accept=".csv" @change="handleFileSelect" />

        <div v-if="!selectedFile" class="prompt">
          <span class="icon">📁</span>
          <p>Arrastra tu archivo CSV aquí o haz clic para buscar</p>
        </div>

        <div v-else class="file-info">
          <span class="icon">📄</span>
          <p>{{ selectedFile.name }}</p>
          <button class="btn-remove" @click.stop="reset">Cambiar archivo</button>
        </div>
      </div>

      <div v-if="errorMessage" class="error-msg">{{ errorMessage }}</div>

      <div class="actions">
        <button
          class="btn-primary"
          :disabled="!selectedFile || uploadStatus === 'uploading'"
          @click="uploadFile"
        >
          {{ uploadStatus === 'uploading' ? 'Procesando...' : 'Iniciar Predicción' }}
        </button>
      </div>

      <div v-if="uploadStatus === 'success'" class="success-banner">
        ✅ ¡Archivo procesado con éxito! Las predicciones han sido guardadas.
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import api from '@/services/api'

const fileInput = ref(null)
const selectedFile = ref(null)
const isDragging = ref(false)
const uploadStatus = ref('idle') // idle, uploading, success, error
const errorMessage = ref('')

const handleFileSelect = (event) => {
  const file = event.target.files[0]
  if (file && file.type === 'text/csv') {
    selectedFile.value = file
    errorMessage.value = ''
  } else {
    errorMessage.value = 'Por favor, selecciona un archivo CSV válido.'
  }
}

const uploadFile = async () => {
  if (!selectedFile.value) return

  uploadStatus.value = 'uploading'
  const formData = new FormData()
  formData.append('file', selectedFile.value)

  try {
    // Replace with your actual backend endpoint
    await api.post('/predictions/batch', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })
    uploadStatus.value = 'success'
  } catch (error) {
    uploadStatus.value = 'error'
    errorMessage.value = error.response?.data?.message || 'Error al subir el archivo.'
  }
}

const reset = () => {
  selectedFile.value = null
  uploadStatus.value = 'idle'
}
</script>


<style scoped>
.batch-upload-container {
  display: flex;
  justify-content: center;
  padding: 4rem 2rem;
}
.card {
  background: var(--bg-white);
  width: 100%;
  max-width: 600px;
  border-radius: 16px;
  box-shadow: var(--shadow-md);
  overflow: hidden;
  transition:
    transform 0.2s,
    box-shadow 0.2s;
}
.card-header {
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--secondary-color) 100%);
  color: white;
  padding: 2rem;
  text-align: center;
}
.card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}
.card-header > p{
  font-size: .8rem;
  margin-top: .6rem;
}

.drop-zone {
  border: 2px dashed var(--border-color);
  border-radius: 8px;
  padding: 40px;
  margin-top: 2rem;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: var(--bg-lighter)
}

.drop-zone.is-dragging {
  border-color: var(--primary-color);
  background: var(--hover-bg);
}

.drop-zone.has-file {
  border-style: solid;
  border-color: var(--primary-color);
}

.icon {
  font-size: 3rem;
  margin-bottom: 1rem;
  display: block;
}

.btn-primary {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
  border: none;
  margin-top: 2rem;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4);
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.error-msg {
  color: #dc3545;
  margin-top: 1rem;
  font-size: 0.9rem;
}
.success-banner {
  margin-top: 1.5rem;
  padding: 1rem;
  background: #d4edda;
  color: #155724;
  border-radius: 8px;
}
</style>
