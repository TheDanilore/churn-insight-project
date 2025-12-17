<script setup lang="ts">
import { ref, reactive } from 'vue'
import { usePredictionStore } from '@/stores/prediction'
import { predictionService, type ClientData } from '@/services/api'

const store = usePredictionStore()
const loading = ref(false)
const error = ref<string | null>(null)
const result = ref(null)

const formData = reactive<ClientData>({
  customerId: '',
  contractLengthMonths: 12,
  monthlyCharges: 65.5,
  totalCharges: 786,
  internetService: 'Fiber optic',
  onlineSecurity: 'Yes',
  paymentMethod: 'Electronic check',
  tenureMonths: 2,
  monthlyLoginFrequency: 8
})

async function submitPrediction() {
  loading.value = true
  error.value = null
  try {
    const response = await predictionService.predict(formData)
    result.value = response
    store.addPrediction({
      id: Math.random().toString(),
      customerId: formData.customerId,
      prediction: response.prediction,
      probability: response.probability,
      confidence: response.confidence,
      topFeatures: response.topFeatures,
      timestamp: new Date().toISOString()
    })
  } catch (err) {
    error.value = 'Error al realizar la predicción. Intente de nuevo.'
    console.error(err)
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="predictor-container">
    <h1>Predictor de Churn</h1>
    <div class="form-section">
      <form @submit.prevent="submitPrediction">
        <div class="form-group">
          <label>ID del Cliente:</label>
          <input v-model="formData.customerId" type="text" required />
        </div>

        <div class="form-group">
          <label>Meses de Contrato:</label>
          <input v-model.number="formData.contractLengthMonths" type="number" />
        </div>

        <div class="form-group">
          <label>Cargo Mensual ($):</label>
          <input v-model.number="formData.monthlyCharges" type="number" step="0.01" />
        </div>

        <div class="form-group">
          <label>Cargo Total ($):</label>
          <input v-model.number="formData.totalCharges" type="number" step="0.01" />
        </div>

        <div class="form-group">
          <label>Servicio de Internet:</label>
          <select v-model="formData.internetService">
            <option>Fiber optic</option>
            <option>DSL</option>
            <option>No</option>
          </select>
        </div>

        <div class="form-group">
          <label>Seguridad en Línea:</label>
          <select v-model="formData.onlineSecurity">
            <option>Yes</option>
            <option>No</option>
          </select>
        </div>

        <div class="form-group">
          <label>Método de Pago:</label>
          <select v-model="formData.paymentMethod">
            <option>Electronic check</option>
            <option>Mailed check</option>
            <option>Bank transfer</option>
            <option>Credit card</option>
          </select>
        </div>

        <div class="form-group">
          <label>Meses de Antigüedad:</label>
          <input v-model.number="formData.tenureMonths" type="number" />
        </div>

        <div class="form-group">
          <label>Frecuencia de Login Mensual:</label>
          <input v-model.number="formData.monthlyLoginFrequency" type="number" />
        </div>

        <button type="submit" :disabled="loading">
          {{ loading ? 'Procesando...' : 'Realizar Predicción' }}
        </button>
      </form>

      <div v-if="error" class="alert alert-error">{{ error }}</div>

      <div v-if="result" class="result-section">
        <h2>Resultado</h2>
        <div class="prediction-card">
          <div :class="['prediction-status', result.prediction.includes('cancelar') ? 'churn' : 'retain']">
            {{ result.prediction }}
          </div>
          <p>Probabilidad: <strong>{{ (result.probability * 100).toFixed(2) }}%</strong></p>
          <p>Confianza: <strong>{{ (result.confidence * 100).toFixed(2) }}%</strong></p>
          <div class="top-features">
            <h4>Variables Más Relevantes:</h4>
            <ul>
              <li v-for="(feature, idx) in result.topFeatures" :key="idx">{{ feature }}</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.predictor-container {
  max-width: 600px;
  margin: 40px auto;
  padding: 20px;
}

h1 {
  color: #333;
  text-align: center;
}

.form-section {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
  color: #555;
}

input,
select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

input:focus,
select:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

button {
  width: 100%;
  padding: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: opacity 0.3s;
}

button:hover:not(:disabled) {
  opacity: 0.9;
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.alert {
  margin-top: 15px;
  padding: 12px;
  border-radius: 4px;
}

.alert-error {
  background: #fee;
  color: #c33;
  border: 1px solid #fcc;
}

.result-section {
  margin-top: 20px;
}

.prediction-card {
  background: #f5f5f5;
  padding: 15px;
  border-radius: 8px;
  margin-top: 10px;
}

.prediction-status {
  font-size: 18px;
  font-weight: 700;
  padding: 10px;
  border-radius: 4px;
  text-align: center;
  margin-bottom: 10px;
}

.prediction-status.churn {
  background: #fee;
  color: #c33;
}

.prediction-status.retain {
  background: #efe;
  color: #3c3;
}

.top-features {
  margin-top: 15px;
}

.top-features h4 {
  margin-bottom: 8px;
  color: #555;
}

.top-features ul {
  list-style: none;
  padding: 0;
}

.top-features li {
  background: white;
  padding: 8px;
  margin: 5px 0;
  border-left: 4px solid #667eea;
}
</style>
