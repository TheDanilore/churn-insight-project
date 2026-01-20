<template>
  <nav :class="['sidebar', { 'sidebar-open': isOpen }]">
    <div class="sidebar-content" ref="sidebarContentRef" @scroll="handleScroll">
      <!-- Brand / Logo -->
      <div class="sidebar-brand">
        <div class="sidebar-brand-icon">
          <IconComponent name="crystal" :size="32"/>
        </div>
        <transition name="fade">
          <span v-if="isOpen" class="sidebar-brand-text">
            ChurnInsight
          </span>
        </transition>
      </div>

      <!-- Toggle Button (Mobile) -->
      <button class="sidebar-toggle-btn" @click="$emit('toggle')" aria-label="Cerrar menú">
        <IconComponent name="x" :size="20" />
      </button>

      <!-- Navigation -->
      <ul class="sidebar-nav">
        <!-- Panel Principal -->
        <li class="sidebar-header">
          <span>PRINCIPAL</span>
        </li>

        <li class="sidebar-item" :class="{ active: isActive('Home') }">
          <router-link class="sidebar-link" to="/">
            <IconComponent name="home" :size="20" />
            <span>Inicio</span>
          </router-link>
        </li>

        <li class="sidebar-divider"></li>

        <!-- Predicción -->
        <li class="sidebar-header">
          <span>PREDICCIÓN</span>
        </li>

        <li class="sidebar-item" :class="{ active: isActive('ChurnPrediction') }">
          <router-link class="sidebar-link" to="/churn">
            <IconComponent name="pie-chart" :size="20" />
            <span>Predicción Individual</span>
          </router-link>
        </li>

        <li class="sidebar-item">
          <a class="sidebar-link" @click.prevent="toggleMenu('batch')" :class="{ collapsed: !menus.batch }">
            <IconComponent name="folder" :size="20" />
            <span>Importación Lotes</span>
            <IconComponent name="chevron-right" :size="16" :class="['menu-arrow', { 'rotate-90': menus.batch }]" />
          </a>
          <transition name="slide">
            <ul v-show="menus.batch" class="sidebar-dropdown">
              <li class="sidebar-item" :class="{ active: isActive('BatchUpload') }">
                <router-link class="sidebar-link" to="/import-batch">
                  <IconComponent name="upload" :size="18" />
                  <span>Cargar Archivo</span>
                </router-link>
              </li>
              <li class="sidebar-item" :class="{ active: isActive(['BatchHistory', 'BatchDetail']) }">
                <router-link class="sidebar-link" to="/batch-history">
                  <IconComponent name="list" :size="18" />
                  <span>Historial</span>
                </router-link>
              </li>
            </ul>
          </transition>
        </li>

        <li class="sidebar-divider"></li>

        <!-- Próximamente -->
        <li class="sidebar-header">
          <span>PRÓXIMAMENTE</span>
        </li>

        <li class="sidebar-item disabled">
          <a class="sidebar-link" title="Próximamente disponible" aria-disabled="true">
            <IconComponent name="trending-up" :size="20" />
            <span>Análisis</span>
          </a>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import IconComponent from '@/components/icons/IconComponents.vue'

const route = useRoute()
const sidebarContentRef = ref(null)

defineProps({
  isOpen: {
    type: Boolean,
    default: true,
  },
})

defineEmits(['toggle'])

// Cargar estado de menús desde localStorage
const loadMenuState = () => {
  const saved = localStorage.getItem('sidebar-menu-state')
  if (saved) {
    try {
      return JSON.parse(saved)
    } catch {
      return getDefaultMenuState()
    }
  }
  return getDefaultMenuState()
}

const getDefaultMenuState = () => ({
  batch: false,
})

const menus = ref(loadMenuState())

// Guardar estado de menús en localStorage
const saveMenuState = () => {
  localStorage.setItem('sidebar-menu-state', JSON.stringify(menus.value))
}

// Guardar posición del scroll en localStorage
const saveSidebarScroll = () => {
  if (sidebarContentRef.value) {
    localStorage.setItem('sidebar-scroll-position', sidebarContentRef.value.scrollTop)
  }
}

// Restaurar posición del scroll
const restoreSidebarScroll = async () => {
  await nextTick()
  const saved = localStorage.getItem('sidebar-scroll-position')
  if (saved && sidebarContentRef.value) {
    sidebarContentRef.value.scrollTop = parseInt(saved, 10)
  }
}

// Determinar qué menús deben estar abiertos basado en la ruta actual
const getMenusForCurrentRoute = () => {
  const menuMapping = {
    BatchUpload: 'batch',
    BatchHistory: 'batch',
  }

  const menuKey = menuMapping[route.name]
  return menuKey ? menuKey : null
}

// Auto-abrir menú basado en la ruta actual
const syncMenusWithRoute = () => {
  const menuToOpen = getMenusForCurrentRoute()
  if (menuToOpen) {
    menus.value[menuToOpen] = true
    saveMenuState()
  }
  restoreSidebarScroll()
}

const toggleMenu = (menu) => {
  menus.value[menu] = !menus.value[menu]
  saveMenuState()
  setTimeout(() => {
    saveSidebarScroll()
  }, 300)
}

const isActive = (routeNames) => {
  if (Array.isArray(routeNames)) {
    return routeNames.includes(route.name)
  }
  return route.name === routeNames
}

watch(
  () => route.name,
  () => {
    syncMenusWithRoute()
  },
)

onMounted(() => {
  syncMenusWithRoute()
})

const handleScroll = () => {
  saveSidebarScroll()
}
</script>

<style scoped>
.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  width: 260px;
  background: var(--bg-white);
  border-right: 1px solid var(--border-color);
  box-shadow: var(--shadow-md);
  transition:
    transform 0.3s ease,
    width 0.3s ease;
  z-index: 1000;
  display: flex;
  flex-direction: column;
}

.sidebar-content {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 0;
  height: 100%;
}

/* Scrollbar Styling */
.sidebar-content::-webkit-scrollbar {
  width: 6px;
}

.sidebar-content::-webkit-scrollbar-track {
  background: transparent;
}

.sidebar-content::-webkit-scrollbar-thumb {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 3px;
}

.sidebar-content::-webkit-scrollbar-thumb:hover {
  background: rgba(0, 0, 0, 0.2);
}

/* Brand */
.sidebar-brand {
  display: flex;
  align-items: center;
  padding: 1.5rem 1.25rem;
  font-size: 1.15rem;
  font-weight: 600;
  color: var(--primary-color);
  background: var(--bg-white);
  border-bottom: 1px solid var(--border-color);
  gap: 0.75rem;
}

.sidebar-brand-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  flex-shrink: 0;
}

.company-logo {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  border-radius: 4px;
}

.sidebar-brand-text {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Toggle Button */
.sidebar-toggle-btn {
  display: none;
  position: absolute;
  top: 1rem;
  right: 1rem;
  background: transparent;
  border: none;
  padding: 0.5rem;
  cursor: pointer;
  color: #6c757d;
  transition: color 0.2s;
}

.sidebar-toggle-btn:hover {
  color: #3b7ddd;
}

/* Navigation */
.sidebar-nav {
  list-style: none;
  padding: 0.75rem 0;
  margin: 0;
}

.sidebar-header {
  padding: 1.25rem 1.25rem 0.75rem;
  font-size: 0.75rem;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  color: var(--text-secondary);
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-top: 0.5rem;
}

.sidebar-item {
  position: relative;
}

.sidebar-link {
  display: flex;
  align-items: center;
  padding: 0.75rem 1.25rem;
  color: var(--text-secondary);
  font-weight: 500;
  text-decoration: none;
  cursor: pointer;
  transition: all 0.2s ease;
  gap: 0.75rem;
}

.sidebar-link:hover {
  background-color: var(--hover-bg);
  color: var(--primary-color);
}

.sidebar-item.active>.sidebar-link {
  background-color: var(--active-bg);
  color: var(--primary-color);
  border-left: 3px solid var(--primary-color);
  font-weight: 600;
}

.sidebar-link span:first-of-type {
  flex: 1;
}

/* Menu Arrow */
.menu-arrow {
  margin-left: auto;
  display: inline-block;
  width: 16px;
  height: 16px;
  transition: transform 0.3s ease;
  flex-shrink: 0;
}

.menu-arrow::before {
  content: '›';
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  font-size: 1.2em;
  font-weight: bold;
}

.menu-arrow.rotate-90 {
  transform: rotate(90deg);
}

/* Dropdown */
.sidebar-dropdown {
  list-style: none;
  padding: 0;
  margin: 0;
  background-color: var(--bg-light);
}

.sidebar-dropdown .sidebar-link {
  padding: 0.65rem 1.25rem 0.65rem 3rem;
  font-size: 0.875rem;
  font-weight: 400;
  color: var(--text-secondary);
  display: flex;
  align-items: center;
  gap: 0.65rem;
}

.sidebar-dropdown .sidebar-link:hover {
  background-color: var(--hover-bg);
  color: var(--primary-color);
}

.sidebar-dropdown .sidebar-item.active .sidebar-link {
  background-color: var(--active-bg);
  border-left-color: var(--primary-color);
  color: var(--primary-color);
  font-weight: 600;
}

/* Badge */
.sidebar-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 1.5rem;
  padding: 0.25rem 0.5rem;
  font-size: 0.75rem;
  font-weight: 600;
  line-height: 1;
  color: #fff;
  background-color: var(--danger-color);
  border-radius: 0.25rem;
  margin-left: auto;
}

/* Divider */
.sidebar-divider {
  height: 0;
  margin: 0.75rem 0;
  overflow: hidden;
  border-top: 1px solid var(--border-color);
}

/* Disabled Items */
.sidebar-item.disabled .sidebar-link {
  color: var(--disabled-color);
  cursor: not-allowed;
  opacity: 0.6;
  pointer-events: none;
}

.sidebar-item.disabled .sidebar-link:hover {
  background-color: transparent;
  color: var(--disabled-color);
}

.sidebar-item.disabled .icon {
  opacity: 0.6;
}

/* Animations */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.slide-enter-active {
  animation: slideDown 0.3s ease;
}

.slide-leave-active {
  animation: slideUp 0.3s ease;
}

@keyframes slideDown {
  from {
    max-height: 0;
    opacity: 0;
  }

  to {
    max-height: 500px;
    opacity: 1;
  }
}

@keyframes slideUp {
  from {
    max-height: 500px;
    opacity: 1;
  }

  to {
    max-height: 0;
    opacity: 0;
  }
}

/* Sidebar cerrado en desktop */
.sidebar:not(.sidebar-open) {
  transform: translateX(-260px);
}

/* Responsive */
@media (max-width: 991px) {
  .sidebar {
    transform: translateX(-100%);
  }

  .sidebar.sidebar-open {
    transform: translateX(0);
  }

  .sidebar-toggle-btn {
    display: block;
  }
}

/* Dark mode con data-theme */

[data-theme='dark'] .sidebar {
  background: var(--bg-white);
  border-right-color: var(--border-color);
  box-shadow: var(--shadow-md);
}

[data-theme='dark'] .sidebar-content {
  scrollbar-color: rgba(255, 255, 255, 0.2) transparent;
}

[data-theme='dark'] .sidebar-content::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.15);
}

[data-theme='dark'] .sidebar-content::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.25);
}

[data-theme='dark'] .sidebar-brand {
  color: var(--secondary-color);
  background: var(--bg-white);
  border-bottom-color: var(--border-color);
}

[data-theme='dark'] .sidebar-header {
  color: var(--secondary-color);
}

[data-theme='dark'] .sidebar-link {
  color: var(--text-secondary);
}

[data-theme='dark'] .sidebar-link:hover {
  background-color: var(--hover-bg);
  color: var(--secondary-color);
}

[data-theme='dark'] .sidebar-item.active>.sidebar-link {
  background-color: var(--active-bg);
  color: var(--secondary-color);
  border-left-color: var(--secondary-color);
  font-weight: 600;
}

[data-theme='dark'] .sidebar-dropdown {
  background-color: var(--bg-light);
}

[data-theme='dark'] .sidebar-dropdown .sidebar-link {
  color: var(--text-secondary);
}

[data-theme='dark'] .sidebar-dropdown .sidebar-link:hover {
  background-color: var(--hover-bg);
  color: var(--secondary-color);
}

[data-theme='dark'] .sidebar-dropdown .sidebar-item.active .sidebar-link {
  background-color: var(--active-bg);
  color: var(--secondary-color);
  font-weight: 600;
}

[data-theme='dark'] .sidebar-divider {
  border-top-color: var(--border-color);
}

[data-theme='dark'] .sidebar-item.disabled .sidebar-link {
  color: var(--disabled-color);
}

[data-theme='dark'] .sidebar-item.disabled .sidebar-link:hover {
  color: var(--disabled-color);
}
</style>
