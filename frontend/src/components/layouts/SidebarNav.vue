<script setup>
import { onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useSidebarState } from '@/composables/useSidebarState'

const router = useRouter()
const {
  sidebarOpen: sidebarVisible,
  syncWithBreakpoint
} = useSidebarState()

// Inicializar el estado cuando monta el componente
onMounted(() => {
  // Restaurar estado guardado
  syncWithBreakpoint()
  
  // Listener para cambios de tamaño de ventana
  window.addEventListener('resize', handleWindowResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleWindowResize)
})

const handleWindowResize = () => {
  // Sincronizar estado con breakpoint cuando cambia el tamaño
  syncWithBreakpoint()
}

const navigateTo = (path) => {
  router.push(path)
}
</script>

<template>
  <aside 
    :class="['sidebar', { 'sidebar-collapsed': !sidebarVisible }]"
    :aria-label="sidebarVisible ? 'Navegación expandida' : 'Navegación contraída'"
  >
    <!-- Header con Logo -->
    <div class="sidebar-header">
      <h1 v-if="sidebarVisible" class="logo">🔮 ChurnInsight</h1>
      <div v-else class="logo-icon">🔮</div>
    </div>

    <!-- Navigation Menu -->
    <nav class="sidebar-nav" aria-label="Menú de navegación">
      <!-- Inicio -->
      <router-link
        to="/"
        :class="['nav-item', { active: $route.path === '/' }]"
        :aria-current="$route.path === '/' ? 'page' : undefined"
        @click="navigateTo('/')"
        title="Ir a inicio"
      >
        <span class="icon" aria-hidden="true">🏠</span>
        <span v-if="sidebarVisible" class="label">Inicio</span>
      </router-link>

      <!-- Predicción Churn -->
      <router-link
        to="/churn"
        :class="['nav-item', { active: $route.path === '/churn' }]"
        :aria-current="$route.path === '/churn' ? 'page' : undefined"
        @click="navigateTo('/churn')"
        title="Predicción de Churn"
      >
        <span class="icon" aria-hidden="true">📊</span>
        <span v-if="sidebarVisible" class="label">Predicción Churn</span>
      </router-link>

      <!-- Próximamente: Importar Lote -->
      <div 
        class="nav-item disabled" 
        title="Próximamente disponible"
        role="menuitem"
        aria-disabled="true"
      >
        <span class="icon" aria-hidden="true">📁</span>
        <span v-if="sidebarVisible" class="label">Importar Lote</span>
      </div>

      <!-- Próximamente: Análisis -->
      <div 
        class="nav-item disabled" 
        title="Próximamente disponible"
        role="menuitem"
        aria-disabled="true"
      >
        <span class="icon" aria-hidden="true">📈</span>
        <span v-if="sidebarVisible" class="label">Análisis</span>
      </div>
    </nav>


  </aside>
</template>

<style scoped>
/* ========================
   SIDEBAR PRINCIPAL
   ======================== */
.sidebar {
  width: 280px;
  background: var(--bg-white);
  border-right: 1px solid var(--border-color);
  box-shadow: var(--shadow-sm);
  display: flex;
  flex-direction: column;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  z-index: 100;
  height: 100vh;
  overflow-y: auto;
  overflow-x: hidden;
}

.sidebar-collapsed {
  width: 80px;
}

/* ========================
   HEADER & LOGO
   ======================== */
.sidebar-header {
  padding: 24px 16px;
  border-bottom: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 80px;
  flex-shrink: 0;
  transition: all 0.3s ease;
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
  overflow: hidden;
  text-overflow: ellipsis;
  transition: opacity 0.3s ease;
}

.logo-icon {
  font-size: 2rem;
  transition: all 0.3s ease;
}

/* ========================
   NAVIGATION MENU
   ======================== */
.sidebar-nav {
  flex: 1;
  padding: 16px 8px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  overflow-y: auto;
  transition: all 0.3s ease;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: transparent;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  color: var(--text-secondary);
  font-size: 0.95rem;
  font-weight: 500;
  transition: all 0.2s ease;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-decoration: none;
  position: relative;
  user-select: none;
}

.nav-item:not(.disabled) {
  cursor: pointer;
}

.nav-item:hover:not(.disabled) {
  background-color: var(--hover-bg);
  color: var(--primary-color);
  transform: translateX(4px);
}

.nav-item:active:not(.disabled) {
  transform: translateX(2px);
}

.nav-item.active {
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
  box-shadow: var(--shadow-md);
  font-weight: 600;
}

.nav-item.active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: white;
  border-radius: 0 3px 3px 0;
  opacity: 0.8;
}

.nav-item.disabled {
  opacity: 0.5;
  cursor: not-allowed;
  color: var(--text-tertiary);
}

.nav-item.disabled:hover {
  background-color: transparent;
  transform: none;
}

.nav-item.disabled::after {
  content: 'Próximamente';
  position: absolute;
  left: 100%;
  top: 50%;
  transform: translateY(-50%);
  margin-left: 12px;
  background: var(--bg-secondary);
  color: var(--text-primary);
  padding: 6px 10px;
  border-radius: 6px;
  font-size: 0.7rem;
  font-weight: 600;
  white-space: nowrap;
  opacity: 0;
  transition: opacity 0.2s ease;
  pointer-events: none;
  z-index: 1000;
  box-shadow: var(--shadow-md);
}

.nav-item.disabled:hover::after {
  opacity: 1;
}

.nav-item .icon {
  font-size: 1.3rem;
  flex-shrink: 0;
  transition: all 0.2s ease;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.nav-item .label {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: all 0.3s ease;
  flex: 1;
}

/* ========================
   SCROLLBAR PERSONALIZADO
   ======================== */
.sidebar-nav::-webkit-scrollbar {
  width: 6px;
}

.sidebar-nav::-webkit-scrollbar-track {
  background: transparent;
}

.sidebar-nav::-webkit-scrollbar-thumb {
  background: var(--border-color);
  border-radius: 3px;
  transition: background 0.2s;
}

.sidebar-nav::-webkit-scrollbar-thumb:hover {
  background: var(--text-secondary);
}

/* ========================
   RESPONSIVE - TABLET
   ======================== */
@media (max-width: 768px) {
  .sidebar {
    width: 100%;
    height: auto;
    border-right: none;
    border-bottom: 1px solid var(--border-color);
    flex-direction: row;
    min-height: 70px;
    padding-bottom: 0;
  }

  .sidebar-collapsed {
    width: 100%;
  }

  .sidebar-header {
    padding: 16px;
    border-bottom: none;
    border-right: 1px solid var(--border-color);
    min-height: auto;
    flex-shrink: 0;
  }

  .logo {
    font-size: 1.3rem;
  }

  .logo-icon {
    font-size: 1.5rem;
  }

  .sidebar-nav {
    flex-direction: row;
    padding: 8px;
    flex: 1;
    overflow-x: auto;
    overflow-y: hidden;
    gap: 4px;
  }

  .nav-item {
    padding: 10px 12px;
    font-size: 0.85rem;
    flex-shrink: 0;
  }

  .nav-item:hover {
    transform: translateX(0);
    transform: translateY(-2px);
  }

  .nav-item.disabled::after {
    display: none;
  }

  .sidebar-spacer {
    display: none;
  }

  .sidebar-toggle {
    position: relative;
    transform: none;
    margin: 0 8px 0 auto;
    width: 44px;
    height: 44px;
  }
}

/* ========================
   RESPONSIVE - MÓVIL
   ======================== */
@media (max-width: 480px) {
  .sidebar {
    min-height: 60px;
  }

  .sidebar-header {
    padding: 12px;
  }

  .logo {
    font-size: 1.1rem;
  }

  .logo-icon {
    font-size: 1.3rem;
  }

  .sidebar-nav {
    padding: 4px;
    gap: 2px;
  }

  .nav-item {
    padding: 8px 10px;
    font-size: 0.8rem;
  }

  .nav-item .icon {
    font-size: 1.1rem;
  }

  .sidebar-toggle {
    width: 40px;
    height: 40px;
    font-size: 0.9rem;
  }
}

/* ========================
   ANIMACIONES SUAVES
   ======================== */
@media (prefers-reduced-motion: reduce) {
  .sidebar,
  .sidebar-header,
  .sidebar-nav,
  .nav-item,
  .sidebar-toggle,
  .nav-item .icon,
  .nav-item .label,
  .toggle-icon {
    transition: none !important;
    animation: none !important;
  }
}

/* ========================
   TEMAS - SCROLLBAR MÓVIL
   ======================== */
@supports selector(::-webkit-scrollbar) {
  .sidebar-nav {
    scrollbar-color: var(--border-color) transparent;
    scrollbar-width: thin;
  }
}
</style>
