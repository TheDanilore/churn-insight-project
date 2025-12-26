<script setup>
</script>

<template>
  <div class="home-container">
    <div class="hero-section">
      <div class="hero-content">
        <h1>🔮 ChurnInsight</h1>
        <p class="tagline">Predicción Inteligente de Retención de Clientes</p>
        <p class="description">
          Utiliza inteligencia artificial para predecir qué clientes tienen riesgo de abandonar tu negocio.
          Toma decisiones informadas y mejora la retención de clientes.
        </p>

        <router-link to="/churn" class="btn-primary">
          Comenzar Predicción → 
        </router-link>
      </div>

      <div class="hero-image">
        <div class="image-placeholder">📊</div>
      </div>
    </div>

    <div class="features-section">
      <h2>Características</h2>
      <div class="features-grid">
        <div class="feature-card">
          <span class="feature-icon">⚡</span>
          <h3>Rápido</h3>
          <p>Obtén predicciones en segundos con nuestro modelo de IA optimizado.</p>
        </div>

        <div class="feature-card">
          <span class="feature-icon">🎯</span>
          <h3>Preciso</h3>
          <p>Modelo entrenado con datos reales para máxima precisión.</p>
        </div>

        <div class="feature-card">
          <span class="feature-icon">📈</span>
          <h3>Analítico</h3>
          <p>Analiza múltiples variables para entender mejor a tus clientes.</p>
        </div>

        <div class="feature-card">
          <span class="feature-icon">🛡️</span>
          <h3>Seguro</h3>
          <p>Tus datos están protegidos y nunca se comparten.</p>
        </div>
      </div>
    </div>

    <div class="cta-section">
      <h2>¿Listo para comenzar?</h2>
      <p>Predice el riesgo de churn de tus clientes ahora mismo</p>
      <router-link to="/churn" class="btn-large">
        Ir a Predicción de Churn
      </router-link>
    </div>
  </div>
</template>

<style scoped>

.home-container {
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

/* Hero Section */
.hero-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
  margin-bottom: 80px;
  padding: 60px 0;
}

.hero-content h1 {
  font-size: 3rem;
  margin: 0 0 16px 0;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.tagline {
  font-size: 1.3rem;
  color: var(--text-primary);
  margin: 0 0 16px 0;
  font-weight: 600;
}

.description {
  font-size: 1rem;
  color: var(--text-secondary);
  line-height: 1.6;
  margin: 0 0 32px 0;
}

.btn-primary {
  display: inline-block;
  padding: 14px 32px;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
  text-decoration: none;
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.2s;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.4);
}

.hero-image {
  display: flex;
  justify-content: center;
  align-items: center;
}

.image-placeholder {
  font-size: 8rem;
  opacity: 0.3;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
}

/* Features Section */
.features-section {
  margin-bottom: 80px;
}

.features-section h2 {
  text-align: center;
  font-size: 2rem;
  margin-bottom: 50px;
  color: var(--text-primary);
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
}

.feature-card {
  padding: 30px;
  background: var(--bg-white);
  border-radius: 12px;
  box-shadow: var(--shadow-md);
  text-align: center;
  transition: all 0.2s;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
}

.feature-icon {
  font-size: 2.5rem;
  display: block;
  margin-bottom: 16px;
}

.feature-card h3 {
  margin: 0 0 12px 0;
  color: var(--text-primary);
  font-size: 1.2rem;
}

.feature-card p {
  margin: 0;
  color: var(--text-secondary);
  font-size: 0.95rem;
  line-height: 1.6;
}

/* CTA Section */
.cta-section {
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
  padding: 60px 40px;
  border-radius: 12px;
  text-align: center;
}

.cta-section h2 {
  font-size: 2rem;
  margin: 0 0 16px 0;
}

.cta-section p {
  font-size: 1.1rem;
  margin: 0 0 32px 0;
  opacity: 0.9;
}

.btn-large {
  display: inline-block;
  padding: 16px 40px;
  background: white;
  color: var(--primary-color);
  text-decoration: none;
  border-radius: 8px;
  font-weight: 700;
  transition: all 0.2s;
  font-size: 1rem;
}

.btn-large:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
}

/* Responsive */
@media (max-width: 768px) {
  .home-container {
    padding: 20px 16px;
  }

  .hero-section {
    grid-template-columns: 1fr;
    gap: 40px;
    padding: 40px 0;
  }

  .hero-content h1 {
    font-size: 2rem;
  }

  .tagline {
    font-size: 1.1rem;
  }

  .image-placeholder {
    font-size: 5rem;
  }

  .features-section h2,
  .cta-section h2 {
    font-size: 1.5rem;
  }

  .cta-section {
    padding: 40px 20px;
  }

  .features-grid {
    gap: 20px;
  }

  .feature-card {
    padding: 20px;
  }
}
</style>
