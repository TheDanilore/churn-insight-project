<script setup>
import { ref } from 'vue'
import churnService from '../services/churnService'

// --- ESTADO ---
const formData = ref({
  antiguedad: 0,
  contrato: 'Month-to-month',
  cargos_mensuales: 0,
  soporte_tecnico: 'No',
  servicio_internet: 'Fiber optic',
  metodo_pago: 'Electronic check',
})

const resultado = ref(null)
const errores = ref({}) // Objeto para guardar errores de validación
const cargando = ref(false)
const errorGeneral = ref(null)

// --- LÓGICA ---
const enviarPrediccion = async () => {
  // 1. Resetear estados
  cargando.value = true
  resultado.value = null
  errores.value = {}
  errorGeneral.value = null

  try {
    // 2. Llamar al servicio
    const data = await churnService.predecirChurn(formData.value)

    // 3. ¡Éxito!
    resultado.value = data
  } catch (err) {
    console.error('Error capturado:', err)

    // 4. Manejo de Errores
    if (err.validationErrors) {
      // Si Spring Boot nos devolvió errores de validación (400)
      errores.value = err.validationErrors
    } else {
      // Error de conexión o 500
      errorGeneral.value = err.message || 'Ocurrió un error inesperado. Intente nuevamente.'
    }
  } finally {
    cargando.value = false
  }
}
</script>

<template>
  <div class="container">
    <div class="card">
      <div class="card-header">
        <h2>🔮 ChurnInsight AI</h2>
        <p>Predicción de Retención de Clientes</p>
      </div>

      <div class="card-body">
        <form @submit.prevent="enviarPrediccion">
          <div class="form-grid">
            <div class="form-group">
              <label>Antigüedad (Meses)</label>
              <input
                type="number"
                v-model="formData.antiguedad"
                :class="{ 'input-error': errores.antiguedad }"
                min="0"
                required
              />
              <span v-if="errores.antiguedad" class="error-text">{{ errores.antiguedad }}</span>
            </div>

            <div class="form-group">
              <label>Cargos Mensuales ($)</label>
              <input
                type="number"
                v-model="formData.cargos_mensuales"
                :class="{ 'input-error': errores.cargosMensuales }"
                step="0.1"
                min="0"
                required
              />
              <span v-if="errores.cargosMensuales" class="error-text">{{
                errores.cargosMensuales
              }}</span>
            </div>

            <div class="form-group">
              <label>Tipo de Contrato</label>
              <select v-model="formData.contrato">
                <option value="Month-to-month">Mes a Mes</option>
                <option value="One year">Un Año</option>
                <option value="Two year">Dos Años</option>
              </select>
            </div>

            <div class="form-group">
              <label>Soporte Técnico</label>
              <select v-model="formData.soporte_tecnico">
                <option value="Yes">Sí</option>
                <option value="No">No</option>
                <option value="No internet service">Sin servicio</option>
              </select>
            </div>

            <div class="form-group">
              <label>Servicio de Internet</label>
              <select v-model="formData.servicio_internet">
                <option value="Fiber optic">Fibra Óptica</option>
                <option value="DSL">DSL</option>
                <option value="No">No tiene</option>
              </select>
            </div>

            <div class="form-group">
              <label>Método de Pago</label>
              <select v-model="formData.metodo_pago">
                <option value="Electronic check">Cheque Electrónico</option>
                <option value="Credit card (automatic)">Tarjeta de Crédito</option>
                <option value="Bank transfer (automatic)">Transferencia</option>
                <option value="Mailed check">Cheque por Correo</option>
              </select>
            </div>
          </div>

          <button type="submit" class="btn-submit" :disabled="cargando">
            <span v-if="cargando">⏳ Analizando...</span>
            <span v-else>✨ Calcular Riesgo</span>
          </button>
        </form>

        <div v-if="errorGeneral" class="alert error">⚠️ {{ errorGeneral }}</div>

        <div
          v-if="resultado"
          class="result-box"
          :class="resultado.alerta === 'ALTA' ? 'risk-high' : 'risk-low'"
        >
          <h3>Resultado del Análisis</h3>

          <div class="stats">
            <div class="stat">
              <span class="label">Previsión</span>
              <strong class="value">{{ resultado.prevision }}</strong>
            </div>
            <div class="stat">
              <span class="label">Probabilidad</span>
              <strong class="value">{{ (resultado.probabilidad * 100).toFixed(1) }}%</strong>
            </div>
          </div>

          <div class="badge">Nivel de Alerta: {{ resultado.alerta }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  padding: 40px 20px;
}

.card {
  background: var(--bg-white);
  width: 100%;
  max-width: 600px;
  border-radius: 16px;
  box-shadow: var(--shadow-md);
  overflow: hidden;
  transition: transform 0.2s, box-shadow 0.2s;
}

.card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.card-header {
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--secondary-color) 100%);
  color: white;
  padding: 32px 28px;
  text-align: center;
}

.card-header h2 {
  margin: 0 0 8px 0;
  font-size: 1.8rem;
  font-weight: 700;
}

.card-header p {
  margin: 0;
  opacity: 0.95;
  font-size: 0.95rem;
}

.card-body {
  padding: 32px;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 24px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

label {
  font-weight: 600;
  font-size: 0.9rem;
  color: var(--text-primary);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

input,
select {
  padding: 12px 14px;
  border: 2px solid var(--border-color);
  border-radius: 8px;
  font-size: 0.95rem;
  transition: all 0.2s;
  background-color: var(--bg-white);
  font-family: inherit;
}

input:focus,
select:focus {
  border-color: var(--primary-color);
  outline: none;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.input-error {
  border-color: var(--danger-color) !important;
  background-color: rgba(245, 101, 101, 0.05);
}

.input-error:focus {
  box-shadow: 0 0 0 3px rgba(245, 101, 101, 0.1) !important;
}

.error-text {
  color: var(--danger-color);
  font-size: 0.8rem;
  font-weight: 500;
}

.btn-submit {
  width: 100%;
  padding: 14px;
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

.btn-submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4);
}

.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Alertas */
.alert {
  margin-top: 20px;
  padding: 16px 20px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 12px;
  font-weight: 500;
  animation: slideIn 0.3s ease;
}

.alert.error {
  background-color: rgba(245, 101, 101, 0.1);
  color: var(--danger-color);
  border-left: 4px solid var(--danger-color);
}

/* Resultados */
.result-box {
  margin-top: 30px;
  padding: 24px;
  border-radius: 12px;
  border-left: 4px solid;
  animation: fadeIn 0.4s ease;
}

.result-box h3 {
  margin-top: 0;
  margin-bottom: 20px;
  font-size: 1.2rem;
}

.risk-high {
  background: linear-gradient(135deg, rgba(245, 101, 101, 0.1), rgba(245, 101, 101, 0.05));
  border-left-color: var(--danger-color);
  color: var(--danger-color);
}

.risk-low {
  background: linear-gradient(135deg, rgba(72, 187, 120, 0.1), rgba(72, 187, 120, 0.05));
  border-left-color: var(--success-color);
  color: var(--success-color);
}

.stats {
  display: flex;
  justify-content: space-around;
  align-items: center;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 20px;
}

.stat {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.stat .label {
  font-size: 0.85rem;
  opacity: 0.8;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.stat .value {
  font-size: 2rem;
  font-weight: 700;
}

.badge {
  display: inline-block;
  padding: 8px 16px;
  background: white;
  border-radius: 20px;
  font-weight: 600;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  font-size: 0.9rem;
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
  .container {
    padding: 20px 16px;
  }

  .card-header {
    padding: 24px 20px;
  }

  .card-header h2 {
    font-size: 1.5rem;
  }

  .card-body {
    padding: 20px;
  }

  .form-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .stats {
    flex-direction: column;
  }
}
</style>
