<script setup>
import { useRouter } from 'vue-router'
import IconComponent from '@/components/icons/IconComponents.vue'

const router = useRouter()

const goHome = () => {
  router.push({ name: 'Home' })
}

const goBack = () => {
  router.back()
}
</script>

<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <div class="icon-wrapper">
        <IconComponent name="x" :size="80" color="#f56565" />
      </div>

      <h1 class="error-code">404</h1>
      <h2 class="error-title">Página No Encontrada</h2>
      <p class="error-description">
        Lo sentimos, la página que buscas no existe o ha sido movida.
      </p>

      <div class="action-buttons">
        <button class="btn btn-primary" @click="goHome">
          <IconComponent name="home" :size="18" />
          Ir al Inicio
        </button>
        <button class="btn btn-secondary" @click="goBack">
          <IconComponent name="chevron-right" :size="18" />
          Volver Atrás
        </button>
      </div>

      <div class="illustration">
        <div class="circle"></div>
        <div class="circle"></div>
        <div class="circle"></div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.not-found-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 20px;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--secondary-color) 100%);
}

.not-found-content {
  background: var(--bg-white);
  border-radius: 16px;
  padding: 60px 40px;
  text-align: center;
  box-shadow: var(--shadow-lg);
  max-width: 500px;
  width: 100%;
  animation: slideIn 0.5s ease-out;
  position: relative;
  overflow: hidden;
}

.icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 120px;
  height: 120px;
  margin: 0 auto 30px;
  background: rgba(245, 101, 101, 0.1);
  border-radius: 50%;
}

.error-code {
  font-size: 6rem;
  font-weight: 800;
  margin: 0 0 10px 0;
  background: linear-gradient(135deg, #f56565 0%, #ff7f50 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.error-title {
  font-size: 1.8rem;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 15px 0;
}

.error-description {
  font-size: 1rem;
  color: var(--text-secondary);
  margin: 0 0 40px 0;
  line-height: 1.6;
}

.action-buttons {
  display: flex;
  gap: 15px;
  justify-content: center;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
}

.btn-primary {
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
}

.btn-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(59, 125, 221, 0.3);
}

.btn-secondary {
  background: transparent;
  color: var(--primary-color);
  border: 2px solid var(--primary-color);
}

.btn-secondary:hover {
  background: rgba(59, 125, 221, 0.1);
  transform: translateY(-2px);
}

.illustration {
  position: absolute;
  top: -50px;
  right: -50px;
  width: 200px;
  height: 200px;
  pointer-events: none;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(59, 125, 221, 0.05);
}

.circle:nth-child(1) {
  width: 120px;
  height: 120px;
  top: 0;
  right: 0;
  animation: float 6s ease-in-out infinite;
}

.circle:nth-child(2) {
  width: 80px;
  height: 80px;
  bottom: 50px;
  right: 150px;
  animation: float 8s ease-in-out infinite 1s;
}

.circle:nth-child(3) {
  width: 60px;
  height: 60px;
  bottom: 100px;
  right: 50px;
  animation: float 10s ease-in-out infinite 2s;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}

@media (max-width: 600px) {
  .not-found-content {
    padding: 40px 20px;
  }

  .error-code {
    font-size: 4rem;
  }

  .error-title {
    font-size: 1.4rem;
  }

  .action-buttons {
    flex-direction: column;
  }

  .btn {
    width: 100%;
    justify-content: center;
  }
}

/* Dark mode */
[data-theme='dark'] .not-found-content {
  background: #0d1117;
  color: #c9d1d9;
}

[data-theme='dark'] .error-title {
  color: #c9d1d9;
}

[data-theme='dark'] .error-description {
  color: #8b949e;
}

[data-theme='dark'] .icon-wrapper {
  background: rgba(245, 101, 101, 0.15);
}

[data-theme='dark'] .btn-secondary {
  border-color: #5a9eeb;
  color: #5a9eeb;
}

[data-theme='dark'] .btn-secondary:hover {
  background: rgba(90, 158, 235, 0.15);
}

[data-theme='dark'] .illustration .circle {
  background: rgba(90, 158, 235, 0.08);
}
</style>
