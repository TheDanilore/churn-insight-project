<template>
  <div class="template-section">
    <div class="template-card">
      <div class="card-header">
        <h3>📋 Descargar Template</h3>
        <p>Selecciona el formato que prefieras y descarga el archivo de ejemplo</p>
      </div>

      <div class="template-options">
        <!-- CSV -->
        <button
          @click="handleDownload('csv')"
          :disabled="isDownloading"
          class="template-btn csv"
        >
          <span class="icon">📄</span>
          <div class="btn-content">
            <strong>Descargar CSV</strong>
            <small>Formato texto separado por comas</small>
          </div>
          <span class="arrow">→</span>
        </button>

        <!-- EXCEL -->
        <button
          @click="handleDownload('excel')"
          :disabled="isDownloading"
          class="template-btn excel"
        >
          <span class="icon">📊</span>
          <div class="btn-content">
            <strong>Descargar Excel</strong>
            <small>Formato .xlsx con columnas formateadas</small>
          </div>
          <span class="arrow">→</span>
        </button>
      </div>

      <!-- INFO -->
      <div class="template-info">
        <h4>💡 Qué incluye el template:</h4>
        <ul>
          <li><strong>clientName:</strong> Nombre del cliente (opcional)</li>
          <li><strong>email:</strong> Correo del cliente (opcional)</li>
          <li><strong>phone:</strong> Teléfono del cliente (opcional)</li>
          <li><strong>antiguedad:</strong> Meses de antigüedad (0-72)</li>
          <li><strong>contrato:</strong> Month-to-month, One year, Two year</li>
          <li><strong>cargosMensuales:</strong> Cargos mensuales (18.25-118.75)</li>
          <li><strong>soporteTecnico:</strong> Yes, No, No internet service</li>
          <li><strong>servicioInternet:</strong> DSL, Fiber optic, No</li>
          <li><strong>metodoPago:</strong> Electronic check, Bank transfer (automatic), Credit card (automatic), Mailed check</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { 
  downloadTemplateCSVRequest, 
  downloadTemplateExcelRequest 
} from '@/services/churnService'

const isDownloading = ref(false)

const handleDownload = async (type) => {
  isDownloading.value = true
  try {
    let blob;

    if (type === 'csv') {
      // Estas funciones ya devuelven el 'response.data' (que es el Blob) gracias a tu churnService
      blob = await downloadTemplateCSVRequest();
    } else {
      blob = await downloadTemplateExcelRequest();
    }

    // Creamos una URL temporal para el Blob que nos devolvió Axios
    const url = window.URL.createObjectURL(new Blob([blob]));
    const link = document.createElement('a');
    link.href = url;
    
    // Nombre del archivo
    link.setAttribute('download', type === 'csv' ? 'template_churn.csv' : 'template_churn.xlsx');
    
    // Click invisible
    document.body.appendChild(link);
    link.click();
    
    // Limpieza
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);

  } catch (error) {
    console.error('Error:', error);
    // Puedes mostrar un mensaje más bonito si quieres
    alert('Error al conectar con el servidor para la descarga.');
  } finally {
    isDownloading.value = false;
  }
}
</script>


<style scoped>
.template-section {
  margin-bottom: 30px;
}

.template-card {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.05), rgba(102, 126, 234, 0.02));
  border: 2px solid var(--primary-color);
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.1);
}
[data-theme="dark"] .template-card {
  background: linear-gradient(135deg, rgba(139, 92, 246, 0.10), rgba(102, 126, 234, 0.04));
  border: 2px solid var(--secondary-color);
  box-shadow: 0 4px 12px rgba(139, 92, 246, 0.13);
}

.card-header {
  margin-bottom: 24px;
}

.card-header h3 {
  margin: 0 0 8px 0;
  font-size: 1.4rem;
  color: var(--text-primary);
}

.card-header p {
  margin: 0;
  color: var(--text-secondary);
  font-size: 0.95rem;
}

.template-options {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.template-btn {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: var(--bg-white);
  border: 2px solid var(--border-color);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: left;
  font-family: inherit;
}
[data-theme="dark"] .template-btn {
  background: var(--bg-light);
  border: 2px solid var(--secondary-color);
}

.template-btn:hover:not(:disabled) {
  border-color: var(--primary-color);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
  transform: translateY(-2px);
}

.template-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.template-btn.csv {
  border-color: #f59e0b;
}

.template-btn.csv:hover:not(:disabled) {
  border-color: #f59e0b;
  background: rgba(245, 158, 11, 0.05);
}

.template-btn.excel {
  border-color: #10b981;
}

.template-btn.excel:hover:not(:disabled) {
  border-color: #10b981;
  background: rgba(16, 185, 129, 0.05);
}

.icon {
  font-size: 2rem;
  flex-shrink: 0;
}

.btn-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.btn-content strong {
  color: var(--text-primary);
  font-weight: 600;
}

.btn-content small {
  color: var(--text-secondary);
  font-size: 0.85rem;
}

.arrow {
  font-size: 1.5rem;
  color: var(--text-secondary);
  transition: transform 0.3s ease;
  flex-shrink: 0;
}

.template-btn:hover:not(:disabled) .arrow {
  transform: translateX(4px);
}

/* INFO */
.template-info {
  background: var(--bg-white);
  border-radius: 12px;
  padding: 20px;
  border-left: 4px solid var(--primary-color);
}

.template-info h4 {
  margin: 0 0 16px 0;
  font-size: 1rem;
  color: var(--text-primary);
}

.template-info ul {
  margin: 0;
  padding-left: 20px;
  list-style: none;
}

.template-info li {
  color: var(--text-secondary);
  margin-bottom: 8px;
  font-size: 0.9rem;
  padding-left: 12px;
  position: relative;
}

.template-info li:before {
  content: '✓';
  position: absolute;
  left: 0;
  color: var(--success-color);
  font-weight: bold;
}

.template-info strong {
  color: var(--primary-color);
  font-weight: 600;
}

@media (max-width: 600px) {
  .template-card {
    padding: 20px;
  }

  .card-header h3 {
    font-size: 1.2rem;
  }

  .template-options {
    grid-template-columns: 1fr;
  }

  .template-btn {
    padding: 16px;
  }

  .icon {
    font-size: 1.8rem;
  }
}
</style>
