<script setup>
import { useRouter } from 'vue-router'
import { ref, onMounted, onUnmounted } from 'vue'

const router = useRouter()
const scrollProgress = ref(0)

const irAPrediccion = () => {
  router.push('/churn')
}

onMounted(() => {
  const handleScroll = () => {
    const scrollHeight = document.documentElement.scrollHeight - window.innerHeight
    scrollProgress.value = scrollHeight > 0 ? (window.scrollY / scrollHeight) * 100 : 0
  }
  window.addEventListener('scroll', handleScroll)

  onUnmounted(() => {
    window.removeEventListener('scroll', handleScroll)
  })
})

const architectureModules = [
  {
    icon: '🎨',
    title: 'Frontend',
    tech: 'Vue.js 3 + Vite',
    port: 'Puerto 5173',
    description: 'Panel intuitivo para predicciones y análisis',
  },
  {
    icon: '☕',
    title: 'Backend',
    tech: 'Spring Boot 3 (Java 21)',
    port: 'Puerto 8080',
    description: 'API REST y lógica de negocio principal',
  },
  {
    icon: '🐍',
    title: 'Data Science',
    tech: 'FastAPI (Python 3.13)',
    port: 'Puerto 8000',
    description: 'Microservicio con modelo ML entrenado',
  },
  {
    icon: '🗄️',
    title: 'Database',
    tech: 'PostgreSQL 15',
    port: 'Puerto 5432',
    description: 'Persistencia de datos y predicciones',
  },
]

const modelStats = [
  { label: 'Precisión', value: '94%', icon: '🎯' },
  { label: 'Análisis', value: '<200ms', icon: '⚡' },
  { label: 'Seguridad', value: 'Enterprise', icon: '🛡️' },
  { label: 'ML', value: 'Avanzado', icon: '🧠' },
]

const features = [
  {
    icon: '⚡',
    title: 'Análisis en Tiempo Real',
    description: 'Procesa datos instantáneamente con nuestro motor de ML optimizado',
  },
  {
    icon: '🎯',
    title: 'Predicciones Precisas',
    description: 'Modelo entrenado que alcanza 94% de precisión en identificación de churn',
  },
  {
    icon: '🔐',
    title: 'Seguridad Enterprise',
    description: 'Validación estricta de datos con estándares de seguridad internacionales',
  },
  {
    icon: '📊',
    title: 'Visualización Intuitiva',
    description: 'Dashboards modernos que muestran resultados de forma clara y accionable',
  },
  {
    icon: '🐳',
    title: 'Escalable con Docker',
    description: 'Orquestación completa con Docker Compose para cualquier entorno',
  },
  {
    icon: '🚀',
    title: 'Deploy Rápido',
    description: 'Modo híbrido o full Docker para máxima flexibilidad en desarrollo',
  },
]
</script>

<template>
  <div class="home-view">
    <!-- Progress Bar -->
    <div class="scroll-progress-bar" :style="{ width: scrollProgress + '%' }"></div>

    <!-- Hero Section -->
    <section class="hero-section">
      <div class="bg-effects">
        <div class="glow-spot top-left"></div>
        <div class="glow-spot bottom-right"></div>
        <div class="grid-pattern"></div>
      </div>

      <div class="hero-container">
        <div class="system-status">ChurnInsight Predict • v1.0.0</div>

        <h1 class="title">Bienvenido a <span class="brand">ChurnInsight</span></h1>

        <p class="subtitle">
          Tu centro de control para la retención de clientes. Detecta patrones de fuga con
          <strong>94% de precisión</strong> usando IA avanzada.
        </p>

        <div class="model-stats">
          <div v-for="stat in modelStats" :key="stat.label" class="stat-badge">
            <span class="stat-emoji">{{ stat.icon }}</span>
            <div class="stat-content">
              <div class="stat-label">{{ stat.label }}</div>
              <div class="stat-value">{{ stat.value }}</div>
            </div>
          </div>
        </div>

        <button @click="irAPrediccion" class="cta-button primary">
          <span class="button-icon">🔮</span>
          <span class="button-text">Iniciar Predicción</span>
        </button>
      </div>
    </section>

    <!-- Arquitectura Section -->
    <section class="architecture-section">
      <div class="section-header">
        <h2>Arquitectura Integrada</h2>
        <p>Orquestación con Docker para máxima escalabilidad</p>
      </div>

      <div class="modules-grid">
        <div v-for="module in architectureModules" :key="module.title" class="module-card">
          <div class="module-icon">{{ module.icon }}</div>
          <h3>{{ module.title }}</h3>
          <div class="tech-stack">{{ module.tech }}</div>
          <div class="port-badge">{{ module.port }}</div>
          <p>{{ module.description }}</p>
        </div>
      </div>
    </section>

    <!-- Features Section -->
    <section class="features-section">
      <div class="section-header">
        <h2>Características Principales</h2>
        <p>Herramientas poderosas para retener tus mejores clientes</p>
      </div>

      <div class="features-grid">
        <div v-for="feature in features" :key="feature.title" class="feature-card">
          <div class="feature-icon">{{ feature.icon }}</div>
          <h3>{{ feature.title }}</h3>
          <p>{{ feature.description }}</p>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section">
      <h2>Comienza Ahora</h2>
      <p>Realiza tu primera predicción de churn en segundos</p>
      <button @click="irAPrediccion" class="cta-button large">
        <span class="button-icon">🚀</span>
        <span class="button-text">Ir a Predicción</span>
      </button>
    </section>
  </div>
</template>

<style scoped>
/* ========================
   SCROLL PROGRESS BAR
   ======================== */
.scroll-progress-bar {
  position: fixed;
  top: 0;
  left: 0;
  height: 3px;
  background: linear-gradient(90deg, var(--primary-color), var(--secondary-color));
  box-shadow: 0 0 10px rgba(99, 102, 241, 0.5);
  z-index: 999;
  transition: width 0.1s ease;
}

/* ========================
   GENERAL
   ======================== */
.home-view {
  min-height: 100vh;
  background-color: var(--bg-body);
  color: var(--text-primary);
  overflow-x: hidden;
}

.section-header {
  text-align: center;
  margin-bottom: 60px;
}

.section-header h2 {
  font-size: 2.5rem;
  font-weight: 800;
  margin-bottom: 12px;
  letter-spacing: -0.5px;
}

.section-header p {
  font-size: 1.1rem;
  color: var(--text-secondary);
  max-width: 600px;
  margin: 0 auto;
}

/* ========================
   HERO SECTION
   ======================== */
.hero-section {
  position: relative;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  overflow: hidden;
  background: linear-gradient(135deg, var(--bg-white) 0%, rgba(99, 102, 241, 0.05) 100%);
  border-bottom: 1px solid var(--border-color);
}

.bg-effects {
  position: absolute;
  inset: 0;
  overflow: hidden;
  pointer-events: none;
}

.glow-spot {
  position: absolute;
  width: 600px;
  height: 600px;
  border-radius: 50%;
  filter: blur(100px);
  opacity: 0.15;
}

.top-left {
  background: var(--primary-color);
  top: -20%;
  left: -10%;
  animation: float 8s ease-in-out infinite;
}

.bottom-right {
  background: var(--secondary-color);
  bottom: -20%;
  right: -10%;
  animation: float 8s ease-in-out infinite reverse;
}

.grid-pattern {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(255, 255, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, 0.03) 1px, transparent 1px);
  background-size: 40px 40px;
  mask-image: radial-gradient(circle at center, black 40%, transparent 100%);
}

@keyframes float {
  0%,
  100% {
    transform: translate(0, 0);
  }
  50% {
    transform: translate(30px, 30px);
  }
}

.hero-container {
  position: relative;
  z-index: 10;
  text-align: center;
  max-width: 900px;
  animation: slideDown 0.8s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.system-status {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid var(--border-color);
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 0.9rem;
  color: var(--primary-color);
  margin-bottom: 24px;
  backdrop-filter: blur(10px);
}

.dot {
  width: 8px;
  height: 8px;
  background: var(--success-color);
  border-radius: 50%;
  box-shadow: 0 0 8px var(--success-color);
}

.title {
  font-size: clamp(2.5rem, 8vw, 4rem);
  font-weight: 800;
  margin-bottom: 16px;
  letter-spacing: -1px;
  line-height: 1.1;
}

.brand {
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  background-clip: text;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.subtitle {
  font-size: 1.2rem;
  color: var(--text-secondary);
  line-height: 1.6;
  margin-bottom: 40px;
  max-width: 700px;
  margin-left: auto;
  margin-right: auto;
}

.model-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 16px;
  margin-bottom: 50px;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
}

.stat-badge {
  display: flex;
  align-items: center;
  gap: 12px;
  background: var(--bg-white);
  padding: 16px;
  border-radius: 12px;
  border: 1px solid var(--border-color);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.stat-badge::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, transparent, rgba(99, 102, 241, 0.08), transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.stat-badge:hover {
  border-color: var(--primary-color);
  box-shadow: var(--shadow-md);
  transform: translateY(-4px);
}

.stat-badge:hover::before {
  opacity: 1;
}

.stat-emoji {
  font-size: 1.8rem;
  flex-shrink: 0;
}

.stat-content {
  text-align: left;
}

.stat-label {
  font-size: 0.85rem;
  color: var(--text-secondary);
  font-weight: 500;
}

.stat-value {
  font-size: 1.3rem;
  font-weight: 700;
  color: var(--primary-color);
}

/* ========================
   BUTTONS
   ======================== */
.cta-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 14px 32px;
  font-size: 1rem;
  font-weight: 600;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  white-space: nowrap;
  position: relative;
  overflow: hidden;
}

.cta-button::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transform: translateX(-100%);
  transition: transform 0.6s;
}

.cta-button:hover::before {
  transform: translateX(100%);
}

.cta-button.primary {
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
  box-shadow: var(--shadow-md);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.cta-button.primary:hover {
  transform: translateY(-3px);
  box-shadow: var(--shadow-lg);
  filter: brightness(1.1);
}

.cta-button.primary:active {
  transform: translateY(-1px);
}

.cta-button.large {
  padding: 16px 40px;
  font-size: 1.1rem;
}

.button-icon {
  font-size: 1.2em;
  display: inline-block;
}

.cta-button:hover .button-icon {
  animation: iconBounce 0.4s ease-in-out infinite;
}

@keyframes iconBounce {
  0%,
  100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-4px);
  }
}

/* ========================
   ARCHITECTURE SECTION
   ======================== */
.architecture-section {
  padding: 100px 20px;
  background: var(--bg-body);
  border-bottom: 1px solid var(--border-color);
}

.modules-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
}

.module-card {
  background: var(--bg-white);
  border: 1px solid var(--border-color);
  padding: 32px 24px;
  border-radius: 12px;
  text-align: center;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  flex-direction: column;
  gap: 12px;
  position: relative;
  overflow: hidden;
}

.module-card::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, transparent, rgba(99, 102, 241, 0.1), transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.module-card:hover {
  border-color: var(--primary-color);
  box-shadow: var(--shadow-lg);
  transform: translateY(-8px);
}

.module-card:hover::before {
  opacity: 1;
}

.module-icon {
  font-size: 2.5rem;
  margin-bottom: 8px;
  transition: all 0.3s ease;
  display: inline-block;
}

.module-card:hover .module-icon {
  transform: scale(1.15) rotate(5deg);
}

.module-card h3 {
  font-size: 1.3rem;
  font-weight: 700;
  margin: 0;
}

.tech-stack {
  font-size: 0.9rem;
  color: var(--primary-color);
  font-weight: 600;
  font-family: 'Monaco', 'Courier New', monospace;
}

.port-badge {
  display: inline-block;
  background: var(--hover-bg);
  color: var(--text-secondary);
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 0.8rem;
  font-weight: 500;
  margin: 0 auto;
}

.module-card p {
  color: var(--text-secondary);
  line-height: 1.6;
  font-size: 0.95rem;
  margin: 0;
}

/* ========================
   FEATURES SECTION
   ======================== */
.features-section {
  padding: 100px 20px;
  background: linear-gradient(135deg, var(--bg-body), var(--bg-white));
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
}

.feature-card {
  background: var(--bg-white);
  border: 1px solid var(--border-color);
  padding: 32px 24px;
  border-radius: 12px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  flex-direction: column;
  gap: 12px;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.feature-card::after {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, transparent, rgba(99, 102, 241, 0.1), transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-8px);
  border-color: var(--primary-color);
  box-shadow: var(--shadow-lg);
}

.feature-card:hover::after {
  opacity: 1;
}

.feature-icon {
  font-size: 2.5rem;
  margin-bottom: 8px;
  transition: all 0.3s ease;
  display: inline-block;
}

.feature-card:hover .feature-icon {
  transform: scale(1.15) rotate(-5deg);
}

.feature-card h3 {
  font-size: 1.2rem;
  font-weight: 700;
  margin: 0;
}

.feature-card p {
  color: var(--text-secondary);
  line-height: 1.6;
  font-size: 0.95rem;
  margin: 0;
}

/* ========================
   CTA SECTION
   ======================== */
.cta-section {
  padding: 100px 20px;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
  text-align: center;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.cta-section h2 {
  font-size: 2.5rem;
  font-weight: 800;
  margin-bottom: 12px;
  letter-spacing: -0.5px;
}

.cta-section p {
  font-size: 1.1rem;
  margin-bottom: 32px;
  opacity: 0.95;
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.cta-section .cta-button {
  background: white;
  color: var(--primary-color);
}

.cta-section .cta-button:hover {
  background: rgba(255, 255, 255, 0.9);
  filter: brightness(1);
}

/* ========================
   RESPONSIVE
   ======================== */
@media (max-width: 768px) {
  .hero-section {
    min-height: auto;
    padding: 40px 20px 60px;
  }

  .title {
    font-size: 2rem;
  }

  .subtitle {
    font-size: 1rem;
  }

  .model-stats {
    grid-template-columns: repeat(2, 1fr);
    margin-bottom: 30px;
  }

  .stat-badge {
    flex-direction: column;
    text-align: center;
  }

  .stat-content {
    text-align: center;
  }

  .architecture-section,
  .features-section,
  .cta-section {
    padding: 60px 20px;
  }

  .section-header h2 {
    font-size: 1.8rem;
  }

  .modules-grid,
  .features-grid {
    grid-template-columns: 1fr;
  }

  .module-card,
  .feature-card {
    padding: 24px 16px;
  }

  .cta-section h2 {
    font-size: 1.8rem;
  }
}

@media (max-width: 480px) {
  .hero-container {
    padding: 0 15px;
  }

  .title {
    font-size: 1.5rem;
    margin-bottom: 12px;
  }

  .subtitle {
    font-size: 0.95rem;
    margin-bottom: 24px;
  }

  .model-stats {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .stat-badge {
    padding: 12px;
  }

  .stat-emoji {
    font-size: 1.5rem;
  }

  .stat-value {
    font-size: 1.1rem;
  }

  .architecture-section,
  .features-section,
  .cta-section {
    padding: 40px 16px;
  }

  .section-header h2 {
    font-size: 1.5rem;
  }

  .cta-button {
    padding: 12px 24px;
    font-size: 0.95rem;
  }

  .cta-button.large {
    width: 100%;
  }
}
</style>
