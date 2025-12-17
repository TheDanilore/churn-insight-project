<script setup lang="ts">
import { usePredictionStore } from '@/stores/prediction'

const store = usePredictionStore()
</script>

<template>
  <div class="home-container">
    <div class="hero">
      <h1>Churn Insight</h1>
      <p>Sistema Inteligente de Predicción de Cancelación de Clientes</p>
    </div>

    <div class="stats-overview">
      <div class="stat-card">
        <h3>Predicciones Realizadas</h3>
        <p class="stat-value">{{ store.totalPredictions }}</p>
      </div>
      <div class="stat-card">
        <h3>Tasa de Churn Predicha</h3>
        <p class="stat-value">{{ store.churnRate.toFixed(2) }}%</p>
      </div>
    </div>

    <div class="info-section">
      <h2>¿Cómo Funciona?</h2>
      <ol>
        <li>Ingresa los datos del cliente en el formulario</li>
        <li>Nuestro modelo analiza más de 20 características</li>
        <li>Obtienes una predicción de cancelación con probabilidad</li>
        <li>Identifica variables clave para tomar acciones preventivas</li>
      </ol>
    </div>

    <div class="cta-section">
      <RouterLink to="/predict" class="btn-primary">Iniciar Predicción</RouterLink>
    </div>
  </div>
</template>

<script lang="ts">
import { RouterLink } from 'vue-router'
</script>

<style scoped>
.home-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.hero {
  text-align: center;
  padding: 60px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 8px;
  margin-bottom: 40px;
}

.hero h1 {
  font-size: 48px;
  margin-bottom: 10px;
}

.hero p {
  font-size: 18px;
  opacity: 0.9;
}

.stats-overview {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.stat-card h3 {
  color: #666;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #667eea;
}

.info-section {
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
}

.info-section h2 {
  color: #333;
  margin-bottom: 20px;
}

.info-section ol {
  list-style-position: inside;
  line-height: 1.8;
}

.info-section li {
  margin-bottom: 10px;
  color: #666;
}

.cta-section {
  text-align: center;
}

.btn-primary {
  display: inline-block;
  padding: 15px 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  text-decoration: none;
  border-radius: 4px;
  font-weight: 600;
  transition: opacity 0.3s;
}

.btn-primary:hover {
  opacity: 0.9;
}
</style>
