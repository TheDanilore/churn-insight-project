<script setup>
import { ref } from 'vue'
import churnService from '@/services/churnService'

// --- ESTADO ---
const archivoSeleccionado = ref(null)
const resultadosLote = ref([])
const cargandoLote = ref(false)
const errorLote = ref(null)
const progreso = ref(0)
const inputFileRef = ref(null)

// --- FUNCIONES ---
const seleccionarArchivo = (event) => {
  const file = event.target.files?.[0]
  if (file) {
    const tipo = file.name.split('.').pop().toLowerCase()
    if (['csv', 'xlsx', 'xls'].includes(tipo)) {
      archivoSeleccionado.value = file
      errorLote.value = null
    } else {
      errorLote.value = 'Por favor selecciona un archivo CSV o Excel válido'
      archivoSeleccionado.value = null
    }
  }
}

const procesarArchivo = async () => {
  if (!archivoSeleccionado.value) {
    errorLote.value = 'Selecciona un archivo primero'
    return
  }

  cargandoLote.value = true
  errorLote.value = null
  resultadosLote.value = []
  progreso.value = 0

  try {
    const file = archivoSeleccionado.value
    const tipo = file.name.split('.').pop().toLowerCase()
    
    let datos = []

    if (tipo === 'csv') {
      datos = await parsearCSV(file)
    } else {
      datos = await parsearExcel(file)
    }

    if (!datos || datos.length === 0) {
      throw new Error('El archivo no contiene datos válidos')
    }

    // Procesar predicciones en lote
    resultadosLote.value = []
    for (let i = 0; i < datos.length; i++) {
      try {
        const datosCliente = mapearDatos(datos[i])
        const resultado = await churnService.predecirChurn(datosCliente)
        resultadosLote.value.push({
          ...datos[i],
          ...resultado,
          estado: 'exitoso'
        })
      } catch (err) {
        resultadosLote.value.push({
          ...datos[i],
          estado: 'error',
          error: err.message || 'Error en la predicción'
        })
      }
      progreso.value = Math.round(((i + 1) / datos.length) * 100)
    }
  } catch (err) {
    console.error('Error procesando archivo:', err)
    errorLote.value = err.message || 'Error al procesar el archivo'
  } finally {
    cargandoLote.value = false
  }
}

const parsearCSV = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = (e) => {
      try {
        const csv = e.target.result
        const lines = csv.split('\n')
        const headers = lines[0].split(',').map(h => h.trim().toLowerCase())
        
        const datos = []
        for (let i = 1; i < lines.length; i++) {
          if (lines[i].trim() === '') continue
          const values = lines[i].split(',').map(v => v.trim())
          const row = {}
          headers.forEach((header, idx) => {
            row[header] = values[idx]
          })
          datos.push(row)
        }
        resolve(datos)
      } catch (err) {
        reject(new Error('Error al parsear CSV: ' + err.message))
      }
    }
    reader.onerror = () => reject(new Error('Error al leer el archivo'))
    reader.readAsText(file)
  })
}

const parsearExcel = async () => {
  throw new Error('Para importar Excel, instala la librería: npm install xlsx')
}

const mapearDatos = (fila) => {
  return {
    antiguedad: parseFloat(fila.antiguedad || fila['antigüedad'] || 0),
    contrato: fila.contrato || 'Month-to-month',
    cargos_mensuales: parseFloat(fila.cargos_mensuales || fila['cargos mensuales'] || 0),
    soporte_tecnico: fila.soporte_tecnico || fila['soporte técnico'] || 'No',
    servicio_internet: fila.servicio_internet || fila['servicio de internet'] || 'Fiber optic',
    metodo_pago: fila.metodo_pago || fila['método de pago'] || 'Electronic check',
  }
}

const descargarResultados = () => {
  const csv = generarCSVResultados()
  const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  const url = URL.createObjectURL(blob)
  link.setAttribute('href', url)
  link.setAttribute('download', 'resultados_churn.csv')
  link.style.visibility = 'hidden'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

const generarCSVResultados = () => {
  const headers = ['antiguedad', 'contrato', 'cargos_mensuales', 'soporte_tecnico', 'servicio_internet', 'metodo_pago', 'prediccion', 'probabilidad', 'alerta', 'estado']
  const rows = resultadosLote.value.map(item => {
    const values = [
      item.antiguedad || '',
      item.contrato || '',
      item.cargos_mensuales || '',
      item.soporte_tecnico || '',
      item.servicio_internet || '',
      item.metodo_pago || '',
      item.prevision || '',
      item.probabilidad ? (item.probabilidad * 100).toFixed(2) + '%' : '',
      item.alerta || '',
      item.estado || ''
    ]
    return values.map(v => `"${v}"`).join(',')
  })
  return [headers.join(','), ...rows].join('\n')
}

const reiniciarLote = () => {
  archivoSeleccionado.value = null
  resultadosLote.value = []
  errorLote.value = null
  progreso.value = 0
  if (inputFileRef.value) {
    inputFileRef.value.value = ''
  }
}
</script>

<template>
  <div class="batch-container">
    <div class="card">
      <div class="card-header">
        <h2>📊 Importación por Lote</h2>
        <p>Importa un CSV o Excel y obtén predicciones para múltiples clientes</p>
      </div>

      <div class="card-body">
        <!-- SECCIÓN DE CARGA -->
        <div v-if="resultadosLote.length === 0" class="batch-upload-section">
          <div class="upload-area">
            <label for="file-input" class="upload-label">
              <div class="upload-content">
                <span class="upload-icon">📁</span>
                <h3>Selecciona un archivo</h3>
                <p>Formatos soportados: CSV, XLSX</p>
                <p class="upload-hint">o arrastra el archivo aquí</p>
              </div>
              <input
                ref="inputFileRef"
                id="file-input"
                type="file"
                accept=".csv,.xlsx,.xls"
                @change="seleccionarArchivo"
                class="file-input"
              />
            </label>
          </div>

          <div v-if="archivoSeleccionado" class="file-selected">
            <div class="file-info">
              <span class="file-icon">📄</span>
              <div>
                <p class="file-name">{{ archivoSeleccionado.name }}</p>
                <p class="file-size">{{ (archivoSeleccionado.size / 1024).toFixed(2) }} KB</p>
              </div>
            </div>
          </div>

          <div v-if="errorLote" class="alert alert-error">
            <span class="alert-icon">❌</span>
            <span>{{ errorLote }}</span>
          </div>

          <div v-if="archivoSeleccionado" class="batch-actions">
            <button class="btn-primary" @click="procesarArchivo" :disabled="cargandoLote">
              <span v-if="cargandoLote">⏳ Procesando...</span>
              <span v-else>🚀 Procesar Archivo</span>
            </button>
            <button class="btn-secondary" @click="reiniciarLote">
              🔄 Cambiar Archivo
            </button>
          </div>

          <!-- BARRA DE PROGRESO -->
          <div v-if="cargandoLote" class="progress-section">
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: progreso + '%' }"></div>
            </div>
            <p class="progress-text">{{ progreso }}% Completado</p>
          </div>
        </div>

        <!-- RESULTADOS DEL LOTE -->
        <div v-else class="batch-results-section">
          <div class="results-header">
            <h3>📊 Resultados ({{ resultadosLote.length }} clientes)</h3>
            <div class="results-stats">
              <div class="stat-box">
                <span class="stat-number">{{
                  resultadosLote.filter(r => r.estado === 'exitoso').length
                }}</span>
                <span class="stat-text">Exitosos</span>
              </div>
              <div class="stat-box">
                <span class="stat-number">{{
                  resultadosLote.filter(r => r.estado === 'error').length
                }}</span>
                <span class="stat-text">Errores</span>
              </div>
              <div class="stat-box">
                <span class="stat-number">{{
                  resultadosLote.filter(r => r.estado === 'exitoso' && r.alerta === 'ALTA')
                    .length
                }}</span>
                <span class="stat-text">Riesgo Alto</span>
              </div>
            </div>
          </div>

          <div class="results-table-wrapper">
            <table class="results-table">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Antigüedad</th>
                  <th>Contrato</th>
                  <th>Cargos</th>
                  <th>Predicción</th>
                  <th>Probabilidad</th>
                  <th>Alerta</th>
                  <th>Estado</th>
                </tr>
              </thead>
              <tbody>
                <tr
                  v-for="(item, idx) in resultadosLote"
                  :key="idx"
                  :class="['result-row', item.estado, item.alerta?.toLowerCase()]"
                >
                  <td>{{ idx + 1 }}</td>
                  <td>{{ item.antiguedad }}</td>
                  <td>{{ item.contrato }}</td>
                  <td>${{ parseFloat(item.cargos_mensuales).toFixed(2) }}</td>
                  <td><strong>{{ item.prevision }}</strong></td>
                  <td>{{ item.probabilidad ? (item.probabilidad * 100).toFixed(1) + '%' : 'N/A' }}</td>
                  <td>
                    <span
                      v-if="item.alerta"
                      class="badge"
                      :class="item.alerta.toLowerCase()"
                    >
                      {{ item.alerta }}
                    </span>
                    <span v-else class="badge error">Error</span>
                  </td>
                  <td>
                    <span class="status" :class="item.estado">
                      {{ item.estado === 'exitoso' ? '✓' : '✕' }}
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <div class="results-actions">
            <button class="btn-primary" @click="descargarResultados">
              📥 Descargar Resultados (CSV)
            </button>
            <button class="btn-secondary" @click="reiniciarLote">
              🔄 Importar Otro Archivo
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>


.batch-container {
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.card {
  background: var(--bg-white);
  border-radius: 12px;
  box-shadow: var(--shadow-md);
  overflow: hidden;
}

.card-header {
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
  padding: 32px 28px;
  text-align: center;
}

.card-header h2 {
  margin: 0 0 8px 0;
  font-size: 1.5rem;
}

.card-header p {
  margin: 0;
  opacity: 0.95;
  font-size: 0.95rem;
}

.card-body {
  padding: 32px;
}

/* Upload Area */
.upload-area {
  border: 2px dashed var(--border-color);
  border-radius: 12px;
  padding: 40px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
  margin-bottom: 24px;
}

.upload-area:hover {
  border-color: var(--primary-color);
  background-color: rgba(102, 126, 234, 0.05);
}

.upload-label {
  cursor: pointer;
  display: block;
}

.upload-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.upload-icon {
  font-size: 3rem;
  display: block;
}

.upload-content h3 {
  margin: 0;
  color: var(--text-primary);
  font-size: 1.3rem;
}

.upload-content p {
  margin: 0;
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.file-input {
  display: none;
}

.file-selected {
  padding: 16px;
  background-color: var(--bg-light);
  border-radius: 8px;
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
}

.file-icon {
  font-size: 1.8rem;
  flex-shrink: 0;
}

.file-name {
  margin: 0;
  font-weight: 600;
  color: var(--text-primary);
}

.file-size {
  margin: 0;
  font-size: 0.85rem;
  color: var(--text-secondary);
}

/* Buttons */
.btn-primary, .btn-secondary {
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-primary {
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4);
}

.btn-secondary {
  background-color: var(--bg-light);
  color: var(--text-primary);
  border: 2px solid var(--border-color);
}

.btn-secondary:hover {
  background-color: var(--text-primary);
  color: white;
  border-color: var(--text-primary);
}

.batch-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
}

/* Progress */
.progress-section {
  padding: 20px;
  background-color: var(--bg-light);
  border-radius: 8px;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background-color: var(--border-color);
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--primary-color), var(--secondary-color));
  transition: width 0.3s ease;
}

.progress-text {
  margin-top: 12px;
  text-align: center;
  color: var(--text-secondary);
  font-weight: 500;
}

/* Results */
.results-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
  padding-bottom: 20px;
  border-bottom: 2px solid var(--border-color);
  margin-bottom: 20px;
}

.results-header h3 {
  margin: 0;
  font-size: 1.3rem;
}

.results-stats {
  display: flex;
  gap: 16px;
}

.stat-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 12px 16px;
  background-color: var(--bg-light);
  border-radius: 8px;
}

.stat-number {
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--primary-color);
}

.stat-text {
  font-size: 0.8rem;
  color: var(--text-secondary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

/* Table */
.results-table-wrapper {
  overflow-x: auto;
  border-radius: 8px;
  border: 1px solid var(--border-color);
  margin-bottom: 20px;
}

.results-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.9rem;
}

.results-table thead {
  background-color: var(--bg-light);
  border-bottom: 2px solid var(--border-color);
}

.results-table th {
  padding: 14px;
  text-align: left;
  font-weight: 600;
  color: var(--text-primary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
  font-size: 0.8rem;
}

.results-table td {
  padding: 14px;
  border-bottom: 1px solid var(--border-color);
}

.result-row:hover {
  background-color: var(--bg-light);
}

.result-row.error {
  background-color: rgba(245, 101, 101, 0.05);
}

.badge {
  display: inline-block;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.badge.alta {
  background-color: rgba(245, 101, 101, 0.2);
  color: var(--danger-color);
  border: 1px solid var(--danger-color);
}

.badge.baja {
  background-color: rgba(72, 187, 120, 0.2);
  color: var(--success-color);
  border: 1px solid var(--success-color);
}

.badge.error {
  background-color: rgba(237, 137, 54, 0.2);
  color: #ed8936;
  border: 1px solid #ed8936;
}

.status {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  font-weight: 700;
  font-size: 0.9rem;
}

.status.exitoso {
  background-color: rgba(72, 187, 120, 0.2);
  color: var(--success-color);
}

.status.error {
  background-color: rgba(245, 101, 101, 0.2);
  color: var(--danger-color);
}

/* Alert */
.alert {
  padding: 16px 20px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 12px;
  font-weight: 500;
  margin-bottom: 16px;
}

.alert-error {
  background-color: rgba(245, 101, 101, 0.1);
  color: var(--danger-color);
  border-left: 4px solid var(--danger-color);
}

.results-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
  padding-top: 20px;
  border-top: 2px solid var(--border-color);
}

@media (max-width: 768px) {
  .batch-container {
    padding: 20px 16px;
  }

  .card-header {
    padding: 24px 20px;
  }

  .results-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .results-stats {
    width: 100%;
    justify-content: space-around;
  }

  .batch-actions,
  .results-actions {
    flex-direction: column;
  }

  .btn-primary,
  .btn-secondary {
    width: 100%;
  }
}
</style>
