import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/HomeView.vue'
import Churn from '@/views/ChurnPredictionView.vue'
import BatchUploadView from '@/views/BatchUploadView.vue'
import BatchHistoryView from '@/views/BatchHistoryView.vue'
import BatchDetailView from '@/views/BatchDetailView.vue'
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
        description: 'Dashboard principal y métricas generales de retención de clientes.',
      }
    },
    {
      path: '/churn',
      name: 'ChurnPrediction',
      component: Churn,
      meta: {
        title: 'Predicción Individual - ChurnInsight',
        description: 'Analiza el riesgo de churn de un cliente específico en tiempo real.',
      }
    },
    {
      path: '/import-batch',
      name: 'BatchUpload',
      component: BatchUploadView,
      meta: {
        title: 'Carga Masiva - ChurnInsight',
        description: 'Sube archivos Excel o CSV para procesar predicciones de múltiples clientes.',
      }
    },
    {
      path: '/batch-history',
      name: 'BatchHistory',
      component: BatchHistoryView,
      meta: {
        title: 'Historial de Importaciones - ChurnInsight',
        description: 'Consulta el estado y resultados de todas las cargas masivas realizadas.',
      }
    },
    {
      path: '/batch/results/:id',
      name: 'BatchDetail',
      component: BatchDetailView,
      props: true, // Para pasar el ID como prop
      meta: {
        title: 'Detalle de Resultados - ChurnInsight',
        description: 'Visualiza el reporte detallado de una importación específica.',
      }
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: NotFoundView,
      meta: {
        title: 'Página No Encontrada - ChurnInsight',
        description: 'La página que buscas no existe.',
      }

    }
  ],
})

// LÓGICA PARA ACTUALIZAR EL TÍTULO Y META DESCRIPTION
router.beforeEach((to, from, next) => {
  // 1. Actualizar Título
  document.title = to.meta.title || 'ChurnInsight';

  // 2. Actualizar Meta Description
  const metaDescription = document.querySelector('meta[name="description"]');
  if (metaDescription) {
    metaDescription.setAttribute('content', to.meta.description || 'Plataforma de predicción de churn');
  } else {
    // Si no existe el tag, lo creamos (buena práctica)
    const meta = document.createElement('meta');
    meta.name = 'description';
    meta.content = to.meta.description || 'Plataforma de predicción de churn';
    document.head.appendChild(meta);
  }

  next();
});

export default router