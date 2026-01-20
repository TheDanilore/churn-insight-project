<template>
  <div class="upload-section">
    <!-- DESCARGAR TEMPLATES -->
    <TemplateDownload />

    <!-- ÁREA DE CARGA -->
    <div class="upload-box">
      <div
        :class="['drop-zone', { dragging: isDragging }]"
        @dragover="handleDragOver"
        @dragleave="handleDragLeave"
        @drop="handleDrop"
      >
        <div class="drop-content">
          <div class="icon">📁</div>
          <h3>Arrastra tu archivo aquí</h3>
          <p>o haz clic para seleccionar</p>
          <input
            type="file"
            accept=".csv,.xlsx,.xls"
            @change="handleFileChange"
            class="file-input"
            :disabled="isUploading"
          />
        </div>
      </div>

      <!-- ARCHIVO SELECCIONADO -->
      <div v-if="file" class="file-selected">
        <div class="file-info">
          <span class="file-icon">📄</span>
          <div class="file-details">
            <strong>{{ file.name }}</strong>
            <small>{{ (file.size / 1024).toFixed(2) }} KB</small>
          </div>
        </div>
        <button @click="handleClear" class="btn-clear" :disabled="isUploading">✕</button>
      </div>

      <!-- ERRORES -->
      <div v-if="error" class="alert error">{{ error }}</div>

      <!-- BOTONES DE ACCIÓN -->
      <div class="action-buttons">
        <button
          @click="handleUpload"
          class="btn-upload"
          :disabled="!file || isUploading"
        >
          <span v-if="isUploading">⏳ Subiendo...</span>
          <span v-else>🚀 Procesar Archivo</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import TemplateDownload from '@/components/batch-upload/TemplateDownload.vue'
import { uploadBatchFileRequest } from '@/services/churnService'

// --- EMITS ---
const emit = defineEmits(['file-selected', 'job-queued'])

// --- ESTADO ---
const file = ref(null)
const isDragging = ref(false)
const isUploading = ref(false)
const error = ref(null)

// --- MÉTODOS ---
const handleFileChange = (event) => {
  const selectedFile = event.target.files?.[0]
  if (selectedFile) {
    validateAndSelect(selectedFile)
  }
}

const handleDragOver = (event) => {
  event.preventDefault()
  isDragging.value = true
}

const handleDragLeave = () => {
  isDragging.value = false
}

const handleDrop = (event) => {
  event.preventDefault()
  isDragging.value = false
  const droppedFile = event.dataTransfer?.files?.[0]
  if (droppedFile) {
    validateAndSelect(droppedFile)
  }
}

// --- VALIDACIÓN ---
const validateAndSelect = (selectedFile) => {
  error.value = null

  // Validar Tipo (MIME y Extensión por seguridad)
  const validTypes = [
    'text/csv', 
    'application/vnd.ms-excel', 
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
  ]
  const validExtensions = ['.csv', '.xlsx', '.xls']

  // A veces el tipo MIME viene vacío en Windows, así que confiamos también en la extensión
  const hasValidType = validTypes.includes(selectedFile.type)
  const hasValidExtension = validExtensions.some(ext => selectedFile.name.toLowerCase().endsWith(ext))

  if (!hasValidType && !hasValidExtension) {
    error.value = '❌ Solo se aceptan archivos CSV o Excel (.xlsx, .xls)'
    return
  }

  // Validar Tamaño (Máx 10MB)
  if (selectedFile.size > 10 * 1024 * 1024) {
    error.value = '❌ El archivo no puede exceder 10MB'
    return
  }

  file.value = selectedFile
  emit('file-selected', selectedFile)
}

// --- SUBIDA (FIXED) ---
const handleUpload = async () => {
  if (!file.value) return

  isUploading.value = true
  error.value = null

  try {
    const data = await uploadBatchFileRequest(file.value)
    
    // Emitimos el ID para que el componente padre empiece el monitoreo (polling)
    emit('job-queued', data.jobId)

  } catch (err) {
    console.error(err)
    // El servicio ya lanza el error formateado
    error.value = `❌ ${err.message || 'Error al subir el archivo'}`
  } finally {
    isUploading.value = false
  }
}

const handleClear = () => {
  file.value = null
  error.value = null
}
</script>


<style scoped>
.upload-section {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.upload-box {
  background: var(--bg-white);
  border-radius: 16px;
  padding: 40px;
  box-shadow: var(--shadow-md);
}

.drop-zone {
  border: 3px dashed var(--border-color);
  border-radius: 12px;
  padding: 40px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 30px;
  position: relative;
}

.drop-zone:hover {
  border-color: var(--primary-color);
  background: rgba(102, 126, 234, 0.03);
}

.drop-zone.dragging {
  border-color: var(--primary-color);
  background: rgba(102, 126, 234, 0.1);
  transform: scale(1.01);
}

.drop-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.icon {
  font-size: 3rem;
}

.drop-zone h3 {
  margin: 0;
  color: var(--text-primary);
  font-size: 1.3rem;
  font-weight: 700;
}

.drop-zone p {
  margin: 0;
  color: var(--text-secondary);
  font-size: 1rem;
}

.file-input {
  position: absolute;
  inset: 0;
  opacity: 0;
  cursor: pointer;
  border-radius: 12px;
}

.file-selected {
  background: linear-gradient(135deg, rgba(72, 187, 120, 0.1), rgba(72, 187, 120, 0.05));
  border: 2px solid var(--success-color);
  border-radius: 12px;
  padding: 16px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  animation: slideIn 0.3s ease;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
}

.file-icon {
  font-size: 1.8rem;
}

.file-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.file-details strong {
  color: var(--text-primary);
  font-weight: 600;
}

.file-details small {
  color: var(--text-secondary);
  font-size: 0.85rem;
}

.btn-clear {
  background: transparent;
  border: none;
  color: var(--text-secondary);
  font-size: 1.5rem;
  cursor: pointer;
  padding: 0 12px;
  transition: color 0.2s;
}

.btn-clear:hover {
  color: var(--danger-color);
}

.btn-clear:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.alert {
  background: rgba(245, 101, 101, 0.1);
  border-left: 4px solid var(--danger-color);
  color: var(--danger-color);
  padding: 12px 16px;
  border-radius: 8px;
  font-weight: 500;
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  gap: 12px;
}

.btn-upload {
  flex: 1;
  padding: 16px 24px;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.btn-upload:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4);
}

.btn-upload:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 600px) {
  .upload-box {
    padding: 24px;
  }

  .drop-zone {
    padding: 30px 20px;
  }

  .icon {
    font-size: 2.5rem;
  }

  .drop-zone h3 {
    font-size: 1.1rem;
  }
}
</style>
