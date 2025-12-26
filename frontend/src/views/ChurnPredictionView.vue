<script setup>
import ChurnForm from '@/components/ChurnForm.vue'
</script>

<template>
  <div class="churn-view">
    <ChurnForm />
  </div>
</template>

<style scoped>
.churn-view {
  padding: 40px 20px;
  max-width: 900px;
  margin: 0 auto;
}

@media (max-width: 768px) {
  .churn-view {
    padding: 20px 16px;
  }
}
</style>
