<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const sidebarVisible = ref(true)

const toggleSidebar = () => {
  sidebarVisible.value = !sidebarVisible.value
}

const navigateTo = (path) => {
  router.push(path)
}
</script>

<template>
  <aside :class="['sidebar', { 'sidebar-collapsed': !sidebarVisible }]">
    <!-- Header -->
    <div class="sidebar-header">
      <h1 v-if="sidebarVisible" class="logo">🔮 ChurnInsight</h1>
    </div>

    <!-- Navigation Menu -->
    <nav class="sidebar-nav">
      <router-link
        to="/"
        :class="['nav-item', { active: $route.path === '/' }]"
        @click="navigateTo('/')"
      >
        <span class="icon">🏠</span>
        <span v-if="sidebarVisible" class="label">Inicio</span>
      </router-link>

      <router-link
        to="/churn"
        :class="['nav-item', { active: $route.path === '/churn' }]"
        @click="navigateTo('/churn')"
      >
        <span class="icon">📊</span>
        <span v-if="sidebarVisible" class="label">Predicción Churn</span>
      </router-link>

      <!-- Próximamente -->
      <div class="nav-item disabled" title="Próximamente">
        <span class="icon">📁</span>
        <span v-if="sidebarVisible" class="label">Importar Lote</span>
      </div>

      <div class="nav-item disabled" title="Próximamente">
        <span class="icon">📈</span>
        <span v-if="sidebarVisible" class="label">Análisis</span>
      </div>
    </nav>

    <!-- Toggle Button -->
    <button
      class="sidebar-toggle"
      @click="toggleSidebar"
      :title="sidebarVisible ? 'Contraer' : 'Expandir'"
    >
      <span>{{ sidebarVisible ? '◀' : '▶' }}</span>
    </button>
  </aside>
</template>

<style scoped>

.sidebar {
  width: 280px;
  background: var(--bg-white);
  border-right: 1px solid var(--border-color);
  box-shadow: var(--shadow-sm);
  display: flex;
  flex-direction: column;
  transition: width 0.3s ease;
  position: relative;
  z-index: 100;
  height: 100vh;
  overflow-y: auto;
}

.sidebar-collapsed {
  width: 80px;
}

.sidebar-header {
  padding: 24px 16px;
  border-bottom: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 700;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  white-space: nowrap;
}

.sidebar-nav {
  flex: 1;
  padding: 16px 8px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: none;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  color: var(--text-secondary);
  font-size: 0.95rem;
  font-weight: 500;
  transition: all 0.2s;
  white-space: nowrap;
  overflow: hidden;
  text-decoration: none;
  display: flex;
  align-items: center;
}

.nav-item:hover:not(.disabled) {
  background-color: #f7fafc;
  color: var(--primary-color);
}

.nav-item.active {
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
  box-shadow: var(--shadow-sm);
}

.nav-item.disabled {
  opacity: 0.5;
  cursor: not-allowed;
  position: relative;
}

.nav-item.disabled::after {
  content: 'Próximamente';
  position: absolute;
  left: 100%;
  margin-left: 8px;
  background: #333;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 0.75rem;
  white-space: nowrap;
  opacity: 0;
  transition: opacity 0.2s;
  pointer-events: none;
}

.nav-item.disabled:hover::after {
  opacity: 1;
}

.nav-item .icon {
  font-size: 1.3rem;
  flex-shrink: 0;
}

.nav-item .label {
  white-space: nowrap;
}

.sidebar-toggle {
  position: absolute;
  bottom: 16px;
  left: 50%;
  transform: translateX(-50%);
  width: 40px;
  height: 40px;
  border-radius: 8px;
  border: none;
  background-color: #f7fafc;
  color: var(--primary-color);
  font-size: 1.2rem;
  cursor: pointer;
  transition: all 0.2s;
  display: flex;
  align-items: center;
  justify-content: center;
}

.sidebar-toggle:hover {
  background-color: var(--primary-color);
  color: white;
}

/* Responsive */
@media (max-width: 768px) {
  .sidebar {
    width: 100%;
    height: auto;
    border-right: none;
    border-bottom: 1px solid var(--border-color);
    flex-direction: row;
    padding-bottom: 60px;
  }

  .sidebar-collapsed {
    width: 100%;
  }

  .sidebar-header {
    padding: 16px;
    border-bottom: none;
    border-right: 1px solid var(--border-color);
  }

  .logo {
    font-size: 1.2rem;
  }

  .sidebar-nav {
    flex-direction: row;
    padding: 8px;
    flex: 1;
  }

  .sidebar-toggle {
    position: static;
    transform: none;
    margin-left: auto;
  }
}
</style>
