import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
// import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    // vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    host: '0.0.0.0', // Permite conexiones desde fuera del contenedor Docker
    port: 5173,
    strictPort: true,
    watch: {
      usePolling: true, // Necesario para hot reload en Windows con Docker
    },
    hmr: {
      host: 'localhost',
    },
  },
})
