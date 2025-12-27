import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/HomeView.vue'
import Churn from '../views/ChurnPredictionView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/churn',
      name: 'Churn',
      component: Churn
    }
  ],
})

export default router
