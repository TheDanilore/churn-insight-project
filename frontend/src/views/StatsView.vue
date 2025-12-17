<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { predictionService } from '@/services/api'

const stats = ref(null)
const loading = ref(true)
const error = ref<string | null>(null)

onMounted(async () => {
  try {
    stats.value = await predictionService.getStats()
  } catch (err) {
    error.value = 'No se pudieron cargar las estadísticas'
    console.error(err)
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="stats-container">
    <h1>Estadísticas del Sistema</h1>

    <div v-if="loading" class="loading">Cargando estadísticas...</div>
    <div v-else-if="error" class="alert alert-error">{{ error }}</div>
    <div v-else-if="stats" class="stats-grid">
      <div class="stat-box">
        <h3>Total Evaluados</h3>
        <p class="value">{{ stats.total_evaluados }}</p>
      </div>
      <div class="stat-box">
        <h3>Tasa de Churn</h3>
        <p class="value">{{ (stats.tasa_churn * 100).toFixed(2) }}%</p>
      </div>
      <div class="stat-box">
        <h3>Predicciones Exactas</h3>
        <p class="value">{{ (stats.accuracy * 100).toFixed(2) }}%</p>
      </div>
      <div class="stat-box">
        <h3>Precisión</h3>
        <p class="value">{{ (stats.precision * 100).toFixed(2) }}%</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.stats-container {
  max-width: 1000px;
  margin: 40px auto;
  padding: 20px;
}

h1 {
  color: #333;
  text-align: center;
  margin-bottom: 30px;
}

.loading,
.alert {
  text-align: center;
  padding: 20px;
  border-radius: 4px;
}

.alert-error {
  background: #fee;
  color: #c33;
  border: 1px solid #fcc;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.stat-box {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.stat-box h3 {
  color: #666;
  margin-bottom: 10px;
  font-size: 14px;
  text-transform: uppercase;
}

.stat-box .value {
  font-size: 32px;
  font-weight: 700;
  color: #667eea;
}
</style>
