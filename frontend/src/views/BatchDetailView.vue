<template>
  <MainLayout>
    <div class="detail-container">
      <div class="header-actions">
        <button @click="$router.back()" class="btn-back">
          <IconComponents name="arrow-left" :size="20" />
          Volver al historial
        </button>
      </div>
      <BatchResults :job-status="{ jobId: id }" />
    </div>
  </MainLayout>
</template>

<script setup>
import MainLayout from '@/components/layouts/MainLayout.vue'
import BatchResults from '@/components/batch/BatchResults.vue'
import IconComponents from '@/components/icons/IconComponents.vue'

defineProps(['id'])
</script>

<style scoped>
.detail-container { padding: 20px; max-width: 1200px; margin: 0 auto; }
.header-actions { margin-bottom: 20px; }
.btn-back { display: flex; align-items: center; gap: 8px; background: none; border: none; font-size: 1rem; font-weight: 500; color: var(--text-secondary); cursor: pointer; padding: 8px 0; transition: color 0.2s; }
.btn-back:hover { color: var(--primary-color); }
</style>