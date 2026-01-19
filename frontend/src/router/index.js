import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/HomeView.vue'
import Churn from '@/views/ChurnPredictionView.vue'
import BatchUploadView from '@/views/BatchUploadView.vue'
import NotFoundView from '@/views/NotFoundView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home,
      meta: {
        title: 'Inicio - ChurnInsight',
        description: 'Predicción de churn de clientes',
      }
    },
    {
      path: '/churn',
      name: 'ChurnPrediction',
      component: Churn,
      meta: {
        title: 'Predicción Individual - ChurnInsight',
        description: 'Predice el riesgo de churn para un cliente individual',
      }
    },
    {
      path: '/import-batch',
      name: 'BatchUpload',
      component: BatchUploadView,
      meta: {
        title: 'Importar Lote - ChurnInsight',
        description: 'Importa y procesa múltiples clientes en lote',
      }
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: NotFoundView,
      meta: {
        title: 'Página No Encontrada - ChurnInsight',
        description: 'Página no encontrada',
      }
    }
  ],
})

export default router
