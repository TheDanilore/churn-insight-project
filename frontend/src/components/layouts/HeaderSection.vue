<template>
  <header class="header">
    <div class="header-content">
      <!-- Sidebar Toggle -->
      <button class="sidebar-toggle" @click="toggleSidebar" :aria-expanded="sidebarOpen" aria-label="Toggle sidebar" title="Toggle Sidebar">
        <svg
          width="20"
          height="20"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
        >
          <line x1="3" y1="12" x2="21" y2="12"></line>
          <line x1="3" y1="6" x2="21" y2="6"></line>
          <line x1="3" y1="18" x2="21" y2="18"></line>
        </svg>
      </button>

      <div class="spacer"></div>

      <!-- Header Actions -->
      <div class="header-actions">
        <!-- Theme Toggle -->
        <button
          class="theme-toggle"
          @click="toggleTheme"
          :title="isDark ? 'Light Mode' : 'Dark Mode'"
        >
          <svg
            v-if="!isDark"
            width="20"
            height="20"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
          >
            <circle cx="12" cy="12" r="5"></circle>
            <line x1="12" y1="1" x2="12" y2="3"></line>
            <line x1="12" y1="21" x2="12" y2="23"></line>
            <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line>
            <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line>
            <line x1="1" y1="12" x2="3" y2="12"></line>
            <line x1="21" y1="12" x2="23" y2="12"></line>
            <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line>
            <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line>
          </svg>
          <svg
            v-else
            width="20"
            height="20"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2"
          >
            <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
          </svg>
        </button>

        <!-- User Menu -->
        <div class="user-menu-wrapper">
          <button class="user-menu" @click="toggleUserMenu">
            <div class="user-avatar">
              {{ userInitials }}
            </div>
            <span class="user-name">{{ displayName }}</span>
          </button>

          <transition name="dropdown-fade">
            <div v-if="showUserMenu" class="dropdown user-dropdown">
              <div class="dropdown-item" @click="$router.push('/profile')">👤 Mi Perfil</div>
              <div class="dropdown-item" @click="$router.push('/settings')">⚙️ Configuración</div>
              <div class="dropdown-divider"></div>
              <div class="dropdown-item danger" @click="handleLogout">🚪 Cerrar Sesión</div>
            </div>
          </transition>
        </div>
      </div>
    </div>
  </header>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useSidebarState } from '@/composables/useSidebarState'

const router = useRouter()
const showUserMenu = ref(false)
const isDark = ref(false)

// Sidebar state
const { sidebarOpen, toggleSidebar } = useSidebarState()

// Mock user data (replace with actual auth store if available)
const user = ref({
  name: 'Usuario',
  email: 'usuario@example.com',
})

const displayName = computed(() => {
  if (!user.value) return 'Usuario'
  return user.value.name || user.value.email || 'Usuario'
})

const userInitials = computed(() => {
  if (!user.value) return 'U'
  const name = user.value.name || user.value.email || 'U'
  return name.charAt(0).toUpperCase()
})

onMounted(() => {
  const savedTheme = localStorage.getItem('theme')
  if (savedTheme) {
    isDark.value = savedTheme === 'dark'
    document.documentElement.setAttribute('data-theme', savedTheme)
  } else {
    isDark.value = false
    document.documentElement.setAttribute('data-theme', 'light')
    localStorage.setItem('theme', 'light')
  }

  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})

const handleClickOutside = (e) => {
  const userMenuWrapper = e.target.closest('.user-menu-wrapper')
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
  showUserMenu.value = false
  // Implement logout logic
  localStorage.removeItem('auth_token')
  router.push('/login')
}
</script>

<style scoped>
.header {
  position: sticky;
  top: 0;
  z-index: 998;
  background: var(--bg-white);
  border-bottom: 1px solid var(--border-color);
  box-shadow: var(--shadow-sm);
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.75rem 1.5rem;
  gap: 1rem;
}

.sidebar-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.5rem;
  background: transparent;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  color: var(--text-primary);
  transition: all 0.2s;
}

.sidebar-toggle:hover {
  background-color: var(--hover-bg);
  color: var(--primary-color);
}

.sidebar-toggle:active {
  transform: scale(0.95);
}

.spacer {
  flex: 1;
}

.theme-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.5rem;
  background: transparent;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  color: var(--text-primary);
  transition: all 0.2s;
}

.theme-toggle:hover {
  color: var(--primary-color);
  background-color: var(--hover-bg);
}

.theme-toggle:active {
  transform: scale(0.95);
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-menu-wrapper {
  position: relative;
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0.75rem;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
  background: transparent;
  color: var(--text-primary);
  font-family: inherit;
}

.user-menu:hover {
  background-color: var(--hover-bg);
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 700;
  font-size: 0.9rem;
}

.user-name {
  font-size: 0.9rem;
  font-weight: 500;
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dropdown {
  position: absolute;
  top: calc(100% + 8px);
  right: 0;
  background: var(--bg-white);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  box-shadow: var(--shadow-md);
  min-width: 200px;
  z-index: 1000;
  animation: slideDown 0.2s ease;
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

.user-dropdown {
  top: calc(100% + 8px);
  right: 0;
}

.dropdown-item {
  padding: 12px 16px;
  cursor: pointer;
  color: var(--text-primary);
  transition: all 0.2s;
  font-size: 0.9rem;
  border: none;
  background: none;
  width: 100%;
  text-align: left;
}

.dropdown-item:hover {
  background-color: var(--hover-bg);
  color: var(--primary-color);
}

.dropdown-item.danger {
  color: var(--danger-color);
}

.dropdown-item.danger:hover {
  background-color: rgba(245, 101, 101, 0.1);
}

.dropdown-divider {
  height: 1px;
  background-color: var(--border-color);
  margin: 8px 0;
}

.dropdown-fade-enter-active,
.dropdown-fade-leave-active {
  transition: all 0.2s ease;
}

.dropdown-fade-enter-from,
.dropdown-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

@media (max-width: 768px) {
  .header-content {
    padding: 0.5rem 1rem;
  }

  .user-name {
    display: none;
  }

  .user-menu {
    padding: 0.5rem;
  }
}
</style>
