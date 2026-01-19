<template>
  <header class="header">
    <div class="header-content">
      <!-- Botón para toggle sidebar -->
      <button
        class="sidebar-toggle"
        @click="$emit('toggleSidebar')"
        title="Ocultar/Mostrar Sidebar"
      >
        <IconComponent name="menu" :size="22" />
      </button>

      <!-- Acciones rápidas -->
      <div class="header-actions">
        <!-- Toggle Tema -->
        <button
          @click="toggleTheme"
          class="theme-toggle"
          :aria-label="isDark ? 'Cambiar a modo claro' : 'Cambiar a modo oscuro'"
        >
          <IconComponent :name="isDark ? 'sun' : 'moon'" :size="20" />
        </button>

        <!-- Usuario con dropdown -->
        <div class="user-menu-wrapper">
          <div class="user-menu" @click="toggleUserMenu">
            <div class="user-avatar">
              U
            </div>
            <span class="user-name">Usuario</span>
            <IconComponent name="chevron-down" :size="16" />
          </div>

          <!-- Dropdown de usuario -->
          <Transition name="dropdown-fade">
            <div v-if="showUserMenu" class="dropdown user-dropdown">
              <a href="#" @click.prevent="handleLogout" class="dropdown-item">
                <IconComponent name="log-out" :size="18" />
                <span>Cerrar Sesión</span>
              </a>
            </div>
          </Transition>
        </div>
      </div>
    </div>

    <!-- Modal de confirmación -->
    <ConfirmModal
      ref="confirmModalRef"
      title="Cerrar Sesión"
      message="¿Estás seguro de que deseas cerrar sesión?"
      confirm-text="Cerrar Sesión"
      cancel-text="Cancelar"
      type="warning"
      icon="log-out"
      @confirm="confirmLogout"
      @cancel="cancelLogout"
    />
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import IconComponent from '@/components/icons/IconComponents.vue'
import ConfirmModal from '@/components/modals/ConfirmModal.vue'

const router = useRouter()
const showUserMenu = ref(false)
const isDark = ref(false)
const confirmModalRef = ref(null)

// Use auth store with reactive getters

// Computed properties for display

// Emitir evento al padre (MainLayout)
defineEmits(['toggleSidebar'])

// Detectar tema inicial
onMounted(() => {
  // Verificar si hay preferencia guardada
  const savedTheme = localStorage.getItem('theme')
  if (savedTheme) {
    isDark.value = savedTheme === 'dark'
    document.documentElement.setAttribute('data-theme', savedTheme)
  } else {
    // Por defecto iniciar en modo claro
    isDark.value = false
    document.documentElement.setAttribute('data-theme', 'light')
    localStorage.setItem('theme', 'light')
  }

  // Cerrar dropdowns al hacer click fuera
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})

const handleClickOutside = (e) => {
  // Verificar si el click fue dentro de los wrappers
  const userMenuWrapper = e.target.closest('.user-menu-wrapper')

  // Cerrar menú de usuario si el click no fue en su wrapper
  if (!userMenuWrapper) {
    showUserMenu.value = false
  }
}

const toggleTheme = () => {
  isDark.value = !isDark.value
  const theme = isDark.value ? 'dark' : 'light'
  document.documentElement.setAttribute('data-theme', theme)
  localStorage.setItem('theme', theme)
}

const toggleUserMenu = () => {
  showUserMenu.value = !showUserMenu.value
}

const handleLogout = async () => {
  // Cerrar menús inmediatamente
  showUserMenu.value = false

  // Mostrar modal de confirmación
  confirmModalRef.value?.show()
}

const confirmLogout = async () => {
  try {
    // Ocultar modal y redirigir
    confirmModalRef.value?.hide()

    setTimeout(() => {
      router.push({ name: 'login' }).catch(() => {
        window.location.href = '/login'
      })
    }, 100)
  } catch (err) {
    console.error('Logout failed', err)
    // Force redirect even on error
    confirmModalRef.value?.hide()
    router.push({ name: 'login' }).catch(() => {
      window.location.href = '/login'
    })
  }
}

const cancelLogout = () => {
  confirmModalRef.value?.hide()
}
</script>

<style scoped>
.header {
  position: sticky;
  top: 0;
  z-index: 998;
  background: var(--bg-white);
  border-bottom: 1px solid var(--border-color);
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.75rem 1.5rem;
  gap: 1rem;
}

/* Botón toggle sidebar */
.sidebar-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.5rem;
  background: transparent;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  color: var(--text-secondary);
  transition: all 0.2s;
}

.sidebar-toggle:hover {
  background-color: var(--hover-bg);
  color: var(--primary-color);
}

.sidebar-toggle:active {
  transform: scale(0.95);
}

/* Toggle de tema */
.theme-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.5rem;
  background: transparent;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  color: var(--text-secondary);
  transition: all 0.2s;
}

.theme-toggle:hover {
  color: var(--primary-color);
  transform: rotate(50deg);
}

.theme-toggle:active {
  transform: scale(0.95);
}

/* Acciones del header */
.header-actions {
  display: flex;
  align-items: center;
  gap: 1rem;
}

/* Wrappers para dropdowns */
.header-action-wrapper,
.user-menu-wrapper {
  position: relative;
}

.header-action {
  position: relative;
  padding: 0.5rem;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.2s;
  color: var(--text-secondary);
}

.header-action:hover {
  background-color: var(--hover-bg);
}

.badge-dot {
  position: absolute;
  top: 0.25rem;
  right: 0.25rem;
  background: var(--danger-color);
  color: white;
  font-size: 0.65rem;
  font-weight: 600;
  padding: 0.125rem 0.375rem;
  border-radius: 0.75rem;
  min-width: 1.25rem;
  text-align: center;
}

/* Menú de usuario */
.user-menu {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0.75rem;
  border-radius: var(--border-radius-lg);
  cursor: pointer;
  transition: background-color 0.2s;
}

.user-menu:hover {
  background-color: var(--tertiary-bg);
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--primary-color);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.user-name {
  font-weight: 500;
  color: var(--text-secondary);
  font-size: 0.9rem;
}

/* Ocultar nombre de usuario y chevron en mobile */
@media (max-width: 768px) {
  .user-name,
  .user-menu .icon-component:last-child {
    display: none;
  }

  .user-menu {
    padding: 0.5rem;
  }
}

/* Dropdowns */
.dropdown {
  position: absolute;
  top: calc(100% + 0.5rem);
  right: 0;
  background: var(--bg-white);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  box-shadow: var(--shadow-lg);
  min-width: 280px;
  animation: slideDown 0.2s ease;
  z-index: 1000;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.dropdown-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem;
  border-bottom: 1px solid var(--border-color);
}

.dropdown-header h4 {
  margin: 0;
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--text-primary);
}

.dropdown-header .badge {
  background: var(--danger-color);
  color: white;
  font-size: 0.75rem;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
}

.notifications-list {
  max-height: 320px;
  overflow-y: auto;
}

.notification-item {
  display: flex;
  gap: 0.75rem;
  padding: 0.75rem 1rem;
  border-bottom: 1px solid var(--border-light);
  cursor: pointer;
  transition: background-color 0.2s;
}

.notification-item:hover {
  background-color: var(--hover-bg);
}

.notification-item:last-child {
  border-bottom: none;
}

.notification-content {
  flex: 1;
}

.notification-title {
  margin: 0 0 0.25rem 0;
  font-weight: 600;
  font-size: 0.85rem;
  color: var(--text-primary);
}

.notification-text {
  margin: 0 0 0.25rem 0;
  font-size: 0.8rem;
  color: var(--text-secondary);
}

.notification-time {
  font-size: 0.75rem;
  color: var(--text-muted);
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem 1rem;
  color: var(--text-primary);
  text-decoration: none;
  transition: background-color 0.2s;
  cursor: pointer;
}

.dropdown-item:hover {
  background-color: var(--hover-bg);
  color: var(--primary-color);
}

.dropdown-divider {
  height: 0;
  margin: 0.5rem 0;
  overflow: hidden;
  border-top: 1px solid var(--border-color);
}

.dropdown-footer {
  padding: 0.75rem 1rem;
  border-top: 1px solid var(--border-color);
  text-align: center;
}

.dropdown-footer a {
  color: var(--primary-color);
  text-decoration: none;
  font-size: 0.85rem;
  font-weight: 500;
}

.dropdown-footer a:hover {
  text-decoration: underline;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .header-content {
    padding: 0.5rem 0.75rem;
    gap: 0.5rem;
  }

  .sidebar-toggle,
  .theme-toggle {
    padding: 0.375rem;
  }

  .header-actions {
    gap: 0.5rem;
    width: auto;
  }

  .header-action {
    padding: 0.375rem;
  }

  .badge-dot {
    top: 0.125rem;
    right: 0.125rem;
    font-size: 0.6rem;
    padding: 0.1rem 0.3rem;
  }

  .dropdown {
    position: fixed;
    top: auto !important;
    right: 0.75rem !important;
    left: auto !important;
    min-width: auto;
    max-width: calc(100vw - 1.5rem);
    z-index: 9999;
  }

  .user-avatar {
    width: 28px;
    height: 28px;
  }
}

@media (max-width: 480px) {
  .dropdown {
    position: fixed;
    right: 0.5rem !important;
    left: auto !important;
    min-width: auto;
    max-width: calc(100vw - 1rem);
  }
}

/* Transiciones de dropdown */
.dropdown-fade-enter-active,
.dropdown-fade-leave-active {
  transition: all 0.2s ease;
}

.dropdown-fade-enter-from {
  opacity: 0;
  transform: translateY(-10px);
}

.dropdown-fade-leave-to {
  opacity: 0;
  transform: translateY(-5px);
}

/* Dark mode */
[data-theme='dark'] .header {
  background: #0d1117;
  border-bottom-color: #30363d;
}

[data-theme='dark'] .header-content {
  background: #0d1117;
  border-bottom-color: #30363d;
  color: white;
}
</style>
