<script setup>
import { onMounted } from 'vue'
import SidebarNav from './components/layouts/SidebarNav.vue'
import HeaderSection from './components/layouts/HeaderSection.vue'

onMounted(() => {
  // Initialize theme from localStorage or system preference
  const savedTheme = localStorage.getItem('theme')
  if (savedTheme) {
    document.documentElement.setAttribute('data-theme', savedTheme)
  } else {
    // Check if user prefers dark mode
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
    const theme = prefersDark ? 'dark' : 'light'
    document.documentElement.setAttribute('data-theme', theme)
    localStorage.setItem('theme', theme)
  }
})
</script>

<template>
  <div class="app-layout">
    <SidebarNav />
    <div class="main-wrapper">
      <HeaderSection />
      <main class="main-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<style>
.app-layout {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.main-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.main-content {
  flex: 1;
  overflow-y: auto;
  background-color: var(--bg-body);
}

@media (max-width: 768px) {
  .app-layout {
    flex-direction: column;
    height: auto;
  }

  .main-content {
    min-height: calc(100vh - 140px);
  }
}
</style>