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
  font-family: 'Inter', sans-serif;
  color: #2d3748;
}

.card {
  background: white;
  width: 100%;
  max-width: 600px;
  border-radius: 16px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.card-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 30px;
  text-align: center;
}
.card-header h2 {
  margin: 0;
  font-size: 1.8rem;
}
.card-header p {
  margin: 5px 0 0;
  opacity: 0.9;
}

.card-body {
  padding: 30px;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

@media (max-width: 600px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
label {
  font-weight: 600;
  font-size: 0.9rem;
  color: #4a5568;
}

input,
select {
  padding: 10px;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.2s;
}

input:focus,
select:focus {
  border-color: #667eea;
  outline: none;
}

.input-error {
  border-color: #fc8181;
  background-color: #fff5f5;
}
.error-text {
  color: #c53030;
  font-size: 0.8rem;
}

.btn-submit {
  width: 100%;
  margin-top: 25px;
  padding: 14px;
  background-color: #2d3748;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  transition: transform 0.1s;
}
.btn-submit:hover {
  transform: translateY(-2px);
  background-color: #1a202c;
}
.btn-submit:disabled {
  opacity: 0.7;
  cursor: wait;
}

/* Resultados */
.result-box {
  margin-top: 30px;
  padding: 20px;
  border-radius: 12px;
  text-align: center;
  animation: fadeIn 0.5s ease;
}

.risk-high {
  background-color: #fff5f5;
  border: 2px solid #fc8181;
  color: #c53030;
}
.risk-low {
  background-color: #f0fff4;
  border: 2px solid #68d391;
  color: #276749;
}

.stats {
  display: flex;
  justify-content: space-around;
  margin: 15px 0;
}
.stat {
  display: flex;
  flex-direction: column;
}
.stat .value {
  font-size: 1.5rem;
}

.badge {
  display: inline-block;
  padding: 5px 15px;
  background: white;
  border-radius: 20px;
  font-weight: bold;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.alert.error {
  margin-top: 20px;
  padding: 15px;
  background-color: #fee2e2;
  color: #991b1b;
  border-radius: 8px;
  text-align: center;
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
</style>
